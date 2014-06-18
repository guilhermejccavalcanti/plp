package memory;

import core.Comando.FuncDef;
import core.Nome;

public interface AmbienteExecucaoImperativa2 extends AmbienteExecucaoImperativa {

	public void mapProcedimento(Nome idArg, FuncDef procedimento) throws FuncaoJaDeclaradaException;

	public FuncDef getProcedimento(Nome idArg)	throws FuncaoNaoDeclaradaException;
	
	public boolean contemVariavel(Nome idArg);

}
