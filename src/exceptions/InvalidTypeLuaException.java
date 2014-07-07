package exceptions;

public class InvalidTypeLuaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidTypeLuaException(String msg){
		super(msg);
	}
	public InvalidTypeLuaException(){
		super();
	}
}