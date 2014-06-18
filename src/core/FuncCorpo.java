package core;

public class FuncCorpo {
	public ParList parlist;
	public Bloco bloco;
	public Escopo escopo;

	public FuncCorpo(ParList parlist, Bloco bloco) {
		this.parlist = parlist!=null? parlist: ParList.PARLISTA_VAZIA;
		this.bloco = bloco;
	}
}
