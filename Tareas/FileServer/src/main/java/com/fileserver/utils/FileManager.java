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
    private final Context context;

    public FileManager(Context context) {
        this.context = context;
        // Crear estructura de directorios automáticamente
        createDirectoryStructure();
    }

    public Context getContext() {
        return context;
    }

    public enum Directory {
        FILES("Files", "Archivos principales"),
        DOWNLOADS("Downloads", "Archivos descargados"),
        LOGS("Logs", "Archivos de registro");

        private final String path;
        private final String description;

        Directory(String path, String description) {
            this.path = path;
            this.description = description;
        }

        public String getPath() {
            return path;
        }

        public String getDescription() {
            return description;
        }

        // Métodos que ahora requieren contexto
        public Path getFullPath(Context context) {
            return FileManager.resolve(context, this);
        }

        public boolean exists(Context context) {
            return Files.exists(getFullPath(context));
        }

        public void ensureExists(Context context) throws IOException {
            if (!exists(context)) {
                Files.createDirectories(getFullPath(context));
            }
        }
    }

    public enum Context {
        SERVER("server"),
        CLIENT("client");

        private final String path;

        Context(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    // Metodos de Instancia

    public Path resolve(Directory directory, String fileName) {
        return resolve(this.context.getPath() + "/" + directory.getPath() + "/" + fileName);
    }

    public Path resolve(Directory directory) {
        return resolve(this.context.getPath() + "/" + directory.getPath());
    }

    public File[] getFiles(Directory directory) {
        Path dirPath = resolve(directory);
        File dir = dirPath.toFile();

        if (!dir.exists() || !dir.isDirectory()) {
            return new File[0];
        }

        File[] files = dir.listFiles(File::isFile);
        return files != null ? files : new File[0];
    }

    public String[] getFilesString(Directory directory) {
        File[] files = getFiles(directory);

        if (files.length == 0) {
            return new String[0];
        }

        String[] filesString = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filesString[i] = String.format("%-30s %6d bytes", files[i].getName(), files[i].length());
        }

        return filesString;
    }

    // Método para obtener archivos de múltiples directorios
    public File[] getFilesFromMultipleDirectories(Directory... directories) {
        // Primero contar el total de archivos
        int totalFiles = 0;
        for (Directory directory : directories) {
            File[] files = getFiles(directory);
            totalFiles += files.length;
        }

        // Crear arreglo del tamaño correcto
        File[] allFiles = new File[totalFiles];
        int currentIndex = 0;

        // Copiar archivos de cada directorio
        for (Directory directory : directories) {
            File[] files = getFiles(directory);
            for (File file : files) {
                allFiles[currentIndex] = file;
                currentIndex++;
            }
        }

        return allFiles;
    }

    public String[] getFilesStringFromMultipleDirectories(Directory... directories) {
        File[] files = getFilesFromMultipleDirectories(directories);

        if (files.length == 0) {
            // Si no hay archivos, solo retornar la opción de regresar
            return new String[] { "Regresar" };
        }

        // Crear arreglo con espacio para archivos + opción "Regresar"
        String[] filesString = new String[files.length + 1];

        for (int i = 0; i < files.length; i++) {
            // Obtener el directorio padre para mostrar de dónde viene
            String parentDir = files[i].getParentFile().getName();
            filesString[i] = String.format("%-30s %6d bytes [%s]",
                    files[i].getName(),
                    files[i].length(),
                    parentDir);
        }

        // Agregar "Regresar" al final
        filesString[files.length] = "Regresar";

        return filesString;
    }

    // Método auxiliar para obtener el directorio enum desde el path (eliminar)
    public Directory getDirectoryFromPath(String path) {
        for (Directory dir : Directory.values()) {
            if (path.contains(dir.getPath())) {
                return dir;
            }
        }
        return Directory.FILES; // default
    }

    public Path handleNameConflict(Directory directory, String fileName) {
        Path filePath = resolve(directory, fileName);

        if (!Files.exists(filePath)) {
            return filePath;
        }

        String name = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        String timestamp = DateUtil.formatDate("yyyyMMdd_HHmmss");
        String newFileName = name + "_" + timestamp + extension;

        return resolve(directory, newFileName);
    }

    public void createDirectoryStructure() {
        System.out.println("Inicializando Estructura de directorios...");
        for (Directory dir : Directory.values()) {
            try {
                Path dirPath = resolve(dir);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                    System.out.println("Creado: " + dirPath);
                }
            } catch (IOException e) {
                System.err.println("Error creando directorio " + this.context + "/" + dir + ": " + e.getMessage());
            }
        }
    }

    public boolean createDirectory(Directory directory) {
        Path path = resolve(directory);

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

    public boolean createFile(Directory directory, String fileName) {
        Path path = resolve(directory, fileName);

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

    public boolean appendToFile(Directory directory, String fileName, String contenido) {
        Path file = resolve(directory, fileName);

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

    // Metodos Estaticos para compatibilidad

    public static Path resolve(Context context, Directory directory) {
        return resolve(context.getPath() + "/" + directory.getPath());
    }

    private static Path resolve(String relativePath) {
        Path projectRoot = Paths.get("").toAbsolutePath();
        return projectRoot.resolve(RESOURCE_DIR).resolve(relativePath);
    }

}