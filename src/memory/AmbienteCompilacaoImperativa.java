package memory;

import core.LuaValor;

public interface AmbienteCompilacaoImperativa extends AmbienteCompilacao {

	public LuaValor getEntrada() throws VariavelNaoDeclaradaException,VariavelJaDeclaradaException, EntradaVaziaException;

}
