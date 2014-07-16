package exceptions;

import core.LuaValor;

public class ReturnLuaException extends Exception {

	private LuaValor luaValor;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ReturnLuaException(LuaValor luaValor){
		super();
		this.luaValor = luaValor;
		
	}
	public LuaValor getLuaValor() {
		return luaValor;
	}
	public void setLuaValor(LuaValor luaValor) {
		this.luaValor = luaValor;
	}
}