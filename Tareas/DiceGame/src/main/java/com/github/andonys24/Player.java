package com.github.andonys24;

public class Player {
    private String name;
    private int totalSum;

    //    Constructor
    public Player(String name) {
        this.name = name;
        this.totalSum = 0;
    }

    //    Metodos Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(int totalSum) {
        this.totalSum = totalSum;
    }

    // Metodos de Comportamiento
    public void throwDice(int numberOfThows, Object lock) {
        Thread[] diceThreads = new Thread[numberOfThows];
        DiceThrower[] throwers = new DiceThrower[numberOfThows];

        for (int i = 0; i < numberOfThows; i++) {
            throwers[i] = new DiceThrower(lock);
            diceThreads[i] = new Thread(throwers[i], name + "-Dado-" + (i + 1));
            diceThreads[i].start();
        }

        for (int i = 0; i < numberOfThows; i++) {
            try {
                diceThreads[i].join();
            } catch (InterruptedException ex) {
                System.out.println("Error de Interrupcion: " + ex.getMessage());
            }
            totalSum += throwers[i].getLaunchValue();
        }
    }
}
