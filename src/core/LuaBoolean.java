package core;

import util.Tipo;
import util.TipoPrimitivo;

public class LuaBoolean extends LuaValorConcreto<Boolean> {

	public LuaBoolean(boolean valor) {
		super(valor);
	}

	public Tipo getTipo() {
		return TipoPrimitivo.BOOLEANO;
	}
}