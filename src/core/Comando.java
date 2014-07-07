package core;

import java.util.List;

import visitor.Visitor;
import core.Exp.VarExp;

abstract public class Comando {
	public abstract LuaValor accept(Visitor visitor);

	public static Comando bloco(Bloco bloco) {
		return bloco;
	}

	public static Comando atribuicao(List<VarExp> vars, List<Exp> exps) {
		return new Atribui(vars, exps);
	}

	public static Comando returncomando(List<Exp> exps) {
		return new Return(exps);
	}

	public static Comando whiledo(Exp exp, Bloco bloco) {
		return new WhileDo(exp, bloco);
	}

	public static Comando chamadafunc(Exp.ChamadaFunc chamadafunc) {
		return new ComandoChamadaFunc(chamadafunc);
	}

	public static Comando ifthenelse(Exp ifexp, Bloco ifbloco,
			List<Exp> elseifexps, List<Bloco> elseifblocos, Bloco elsebloco) {
		return new IfThenElse(ifexp, ifbloco, elseifexps, elseifblocos,
				elsebloco);
	}

	public static Comando fornumerico(String nome, Exp inicial, Exp limite,
			Exp passo, Bloco bloco) {
		return new ForNumerico(nome, inicial, limite, passo, bloco);
	}

	public static Comando deffuncao(FuncNome funcnome, FuncCorpo funccorpo) {
		return new FuncDef(funcnome, funccorpo);
	}

	public static Comando forgenerico(List<Nome> nomes, List<Exp> exps,
			Bloco bloco) {
		return new ForGenerico(nomes, exps, bloco);
	}

	public static Comando atribuicaolocal(List<Nome> nomes, List<Exp> valores) {
		return new AtribuicaoLocal(nomes, valores);
	}

	/*
	 * SUBCLASSES
	 */

	public static class Atribui extends Comando {
		public final List<VarExp> vars;
		public final List<Exp> exps;

		public Atribui(List<VarExp> vars, List<Exp> exps) {
			this.vars = vars;
			this.exps = exps;
		}

		public LuaValor accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}

	public static class Return extends Comando {
		public final List<Exp> valores;

		public Return(List<Exp> values) {
			this.valores = values;
		}

		public LuaValor accept(Visitor visitor) {
			return visitor.visit(this);
		}

		public int nreturns() {
			int n = valores != null ? valores.size() : 0;
			// if (n > 0 && ((Exp) valores.get(n - 1)).isvarargexp())
			// n = -1;
			return n;
		}
	}

	public static class WhileDo extends Comando {
		public final Exp exp;
		public final Bloco bloco;

		public WhileDo(Exp exp, Bloco bloco) {
			this.exp = exp;
			this.bloco = bloco;
		}

		public LuaValor accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}

	public static class ComandoChamadaFunc extends Comando {
		public final Exp.ChamadaFunc chamadafunc;

		public ComandoChamadaFunc(Exp.ChamadaFunc chamadafunc) {
			this.chamadafunc = chamadafunc;
		}

		public LuaValor accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}

	public static class IfThenElse extends Comando {
		public final Exp ifexp;
		public final Bloco ifbloco;
		public final List<Exp> elseifexps;
		public final List<Bloco> elseifblocos;
		public final Bloco elsebloco;

		public IfThenElse(Exp ifexp, Bloco ifbloco, List<Exp> elseifexps,
				List<Bloco> elseifblocos, Bloco elsebloco) {
			this.ifexp = ifexp;
			this.ifbloco = ifbloco;
			this.elseifexps = elseifexps;
			this.elseifblocos = elseifblocos;
			this.elsebloco = elsebloco;
		}

		public LuaValor accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}

	public static class ForNumerico extends Comando {
		public final Nome nome;
		public final Exp inicial, limite, passo;
		public final Bloco bloco;

		public ForNumerico(String nome, Exp inicial, Exp limite, Exp passo,
				Bloco bloco) {
			this.nome = new Nome(nome);
			this.inicial = inicial;
			this.limite = limite;
			this.passo = passo;
			this.bloco = bloco;
		}

		public LuaValor accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}

	public static class FuncDef extends Comando {
		public final FuncNome nome;
		public final FuncCorpo corpo;

		public FuncDef(FuncNome nome, FuncCorpo corpo) {
			this.nome = nome;
			this.corpo = corpo;
		}

		public LuaValor accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}

	public static class ForGenerico extends Comando {
		public List<Nome> nomes;
		public List<Exp> exps;
		public Bloco bloco;

		public ForGenerico(List<Nome> nomes, List<Exp> exps, Bloco bloco) {
			this.nomes = nomes;
			this.exps = exps;
			this.bloco = bloco;
		}

		public LuaValor accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}

	public static class AtribuicaoLocal extends Comando {
		public final List<Nome> nomes;
		public final List<Exp> valores;
		public AtribuicaoLocal(List<Nome> names, List<Exp> values) {
			this.nomes = names;
			this.valores = values;
		}

		public LuaValor accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}
}
