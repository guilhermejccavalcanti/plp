package core;

import java.util.List;

import core.Exp.VarExp;

abstract public class Comando {
	public abstract void accept(Visitor visitor);

	public static Comando bloco(Bloco bloco) {
		return bloco;
	}

	public static Comando assignment(List<VarExp> vars, List<Exp> exps) {
		return new Assign(vars, exps);
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

	public static Comando chamadafunc(Exp.ChamadaFunc chamadafunc) {
		return new ComandoChamadaFunc(chamadafunc);
	}

	public static class ComandoChamadaFunc extends Comando {
		public final Exp.ChamadaFunc chamadafunc;

		public ComandoChamadaFunc(Exp.ChamadaFunc chamadafunc) {
			this.chamadafunc = chamadafunc;
		}

		public void accept(Visitor visitor) {
			visitor.visit(this);
		}
	}
}
