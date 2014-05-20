package core;

abstract public class LuaValor extends Exp {
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
