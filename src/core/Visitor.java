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

	public void visit(Comando.Atribui comando) {
		visitVars(comando.vars);
		visitExps(comando.exps);
	}

	public void visit(Exp.BinopExp exp) {
		exp.lhs.accept(this);
		exp.rhs.accept(this);
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

	public void visit(LuaValor exp) {
		
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

	public void visitNomes(List<Nome> nomes) {
		if (nomes != null)
			for (int i = 0, n = nomes.size(); i < n; i++)
				visit((Nome) nomes.get(i));
	}

	public void visit(Exp.ChamadaFunc exp) {
		exp.lhs.accept(this);
		exp.args.accept(this);
	}

	public void visit(FuncArgs args) {
		visitExps(args.exps);
	}

	public void visit(Comando.WhileDo comando) {
		comando.exp.accept(this);
		comando.bloco.accept(this);
	}
	
	public void visit(Comando.ComandoChamadaFunc comando) {
		comando.chamadafunc.accept(this);
	}

	public void visit(Comando.IfThenElse comando) {
		comando.ifexp.accept(this);
		comando.ifbloco.accept(this);
		if ( comando.elseifblocos != null ) 
			for ( int i=0, n=comando.elseifblocos.size(); i<n; i++ ) {
				((Exp)comando.elseifexps.get(i)).accept(this);
				((Bloco)comando.elseifblocos.get(i)).accept(this);
			}
		if ( comando.elsebloco != null )
			visit(comando.elsebloco);
	}

	public void visit(Comando.ForNumerico comando) {
		visit(comando.escopo);
		visit(comando.nome);
		comando.inicial.accept(this);
		comando.limite.accept(this);
		if (comando.passo != null )
			comando.passo.accept(this);
		comando.bloco.accept(this);
	}
	
	public void visit(Comando.FuncDef comando) {
		comando.corpo.accept(this);
	}
	
	public void visit(FuncCorpo corpo) {
		visit(corpo.escopo);
		corpo.parlist.accept(this);
		corpo.bloco.accept(this);
	}
	
	public void visit(ParList pars) {
		visitNomes(pars.nomes);
	}
	
	public void visit(ConstrutorTabela tabela) {
		if( tabela.campos != null)
			for ( int i=0, n=tabela.campos.size(); i<n; i++ )
				((CampoTabela)tabela.campos.get(i)).accept(this);
	}
	
	public void visit(CampoTabela campo) {
		if ( campo.nome != null );
			visit( campo.nome );
		if ( campo.index != null )
			campo.index.accept(this);
		campo.rhs.accept(this);
	}
	
	public void visit(Comando.Return comando) {
		visitExps(comando.valores);
	}

	public void visit(Nome name) {
	}

	public void visit(String name) {
	}

	public void visit(Escopo scope) {
	}
}
