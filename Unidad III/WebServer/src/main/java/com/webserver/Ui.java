package com.webserver;

import java.io.IOException;

public class Ui {
    public static void cleanConsole() {
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
}
