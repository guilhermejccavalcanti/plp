package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import core.Exp.ChamadaFunc;
import core.Exp.NomeExp;
import core.Exp.VarExp;
import core.FuncArgs;
import core.FuncCorpo;
import core.LuaBoolean;
import core.LuaNil;
import core.LuaNumber;
import core.LuaString;
import core.LuaTable;
import core.LuaValor;
import core.Nome;
import core.ParList;
import core.Trecho;
import exceptions.CoercionException;

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
		LuaValor retorno = new LuaNil();
		visit(bloco.escopo);
		if (bloco.comandos != null) {
			for (int i = 0, n = bloco.comandos.size(); i < n; i++) {
				if (bloco.comandos.get(i) instanceof Comando.Return)
					return ((Comando.Return) bloco.comandos.get(i)).accept(this);
				else
					retorno = ((Comando) bloco.comandos.get(i)).accept(this);
			}
		}
		return retorno;
	}

	public LuaValor visit(Comando.Atribui comando) {
		try {
			List<NomeExp> nomes = visitVars(comando.vars);
			List<LuaValor> valores = visitExps(comando.exps);
			try {
				for (int i = 0; i < valores.size(); i++) {
					LuaValor valor = valores.get(i);
					Nome nome = nomes.get(i).nome;
					((AmbienteExecucaoImperativa2) ambiente).changeValor(nome,valor);
				}
			} catch (IndexOutOfBoundsException e) {
				return null;
			}
		} catch (VariavelNaoDeclaradaException e) {
			e.printStackTrace();
		}
		return new LuaNil();
	}

	// ATUALMENTE SAO IMPLEMENTAS ALGUMAS OPERACOES PARA NUMBER, DEVERAO
	// SUPORTAR
	// MULTIPLOS TIPOS
	public LuaValor visit(Exp.BinopExp exp) {
		LuaValor nvalor = new LuaNil();
		LuaValor lvalor;
		LuaValor rvalor;
		Integer ilInteger;
		Integer rlInterger;
		boolean ilBoolean;
		boolean rlBoolean;
		String ilString;
		String rlString;

		switch (exp.op) {
		case LuaOps.OP_OR:
			ilBoolean 	= this.convertToLuaBoolean(exp.lhs.accept(this));
			rlBoolean 	= this.convertToLuaBoolean(exp.rhs.accept(this));
			nvalor = new LuaBoolean(ilBoolean || rlBoolean);
			break;
		case LuaOps.OP_AND:
			ilBoolean 	= this.convertToLuaBoolean(exp.lhs.accept(this));
			rlBoolean 	= this.convertToLuaBoolean(exp.rhs.accept(this));
			nvalor = new LuaBoolean(ilBoolean && rlBoolean);
			break;
		case LuaOps.OP_LT:
			return nvalor;
		case LuaOps.OP_GT:
			return nvalor;
		case LuaOps.OP_LE:
			return nvalor;
		case LuaOps.OP_GE:
			return nvalor;
		case LuaOps.OP_NEQ:
			return nvalor;
		case LuaOps.OP_EQ:
			lvalor = exp.lhs.accept(this);
			rvalor = exp.rhs.accept(this);
			String lhs = lvalor.toString();
			String rhs = rvalor.toString();
			nvalor = new LuaBoolean(lhs.equals(rhs));
			return nvalor;
			//TODO COERÇÃO
		case LuaOps.OP_CONCAT:
			ilString 	= this.convertToLuaString(exp.lhs.accept(this));
			rlString 	= this.convertToLuaString(exp.rhs.accept(this));
			nvalor = new LuaString(ilString.concat(rlString));
			break;
		case LuaOps.OP_ADD:
			ilInteger 	= this.convertToLuaNumber(exp.lhs.accept(this));
			rlInterger 	= this.convertToLuaNumber(exp.rhs.accept(this));
			nvalor = new LuaNumber(ilInteger + rlInterger);
			break;
		case LuaOps.OP_SUB:
			ilInteger 	= this.convertToLuaNumber(exp.lhs.accept(this));
			rlInterger 	= this.convertToLuaNumber(exp.rhs.accept(this));
			nvalor = new LuaNumber(ilInteger - rlInterger);
			break;
		case LuaOps.OP_MUL:
			ilInteger 	= this.convertToLuaNumber(exp.lhs.accept(this));
			rlInterger 	= this.convertToLuaNumber(exp.rhs.accept(this));
			nvalor = new LuaNumber(ilInteger * rlInterger);
			break;
		case LuaOps.OP_DIV:
			ilInteger 	= this.convertToLuaNumber(exp.lhs.accept(this));
			rlInterger 	= this.convertToLuaNumber(exp.rhs.accept(this));
			nvalor = new LuaNumber(ilInteger / rlInterger);
			break;
		case LuaOps.OP_MOD:
			ilInteger 	= this.convertToLuaNumber(exp.lhs.accept(this));
			rlInterger 	= this.convertToLuaNumber(exp.rhs.accept(this));
			nvalor = new LuaNumber(ilInteger % rlInterger);
			break;
		case LuaOps.OP_POW:
			ilInteger 	= this.convertToLuaNumber(exp.lhs.accept(this));
			rlInterger 	= this.convertToLuaNumber(exp.rhs.accept(this));
			nvalor = new LuaNumber((int)(Math.pow(ilInteger,rlInterger)));
			break;			
		default:
			throw new IllegalStateException("Operacao nao implementada.");
		}
		return nvalor;
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
		switch (exp.op) {
		case LuaOps.OP_UNM: // "-"
			LuaNumber rvalorNumber = (LuaNumber) exp.rhs.accept(this);
			LuaValor nvalor = new LuaNumber(-rvalorNumber.valor());
			return nvalor;
		case LuaOps.OP_NOT: // <NOT>
		case LuaOps.OP_LEN: // "#"
		default:
			throw new IllegalStateException("Operacao nao implementada.");
		}
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
		LuaValor retorno = new LuaNil();
		while (((LuaBoolean) comando.exp.accept(this)).valor()) {
			this.ambiente.incrementa();
			retorno = comando.bloco.accept(this);
			this.ambiente.restaura();
		}
		return retorno;
	}

	public LuaValor visit(Comando.ComandoChamadaFunc comando) {
		return comando.chamadafunc.accept(this);
	}

	public LuaValor visit(Comando.IfThenElse comando) {
		LuaValor retorno = new LuaNil();
		if (((LuaBoolean) comando.ifexp.accept(this)).valor()) {
			this.ambiente.incrementa();
			retorno = comando.ifbloco.accept(this);
			this.ambiente.restaura();
		} else {

			Queue<Exp> elseifexps = new LinkedList<Exp>();
			if (comando.elseifexps != null) {
				elseifexps.addAll(comando.elseifexps);
			}

			Queue<Bloco> elseifblocos = new LinkedList<Bloco>();
			if (comando.elseifblocos != null) {
				elseifblocos.addAll(comando.elseifblocos);
			}

			retorno = visitElseIfs(elseifexps, elseifblocos, comando.elsebloco);
		}
		return retorno;
	}

	public LuaValor visitElseIfs(Queue<Exp> elseifexps, Queue<Bloco> elseifblocos, Comando elsebloco) {
		LuaValor retorno = new LuaNil();
		Exp elseifexp = elseifexps.poll();
		Bloco elseifbloco = elseifblocos.poll();
		if (elseifbloco != null) {
			if (((LuaBoolean) elseifexp.accept(this)).valor()) {
				this.ambiente.incrementa();
				retorno = elseifbloco.accept(this);
				this.ambiente.restaura();
			} else {
				retorno = visitElseIfs(elseifexps, elseifblocos, elsebloco);
			}
		} else {
			if (elsebloco != null) {
				this.ambiente.incrementa();
				retorno = elsebloco.accept(this);
				this.ambiente.restaura();
			}
		}
		return retorno;
	}

	public LuaValor visit(Comando.ForNumerico comando) {
		LuaValor retorno = new LuaNil();
		LuaNumber limite = (LuaNumber) comando.limite.accept(this);
		LuaNumber inicial = (LuaNumber) comando.inicial.accept(this);

		// Adicionar a variavel e valor no ambiante (pilha).
		try {
			ambiente.map(comando.nome, inicial);
		} catch (VariavelJaDeclaradaException e) {
			System.out.println(e.getMessage());
		}

		int passoInt = 1;
		if (comando.passo != null) {
			LuaNumber passo = (LuaNumber) comando.passo.accept(this);
			passoInt = passo.valor();
		}

		if (passoInt >= 0 && inicial.valor() <= limite.valor()) {
			for (int i = inicial.valor(); i <= limite.valor(); i = i
					+ (passoInt))
				this.executaBlocoFor(comando, i);

		} else if (passoInt <= 0 && inicial.valor() >= limite.valor()) {
			for (int i = inicial.valor(); i >= limite.valor(); i = i+ (passoInt))
				retorno = this.executaBlocoFor(comando, i);
		}

		return retorno;
	}

	private LuaValor executaBlocoFor(Comando.ForNumerico comando, int i) {
		LuaValor retorno = new LuaNil();
		try {
			// Atualizar a variavel e valor no ambiante (pilha)
			((AmbienteExecucaoImperativa2) ambiente).changeValor(comando.nome,new LuaNumber(i));
		} catch (VariavelNaoDeclaradaException e) {
			System.out.println(e.getMessage());
		}

		// Bloco
		this.ambiente.incrementa();
		retorno = comando.bloco.accept(this);
		this.ambiente.restaura();
		return retorno;
	}

	public LuaValor visit(Comando.FuncDef comando) {
		try {
			Nome funcNome = comando.nome.nome;
			FuncDef funcDef = comando;
			((AmbienteExecucaoImperativa2) ambiente).mapProcedimento(funcNome,funcDef);
		} catch (FuncaoJaDeclaradaException e) {
			System.out.println(e.getMessage());
		}
		return new LuaNil();
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
		HashMap<LuaValor, LuaValor> tabelaMap = new LinkedHashMap<LuaValor, LuaValor>();
		LuaNumber indice = new LuaNumber(1);
		if (tabela.campos != null) {
			for (int i = 0, n = tabela.campos.size(); i < n; i++) {
				if (tabela.campos.get(i).nome != null) {
					LuaString keyTable = new LuaString(tabela.campos.get(i).nome);

					// campo de tabela com chave (indice)
					tabelaMap.put(keyTable,tabela.campos.get(i).rhs.accept(this));

				} else {
					// campo de tabela sem chave (adicionar indice)
					tabelaMap.put(indice, tabela.campos.get(i).rhs.accept(this));
					indice = new LuaNumber(indice.valor() + 1);
				}
			}
		}
		return new LuaTable(tabelaMap);
	}

	public LuaValor visit(CampoTabela campo) {
		return new LuaNil();
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
		return new LuaNil();
	}

	public void visit(String name) {
	}

	public void visit(Escopo scope) {
	}

	public LuaValor visit(Exp.IndexExp exp) {
		LuaValor valorIndex = new LuaNil();
		if (exp.lhs.accept(this) instanceof LuaTable) {
			HashMap<LuaValor, LuaValor> luaTable = ((LuaTable) exp.lhs.accept(this)).valor();
			for (LuaValor keyValor : luaTable.keySet()) {
				if (exp.exp.accept(this) instanceof LuaString
						&& !this.isNumer(((LuaString) exp.exp.accept(this)).valor())
						&& ((LuaString) exp.exp.accept(this)).valor().toString().equals(keyValor.toString())) {
					valorIndex = luaTable.get(keyValor);
					break;
				}
				if (exp.exp.accept(this) instanceof LuaNumber
						&& ((LuaNumber) exp.exp.accept(this)).valor().toString().equals(keyValor.toString())) {
					valorIndex = luaTable.get(keyValor);
					break;
				}
			}
		}
		return valorIndex;
	}

	public boolean isNumer(String s) {
		return s.matches("[0-9]*");
	}

	public LuaValor visit(Comando.ForGenerico comando) {
		LuaValor retorno = new LuaNil();
		ChamadaFunc chamadaFunc = ((ChamadaFunc) comando.exps.get(0));
		if (chamadaFunc != null	&& chamadaFunc.args.accept(this).get(0) instanceof LuaTable) {
			LuaTable table = (LuaTable) chamadaFunc.args.accept(this).get(0);
			for (LuaValor keyValor : table.valor().keySet()) {
				try {
					ambiente.map(comando.nomes.get(0), keyValor);
				} catch (VariavelJaDeclaradaException e) {
					try {
						((AmbienteExecucaoImperativa2) ambiente).changeValor(comando.nomes.get(0), keyValor);
					} catch (VariavelNaoDeclaradaException e1) {
						e.getMessage();
					}
				}
				if (comando.nomes.size() > 1) {
					try {
						ambiente.map(comando.nomes.get(1),table.valor().get(keyValor));

					} catch (VariavelJaDeclaradaException e) {
						try {
							((AmbienteExecucaoImperativa2) ambiente)
							.changeValor(comando.nomes.get(1), table
									.valor().get(keyValor));
						} catch (VariavelNaoDeclaradaException e1) {
							e.getMessage();
						}
					}
				}
				// Bloco
				this.ambiente.incrementa();
				retorno = comando.bloco.accept(this);
				this.ambiente.restaura();
			}
		}
		return retorno;
	}

	private Integer convertToLuaNumber(LuaValor accept) throws CoercionException {
		try{
			if(accept instanceof LuaNumber)
				return ((LuaNumber)accept).valor();
			else if (accept instanceof LuaString)
				return Integer.valueOf(((LuaString)accept).valor());
			else
				throw new CoercionException("Erro: tentativa de operar tipos inválidos.");
		}catch(NumberFormatException n){
			throw new CoercionException("Erro: tentativa de operar tipos inválidos.");
		}
	}

	private Boolean convertToLuaBoolean(LuaValor accept) {
		if(accept instanceof LuaBoolean)
			return ((LuaBoolean)accept).valor();
		else if (accept instanceof LuaNil)
			return false;
		else
			return true;
	}

	private String convertToLuaString(LuaValor accept) throws CoercionException {
		if(accept instanceof LuaString)
			return ((LuaString)accept).valor();
		else if (accept instanceof LuaNumber)
			return String.valueOf(((LuaNumber)accept).valor());
		else
			throw new CoercionException("Erro: tentativa de operar tipos inválidos.");
	}
}
