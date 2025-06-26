package menumultinivel;

import java.util.Scanner;

/**
 * @author Andoni
 */
public class MenuMultiNivel {
    private static int nivelMenu = 0;
    private static final int MAX_NIVEL = 4;
    private static final int CAT_OPCIONES = 8;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        generarMenu(input, "");
        input.close();
        System.out.println("Saliendo del Programa...");

    }

    private static void imprimirTitulo(String titulo) {
        String asteriscos = "*".repeat(titulo.length() * 3);
        String espacios = " ".repeat(titulo.length() - 1);

        System.out.println("\n" + asteriscos);
        System.out.println("*" + espacios + titulo + espacios + "*");
        System.out.println(asteriscos + "\n");
    }

    private static void imprimirOpciones(String cadena, String titulo) {
        for (int i = 1; i < CAT_OPCIONES; i++) {
            System.out.println("[" + i + "] - Opcion " + (!"".equals(cadena) ? cadena : "") + i);
        }

        System.out.print("[" + CAT_OPCIONES + "] - ");
        System.out.println((nivelMenu == 0) ? "Salir" : "regresar a " + titulo);
    }

    private static int validarEntrada(Scanner input, String mensaje) {
        int entrada = 0;
        boolean validado = false;

        while (!validado) {
            try {
                System.out.print(mensaje + ": ");
                entrada = input.nextInt();
                validado = true;
            } catch (Exception e) {
                System.out.println("Erros: Debe ingresar un numero entero");
            } finally {
                // Limpiar Buffer
                input.nextLine();
            }
        }

        return entrada;
    }

    private static void generarMenu(Scanner input, String cadena) {
        String titulo = (nivelMenu == 0) ? "Menu Principal" : ("Menu nivel " + nivelMenu);
        int opcion;

        imprimirTitulo(titulo);
        imprimirOpciones(cadena, titulo);

        if (nivelMenu < MAX_NIVEL) {
            while (true) {

                opcion = validarEntrada(input, "Ingrese una opcion");

                if (opcion < 1 || opcion > CAT_OPCIONES) {
                    System.out.println("\nOpcion Fuera del Rango.\n");
                    continue;
                }

                if (opcion == CAT_OPCIONES) {
                    nivelMenu--;
                    return;
                }

                nivelMenu++;
                generarMenu(input, cadena + opcion + ".");
                imprimirTitulo(titulo);
                imprimirOpciones(cadena, titulo);
            }
        }

        while (true) {
            switch (validarEntrada(input, "Ingrese una opcion")) {
                case 1 -> {
                    System.out.println("Dato Curioso 1: Java fue originalmente llamado 'Oak' por James Gosling en 1991.");
                }
                case 2 -> {
                    System.out.println("Dato Curioso 2: El nombre 'Java' proviene del café Java que los desarrolladores solían tomar.");
                }
                case 3 -> {
                    System.out.println("Dato Curioso 3: Java compila a bytecode, lo que le permite ejecutarse en cualquier máquina con JVM.");
                }
                case 4 -> {
                    System.out.println("Dato Curioso 4: La recolección de basura en Java ayuda a manejar la memoria automáticamente.");
                }
                case 5 -> {
                    System.out.println("Dato Curioso 5: Java es uno de los lenguajes más usados en el mundo empresarial.");
                }
                case 6 -> {
                    System.out.println("Dato Curioso 6: El lema de Java es 'Write Once, Run Anywhere'.");
                }
                case 7 -> {
                    System.out.println("Dato Curioso 7: Android usa una versión modificada de la JVM llamada ART.");
                }
                case CAT_OPCIONES -> {
                    nivelMenu--;
                    return;
                }
                default -> {
                    System.out.println("\nOpcion Fuera del Rango.\n");
                }
            }
            System.out.print("\nPresione una tecla para continuar . . .");
            input.nextLine();
            imprimirTitulo(titulo);
            imprimirOpciones(cadena, titulo);
        }

    }

}
