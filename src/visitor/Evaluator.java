package visitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import memory.AmbienteExecucao;
import memory.AmbienteExecucaoImperativa2;
import memory.FuncaoJaDeclaradaException;
import memory.FuncaoNaoDeclaradaException;
import memory.VariavelJaDeclaradaException;
import memory.VariavelNaoDeclaradaException;
import util.LuaOps;
import core.Bloco;
import core.CampoTabela;
import core.Comando;
import core.Comando.FuncDef;
import core.ConstrutorTabela;
import core.Escopo;
import core.Exp;
import core.Exp.NomeExp;
import core.Exp.VarExp;
import core.FuncArgs;
import core.FuncCorpo;
import core.LuaBoolean;
import core.LuaNil;
import core.LuaNumber;
import core.LuaValor;
import core.Nome;
import core.ParList;
import core.Trecho;

public class Evaluator implements Visitor {

	private AmbienteExecucao ambiente;

	public Evaluator(AmbienteExecucao ambiente) {
		this.ambiente = ambiente;
	}

	public AmbienteExecucao getAmbiente() {
		return this.ambiente;
	}

	public void setAmbiente(AmbienteExecucao ambiente) {
		this.ambiente = ambiente;
	}

	public void visit(Trecho trecho) {
		this.ambiente.incrementa();
		trecho.bloco.accept(this);
		this.ambiente.restaura();
	}

	public LuaValor visit(Bloco bloco) {
		visit(bloco.escopo);
		if (bloco.comandos != null) {
			for (int i = 0, n = bloco.comandos.size(); i < n; i++) {
				if (bloco.comandos.get(i) instanceof Comando.Return)
					return ((Comando.Return) bloco.comandos.get(i))
							.accept(this);
				else
					((Comando) bloco.comandos.get(i)).accept(this);
			}
		}
		return null;
	}

	public LuaValor visit(Comando.Atribui comando) {
		try {
			List<NomeExp> nomes = visitVars(comando.vars);
			List<LuaValor> valores = visitExps(comando.exps);
			try {
				for (int i = 0; i < valores.size(); i++) {
					LuaValor valor = valores.get(i);
					Nome nome = nomes.get(i).nome;
					((AmbienteExecucaoImperativa2) ambiente).changeValor(nome,
							valor);
				}
			} catch (IndexOutOfBoundsException e) {
				return null;
			}
		} catch (VariavelNaoDeclaradaException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ATUALMENTE SÓ IMPLEMENTA ALGUMAS OPERAÇÕES PARA NUMBER, DEVERÁ SUPORTAR
	// MÚLTIPLOS TIPOS
	public LuaValor visit(Exp.BinopExp exp) {
		LuaNumber lvalor;
		LuaNumber rvalor;
		Integer il;
		Integer rl;
		LuaValor nvalor;
		switch (exp.op) {
		case LuaOps.OP_OR:
		case LuaOps.OP_AND:
		case LuaOps.OP_LT:
			lvalor = (LuaNumber) exp.lhs.accept(this);
			rvalor = (LuaNumber) exp.rhs.accept(this);
			il = lvalor.valor();
			rl = rvalor.valor();
			nvalor = new LuaBoolean(il < rl);
			return nvalor;
		case LuaOps.OP_GT:
		case LuaOps.OP_LE:
		case LuaOps.OP_GE:
		case LuaOps.OP_NEQ:
		case LuaOps.OP_EQ:
			lvalor = (LuaNumber) exp.lhs.accept(this);
			rvalor = (LuaNumber) exp.rhs.accept(this);
			il = lvalor.valor();
			rl = rvalor.valor();
			nvalor = new LuaBoolean(il.equals(rl));
			return nvalor;
		case LuaOps.OP_CONCAT:
		case LuaOps.OP_ADD:
			lvalor = (LuaNumber) exp.lhs.accept(this);
			rvalor = (LuaNumber) exp.rhs.accept(this);
			il = lvalor.valor();
			rl = rvalor.valor();
			nvalor = new LuaNumber(il + rl);
			return nvalor;
		case LuaOps.OP_SUB:
		case LuaOps.OP_MUL:
		case LuaOps.OP_DIV:
		case LuaOps.OP_MOD:
		case LuaOps.OP_NOT:
		case LuaOps.OP_UNM:
		case LuaOps.OP_LEN:
		case LuaOps.OP_POW:
		default:
			throw new IllegalStateException("Operação não implementada.");
		}

		// exp.lhs.accept(this);
		// exp.rhs.accept(this);
	}

	public LuaValor visit(Exp.FieldExp exp) {
		exp.lhs.accept(this);
		visit(exp.nome);
		return null;
	}

	public LuaValor visit(Exp.NomeExp exp) {
		return visit(exp.nome);

	}

	public LuaValor visit(Exp.ParensExp exp) {
		return exp.exp.accept(this);
	}

	public LuaValor visit(Exp.UnopExp exp) {
		exp.rhs.accept(this);
		return null;
	}

	public LuaValor visit(LuaValor exp) {
		return (LuaValor) exp;
	}

	public List<NomeExp> visitVars(List<VarExp> vars) {
		List<NomeExp> nomes = new ArrayList<NomeExp>();
		try {
			if (vars != null) {
				for (int i = 0, n = vars.size(); i < n; i++) {
					VarExp varExp = ((Exp.VarExp) vars.get(i));
					// varExp.accept(this);
					NomeExp nome = (NomeExp) varExp;
					if (!((AmbienteExecucaoImperativa2) ambiente)
							.contemVariavel(nome.nome))
						ambiente.map(nome.nome, new LuaNil());
					nomes.add(nome);
				}
			}
		} catch (VariavelJaDeclaradaException e) {
			System.out.println(e.getMessage());
		}
		return nomes;
	}

	public List<LuaValor> visitExps(List<Exp> exps) {
		List<LuaValor> valores = new ArrayList<LuaValor>();
		if (exps != null)
			for (int i = 0, n = exps.size(); i < n; i++) {
				LuaValor valor = ((Exp) exps.get(i)).accept(this);
				valores.add(valor);
			}
		return valores;
	}

	public void visitNomes(List<Nome> nomes) {
		if (nomes != null)
			for (int i = 0, n = nomes.size(); i < n; i++)
				visit((Nome) nomes.get(i));
	}

	public LuaValor visit(Exp.ChamadaFunc exp) {
		LuaValor retorno = new LuaNil();
		try {
			if (exp.lhs instanceof NomeExp) {
				Nome nomeFunc = ((NomeExp) exp.lhs).nome;
				if (nomeFunc.nome.equals("print")) {
					List<LuaValor> valores = exp.args.accept(this);
					for (LuaValor valor : valores) {
						// ((AmbienteExecucaoImperativa2)ambiente).write(valor);
						System.out.println(valor);
					}
				} else {
					FuncDef funcDef = ((AmbienteExecucaoImperativa2) ambiente)
							.getProcedimento(nomeFunc);
					List<LuaValor> valores = exp.args.accept(this);

					this.ambiente.incrementa();
					FuncCorpo corpo = funcDef.corpo;
					corpo.parlist.accept(this);
					List<Nome> parametros = corpo.parlist.nomes;
					for (int i = 0; i < valores.size(); i++) {
						LuaValor valor = valores.get(i);
						Nome parametro = parametros.get(i);
						((AmbienteExecucaoImperativa2) ambiente).changeValor(
								parametro, valor);
					}
					retorno = corpo.bloco.accept(this);
					this.ambiente.restaura();
				}
			}
		} catch (FuncaoNaoDeclaradaException e) {
			System.out.println(e.getMessage());
		} catch (VariavelNaoDeclaradaException e) {
			System.out.println(e.getMessage());
		}
		return retorno;
	}

	public List<LuaValor> visit(FuncArgs args) {
		return visitExps(args.exps);
	}

	public LuaValor visit(Comando.WhileDo comando) {
		while (((LuaBoolean) comando.exp.accept(this)).valor()) {
			this.ambiente.incrementa();
			comando.bloco.accept(this);
			this.ambiente.restaura();
		}
		return null;
	}

	public LuaValor visit(Comando.ComandoChamadaFunc comando) {
		return comando.chamadafunc.accept(this);
	}

	public LuaValor visit(Comando.IfThenElse comando) {
		if (((LuaBoolean) comando.ifexp.accept(this)).valor()) {
			this.ambiente.incrementa();
			comando.ifbloco.accept(this);
			this.ambiente.restaura();
		} else {
			Queue<Exp> elseifexps = new LinkedList<Exp>();
			elseifexps.addAll(comando.elseifexps);

			Queue<Bloco> elseifblocos = new LinkedList<Bloco>();
			elseifblocos.addAll(comando.elseifblocos);

			visitElseIfs(elseifexps, elseifblocos, comando.elsebloco);
		}
		return null;
	}

	public void visitElseIfs(Queue<Exp> elseifexps, Queue<Bloco> elseifblocos,
			Comando elsebloco) {
		Exp elseifexp = elseifexps.poll();
		Bloco elseifbloco = elseifblocos.poll();
		if (elseifbloco != null) {
			if (((LuaBoolean) elseifexp.accept(this)).valor()) {
				this.ambiente.incrementa();
				elseifbloco.accept(this);
				this.ambiente.restaura();
			} else {
				visitElseIfs(elseifexps, elseifblocos, elsebloco);
			}
		} else {
			if (elsebloco != null)
				this.ambiente.incrementa();
				elsebloco.accept(this);
				this.ambiente.restaura();
		}
	}

	public LuaValor visit(Comando.ForNumerico comando) {
		visit(comando.escopo);
		visit(comando.nome);
		comando.inicial.accept(this);
		comando.limite.accept(this);
		if (comando.passo != null)
			comando.passo.accept(this);
		comando.bloco.accept(this);
		return null;
	}

	public LuaValor visit(Comando.FuncDef comando) {
		try {
			Nome funcNome = comando.nome.nome;
			FuncDef funcDef = comando;
			((AmbienteExecucaoImperativa2) ambiente).mapProcedimento(funcNome,
					funcDef);
		} catch (FuncaoJaDeclaradaException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void visit(ParList pars) {
		// visitNomes(pars.nomes);
		try {
			for (Nome n : pars.nomes)
				ambiente.map(n, new LuaNil());
		} catch (VariavelJaDeclaradaException e) {
			e.printStackTrace();
		}

	}

	public LuaValor visit(ConstrutorTabela tabela) {
		if (tabela.campos != null)
			for (int i = 0, n = tabela.campos.size(); i < n; i++)
				((CampoTabela) tabela.campos.get(i)).accept(this);
		return null;
	}

	public void visit(CampoTabela campo) {
		if (campo.nome != null)	;
		visit(campo.nome);
		if (campo.index != null)
			campo.index.accept(this);
		campo.rhs.accept(this);
	}

	public LuaValor visit(Comando.Return comando) {
		return visitExps(comando.valores).get(0);
	}

	public LuaValor visit(Nome name) {
		/*
		 * try { LuaValor valor = null;
		 * if(!((AmbienteExecucaoImperativa2)ambiente).contemVariavel(name)){
		 * valor = new LuaNil(); ambiente.map(name, valor); }else{ valor =
		 * (LuaValor)ambiente.get(name); } return valor; } catch (Exception e) {
		 * throw new RuntimeException (
		 * "O Avaliador desta linguagem detectou um erro de tipos. A verificação de tipos pode não estar implementada, ou estar desabilitada"
		 * ); }
		 */

		try {
			LuaValor valor = (LuaValor) ambiente.get(name);
			return valor;
		} catch (VariavelNaoDeclaradaException e) {
			try {
				LuaValor valor = new LuaNil();
				ambiente.map(name, valor);
				return valor;
			} catch (VariavelJaDeclaradaException e1) {
				System.out.println(e1.getMessage());
			}
		}
		return null;
	}

	public void visit(String name) {
	}

	public void visit(Escopo scope) {
	}
}
