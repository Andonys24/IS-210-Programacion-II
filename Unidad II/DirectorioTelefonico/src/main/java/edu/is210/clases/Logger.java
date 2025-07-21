package edu.is210.clases;

import edu.is210.includes.Utilidades;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Logger {

    private final String FILE_DIR = "src/main/resources/logs";
    private final String nombreArchivoLog;
    private final ExecutorService executor;

    public Logger() {
        String fechaFormateada = formatearFecha("dd-MM-yyyy_HH-mm-ss");
        nombreArchivoLog = fechaFormateada + ".log";

        // Crear directorio si no existe
        Utilidades.crearDirectorio(FILE_DIR);

        // Crear hilo dedicado a los logs
        executor = Executors.newSingleThreadExecutor();
        registrarLog("Inicio");
    }

    public void cerrar() {
        registrarLog("Salida");
        // Cerrar Hilo dedicado
        executor.shutdown();
    }

    // registrar el log
    public void registrarLog(String accion) {
        executor.submit(() -> {
            String identificador = String.format("%08d", Utilidades.generarNumRandom(0, 99999999));
            String fechaFormateada = formatearFecha("dd-MM-yyyy_HH:mm:ss");
            File logFile = new File(FILE_DIR, nombreArchivoLog);

            try (FileWriter fileWriter = new FileWriter(logFile, true);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                 PrintWriter logWriter = new PrintWriter(bufferedWriter)) {
                logWriter.println(accion + " | " + identificador + " | " + fechaFormateada);
            } catch (IOException e) {
                System.err.println("No se pudo escribir en el archivo de log: " + e.getMessage());
            }
        });
    }

    private String formatearFecha(final String formato) {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern(formato);
        return formatoFecha.format(LocalDateTime.now());
    }
}
