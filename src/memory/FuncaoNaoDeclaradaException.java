package memory;

import core.Nome;

public class FuncaoNaoDeclaradaException extends	IdentificadorJaDeclaradoException {

	private static final long serialVersionUID = -3352450669259525953L;

	public FuncaoNaoDeclaradaException(Nome id) {
		super("Função " + id + " não declarada.");
	}
}
