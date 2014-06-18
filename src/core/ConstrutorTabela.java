package core;

import java.util.List;

import visitor.Visitor;

public class ConstrutorTabela extends Exp {
	public List<CampoTabela> campos;

	public LuaValor accept(Visitor visitor) {
		return visitor.visit(this);
	}
}
