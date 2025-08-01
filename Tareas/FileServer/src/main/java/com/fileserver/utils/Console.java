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

    public void close() {
        if (input != null) {
            input.close();
        }
    }
}
