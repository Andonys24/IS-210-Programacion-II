package com.fileserver.utils;

import java.util.Scanner;

public class Console {
    private Scanner input;

    public Console() {
        input = new Scanner(System.in);
    }

    public void pause(String message) {
        if (message == null)
            message = "continuar";

        System.out.print("\nPresione una tecla para " + message + "... ");
        input.nextLine();
    }

    public int validateOption(final String mensaje) {
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

    public int validateOptionRange(String message, int max) {
        if (max < 1) {
            System.err.println("El valor max no puede ser < 1");
            return 0;
        }

        while (true) {
            int option = validateOption(message);

            if (option < 1) {
                System.err.println("La eleccion no puede ser menor que 1.");
                continue;
            }

            if (option > max) {
                System.err.println("La eleccion no puede ser mayor que " + max);
                continue;
            }

            return option;
        }
    }

    public void closeConsole() {
        if (input != null) {
            input.close();
        }
    }
}
