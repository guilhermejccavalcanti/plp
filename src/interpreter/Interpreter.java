package interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import parser.LuaParser;
import parser.ParseException;
import core.Trecho;
import core.Visitor;

@SuppressWarnings("unused")
public class Interpreter extends Visitor {
	public static void main(String[] args) {
		try {
			Interpreter interpreter = new Interpreter("TestLua.lua");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Interpreter(String fileTestCaseLua) throws ParseException,FileNotFoundException {
		Trecho programa = LuaParser.parse(new File(fileTestCaseLua));
		if(programa != null)
			programa.accept(this);
	}
}
