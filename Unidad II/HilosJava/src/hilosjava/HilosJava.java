package hilosjava;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HilosJava {

    public static void main(String[] args) {
        var hilo0 = new HiloHeredado();
        var hilo1 = new HiloHeredado();
        var hilo2 = new Thread(new HiloImplementado());
        var hilo3 = new Thread(() -> {
            System.out.println("Hilo En ejecucion desde una funcion lambda...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(HilosJava.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        hilo0.start();
        hilo1.start();
        hilo2.start();
        hilo3.start();

        try {
            hilo0.join();
            hilo3.join();
        } catch (InterruptedException ex) {
            System.out.println("Error al esperar el Hilo: " + ex.getMessage());
            Logger.getLogger(HilosJava.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Hilo 0 Activo: " + ((hilo0.isAlive()) ? "SI" : "NO"));
        System.out.println("Hilo 1 Activo: " + ((hilo1.isAlive()) ? "SI" : "NO"));
        System.out.println("Hilo 2 Activo: " + ((hilo2.isAlive()) ? "SI" : "NO"));
        System.out.println("Hilo 3 Activo: " + ((hilo3.isAlive()) ? "SI" : "NO"));
    }

}
