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
    private final Logger logger; // Si es != null es el Servidor
    private Console console;

    public enum CommandType {
        MESSAGE, DOWNLOAD_FILE, UPLOAD_FILE, TITLE, MENU, OPTION, EXIT, PAUSE
    }

    // Contructor
    public RequestManager(InputStream input, OutputStream output) {
        this.in = new DataInputStream(input);
        this.out = new DataOutputStream(output);
        logger = null;
    }

    public RequestManager(InputStream input, OutputStream output, Logger logger) {
        this.in = new DataInputStream(input);
        this.out = new DataOutputStream(output);
        this.logger = logger;
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

    public void sendTitle(String title) throws IOException {
        sendReply(CommandType.TITLE, title);
    }

    public void sendPause(String message) throws IOException {
        sendReply(CommandType.PAUSE, message);
    }

    public void sendUploadFile(String message) throws IOException {
        sendReply(CommandType.UPLOAD_FILE, message);
    }

    public void sendExit() throws IOException {
        sendReply(CommandType.EXIT, "");
    }

    public void sendMenu(String title, String[] options) throws IOException {
        var content = new StringBuilder(title);

        for (String option : options) {
            content.append("|").append(option);
        }

        sendReply(CommandType.MENU, content.toString());

        if (logger != null)
            logger.info("MENU", "MENU_SENT", "Menú enviado: " + title + " (" + options.length + " opciones)", "SERVER");

    }

    public void sendOption(final String option) throws IOException {
        sendReply(CommandType.OPTION, option);
    }

    public void sendFile(String fileName) throws IOException {
        String directory = (logger != null) ? "Files" : "Downloads";
        Path filePath = FileManager.resolve(directory + "/" + fileName);
        File file = filePath.toFile();

        if (!file.exists()) {
            if (logger != null)
                logger.error("FILE", "SEND_FILE_NOT_FOUND", "Archivo no encontrado: " + fileName, "SERVER");

            throw new FileNotFoundException("Archivo no encontado: " + fileName);
        }

        if (logger != null)
            logger.info("FILE", "SEND_FILE_STARTED", fileName + " (" + file.length() + " bytes)", "SERVER");

        // Enviar Header con la informacion del archivo
        sendReply(CommandType.DOWNLOAD_FILE, file.getName() + "|" + file.length());

        // Enviar contenido del archivo
        try (var fileIn = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();

            if (logger != null) {
                logger.info("FILE", "SEND_FILE_COMPLETED", fileName + " enviado exitosamente", "SERVER");
            }
        } catch (IOException e) {
            if (logger != null)
                logger.error("FILE", "SEND_FILE_ERROR", fileName + " - " + e.getMessage(), "SERVER");

            throw e;
        }
    }

    // Metodos para recibir respuestas
    public int readClientOption() throws IOException {
        String[] parts = getParts();

        if ("OPTION".equals(parts[0])) {
            int option = Integer.parseInt(parts[1]);
            if (logger != null)
                logger.info("CLIENT", "OPTION_RECEIVED", "Opción recibida: " + option, "SERVER");

            return option;
        }

        if (logger != null)
            logger.warn("CLIENT", "INVALID_COMMAND", "Esperaba OPTION pero recibió: " + parts[0], "SERVER");

        throw new IllegalArgumentException("El comando '" + parts[0] + "' NO es una opcion");
    }

    public void receiveFile(String fileName, long fileSize) throws IOException {
        String directory = (logger != null) ? "Files" : "Downloads";
        Path fullPath = FileManager.handleNameConflict(directory, fileName);

        if (logger != null) {
            logger.info("FILE", "RECEIVE_FILE_STARTED", fileName + " (" + fileSize + " bytes)", "SERVER");
        } else {
            System.out.print("\nRecibiendo el archivo " + fileName);
            System.out.println(" con tamanio de " + fileSize + " bytes");
        }

        try (var fileOut = new FileOutputStream(fullPath.toFile())) {
            byte[] buffer = new byte[4096];
            long totalBytesRead = 0;

            while (totalBytesRead < fileSize) {
                int bytesToRead = (int) Math.min(buffer.length, fileSize - totalBytesRead);
                int bytesRead = in.read(buffer, 0, bytesToRead);

                if (bytesRead == -1) {
                    if (logger != null) {
                        logger.error("FILE", "RECEIVE_FILE_CONNECTION_LOST",
                                fileName + " - Conexión cerrada inesperadamente", "SERVER");
                    }
                    throw new IOException("Conexion cerrada inesperadamente");
                }

                fileOut.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
            }

            if (logger != null) {
                logger.info("FILE", "RECEIVE_FILE_COMPLETED", fileName + " guardado en: " + fullPath, "SERVER");
            } else {
                System.out.println("Archivo guardado en: " + fullPath);
            }
        } catch (IOException e) {
            if (logger != null)
                logger.error("FILE", "RECEIVE_FILE_ERROR", fileName + " - " + e.getMessage(), "SERVER");

            throw e;
        }
    }

    public boolean processInput() throws IOException {
        String context = (logger != null) ? "Servidor" : "Cliente";
        String[] parts = getParts();
        CommandType header = CommandType.valueOf(parts[0]);

        switch (header) {
            case MESSAGE:
                System.out.println(context + ": " + parts[1]);
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
                System.out.println("El servidor Pidio una opcion");
                startConsole();
                int option = console.validateOption(parts[1]);
                sendOption(Integer.toString(option));
                break;
            case DOWNLOAD_FILE:
                String fileName = parts[1];
                long fileSize = Long.parseLong(parts[2]);
                receiveFile(fileName, fileSize);
                break;
            case UPLOAD_FILE:
                if (logger != null)
                    logger.info("CLIENT", "UPLOAD_REQUEST", "Cliente solicitó subir archivo", "CLIENT");

                startConsole();
                File[] files = FileManager.getFiles("Downloads");
                String[] filesString = null;
                boolean validate = false;
                int choice = 0;

                if (files == null || files.length == 0) {
                    UI.generateTitle("No hay archivos disponibles para subir.", true);
                    sendMessage("NO_FILES_TO_UPLOADS");

                    if (logger != null)
                        logger.info("CLIENT", "NO_FILES_TO_UPLOAD", "Cliente no tiene archivos para subir", "CLIENT");

                } else {
                    String[] fileStringsOriginal = FileManager.getFilesString("Downloads");
                    filesString = Arrays.copyOf(fileStringsOriginal, fileStringsOriginal.length + 1);
                    filesString[fileStringsOriginal.length] = "Regresar";
                    while (!validate) {
                        UI.generateMenu("Seleccione un archivo para subir", filesString);
                        choice = console.validateOption("Elije un archivo") - 1;
                        if (choice < 0 || choice > filesString.length) {
                            System.out.println("Opcion No valida");
                            continue;
                        }
                        validate = true;
                    }

                    if (choice == fileStringsOriginal.length) {
                        sendMessage("UPLOAD_CANCELLED");

                        if (logger != null)
                            logger.info("CLIENT", "UPLOAD_CANCELLED", "Cliente canceló la subida", "CLIENT");

                    } else {
                        sendMessage("DOWNLOAD_FILE");

                        if (logger != null)
                            logger.info("CLIENT", "UPLOAD_FILE_SELECTED",
                                    "Archivo seleccionado: " + files[choice].getName(), "CLIENT");

                        sendFile(files[choice].getName());
                    }
                }
                break;
            case PAUSE:
                startConsole();
                console.pause(parts[1]);
                break;
            case EXIT:
                if (logger != null)
                    logger.info("CLIENT", "EXIT_RECEIVED", "Cliente cerró la conexión", "CLIENT");

                return true;
            default:
                if (logger != null) {
                    logger.warn("SYSTEM", "UNKNOWN_COMMAND", "Comando no reconocido: " + header,
                            logger != null ? "SERVER" : "CLIENT");
                }
                System.out.println("El Encabezado '" + header + "' No se reconoce");
                break;
        }

        return false;
    }

    private void startConsole() {
        if (console == null) {
            console = new Console();
        }
    }

    // Metodo para limpiar memoria
    public void closeRequestManager() throws IOException {
        if (logger != null) {
            logger.info("SYSTEM", "REQUEST_MANAGER_CLOSING", "Cerrando RequestManager", "SERVER");
        }

        if (in != null)
            in.close();
        if (out != null)
            out.close();
        if (console != null)
            console.closeConsole();

        if (logger != null) {
            logger.info("SYSTEM", "REQUEST_MANAGER_CLOSED", "RequestManager cerrado exitosamente", "SERVER");
        }
    }
}
