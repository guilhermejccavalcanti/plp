package memory;

import java.util.HashMap;
import java.util.Stack;

import core.Nome;
import core.ParList;

public class ContextoCompilacaoImperativa2 extends ContextoCompilacaoImperativa implements AmbienteCompilacaoImperativa2 {

	private Stack<HashMap<Nome, ParList>> pilhaParametrosProcedimento;

	public ContextoCompilacaoImperativa2(ListaValor entrada) {
		super(entrada);
		pilhaParametrosProcedimento = new Stack<HashMap<Nome, ParList>>();
	}

	public void incrementa() {
		super.incrementa();
		pilhaParametrosProcedimento.push(new HashMap<Nome, ParList>());
	}

	public void restaura() {
		super.restaura();
		pilhaParametrosProcedimento.pop();
	}

	public void mapParametrosProcedimento(Nome idArg, ParList parametrosId) throws FuncaoJaDeclaradaException {
		HashMap<Nome, ParList> aux = pilhaParametrosProcedimento.peek();
		if (aux.put(idArg, parametrosId) != null) {
			throw new FuncaoJaDeclaradaException(idArg);
		}
	}

	public ParList getParametrosProcedimento(Nome idArg) throws FuncaoNaoDeclaradaException {
		ParList result = null;
		Stack<HashMap<Nome, ParList>> auxStack = new Stack<HashMap<Nome, ParList>>();
		while (result == null && !pilhaParametrosProcedimento.empty()) {
			HashMap<Nome, ParList> aux = pilhaParametrosProcedimento.pop();
			auxStack.push(aux);
			result = aux.get(idArg);
		}
		while (!auxStack.empty()) {
			pilhaParametrosProcedimento.push(auxStack.pop());
		}
		if (result == null) {
			throw new FuncaoNaoDeclaradaException(idArg);
		}
		return result;
	}
}
