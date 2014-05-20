package core;

import java.util.List;

public class ConstrutorTabela extends Exp {
	public List<CampoTabela> campos;

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
