package core;

import java.util.ArrayList;
import java.util.List;

public class Bloco extends Comando{
	public List<Comando> comandos = new ArrayList<Comando>();
	public Escopo escopo;
	
	public void add(Comando c) {
		if (c == null)
			return;
		comandos.add(c);
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
