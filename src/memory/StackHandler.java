package memory;

import java.util.Map;
import java.util.Stack;

import core.Nome;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class StackHandler {
	
	private StackHandler() {
	}
	
	public static Object getFromId (Stack stack, Nome id) throws IdentificadorNaoDeclaradoException {
		Object result = null;
		Stack auxStack = new Stack();
		while (result == null && !stack.empty()) {
			Map aux = (Map) stack.pop();
			auxStack.push(aux);
			result = aux.get(id);
		}
		while (!auxStack.empty()) {
			stack.push(auxStack.pop());
		}
		if (result == null) {
			throw new IdentificadorNaoDeclaradoException();
		} 
		return result;
	}

			
	public static void mapIdObject (Stack stack, Nome id, Object object) throws IdentificadorJaDeclaradoException {
		Map aux = (Map) stack.peek();
    	if (aux.put(id, object) != null) {
    		throw new IdentificadorJaDeclaradoException();
    	}
	}
}
