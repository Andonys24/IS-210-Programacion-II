
package eliminarrepetidos;

import java.util.Scanner;

/**
 *
 * @author Lab Sistemas
 */
public class EliminarRepetidos {

    public static void main(String[] args) {
        // TODO code application logic here
        final int catElementos = 20;
        var arreglo = new char[catElementos];
        Scanner input = new Scanner(System.in);

        ingresarDatos(input, arreglo);
        eliminarRepetidos(arreglo);
        imprimirArreglo(arreglo);
        buscarElemento(arreglo, input);

        input.close();

    }

    private static void ingresarDatos(Scanner input, char[] arreglo) {
        for (int i = 0; i < arreglo.length; i++) {
            System.out.print("Ingrese el caracter [" + i + "]: ");
            arreglo[i] = input.nextLine().charAt(0);
        }
    }

    private static void imprimirArreglo(final char[] arreglo) {
        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i] == '\0') {
                break;
            }

            System.out.println("[" + i + "] = " + arreglo[i]);
        }
    }

    private static void eliminarRepetidos(char[] arreglo) {
        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i] == '\0') break;
            for (int j = i + 1; j < arreglo.length; j++) {
                if (arreglo[j] == '\0') break;
                if (arreglo[i] == arreglo[j]) {
                    // Desplazar a la izquierda
                    for (int k = j; k < arreglo.length - 1; k++) {
                        arreglo[k] = arreglo[k + 1];
                    }
                    arreglo[arreglo.length - 1] = '\0';
                    j--; // volver a revisar la posición j
                }
            }
        }
    }

    private static boolean buscarElemento(char[] arreglo, Scanner input) {
        System.out.print("Ingrese el elemento a buscar: ");
        char elementoBuscar = input.nextLine().charAt(0);
        boolean encontrado = false;

        for (int i = 0; i < arreglo.length; i++) {
            if (elementoBuscar == arreglo[i]) {
                System.out.println("El caracter '" + elementoBuscar + "' fue encontrado en el indice: " + i);
                return true;
            }
        }

        System.out.println("El caracter '" + elementoBuscar + " NO fue encontrado.");
        return encontrado;
    }

}
