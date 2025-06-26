package edu.is210;

import java.util.Scanner;

import edu.is210.classes.Objetos;
import edu.is210.includes.Utilidades;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Objetos objetos = new Objetos();
        boolean salir = false;
        String[] opciones = { "Agregar objeto", "Ver Objetos Disponibles", "Eliminar Objeto", "Salir" };

        while (!salir) {

            switch (Utilidades.generarMenu(input, "Menu Principal", opciones, "Ingrese una opcion")) {
                case 1 -> objetos.agregarObjeto(input);
                case 2 -> objetos.verObjetos(input);
                case 3 -> objetos.eliminarObjeto(input);
                case 4 -> {
                    salir = true;
                    objetos.guardarObjetos();
                }
                default -> {
                    System.out.println("Opcion No valida");
                    Utilidades.pausarPrograma(input, "continuar");
                }

            }

        }
        System.out.println("Cerrando el Programa...");
        input.close();
    }

}