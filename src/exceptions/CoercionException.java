package exceptions;

public class CoercionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CoercionException(String msg){
		super(msg);
	}
	public CoercionException(){
		super();
	}
}