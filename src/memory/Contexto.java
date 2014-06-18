package memory;

import java.util.HashMap;
import java.util.Stack;

import core.Nome;

public abstract class Contexto<T> {
	private Stack<HashMap<Nome,T>> pilha;

	public Contexto() {
		pilha = new Stack<HashMap<Nome,T>>();
	}

	public void incrementa(){
		pilha.push(new HashMap<Nome,T>());
	}

	public void restaura(){
		pilha.pop();
	}

	public void map(Nome idArg, T valorId) throws VariavelJaDeclaradaException {
		//		try {
		HashMap<Nome,T> aux =  pilha.peek();
		if (aux.put(idArg, valorId) != null) {
			//	    		throw new IdentificadorJaDeclaradoException();
			throw new VariavelJaDeclaradaException (idArg);
		}
		//		}
		//		catch (IdentificadorJaDeclaradoException e) {
		//			throw new VariavelJaDeclaradaException (idArg);
		//		}
	}

	public T get(Nome idArg) throws VariavelNaoDeclaradaException {
		try {
			T result = null;
			Stack<HashMap<Nome,T>> auxStack = new Stack<HashMap<Nome,T>>();
			while (result == null && !pilha.empty()) {
				HashMap<Nome,T> aux = pilha.pop();
				auxStack.push(aux);
				result = (T) aux.get(idArg);
			}
			while (!auxStack.empty()) {
				pilha.push(auxStack.pop());
			}
			if (result == null) {
				throw new IdentificadorNaoDeclaradoException();
			} 

			return result;
		} catch (IdentificadorNaoDeclaradoException e) {
			throw new VariavelNaoDeclaradaException(idArg);
			//return null;
		}
	}

	protected Stack<HashMap<Nome,T>> getPilha() {
		return pilha;
	}

	protected void setPilha(Stack<HashMap<Nome,T>> pilha) {
		this.pilha = pilha;
	}
}
