package interpreter;

import java.io.File;

import memory.AmbienteExecucaoImperativa2;
import memory.ContextoExecucaoImperativa2;
import memory.ListaValor;
import parser.LuaParser;
import visitor.Evaluator;
import core.Trecho;

@SuppressWarnings("unused")
public class Interpreter {
	public static void main(String[] args) {
		try {
			Interpreter interpreter = new Interpreter("TestLua.lua");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public Interpreter(String fileTestCaseLua) throws Exception {
		Trecho programa = LuaParser.parse(new File(fileTestCaseLua));
		Evaluator evaluator = new Evaluator(new ContextoExecucaoImperativa2(null));
		if(programa != null){
			programa.accept(evaluator);
			ListaValor saida = ((AmbienteExecucaoImperativa2)evaluator.getAmbiente()).getSaida();
			System.out.println(saida);
		}
	}
}
