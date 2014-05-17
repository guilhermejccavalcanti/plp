package core;

import java.util.List;
import core.Exp.VarExp;

abstract public class Visitor {

	public void visit(Trecho trecho) {
		trecho.bloco.accept(this);
	};

	public void visit(Bloco bloco) {
		visit(bloco.escopo);
		if (bloco.comandos != null)
			for (int i = 0, n = bloco.comandos.size(); i < n; i++)
				((Comando) bloco.comandos.get(i)).accept(this);
	};

	public void visit(Comando.Assign comando) {
		visitVars(comando.vars);
		visitExps(comando.exps);
	}

	public void visit(Exp.BinopExp exp) {
		exp.lhs.accept(this);
		exp.rhs.accept(this);
	}

	public void visit(Exp.Constante exp) {
	}

	public void visit(Exp.FieldExp exp) {
		exp.lhs.accept(this);
		visit(exp.nome);
	}

	public void visit(Exp.NomeExp exp) {
		visit(exp.nome);
	}

	public void visit(Exp.ParensExp exp) {
		exp.exp.accept(this);
	}

	public void visit(Exp.UnopExp exp) {
		exp.rhs.accept(this);
	}

	public void visitVars(List<VarExp> vars) {
		if (vars != null)
			for (int i = 0, n = vars.size(); i < n; i++)
				((Exp.VarExp) vars.get(i)).accept(this);
	}

	public void visitExps(List<Exp> exps) {
		if (exps != null)
			for (int i = 0, n = exps.size(); i < n; i++)
				((Exp) exps.get(i)).accept(this);
	}

	public void visitNames(List<Nome> names) {
		if (names != null)
			for (int i = 0, n = names.size(); i < n; i++)
				visit((Nome) names.get(i));
	}
	
	public void visit(Exp.ChamadaFunc exp) {
		exp.lhs.accept(this);
		exp.args.accept(this);
	}

	public void visit(FuncArgs args) {
		visitExps(args.exps);
	}

	public void visit(Comando.ComandoChamadaFunc comando) {
		comando.chamadafunc.accept(this);
	}
	public void visit(Nome name) {
	}

	public void visit(String name) {
	}

	public void visit(NameScope scope) {
	}
}
