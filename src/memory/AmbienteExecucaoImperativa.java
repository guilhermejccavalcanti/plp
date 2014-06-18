package memory;

import core.LuaValor;
import core.Nome;


public interface AmbienteExecucaoImperativa extends AmbienteExecucao {

	public void changeValor(Nome idArg, LuaValor valorId) throws VariavelNaoDeclaradaException;

	public LuaValor read() throws EntradaVaziaException;

	public void write(LuaValor v);

	public ListaValor getSaida();
}
