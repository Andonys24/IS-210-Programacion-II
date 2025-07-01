package com.github.andonys24;

public class Main {
    public static void main(String[] args) {
        int numberOfThows = 4; // Numero de Lanzamientos
        var lock = new Object(); // Objetos de Sincronizacion
        Thread[] dice = new Thread[numberOfThows];

//        Crear Hilos y Lanzar los dados
        for (int i = 0; i < dice.length; i++) {
            dice[i] = new Thread(new DiceThrower(lock), "Dado-" + (i + 1));
            dice[i].start();
        }

//        Esperar que todos los lanzamientos Terminen
        for (Thread die : dice) {
            try {
                die.join();
            } catch (InterruptedException ex) {
                System.out.println("Error de Interrupcion: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Error desconocido: " + ex.getMessage());
            }
        }

//        Mostrar Suma total de los lanzamientos
        System.out.println("La suma total de los lanzamientos es: " + DiceThrower.getTotalSum());
    }
}