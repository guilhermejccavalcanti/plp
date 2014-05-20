package core;


/**
 * @author bldb, efas, jcbr, srmq
 * 
 *         This class groups different types of ValorConcreto.
 */
public abstract class LuaValorConcreto<T> extends LuaValor {

	private T valor;

	public String toString() {
		return String.valueOf(valor);
	}

	public LuaValorConcreto(T valor) {
		this.valor = valor;
	}

	public T valor() {
		return valor;
	}

	public boolean isEquals(LuaValorConcreto<T> obj) {
		return valor().equals(obj.valor());

	}
	public LuaValor avaliar() {
		return this;
	}

	public boolean checaTipo() {
		return true;
	}
}
