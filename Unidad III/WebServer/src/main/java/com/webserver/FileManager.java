package com.webserver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    private static final String RESOURCE_DIR = "src/main/resources";

    public static String readFile(String filePath) {
        var in = FileManager.class.getClassLoader().getResourceAsStream(filePath);

        if (in == null) {
            return null;
        }

        try (var reader = new BufferedReader(new InputStreamReader(in))) {
            var content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo '" + filePath + "' : " + e.getMessage());
            return null;
        }
    }

    // Buscar un archivo
    public static boolean fileExists(String filePath) {
        try (var inputStream = FileManager.class.getClassLoader().getResourceAsStream(filePath)) {
            return inputStream != null;
        } catch (Exception e) {
            System.err.println("Error al buscar el archivo: " + e.getMessage());
            return false;
        }
    }

    // Leer archivo Binario
    public static byte[] readBinaryFile(String filePath) {
        try (var in = FileManager.class.getClassLoader().getResourceAsStream(filePath)) {
            if (in == null) {
                System.err.println("El archivo '" + filePath + "' no fue encontrado.");
                return null;
            }
            return in.readAllBytes();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo binario: " + e.getMessage());
            return null;
        }
    }

    public static boolean createDirectory(String relativePath) {
        Path path = resolve(relativePath);

        if (Files.exists(path)) {
            return false;
        }

        try {
            Files.createDirectories(path);
            return true;
        } catch (IOException e) {
            System.err.println("Error al crear el directorio: " + e.getMessage());
            return false;
        }
    }

    public static boolean createFile(String fileName) {
        Path path = resolve(fileName);

        if (Files.exists(path)) {
            return false;
        }

        try {
            // Crear directorios padre si no existen
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            System.err.println("Error al crear el archivo: " + e.getMessage());
            return false;
        }
    }

    public static boolean appendToFile(String fileName, String contenido) {
        Path file = resolve(fileName);

        try {
            // Crear directorios padre si no existen
            Files.createDirectories(file.getParent());

            try (var writer = new BufferedWriter(new FileWriter(file.toFile(), true))) {
                writer.write(contenido);
                writer.newLine();
                return true;
            }
        } catch (IOException e) {
            System.err.println("No se pudo escribir en el archivo: " + e.getMessage());
            return false;
        }
    }

    private static Path resolve(String relativePath) {
        Path projectRoot = Paths.get("").toAbsolutePath();
        return projectRoot.resolve(RESOURCE_DIR).resolve(relativePath);
    }

}
