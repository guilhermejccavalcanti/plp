package memory;

public class IdentificadorNaoDeclaradoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IdentificadorNaoDeclaradoException(String msg){
		super(msg);
	}
	
	public IdentificadorNaoDeclaradoException(){
		super();
	}
}

