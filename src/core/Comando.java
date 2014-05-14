package core;

import java.util.List;

import core.Exp.VarExp;

abstract public class Comando{
	public abstract void accept(Visitor visitor);

	public static Comando bloco(Bloco bloco) {
		return bloco;
	}

	public static class Assign extends Comando {
		public final List<VarExp> vars;
		public final List<Exp> exps;
		
		public Assign(List<VarExp> vars, List<Exp> exps) {
			this.vars = vars;
			this.exps = exps;
		}

		public void accept(Visitor visitor) {
			visitor.visit(this);
		}
	}
}
