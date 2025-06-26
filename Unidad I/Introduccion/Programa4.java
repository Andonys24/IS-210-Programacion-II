public class Programa4 {
	// Enteros
	static byte var_byte;	// 8 bits
	static short var_short;	// 16 bits
	static int var_int;	// 32 bits
	static long var_long;	// 64 bits

	// Float
	static float var_float;	// 32 bits
	static double var_double;	// 64 bits

	// Caracter
	static char var_char; // 16 bits UNICODE

	// Logico
	static boolean var_bool; //	true/false

	// Cadena de caracteres
	static String var_string;

	public static void main(String args[]) {
		System.out.println("\n *** Tipos de datos primitivos ***\n");

		System.out.println("\n --- Tipos de datos Entero ----\n");
		System.out.println("var_byte = " + var_byte);
		System.out.println("var_short = " + var_short);
		System.out.println("var_int = " + var_int);
		System.out.println("var_long = " + var_long);
		System.out.println("\n --- Tipos de datos Flotante ----\n");
		System.out.println("var_double = " + var_double);
		System.out.println("\n --- Tipos de datos Char ----\n");
		System.out.println("var_char = " + var_char);
		System.out.println("\n --- Tipos de datos Logico ----\n");
		System.out.println("var_bool = " + var_bool);
		System.out.println("\n --- Objeto String ----\n");
		System.out.println("var_string = " + var_string);
	}
}