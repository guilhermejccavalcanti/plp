package core;

import visitor.Visitor;

public class CampoTabela  {

	public final Exp index;
	public final String nome;
	public final Exp rhs;
	
	public CampoTabela(Exp index, String nome, Exp rhs) {
		this.index = index;
		this.nome = nome;
		this.rhs = rhs;
	}
	
	public static CampoTabela campoChaveado(Exp index, Exp rhs) {
		return new CampoTabela(index, null, rhs);
	}

	public static CampoTabela campoNomeado(String nome, Exp rhs) {
		return new CampoTabela(null, nome, rhs);
	}

	public static CampoTabela listCampos(Exp rhs) {
		return new CampoTabela(null, null, rhs);
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
