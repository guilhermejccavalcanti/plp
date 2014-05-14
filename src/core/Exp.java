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
		if ( lhs instanceof UnopExp ) {
			UnopExp u = (UnopExp) lhs;
			if ( precedencia(op) > precedencia(u.op) )
				return expunaria( u.op, expbinaria( u.rhs, op, rhs ) );
		}
		if ( lhs instanceof BinopExp ) {
			BinopExp b = (BinopExp) lhs;
			if ( (precedencia(op) > precedencia(b.op)) ||
					((precedencia(op) == precedencia(b.op)) && isrightassoc(op)) )
				return expbinaria( b.lhs, b.op, expbinaria( b.rhs, op, rhs ) );
		}
		if ( rhs instanceof BinopExp ) {
			BinopExp b = (BinopExp) rhs;
			if ( (precedencia(op) > precedencia(b.op)) ||
					((precedencia(op) == precedencia(b.op)) && ! isrightassoc(op)) )
				return expbinaria( expbinaria( lhs, op, b.lhs ), b.op, b.rhs );
		}
		return new BinopExp(lhs, op, rhs);
	}

	static boolean isrightassoc(int op) {
		switch ( op ) {
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
	public static NameExp nomeprefix(String name) {
		return new NameExp(name);
	}

	/** ( foo.bar ) */
	public static ParensExp parensprefix(Exp exp) {
		return new ParensExp(exp);
	}

	public boolean isvarexp() {
		return false;
	}

	abstract public static class PrimaryExp extends Exp {
		public boolean isvarexp() {
			return false;
		}
		public boolean isfunccall() {
			return false;
		}
	}

	abstract public static class VarExp extends PrimaryExp {
		public boolean isvarexp() {
			return true;
		}
		public void markHasAssignment() {
		}
	}

	public static class NameExp extends VarExp {
		public final Nome name;
		public NameExp(String name) {
			this.name = new Nome(name);
		}
		public void markHasAssignment() {
			name.variavel.hasassignments = true;
		}
		public void accept(Visitor visitor) {
			visitor.visit(this);
		}
	}

	public static class ParensExp extends PrimaryExp {
		public final Exp exp;
		public ParensExp(Exp exp) {
			this.exp = exp;
		}

		public void accept(Visitor visitor) {
			visitor.visit(this);
		}
	}

	public static class FieldExp extends VarExp {
		public final PrimaryExp lhs;
		public final Nome name;
		public FieldExp(PrimaryExp lhs, String name) {
			this.lhs = lhs;
			this.name = new Nome(name);
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
}
