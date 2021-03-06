package core;

import java.util.ArrayList;
import java.util.List;

import exceptions.ReturnLuaException;
import visitor.Visitor;

public class Bloco extends Comando{
	public List<Comando> comandos = new ArrayList<Comando>();
	
	public void add(Comando c) {
		if (c == null)
			return;
		comandos.add(c);
	}

	public LuaValor accept(Visitor visitor) throws ReturnLuaException {
		return visitor.visit(this);
	}
}
