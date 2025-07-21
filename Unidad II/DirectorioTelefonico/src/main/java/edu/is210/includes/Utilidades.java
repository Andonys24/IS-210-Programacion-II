package edu.is210.includes;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Utilidades {

    public static void limpiarConsola() {
        try {
            final String so = System.getProperty("os.name").toLowerCase();

            ProcessBuilder pb;

            if (so.contains("win")) {
                pb = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                pb = new ProcessBuilder("clear");
            }

            Process proceso = pb.inheritIO().start();
            proceso.waitFor();
        } catch (IOException ex) {
            System.out.println("Error de entrada/salida: " + ex.getMessage());
        } catch (InterruptedException ex) {
            System.out.println("Error, proceso interrupido: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error inesperado: " + ex.getMessage());
        }
    }

    public static void generarTitulo(final String titulo, boolean limpiar) {
        String asteriscos = "*".repeat(titulo.length() * 3);
        String espacios = " ".repeat(titulo.length() - 1);

        if (limpiar) {
            limpiarConsola();
        }
        System.out.println("\n" + asteriscos);
        System.out.println("*" + espacios + titulo + espacios + "*");
        System.out.println(asteriscos + "\n");
    }

    public static void pausarPrograma(final Scanner input, final String accion) {
        System.out.print("Presione Enter para " + accion + ". . . ");
        input.nextLine();
    }

    public static void crearDirectorio(final String directorio) {
        File dir = new File(directorio);

        if (!dir.exists())
            dir.mkdir();
    }

    public static boolean arrayVacio(Object[] array) {
        if (array == null)
            return true;

        for (var elemento : array) {
            if (elemento != null)
                return false;
        }

        return true;
    }

    public static int generarNumRandom(int min, int max) {
        if (min > max) {
            System.err.println("EL valor minimo no puede ser q el mayor");
            return 0;
        }

        var random = new Random();

        return random.nextInt(max - min + 1) + min;
    }

    public static int validarOpcion(final Scanner input, final String mensaje) {
        int entrada = 0;
        boolean validado = false;

        while (!validado) {
            try {
                System.out.print(mensaje + ": ");
                entrada = input.nextInt();
                validado = true;
            } catch (Exception e) {
                System.out.println("Error: Debe ingresar un numero entero");
            } finally {
                // Limpiar Buffer
                input.nextLine();
            }
        }

        return entrada;
    }

    public static String validarString(final Scanner input, final String mensaje) {
        String texto;

        while (true) {
            System.out.print(mensaje + ": ");
            texto = input.nextLine().trim();

            if (!texto.matches("[a-zA-Z ]+")) {
                System.out.println("El texto solo debe contener letras.");
                continue;
            }

            break;
        }

        return texto;
    }

    public static int generarMenu(final Scanner input, final String titulo, final String[] opciones) {

        generarTitulo(titulo, true);
        for (int i = 0; i < opciones.length; i++) {
            System.out.println("[" + (i + 1) + "] - " + opciones[i]);
        }

        return validarOpcion(input, "Ingrese una opcion");
    }

}
