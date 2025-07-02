package com.github.andonys24;

import java.util.Random;

public class DiceThrower implements Runnable {
    private final Object lock;
    private static int totalSum = 0;
    private int launchValue;

    //    Constructor q recibe lock como parametro
    public DiceThrower(Object lock) {
        this.lock = lock;
        this.launchValue = 0;
    }


    @Override
    public void run() {
        var rand = new Random();
        launchValue = rand.nextInt(6) + 1;

        synchronized (lock) { // Sincronizar recurso compartido
            System.out.println(Thread.currentThread().getName() + " obtuvo un : " + launchValue);
            totalSum += launchValue;
            drawDice();
        }
    }

    //    Getters y Setters
    public static synchronized int getTotalSum() {
        return totalSum;
    }

    public int getLaunchValue() {
        return launchValue;
    }

    //    Metodos de Comportamiento
    public void drawDice() {
        System.out.println("\n -------");

        switch (launchValue) {
            case 1 -> {
                System.out.println("|       |");
                System.out.println("|   *   |");
                System.out.println("|       |");
            }
            case 2 -> {
                System.out.println("| *     |");
                System.out.println("|       |");
                System.out.println("|     * |");
            }
            case 3 -> {
                System.out.println("| *     |");
                System.out.println("|   *   |");
                System.out.println("|     * |");
            }
            case 4 -> {
                System.out.println("| *   * |");
                System.out.println("|       |");
                System.out.println("| *   * |");
            }
            case 5 -> {
                System.out.println("| *   * |");
                System.out.println("|   *   |");
                System.out.println("| *   * |");
            }
            case 6 -> {
                System.out.println("| *   * |");
                System.out.println("| *   * |");
                System.out.println("| *   * |");
            }
            default -> System.out.println("Lanzamiento de dado NO valido");
        }
        System.out.println(" ------- \n");
    }
}
