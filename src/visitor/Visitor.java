package visitor;

import java.util.List;

import core.Bloco;
import core.CampoTabela;
import core.Comando;
import core.ConstrutorTabela;
import core.Escopo;
import core.Exp;
import core.Exp.NomeExp;
import core.Exp.VarExp;
import core.FuncArgs;
import core.LuaValor;
import core.Nome;
import core.ParList;
import core.Trecho;

public interface Visitor {

	public void visit(Trecho trecho); 

	public LuaValor visit(Bloco bloco);
	
	public LuaValor visit(Comando.Atribui comando);

	public LuaValor visit(Exp.BinopExp exp);

	public LuaValor visit(Exp.FieldExp exp);

	public LuaValor visit(Exp.NomeExp exp);

	public LuaValor visit(Exp.ParensExp exp);

	public LuaValor visit(Exp.UnopExp exp);

	public LuaValor visit(LuaValor exp);
	
	public List<NomeExp> visitVars(List<VarExp> vars);

	public List<LuaValor> visitExps(List<Exp> exps);

	public void visitNomes(List<Nome> nomes);

	public LuaValor visit(Exp.ChamadaFunc exp);

	public List<LuaValor> visit(FuncArgs args);

	public LuaValor visit(Comando.WhileDo comando);
	
	public LuaValor visit(Comando.ComandoChamadaFunc comando);

	public LuaValor visit(Comando.IfThenElse comando);

	public LuaValor visit(Comando.ForNumerico comando);
	
	public LuaValor visit(Comando.FuncDef comando);
	
	public void visit(ParList pars);
	
	public LuaValor visit(ConstrutorTabela tabela);
	
	public void visit(CampoTabela campo);
	
	public LuaValor visit(Comando.Return comando);

	public LuaValor visit(Nome name);

	public void visit(String name);

	public void visit(Escopo scope);
}
