package com.fileserver.utils;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

/* Formato para enviar mensajes 
 * [TIPO]|[nombre]|[tamaño]
 * (dato binario si es archivo)
 * Ejemplos:
 * Mesaje|Bienvenido al servidor
 * ARCHIVO|documento.txt|1024
 * LISTA|archivo1.txt,archivo2.txt
*/

public class RequestManager {
    private final DataInputStream in;
    private final DataOutputStream out;

    public enum CommandType {
        MESSAGE, FILE, TITLE, MENU, OPTION
    }

    // Contructor
    public RequestManager(InputStream input, OutputStream output) {
        this.in = new DataInputStream(input);
        this.out = new DataOutputStream(output);
    }

    // Metodos de manejo de partes de la clase
    private String[] getParts() throws IOException {
        var header = in.readUTF();
        return header.split("\\|");

    }

    private void sendReply(CommandType header, String content) throws IOException {
        out.writeUTF(header + "|" + content);
        out.flush();
    }

    // Metodos para enviar instrucciones
    public void sendMessage(final String message) throws IOException {
        sendReply(CommandType.MESSAGE, message);
    }

    public void sendTitle(String title) throws IOException {
        sendReply(CommandType.TITLE, title);
    }

    public void sendMenu(String title, String[] options) throws IOException {
        var content = new StringBuilder(title);

        for (String option : options) {
            content.append("|").append(option);
        }

        sendReply(CommandType.MENU, content.toString());
    }

    public void sendOption(final String option) throws IOException {
        sendReply(CommandType.OPTION, option);
    }

    public void sendFile(String fileName) throws IOException {
        Path filePath = FileManager.resolve("Files/" + fileName);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException("Archivo no encontado: " + fileName);
        }

        // Enviar Header con la informacion del archivo
        sendReply(CommandType.FILE, file.getName() + "|" + file.length());

        // Enviar contenido del archivo
        try (var fileIn = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }
    }

    // Metodos para recibir respuestas
    public int readClientOption() throws IOException {
        String[] parts = getParts();

        if ("OPTION".equals(parts[0])) {
            return Integer.parseInt(parts[1]);
        }

        throw new IllegalArgumentException("El comando '" + parts[0] + "' NO es una opcion");
    }

    public void receiveFile(String filename, long fileSize) throws IOException {
        Path fullPath = FileManager.resolve("Downloads/" + filename);
        System.out.println("Ruta completa: " + fullPath);

        try (var fileOut = new FileOutputStream(fullPath.toFile())) {
            byte[] buffer = new byte[4096];
            long totalBytesRead = 0;

            while (totalBytesRead < fileSize) {
                int bytesToRead = (int) Math.min(buffer.length, fileSize - totalBytesRead);
                int bytesRead = in.read(buffer, 0, bytesToRead);

                if (bytesRead == -1) {
                    throw new IOException("Conexion cerrada inesperadamente");
                }

                fileOut.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
            }
        }

        System.out.println("Archivo guardado: " + fullPath);
    }

    public void processInput() throws IOException {
        String[] parts = getParts();
        CommandType header = CommandType.valueOf(parts[0]);

        switch (header) {
            case MESSAGE:
                System.out.println(parts[1]);
                break;
            case TITLE:
                UI.generateTitle(parts[1], false);
                break;
            case MENU:
                // Obtener Contenido del Menu
                String title = parts[1];
                String[] options = Arrays.copyOfRange(parts, 2, parts.length);
                // Imprimir Menu
                UI.generateMenu(title, options);
                break;
            case OPTION:
                System.out.println("Opción recibida: " + parts[1]);
                break;
            case FILE:
                String fileName = parts[1];
                long fileSize = Long.parseLong(parts[2]);
                receiveFile(fileName, fileSize);
                break;
            default:
                System.out.println("El Encabezado '" + header + "' No se reconoce");
                break;
        }
    }

    // Metodo para limpiar memoria
    public void closeRequestManager() throws IOException {
        if (in != null)
            in.close();
        if (out != null)
            out.close();
    }
}
