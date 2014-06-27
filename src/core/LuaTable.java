package core;

import java.util.HashMap;

import util.Tipo;
import util.TipoPrimitivo;

public class LuaTable extends LuaValorConcreto<HashMap<LuaValor, LuaValor>> {

	public LuaTable(HashMap<LuaValor, LuaValor> valor) {
		super(valor);
	}

	public Tipo getTipo() {
		return TipoPrimitivo.TABLE;
	}
}
