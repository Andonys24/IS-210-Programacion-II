package com.fileserver.utils;

import java.io.IOException;

public class UI {
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

    public static void generateTitle(final String title, boolean clean) {
        String asterisk = "*".repeat(title.length() * 3);
        String spaces = " ".repeat(title.length() - 1);

        if (clean)
            cleanConsole();

        System.out.println("\n" + asterisk);
        System.out.println("*" + spaces + title + spaces + "*");
        System.out.println(asterisk + "\n");
    }

    public static void generateMenu(final String title, final String[] options) {
        generateTitle(title, false);

        if (options.length == 0 || options == null) {
            System.out.println("NO hay opciones disponibles.");
        }

        for (int i = 0; i < options.length; i++) {
            System.out.println("[" + (i + 1) + "] - " + options[i]);
        }
    }
}
