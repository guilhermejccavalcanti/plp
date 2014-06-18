package memory;

public class IdentificadorJaDeclaradoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IdentificadorJaDeclaradoException(String msg){
		super(msg);
	}
	public IdentificadorJaDeclaradoException(){
		super();
	}
}