package core;

import util.*;

abstract public class Exp {
	abstract public void accept(Visitor visitor);

	public static Exp constante(LuaValue value) {
		return new Constante(value);
	}

	public static Exp constantenumerica(String token) {		
		return new Constante(LuaValue.valueOf(token).tonumber());
	}

	public static Exp expunaria(int op, Exp rhs) {
		if (rhs instanceof BinopExp) {
			BinopExp b = (BinopExp) rhs;
			if (precedencia(op) > precedencia(b.op))
				return expbinaria(expunaria(op, b.lhs), b.op, b.rhs);
		}
		return new UnopExp(op, rhs);
	}

	public static Exp expbinaria(Exp lhs, int op, Exp rhs) {
		if (lhs instanceof UnopExp) {
			UnopExp u = (UnopExp) lhs;
			if (precedencia(op) > precedencia(u.op))
				return expunaria(u.op, expbinaria(u.rhs, op, rhs));
		}
		if (lhs instanceof BinopExp) {
			BinopExp b = (BinopExp) lhs;
			if ((precedencia(op) > precedencia(b.op)) ||
					((precedencia(op) == precedencia(b.op)) && isrightassoc(op)))
				return expbinaria(b.lhs, b.op, expbinaria(b.rhs, op, rhs));
		}
		if (rhs instanceof BinopExp) {
			BinopExp b = (BinopExp) rhs;
			if ( (precedencia(op) > precedencia(b.op)) ||
					((precedencia(op) == precedencia(b.op)) && !isrightassoc(op)))
				return expbinaria(expbinaria(lhs, op, b.lhs), b.op, b.rhs);
		}
		return new BinopExp(lhs, op, rhs);
	}
	
	/** foo(2,3) */
	public static ChamadaFunc functionop(ExpPrimaria lhs, FuncArgs args) {
		return new ChamadaFunc(lhs, args);
	}

	static boolean isrightassoc(int op) {
		switch (op) {
		case Lua.OP_CONCAT:
		case Lua.OP_POW: return true;
		default: return false;
		}
	}

	static int precedencia(int op) {
		switch ( op ) {
		case Lua.OP_OR: return 0;
		case Lua.OP_AND: return 1;
		case Lua.OP_LT: case Lua.OP_GT: case Lua.OP_LE: case Lua.OP_GE: case Lua.OP_NEQ: case Lua.OP_EQ: return 2;
		case Lua.OP_CONCAT: return 3;
		case Lua.OP_ADD: case Lua.OP_SUB: return 4;
		case Lua.OP_MUL: case Lua.OP_DIV: case Lua.OP_MOD: return 5;
		case Lua.OP_NOT: case Lua.OP_UNM: case Lua.OP_LEN: return 6;
		case Lua.OP_POW: return 7;
		default: throw new IllegalStateException("precedence of bad op "+op);
		}
	}	


	/** foo */
	public static NomeExp nomeprefix(String name) {
		return new NomeExp(name);
	}

	/** ( foo.bar ) */
	public static ParensExp parensprefix(Exp exp) {
		return new ParensExp(exp);
	}

	public boolean isvarexp() {
		return false;
	}

	abstract public static class ExpPrimaria extends Exp {
		public boolean isvarexp() {
			return false;
		}
		public boolean isfunccall() {
			return false;
		}
	}

	abstract public static class VarExp extends ExpPrimaria {
		public boolean isvarexp() {
			return true;
		}
		public void markHasAssignment() {
		}
	}

	public static class NomeExp extends VarExp {
		public final Nome nome;
		public NomeExp(String name) {
			this.nome = new Nome(name);
		}
		public void markHasAssignment() {
			nome.variavel.hasassignments = true;
		}
		public void accept(Visitor visitor) {
			visitor.visit(this);
		}
	}

	public static class ParensExp extends ExpPrimaria {
		public final Exp exp;
		public ParensExp(Exp exp) {
			this.exp = exp;
		}

		public void accept(Visitor visitor) {
			visitor.visit(this);
		}
	}

	public static class FieldExp extends VarExp {
		public final ExpPrimaria lhs;
		public final Nome nome;
		public FieldExp(ExpPrimaria lhs, String name) {
			this.lhs = lhs;
			this.nome = new Nome(name);
		}

		public void accept(Visitor visitor) {
			visitor.visit(this);
		}
	}

	public static class Constante extends Exp {
		public final LuaValue valor;
		public Constante(LuaValue value) {
			this.valor = value;
		}

		public void accept(Visitor visitor) {
			visitor.visit(this);
		}		
	}

	public static class UnopExp extends Exp {
		public final int op;
		public final Exp rhs;
		public UnopExp(int op, Exp rhs) {
			this.op = op;
			this.rhs = rhs;
		}

		public void accept(Visitor visitor) {
			visitor.visit(this);
		}		
	}

	public static class BinopExp extends Exp {
		public final Exp lhs,rhs;
		public final int op;
		public BinopExp(Exp lhs, int op, Exp rhs) {
			this.lhs = lhs;
			this.op = op;
			this.rhs = rhs;
		}

		public void accept(Visitor visitor) {
			visitor.visit(this);
		}		
	}
	
	public static class ChamadaFunc extends ExpPrimaria {
		public final ExpPrimaria lhs;
		public final FuncArgs args;
		
		public ChamadaFunc(ExpPrimaria lhs, FuncArgs args) {
			this.lhs = lhs;
			this.args = args;
		}

		public boolean isfunccall() {
			return true;
		}
		
		public void accept(Visitor visitor) {
			visitor.visit(this);
		}
		
		public boolean isvarargexp() {
			return true;
		}
	}
}
