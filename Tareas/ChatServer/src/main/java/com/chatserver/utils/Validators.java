package com.chatserver.utils;

import java.util.Scanner;

public class Validators {
    public static String validateUsername(final Scanner input, String prompt) {
        String nickname;

        while (true) {
            System.out.print(prompt + ": ");
            nickname = input.nextLine().trim();

            if (isValidUsername(nickname))
                return nickname;

            System.err.println("\tNickname invalido. Debe contener solo letras, numeros y guones bajos.");
            System.err.println("\tLongitud: 3-12 caracteres.");
            System.err.println("\tEjemplo: Juan123, user_01, gamer_pro");
        }
    }

    private static boolean isValidUsername(String nickname) {
        if (nickname == null || nickname.isEmpty())
            return false;

        if (nickname.length() < 3 || nickname.length() > 12)
            return false;

        char firstChar = nickname.charAt(0);
        if (Character.isDigit(firstChar) || firstChar == '_')
            return false;

        for (char c : nickname.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '_')
                return false;
        }

        return true;
    }

    public static String readValidatedLine(final Scanner input) {
        String line;

        while (true) {
            line = input.nextLine().trim();

            if (line.isEmpty()) {
                System.err.print("No puede estar vacío. Intente nuevamente: ");
            } else if (line.contains("|")) {
                System.err.print("No puede contener '|'. Intente nuevamente: ");
            } else {
                return line; // Entrada válida
            }
        }
    }

    public static String readValidatedLine(final Scanner input, String prompt) {
        System.out.print(prompt + ": ");
        return readValidatedLine(input);
    }
}
