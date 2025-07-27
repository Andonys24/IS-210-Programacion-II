package com.fileserver.utils;

import java.io.*;
import java.nio.file.Path;

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

    public RequestManager(InputStream input, OutputStream output, String context) {
        this.in = new DataInputStream(input);
        this.out = new DataOutputStream(output);
    }

    public void sendMessage(final String message) throws IOException {
        out.writeUTF("MESSAGE|" + message);
        out.flush();
    }

    public void sendFile(String fileName) throws IOException {
        Path filePath = FileManager.resolve("Files/" + fileName);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException("Archivo no encontado: " + fileName);
        }

        // Enviar Header con la informacion del archivo
        String header = "FILE|" + file.getName() + "|" + file.length();
        out.writeUTF(header);
        out.flush();

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
        String header = in.readUTF();
        String[] parts = header.split("\\|");

        switch (parts[0]) {
            case "MESSAGE":
                System.out.println("Mensaje Recibido: " + parts[1]);
                break;
            case "FILE":
                String fileName = parts[1];
                long fileSize = Long.parseLong(parts[2]);
                receiveFile(fileName, fileSize);
                break;
            case "LIST":
                System.out.println("Lista Recibido: " + parts[1]);
                break;
            default:
                System.out.println("El comando '" + parts[0] + "' No se reconoce");
                break;
        }

    }

    public void closeRequestManager() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar in/out: " + e.getMessage());
        }
    }
}
