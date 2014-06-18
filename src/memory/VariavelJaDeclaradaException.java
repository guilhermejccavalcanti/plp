package memory;

import core.Nome;

public class VariavelJaDeclaradaException extends IdentificadorJaDeclaradoException{
  
	private static final long serialVersionUID = 1L;

	public VariavelJaDeclaradaException(Nome id){
		super("Variável " + id + " já declarada.");
	}
}
