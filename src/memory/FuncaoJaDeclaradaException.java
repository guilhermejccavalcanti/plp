package memory;

import core.Nome;

public class FuncaoJaDeclaradaException extends IdentificadorJaDeclaradoException {

	private static final long serialVersionUID = 2554259260677239211L;

	public FuncaoJaDeclaradaException(Nome id) {
		super("Função " + id + " já declarada.");
	}
}
