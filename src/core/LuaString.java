package core;

import util.Tipo;
import util.TipoPrimitivo;

public class LuaString extends LuaValorConcreto<String> {

	public LuaString(String valor) {
		super(valor);
	}

	public Tipo getTipo() {
		return TipoPrimitivo.STRING;
	}

	@Override
	public String toString() {
		return String.format("\"%s\"", super.toString());
	}
}
