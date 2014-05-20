package core;

import util.Tipo;
import util.TipoPrimitivo;

public class LuaNil extends LuaValorConcreto<Boolean> {

	public LuaNil() {
		super(null);
	}

	public Tipo getTipo() {
		return TipoPrimitivo.NIL;
	}
}