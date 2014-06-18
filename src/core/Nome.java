package core;

public class Nome {
	public String nome;
	
	public Variavel variavel;

	public Nome(String name) {
		this.nome = name;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Nome) {
			return ((Nome) obj).nome.equals(this.nome);
		}
		return false;
	}

	public int hashCode() {
		return nome.hashCode();
	}

	public String toString() {
		return nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
