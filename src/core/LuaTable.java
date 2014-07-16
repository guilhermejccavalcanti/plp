package core;

import java.util.TreeMap;

import util.Tipo;
import util.TipoPrimitivo;

public class LuaTable extends LuaValorConcreto<TreeMap<LuaValor, LuaValor>> {

	public LuaTable(TreeMap<LuaValor, LuaValor> valor) {
		super(valor);
	}

	public Tipo getTipo() {
		return TipoPrimitivo.TABLE;
	}
}
