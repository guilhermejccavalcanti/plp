package memory;

import java.util.HashMap;
import java.util.Stack;

import core.Comando.FuncDef;
import core.LuaValor;
import core.Nome;

@SuppressWarnings("unused")
public class ContextoExecucaoImperativa2 extends ContextoExecucaoImperativa implements AmbienteExecucaoImperativa2 {

	private Stack<HashMap<Nome, FuncDef>> pilhaProcedimento;

	private ListaValor entrada;

	private ListaValor saida;

	public ContextoExecucaoImperativa2(ListaValor entrada) {
		super(entrada);
		pilhaProcedimento = new Stack<HashMap<Nome, FuncDef>>();
	}

	public void incrementa() {
		super.incrementa();
		pilhaProcedimento.push(new HashMap<Nome, FuncDef>());
	}

	public void restaura() {
		super.restaura();
		pilhaProcedimento.pop();
	}

	public void mapProcedimento(Nome idArg, FuncDef procedimentoId) throws FuncaoJaDeclaradaException {
		HashMap<Nome, FuncDef> aux = pilhaProcedimento.peek();
		if (aux.put(idArg, procedimentoId) != null) {
			throw new FuncaoJaDeclaradaException(idArg);
		}
	}

	public FuncDef getProcedimento(Nome idArg) throws FuncaoNaoDeclaradaException {
		FuncDef result = null;
		Stack<HashMap<Nome, FuncDef>> auxStack = new Stack<HashMap<Nome, FuncDef>>();
		while (result == null && !pilhaProcedimento.empty()) {
			HashMap<Nome, FuncDef> aux = pilhaProcedimento.pop();
			auxStack.push(aux);
			result = aux.get(idArg);
		}
		while (!auxStack.empty()) {
			pilhaProcedimento.push(auxStack.pop());
		}
		if (result == null) {
			throw new FuncaoNaoDeclaradaException(idArg);
		}
		return result;
	}
	
	public boolean contemVariavel(Nome idArg){
		boolean taNoEscopoAtual = false;
		Stack<HashMap<Nome,LuaValor>> auxStack = new Stack<HashMap<Nome,LuaValor>>();
		Stack<HashMap<Nome,LuaValor>> stack = this.getPilha();
		if (!stack.empty()) {
			HashMap<Nome,LuaValor> aux = stack.pop();
			auxStack.push(aux);
			if(aux.containsKey(idArg)){
				taNoEscopoAtual = true;
			}
		}
		while (!auxStack.empty()) {
			stack.push(auxStack.pop());
		}
		
		return (taNoEscopoAtual);
	}
}
