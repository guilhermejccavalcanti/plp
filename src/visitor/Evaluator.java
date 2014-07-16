package visitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TreeMap;

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
import core.Comando.AtribuicaoLocal;
import core.Comando.FuncDef;
import core.ConstrutorTabela;
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
import exceptions.InvalidTypeLuaException;
import exceptions.ReturnLuaException;

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
		try {
			trecho.bloco.accept(this);
			this.ambiente.restaura();
		} catch (ReturnLuaException e) {
			// empty, stop program.
		}
	}

	public LuaValor visit(Bloco bloco) throws ReturnLuaException {
		LuaValor retorno = new LuaNil();
		if (bloco.comandos != null) {
			
			for (int i = 0, n = bloco.comandos.size(); i < n; i++) {
				
				if (bloco.comandos.get(i) instanceof Comando.Return){					
					retorno = ((Comando.Return) bloco.comandos.get(i)).accept(this);						
					throw new ReturnLuaException(retorno);					
				}				
				else if(bloco.comandos.get(i) instanceof Bloco) {
					this.ambiente.incrementa();
					retorno = ((Comando) bloco.comandos.get(i)).accept(this);
					this.ambiente.restaura();
				}else
					retorno = ((Comando) bloco.comandos.get(i)).accept(this);
			}				
		}
		return retorno;
	}

	public LuaValor visit(Comando.Atribui comando) {
		try {
			List<LuaValor> valores = visitExps(comando.exps);
			List<NomeExp> nomes = visitVars(comando.vars);
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

	public LuaValor visit(Exp.BinopExp exp) {
		LuaValor nvalor = new LuaNil();
		Integer ilInteger;
		Integer rlInterger;
		boolean ilBoolean;
		String ilString;
		String rlString;

		switch (exp.op) {
		case LuaOps.OP_OR:
			ilBoolean 	= this.convertToLuaBoolean(exp.lhs.accept(this));

			if(ilBoolean){
				nvalor = exp.lhs.accept(this);
			}else{
				nvalor = exp.rhs.accept(this);
			}
			break;
		case LuaOps.OP_AND:
			ilBoolean 	= this.convertToLuaBoolean(exp.lhs.accept(this));

			if(!ilBoolean){
				nvalor = exp.lhs.accept(this);
			}else{
				nvalor = exp.rhs.accept(this);
			}
			break;
		case LuaOps.OP_LT: // "<" 
			nvalor = this.evaluateLessAndEqualOperation(exp, false);
			break;			
		case LuaOps.OP_GT: // ">"
			nvalor = this.evaluateGreaterAndEqualOperation(exp, false);
			break;			
		case LuaOps.OP_LE: // "<=" 
			nvalor = this.evaluateLessAndEqualOperation(exp, true);
			break;
		case LuaOps.OP_GE: // ">="
			nvalor = this.evaluateGreaterAndEqualOperation(exp, true);
			break;
		case LuaOps.OP_NEQ: // "~="
			nvalor = this.evaluateNotEqualOperation(exp);
			break;
		case LuaOps.OP_EQ: // "=="
			nvalor = this.evaluateEqualOperation(exp);
			break;
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
		LuaValor nvalor = null;
		LuaValor valorVar = null;
		switch (exp.op) {
		case LuaOps.OP_UNM: // "-"
			LuaNumber rvalorNumber = (LuaNumber) exp.rhs.accept(this);
			nvalor = new LuaNumber(-rvalorNumber.valor());
			return nvalor;
		case LuaOps.OP_NOT: // <NOT>

			valorVar = exp.rhs.accept(this);

			if(valorVar instanceof LuaBoolean){

				nvalor = new LuaBoolean(!((LuaBoolean) valorVar).valor());

			}else{
				nvalor = new LuaBoolean(false);
			}		

			return nvalor;

		case LuaOps.OP_LEN: // "#

			valorVar = exp.rhs.accept(this);

			if(valorVar instanceof LuaString){				
				nvalor = new LuaNumber(valorVar.toString().length());

			}else if(valorVar instanceof LuaTable){				
				nvalor = new LuaNumber(((LuaTable) valorVar).valor().size());

			}else{
				throw new InvalidTypeLuaException("Incorrect Lua type: " + exp.rhs.accept(this).getClass());
			}			

			return nvalor;


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
					if (!((AmbienteExecucaoImperativa2) ambiente).contemVariavel(nome.nome))
						((AmbienteExecucaoImperativa2)ambiente).mapGlobal(nome.nome, new LuaNil());
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
				visit((Nome)nomes.get(i));
	}

	public void visitNomesLocais(List<Nome> nomes) {
		if (nomes != null)
			for (int i = 0, n = nomes.size(); i < n; i++)
				visitNomeLocal((Nome)nomes.get(i));
	}

	public LuaValor visit(Exp.ChamadaFunc exp) {
		LuaValor retorno = new LuaNil();
		try {
			if (exp.lhs instanceof NomeExp) {
				Nome nomeFunc = ((NomeExp) exp.lhs).nome;
				if (nomeFunc.nome.equals("print")) {
					List<LuaValor> valores = exp.args.accept(this);
					printValores(valores);
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
					try {
						retorno = corpo.bloco.accept(this);
					} catch (ReturnLuaException e) {
						retorno = e.getLuaValor();
					}
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

	public LuaValor visit(Comando.WhileDo comando) throws ReturnLuaException {
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

	public LuaValor visit(Comando.IfThenElse comando) throws ReturnLuaException {
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

	public LuaValor visitElseIfs(Queue<Exp> elseifexps, Queue<Bloco> elseifblocos, Comando elsebloco) throws ReturnLuaException {
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

	public LuaValor visit(Comando.ForNumerico comando) throws ReturnLuaException {
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
			for (int i = inicial.valor(); i <= limite.valor(); i = i + (passoInt))
				this.executaBlocoFor(comando, i);
			
		} else if (passoInt <= 0 && inicial.valor() >= limite.valor()) {
			for (int i = inicial.valor(); i >= limite.valor(); i = i+ (passoInt))
				retorno = this.executaBlocoFor(comando, i);
		}			

		return retorno;
	}

	private LuaValor executaBlocoFor(Comando.ForNumerico comando, int i) throws ReturnLuaException {
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
		TreeMap<LuaValor, LuaValor> tabelaMap = new TreeMap<LuaValor, LuaValor>();
		int indice = 1;
		if (tabela.campos != null) {
			for (int i = 0, n = tabela.campos.size(); i < n; i++) {
				if (tabela.campos.get(i).nome != null) {
					LuaString keyTable = new LuaString(tabela.campos.get(i).nome);

					// campo de tabela com chave (indice)
					tabelaMap.put(keyTable,tabela.campos.get(i).rhs.accept(this));

				} else {
					// campo de tabela sem chave (adicionar indice)
					tabelaMap.put(new LuaString(""+indice), tabela.campos.get(i).rhs.accept(this));
					indice = indice + 1;
				}
			}
		}
		
		return new LuaTable(tabelaMap);
	}

	public LuaValor visit(CampoTabela campo) {
		return new LuaNil();
	}

	public LuaValor visit(Comando.Return comando) {
		List<LuaValor> luaValorList = visitExps(comando.valores);		
		LuaValor luaValor = new LuaNil();
		
		if(luaValorList.size() > 0){
			luaValor = luaValorList.get(0);
		}
		
		return luaValor;
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

	public LuaValor visitNomeLocal(Nome name) {
		try {
			LuaValor valor = (LuaValor) ((AmbienteExecucaoImperativa2) ambiente).getLocal(name);
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

	public LuaValor visit(Exp.IndexExp exp) {
		LuaValor valorIndex = new LuaNil();
		if (exp.lhs.accept(this) instanceof LuaTable) {
			TreeMap<LuaValor, LuaValor> luaTable = ((LuaTable) exp.lhs.accept(this)).valor();
			for (LuaValor keyValor : luaTable.keySet()) {
				if (exp.exp.accept(this) instanceof LuaString
						&& !this.isNumber(((LuaString) exp.exp.accept(this)).valor())
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
		}else{
			throw new InvalidTypeLuaException("Incorrect Lua type: " + exp.lhs.accept(this).getClass());
		}
		return valorIndex;
	}

	public LuaValor visit(Comando.ForGenerico comando) throws ReturnLuaException {
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
							((AmbienteExecucaoImperativa2) ambiente).changeValor(comando.nomes.get(1), table.valor().get(keyValor));
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

	@Override
	public LuaValor visit(AtribuicaoLocal comando) {
		try {
			List<LuaValor> valores = visitExps(comando.valores);
			visitNomesLocais(comando.nomes);
			try {
				for (int i = 0; i < valores.size(); i++) {
					LuaValor valor = valores.get(i);
					Nome nome = comando.nomes.get(i);
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

	public boolean isNumber(String s) {
		return s.matches("[0-9]*");
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

	private LuaValor evaluateLessAndEqualOperation(Exp.BinopExp exp, boolean isCompareEquals) {
		LuaValor nvalor = null;
		LuaValor lvalor;
		LuaValor rvalor;
		lvalor = exp.lhs.accept(this);
		rvalor = exp.rhs.accept(this);

		if(this.checaTipo(lvalor, rvalor)){	

			LuaValor ilValor = exp.lhs.accept(this);
			LuaValor rlValor = exp.rhs.accept(this);

			if(ilValor instanceof LuaNumber && rlValor instanceof LuaNumber){
				// Compare Number
				boolean result = ((LuaNumber)ilValor).valor() < ((LuaNumber)rlValor).valor();	

				if(!result && isCompareEquals){
					result = ((LuaNumber)ilValor).valor() == ((LuaNumber)rlValor).valor();	
				}

				nvalor = new LuaBoolean(result);

			}else if (ilValor instanceof LuaString && rlValor instanceof LuaString){
				// Compare Strings
				String ilString = ((LuaString)exp.lhs.accept(this)).valor();
				String rlString = ((LuaString)exp.rhs.accept(this)).valor();

				int result = ilString.compareTo(rlString);

				if(isCompareEquals && result == 0){
					nvalor = new LuaBoolean(true);	

				}else if(!isCompareEquals && result == 0){
					nvalor = new LuaBoolean(false);	

				}else if(result <= -1){
					nvalor = new LuaBoolean(true);	

				}else if(result >= 1){
					nvalor = new LuaBoolean(false);
				}				
			}else{
				throw new InvalidTypeLuaException("Incorrect Lua type: " + exp.rhs.accept(this).getClass());
			}
		}else{
			throw new InvalidTypeLuaException("Incorrect Lua type: " + exp.rhs.accept(this).getClass());
		}
		return nvalor;
	}

	private LuaValor evaluateGreaterAndEqualOperation(Exp.BinopExp exp, boolean isCompareEquals) {
		LuaValor nvalor = null;
		LuaValor lvalor;
		LuaValor rvalor;
		lvalor = exp.lhs.accept(this);
		rvalor = exp.rhs.accept(this);

		if(this.checaTipo(lvalor, rvalor)){	
			LuaValor ilValor = exp.lhs.accept(this);
			LuaValor rlValor = exp.rhs.accept(this);

			if(ilValor instanceof LuaNumber && rlValor instanceof LuaNumber){

				// Compare Number
				boolean result = ((LuaNumber)ilValor).valor() > ((LuaNumber)rlValor).valor();	

				if(!result && isCompareEquals){
					result = ((LuaNumber)ilValor).valor() == ((LuaNumber)rlValor).valor();	
				}

				nvalor = new LuaBoolean(result);

			}else if (ilValor instanceof LuaString && rlValor instanceof LuaString){
				// Compare Strings
				String ilString = ((LuaString)exp.lhs.accept(this)).valor();
				String rlString = ((LuaString)exp.rhs.accept(this)).valor();

				int result = ilString.compareTo(rlString);

				if(isCompareEquals && result == 0){
					nvalor = new LuaBoolean(true);	

				}else if(!isCompareEquals && result == 0){
					nvalor = new LuaBoolean(false);	

				}else if(result <= -1){
					nvalor = new LuaBoolean(false);	

				}else if(result >= 1){
					nvalor = new LuaBoolean(true);
				}				
			}else{
				throw new InvalidTypeLuaException("Incorrect Lua type: " + exp.rhs.accept(this).getClass());
			}

		}else{
			throw new InvalidTypeLuaException("Incorrect Lua type: " + exp.rhs.accept(this).getClass());
		}
		return nvalor;
	}

	private LuaValor evaluateEqualOperation(Exp.BinopExp exp) {
		LuaValor nvalor;
		LuaValor lvalor;
		LuaValor rvalor;
		lvalor = exp.lhs.accept(this);
		rvalor = exp.rhs.accept(this);

		if(this.checaTipo(lvalor, rvalor)){
			String lhs = lvalor.toString();
			String rhs = rvalor.toString();
			nvalor = new LuaBoolean(lhs.equals(rhs));				
		}else{
			nvalor = new LuaBoolean(false);		
		}
		return nvalor;
	}

	private LuaValor evaluateNotEqualOperation(Exp.BinopExp exp) {
		LuaValor nvalor;
		LuaValor lvalor;
		LuaValor rvalor;
		lvalor = exp.lhs.accept(this);
		rvalor = exp.rhs.accept(this);

		if(this.checaTipo(lvalor, rvalor)){
			String lhs = lvalor.toString();
			String rhs = rvalor.toString();
			nvalor = new LuaBoolean(!lhs.equals(rhs));				
		}else{
			nvalor = new LuaBoolean(true);		
		}
		return nvalor;
	}

	private boolean checaTipo(LuaValor luaValor1, LuaValor luaValor2) {
		boolean result = false;
		if(luaValor1.getClass().equals(luaValor2.getClass())){
			result = true;		
		}
		return result;
	}

	private void printValores(List<LuaValor> valores) {
		StringBuffer bs = new StringBuffer();
		for (LuaValor valor : valores) 
			bs.append(valor+" ");
		System.out.println(bs);
	}
}
