/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package manejoarchivos;

import java.util.Scanner;

/**
 * @author Andonys24
 */
public class ManejoArchivos {

    public static void main(String[] args) {
        var archivo = new AdminArchivo();
        var input = new Scanner(System.in);
        var texto = "";

        while (true) {
            System.out.print("Ingrese un texto ('exit' para salir): ");
            texto = input.nextLine().strip();

            if (texto.equals("exit")) {
                break;
            }

            archivo.escribirArchivo(texto);
        }
        
        input.close();

        System.out.println("\nContenido archivo:\n" + archivo.leerArchivo());
    }

}
