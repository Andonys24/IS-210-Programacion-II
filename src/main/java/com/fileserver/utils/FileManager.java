package com.fileserver.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    private static final String RESOURCE_DIR = "src/main/resources";

    static {
        createDirectory("Downloads");
        createDirectory("Files");
        createDirectory("Logs");
    }

    public static Path resolve(String relativePath) {
        return Paths.get(RESOURCE_DIR, relativePath);
    }

    public static boolean createDirectory(final String nameDir) {
        Path path = resolve(nameDir);

        if (Files.exists(path)) {
            return false;
        }

        try {
            Files.createDirectory(path);
            return true;
        } catch (IOException e) {
            System.err.println("Error al crear el directorio: " + e.getMessage());
            return false;
        }
    }

    // Crear un archivo vacio
    public static boolean createFile(String relativeFilePath) {
        Path path = resolve(relativeFilePath);

        if (Files.exists(path)) {
            // El archivo ya existe
            return false;
        }

        try {
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            System.err.println("Error al crear el archivo: " + e.getMessage());
            return false;
        }
    }

    // Escribir (o agregar) contenido al archivo
    public static boolean appendToFile(final String relativeFilePath, String contenido) {
        Path file = resolve(relativeFilePath);

        try (var writer = new BufferedWriter(new FileWriter(file.toFile(), true))) {
            writer.write(contenido);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("No se pudo escribir en el archivo: " + e.getMessage());
            return false;
        }
    }

    public static long getFileSizeKilobytes(File file) {
        return file.length() / 1024;
    }

    // Listar archivos en un directorio
    public static File[] getFiles(String relativeDirPath) {
        Path dir = resolve(relativeDirPath);

        var directory = new File(dir.toString());

        File[] files = directory.listFiles();

        if (files == null || files.length < 1) {
            return null;
        }

        return files;
    }

    public static String[] getFilesString(String relativeDirPath) {
        Path dir = resolve(relativeDirPath);
        String[] filesString;
        File[] files;
        var directory = dir.toFile();

        files = directory.listFiles();

        if (files == null || files.length < 1) {
            return null;
        }

        filesString = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            long sizeInKB = getFileSizeKilobytes(files[i]);
            filesString[i] = String.format("%-30s %6d KB", files[i].getName(), sizeInKB);
        }

        return filesString;
    }
}
