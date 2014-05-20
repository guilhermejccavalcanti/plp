package core;

import util.Tipo;
import util.TipoPrimitivo;

public class LuaNumber extends LuaValorConcreto<Integer> {

	public LuaNumber(int valor) {
		super(valor);
	}

	public Tipo getTipo() {
		return TipoPrimitivo.INTEIRO;
	}
}
