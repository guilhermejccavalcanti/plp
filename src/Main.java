public class Main {
	public static void main(String[] args) {
		System.out.println("Digite uma expressao aritmetica");
		System.out.print("e um ponto-e-virgula para finalizar: ");
		LuaGrammar interpreter = new LuaGrammar(System.in);
		try {
			interpreter.process();
		} catch (Exception e) {
			System.out.println("Ocorreu um erro.");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
