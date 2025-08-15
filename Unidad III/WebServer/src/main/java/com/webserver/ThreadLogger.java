package com.webserver;

public class ThreadLogger {

    /*
     * Imprimir un mensaje con formato estandar del hilo
     * Formato: Hilo [nombreHilo] - mensaje
     */
    public static void log(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.println("Hilo [" + threadName + "] - " + message);
    }

    /*
     * Imprimir un mensaje con formato estadar especificando el hilo el hilo
     * manualmente
     */
    public static void log(String threadName, String message) {
        System.out.println("Hilo [" + threadName + "] - " + message);
    }

    /**
     * Log para errores con formato estándar
     */
    public static void logError(String message) {
        String threadName = Thread.currentThread().getName();
        System.err.println("Hilo [" + threadName + "] - ERROR: " + message);
    }

    /**
     * Log para errores especificando el hilo manualmente
     */
    public static void logError(String threadName, String message) {
        System.err.println("Hilo [" + threadName + "] - ERROR: " + message);
    }
}
