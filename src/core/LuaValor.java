package core;

import visitor.Visitor;

abstract public class LuaValor extends Exp {
	public LuaValor accept(Visitor visitor) {
		return visitor.visit(this);
	}
}
