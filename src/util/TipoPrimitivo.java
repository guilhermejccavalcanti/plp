package util;

public enum TipoPrimitivo implements Tipo {

	INTEIRO("INTEIRO"),
	BOOLEANO("BOOLEANO"),
	STRING("STRING"),
	NIL("NIL"),
	TABLE("TABLE");

	protected String nome;

	private TipoPrimitivo(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public boolean eInteiro() {
		return this.eIgual(INTEIRO);
	}

	public boolean eBooleano() {
		return this.eIgual(BOOLEANO);
	}

	public boolean eString() {
		return this.eIgual(STRING);
	}
	

	public boolean eNil() {
		return this.eIgual(NIL);
	}

	public boolean eTable() {
		return this.eIgual(TABLE);
	}
	
	public boolean eIgual(Tipo tipo) {
		boolean ret = false;
		if (eValido()) {
			if (tipo.eValido()) {
				ret = this.nome.equals(tipo.getNome());
			} else {
				ret = tipo.eIgual(this);
			}
		}
		return ret;
	}

	public boolean eValido() {
		return this.nome != null && nome.length() > 0;
	}

	public TipoPrimitivo intersecao(Tipo outroTipo) {
		if (outroTipo.eIgual(this)) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return this.nome;
	}
}
