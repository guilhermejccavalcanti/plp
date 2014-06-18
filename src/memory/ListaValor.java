package memory;

import core.LuaValor;

public class ListaValor extends Lista<LuaValor> {

	public ListaValor() {

	}

	public ListaValor(LuaValor valor) {
		super(valor, new ListaValor());
	}

	public ListaValor(LuaValor valor, ListaValor listaValor) {
		super(valor, listaValor);
	}
}
