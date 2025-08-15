package com.webserver;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Logger {

    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR, FATAL
    }

    private final String nameFileLog;
    private final ExecutorService executor;
    private final Object logLock = new Object();
    private String lastLog;

    public String getLastLog() {
        synchronized (logLock) {
            return lastLog;
        }
    }

    public Logger() {
        // Crear directorio para almacenar logs
        FileManager.createDirectory("log");

        var dateFormat = DateUtil.formatDate("dd-MM-yyyy");
        nameFileLog = dateFormat + ".log";

        // Crear archivo q almacena los logs (por dia)
        FileManager.createFile("log" + "/" + nameFileLog);

        // Iniciar hilo independiente para los logs
        executor = Executors.newSingleThreadExecutor();
    }

    // Método principal de logging
    public void log(LogLevel level, String category, String event, String details, String source) {
        executor.submit(() -> {
            String identifier = generateId();
            String timestamp = DateUtil.formatDate("yyyy-MM-dd HH:mm:ss");

            StringBuilder logEntry = new StringBuilder();
            logEntry.append("[").append(timestamp).append("] ");
            logEntry.append(level.name()).append(" | ");
            logEntry.append(category).append(" | ");
            logEntry.append(event).append(" | ");

            if (details != null && !details.isEmpty()) {
                logEntry.append(details).append(" | ");
            }

            logEntry.append(source != null ? source : "SERVER").append(" | ");
            logEntry.append("ID:").append(identifier);

            String finalLog = logEntry.toString();
            writeLog(finalLog);

            synchronized (logLock) {
                lastLog = finalLog;
                printLogWithColor(level, finalLog);
            }
        });
    }

    public void info(String category, String event, String details, String source) {
        log(LogLevel.INFO, category, event, details, source);
    }

    public void error(String category, String event, String details, String source) {
        log(LogLevel.ERROR, category, event, details, source);
    }

    public void warn(String category, String event, String details, String source) {
        log(LogLevel.WARN, category, event, details, source);
    }

    public void debug(String category, String event, String details, String source) {
        log(LogLevel.DEBUG, category, event, details, source);
    }

    /*
     * Métodos simplificados para logging rápido de eventos importantes del servidor
     */
    public void logServerEvent(String message) {
        log(LogLevel.INFO, "SERVER", message, "", "Main");
    }

    public void logError(String message) {
        log(LogLevel.ERROR, "SERVER", message, "", "Main");
    }

    private void writeLog(String logEntry) {
        FileManager.appendToFile("log" + "/" + nameFileLog, logEntry);
    }

    private void printLogWithColor(LogLevel level, String logEntry) {
        // Solo imprimir logs en consola cuando el contexto es SERVER
        String color;
        switch (level) {
            case ERROR:
            case FATAL:
                color = "\033[91m"; // Rojo
                break;
            case WARN:
                color = "\033[93m"; // Amarillo
                break;
            case INFO:
                color = "\033[92m"; // Verde
                break;
            case DEBUG:
                color = "\033[94m"; // Azul
                break;
            case TRACE:
                color = "\033[95m"; // Magenta
                break;
            default:
                color = "\033[0m"; // Reset
                break;
        }
        String reset = "\033[0m";

        System.out.println(color + "Log: " + logEntry + reset);
    }

    public void close() {
        if (!executor.isTerminated()) {
            executor.shutdown();
        }
    }

    private static int getRandomInt(final int min, final int max) {
        if (min < Integer.MIN_VALUE) {
            System.err.println("Error el valor minimo sobrepasa el rango");
        }

        if (max > Integer.MAX_VALUE) {
            System.err.println("Error el valor maximo sobrepasa el rango");
        }
        var random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    private String generateId() {
        return String.format("%08d", getRandomInt(0, 99999999));
    }
}
