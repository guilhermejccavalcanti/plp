package core;

import java.util.ArrayList;
import java.util.List;

public class FuncArgs{

	public final List<Exp> exps;
	
	/** exp1,exp2... */
	public static FuncArgs explist(List<Exp> explist) {
		return new FuncArgs(explist);
	}

	/** "mylib" */
	public static FuncArgs string(LuaString string) {
		return new FuncArgs(string);
	}

	public FuncArgs(List<Exp> exps) {
		this.exps = exps;
	}

	public FuncArgs(LuaString string) {
		this.exps = new ArrayList<Exp>();
		this.exps.add(string);
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
