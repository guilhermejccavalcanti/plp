package core;

import java.util.ArrayList;
import java.util.List;

public class FuncNome {
	// exemplo: a.b.c.d:e

	// nome base inicial: "a"
	public final Nome nome;

	// campos de acesso intermediários: "b", "c", "d"
	public List<String> pontos;

	// metódo final opcional nome: "e"
	public String metodo;

	public FuncNome(String nome) {
		this.nome = new Nome(nome);
	}

	public void addponto(String ponto) {
		if (pontos == null )
			pontos = new ArrayList<String>();
		pontos.add(ponto);
	}
}
