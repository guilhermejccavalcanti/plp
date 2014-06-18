package memory;

import core.Nome;
import core.ParList;

public interface AmbienteCompilacaoImperativa2 extends AmbienteCompilacaoImperativa {

	public void mapParametrosProcedimento(Nome idArg, ParList parametrosId) throws FuncaoJaDeclaradaException;

	public ParList getParametrosProcedimento(Nome idArg) throws FuncaoNaoDeclaradaException;
}
