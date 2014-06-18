package memory;

import java.util.HashMap;
import java.util.Stack;
import core.LuaValor;
import core.Nome;

public class ContextoExecucaoImperativa extends ContextoExecucao implements AmbienteExecucaoImperativa {

	private ListaValor entrada;

	private ListaValor saida;

	public ContextoExecucaoImperativa(ListaValor entrada){
		super();
		this.entrada = entrada;
		this.saida = new ListaValor();        
	}

	public LuaValor read() throws EntradaVaziaException {
		if(entrada == null || entrada.getHead() == null) {
			throw new EntradaVaziaException();
		}
		LuaValor aux =  entrada.getHead();
		entrada = (ListaValor) entrada.getTail();
		return aux;
	}

	public ListaValor getSaida() {
		return saida;    
	}

	public void write(LuaValor v){
		saida = new ListaValor(v, saida);
	}

	public void changeValor(Nome idArg, LuaValor valorId) 	throws VariavelNaoDeclaradaException {    
		Object result = null;
		Stack<HashMap<Nome,LuaValor>> auxStack = new Stack<HashMap<Nome,LuaValor>>();
		Stack<HashMap<Nome,LuaValor>> stack = this.getPilha();

		while (result == null && !stack.empty()) {
			HashMap<Nome,LuaValor> aux = stack.pop();
			auxStack.push(aux);
			result = aux.get(idArg);
			if (result != null) {
				aux.put(idArg, valorId);
			}
		}
		while (!auxStack.empty()) {
			stack.push(auxStack.pop());
		}
		if (result == null) {
			throw new VariavelNaoDeclaradaException(idArg);
		}
	}
}

