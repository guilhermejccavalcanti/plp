package memory;

import core.Nome;


public interface Ambiente<T> {

	public void incrementa();

	public void restaura();

	public void map(Nome idArg, T tipoId) throws VariavelJaDeclaradaException;

	public T get(Nome idArg) throws VariavelNaoDeclaradaException;
}