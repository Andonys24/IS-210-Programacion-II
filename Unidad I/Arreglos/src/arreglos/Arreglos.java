/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package arreglos;

/**
 *
 * @author UNAH-IS
 */
public class Arreglos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int[] enteros = new int[10];
        float[] flotantes = new float[10];
        long[] largos = {2345, 2231, 8674, 3537, 243243, 867654};
        
        PuntoGeografico[] pg = {
            new PuntoGeografico(45.6789, 42.697, "Puente"),
            new PuntoGeografico(56.7892, 78.55456, "N/A")
        };

        System.out.println("\nArreglo de enteros");
        for (int i = 0; i < enteros.length; i++) {
            System.out.println(enteros[i]);
        }

        System.out.println("\nArreglo de Flotantes");
        for (int i = 0; i < flotantes.length; i++) {
            System.out.println(flotantes[i]);
        }

        System.out.println("\nArreglo de Largos");
        for (int i = 0; i < largos.length; i++) {
            System.out.println(largos[i]);
        }

        System.out.println("\nArreglo de Largos Foreach");
        for (long element : largos) {
            System.out.println("element = " + element);
        }

        String[] cadenas = {"Hector", "Maria", "Juan", "Pedro", "Karla"};

        System.out.println("\nArreglo de String");
        for (String cadena : cadenas) {
            System.out.println("cadena = " + cadena);
        }

        System.out.println("\nArreglo de PuntoGeografico");
        for (PuntoGeografico element : pg) {
            System.out.println("Punto Geografico = " + element);
        }
    }

}
