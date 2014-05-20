package util;
public interface Tipo {

	public abstract String getNome();

	public abstract boolean eInteiro();

	public abstract boolean eBooleano();

	public abstract boolean eString();

	public abstract boolean eIgual(Tipo tipo);

	public abstract boolean eValido();

	public abstract Tipo intersecao(Tipo outroTipo);
}
