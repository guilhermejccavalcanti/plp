package core;

import util.Tipo;
import util.TipoPrimitivo;

public class LuaString extends LuaValorConcreto<String> implements Comparable<LuaString> {

	public LuaString(String valor) {
		super(valor);
	}

	public Tipo getTipo() {
		return TipoPrimitivo.STRING;
	}

	@Override
	public String toString() {
		return String.format("%s", super.toString());
	}

	@Override
	public int compareTo(LuaString o) {		
		return this.valor().toString().compareTo(o.toString());		
	}
}
