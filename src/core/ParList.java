package core;

import java.util.ArrayList;
import java.util.List;

import visitor.Visitor;

public class ParList {
	public static final List<Nome> LISTANOME_VAZIA = new ArrayList<Nome>();
	public static final ParList PARLISTA_VAZIA = new ParList(LISTANOME_VAZIA,false);
	
	public final List<Nome> nomes;
	public final boolean isvararg;

	public ParList(List<Nome> nomes, boolean isvararg) {
		this.nomes = nomes;
		this.isvararg = isvararg;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
