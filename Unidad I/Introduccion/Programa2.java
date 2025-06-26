public class Programa2 {
	public static void main(String args[]) {
		System.out.println("\nLista de Argumentos recibidos (For)");
		for (int i = 0; i < args.length; i++)
			System.out.println(args[i]);

		System.out.println("\nLista de Argumentos recibidos (Foreach)");
		for (String element : args) {
			System.out.println(element);
		}
	}
}