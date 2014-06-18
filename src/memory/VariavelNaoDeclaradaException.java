package memory;

import core.Nome;

public class VariavelNaoDeclaradaException extends IdentificadorNaoDeclaradoException{

	private static final long serialVersionUID = 1L;

	public VariavelNaoDeclaradaException(Nome id){
		super("Varíavel " + id + " não declarada.");
	}
}
