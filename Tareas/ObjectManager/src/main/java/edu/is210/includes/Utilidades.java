package edu.is210.includes;

import java.io.IOException;
import java.util.Scanner;
import java.util.function.Function;

public class Utilidades {
    public static void limpiarConsola() {
        try {
            final String so = System.getProperty("os.name").toLowerCase();
            // System.out.println("Sistema operativo: " + so);

            ProcessBuilder processBuilder;

            if (so.contains("win")) {
                processBuilder = new ProcessBuilder("cmd", "/c", "cls");
            } else if (so.contains("linux") || so.contains("mac")) {
                processBuilder = new ProcessBuilder("clear");
            } else {
		System.out.println("Error, No se puede Limpiar pantalla.");
                System.out.println("No se puede determinar el Sistema Operativo.");
                return;
            }

            Process process = processBuilder.inheritIO().start();
            process.waitFor();
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("El proceso fue interrumpido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error Inesperado: " + e.getMessage());
        }
    }

    public static void imprimirTitulo(final String titulo) {
        var asteriscos = "*".repeat(titulo.length() * 3);
        var espacios = " ".repeat(titulo.length() - 1);

        limpiarConsola();
        System.out.println("\n" + asteriscos);
        System.out.println("*" + espacios + titulo + espacios + "*");
        System.out.println(asteriscos + "\n");
    }

    public static <T> T validarEntrada(Scanner input, String mensaje, Function<String, T> parser, String tipo) {
        boolean validado = false;
        T entrada = null;

        while (!validado) {
            try {
                System.out.print(mensaje + ": ");
                String valor = input.nextLine();
                entrada = parser.apply(valor);
                validado = true;
            } catch (Exception e) {
                System.out.println("Error: Debe ingresar un valor de tipo " + tipo);
            }
        }

        return entrada;
    }

    public static int generarMenu(Scanner input, final String titulo, final String[] opciones, final String mensaje) {
        int i = 1;

        imprimirTitulo(titulo);
        for (String opcion : opciones)
            System.out.println("[" + (i++) + "] - " + opcion);

        return validarEntrada(input, mensaje, Integer::parseInt, "entero");

    }

    public static void pausarPrograma(Scanner input, String mensaje) {
        System.out.print("Presione Enter para " + mensaje + "...");
        input.nextLine();
    }

}
