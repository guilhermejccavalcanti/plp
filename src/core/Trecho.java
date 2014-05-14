package core;

public class Trecho {
	public final Bloco bloco;
	
	public Trecho(Bloco b) {
		this.bloco = b;
	}
	
	public void accept( Visitor visitor ) {
		visitor.visit( this );
	}
}
