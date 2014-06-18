package memory;

import java.util.HashMap;
import java.util.Stack;

import util.Tipo;
import core.Nome;

public class ContextoCompilacao extends Contexto<Tipo> implements AmbienteCompilacao {

	public void changeTipo(Nome idArg, Tipo tipoId) throws VariavelNaoDeclaradaException {
		Object result = null;
		Stack<HashMap<Nome, Tipo>> auxStack = new Stack<HashMap<Nome, Tipo>>();
		Stack<HashMap<Nome, Tipo>> stack = this.getPilha();

		while (result == null && !stack.empty()) {
			HashMap<Nome, Tipo> aux = stack.pop();
			auxStack.push(aux);
			result = aux.get(idArg);
			if (result != null) {
				aux.put(idArg, tipoId);
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
