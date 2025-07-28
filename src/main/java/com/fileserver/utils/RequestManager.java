package com.fileserver.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private final FileManager fileManager;

    public enum CommandType {
        MESSAGE, DOWNLOAD_FILE, UPLOAD_FILE, TITLE, MENU, OPTION, EXIT, PAUSE
    }

    // Contructor
    public RequestManager(InputStream input, OutputStream output, FileManager fileManager) {
        this.in = new DataInputStream(input);
        this.out = new DataOutputStream(output);
        this.fileManager = fileManager;
    }

    // Metodos de manejo de partes de la clase
    public String[] getParts() throws IOException {
        var header = in.readUTF();
        return header.split("\\|");

    }

    public void sendReply(CommandType header, String content) throws IOException {
        out.writeUTF(header + "|" + content);
        out.flush();
    }

    // Metodos para enviar instrucciones
    public void sendMessage(final String message) throws IOException {
        sendReply(CommandType.MESSAGE, message);
    }

    public void sendExit() throws IOException {
        sendReply(CommandType.EXIT, "");
    }

    public void sendOption(final String option) throws IOException {
        sendReply(CommandType.OPTION, option);
    }

    public void sendFile(File fileSend) throws IOException {
        String parentDirName = fileSend.getParentFile().getName().toUpperCase();
        var directory = FileManager.Directory.valueOf(parentDirName);
        Path filePath = fileManager.resolve(directory, fileSend.getName());
        File file = filePath.toFile();

        if (!file.exists())
            throw new FileNotFoundException("Archivo no encontrado: " + fileSend);

        sendReply(CommandType.DOWNLOAD_FILE, file.getName() + "|" + file.length());

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
        var fullPath = fileManager.handleNameConflict(FileManager.Directory.DOWNLOADS, filename);

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
    }

    // Metodo para limpiar memoria
    public void close() throws IOException {
        if (in != null)
            in.close();
        if (out != null)
            out.close();
    }
}
