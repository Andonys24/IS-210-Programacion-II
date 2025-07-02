package com.github.andonys24;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var input = new Scanner(System.in);
        var lock = new Object();

        printTitle("Bienvenido al juego de dados");

        int numberOfPlayers = validateNumberOfPlayers(input);
        int dicePerPlayer = validateDicePerPlayer(input);
        Player[] players = new Player[numberOfPlayers];


        for (int i = 0; i < numberOfPlayers; i++) {
            var player = new Player("jugador-" + (i + 1));
            printTitle(player.getName() + " esta lanzando los dados...");
            player.throwDice(dicePerPlayer, lock);
            System.out.println("La suma de los lanzamientos de " + player.getName() + " es: " + player.getTotalSum());
            players[i] = player;
        }

//        Mostrar los resultados Finales
        printTitle("Resultados final");
        for (Player player : players) {
            System.out.println(player.getName() + " obtuvo un total de: " + player.getTotalSum() + " puntos.");
        }
        System.out.println("\nTotal de puntos lanzados: " + DiceThrower.getTotalSum());

        input.close();
    }

    //    UI
    public static void printTitle(final String title) {
        var asterisk = "*".repeat(title.length() * 3);
        var spaces = " ".repeat(title.length() - 1);

        System.out.println("\n" + asterisk);
        System.out.println("*" + spaces + title + spaces + "*");
        System.out.println(asterisk + "\n");
    }

    //    Validaciones de entrada
    private static int validateIntegert(Scanner input, String message) {
        int number = 0;

        while (true) {
            System.out.print(message + ": ");
            try {
                number = input.nextInt();
                break;
            } catch (InputMismatchException ex) {
                System.out.println("Error, debe ingresar un numero entero: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Error inesperado: " + ex.getMessage());
            } finally {
                input.nextLine(); // Limpiar el buffer
            }
        }

        return number;
    }

    private static int validateNumberOfPlayers(Scanner input) {
        int numberOfPlayers = 0;

        while (true) {
            numberOfPlayers = validateIntegert(input, "Ingrese el numero de jugadores");
            if (numberOfPlayers < 2) {
                System.out.println("Error, el numero de jugadores debe ser al menos 2.");
                continue;
            }
            if (numberOfPlayers > 5) {
                System.out.println("Error, el numero de jugadores no puede ser mayor a 5.");
                continue;
            }

            break;
        }

        return numberOfPlayers;
    }

    private static int validateDicePerPlayer(Scanner input) {
        int dicePerPlayer = 0;

        while (true) {
            dicePerPlayer = validateIntegert(input, "Ingrese el numero de dados por jugador");
            if (dicePerPlayer < 1) {
                System.out.println("Error, el numero de dados debe ser al menos 1.");
                continue;
            }
            if (dicePerPlayer > 5) {
                System.out.println("Error, el numero de dados no puede ser mayor a 5.");
                continue;
            }

            break;
        }

        return dicePerPlayer;
    }

}