package com.fileserver.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fileserver.utils.CommandValidator;
import com.fileserver.utils.CommandValidator.ValidationResult;
import com.fileserver.utils.FileManager;
import com.fileserver.utils.FileManager.Directory;
import com.fileserver.utils.Logger;
import com.fileserver.utils.RequestManager;

public class ServerRequestHandler extends RequestManager {
    private final Logger logger;
    private final FileManager fileManager;

    public ServerRequestHandler(InputStream input, OutputStream output, FileManager fileManager, Logger logger) {
        super(input, output, fileManager);
        this.fileManager = fileManager;
        this.logger = logger;
    }

    // Metodos Especificos del Servidor
    public void sendMenu(String title, String[] options) throws IOException {
        var content = new StringBuilder(title);
        for (String option : options) {
            content.append("|").append(option);
        }
        sendReply(RequestManager.CommandType.MENU, content.toString());

        logger.info("MENU", "MENU_SENT", "Menú enviado: " + title, "SERVER");
    }

    public int readClientOption() throws IOException {
        String[] parts = getParts();

        if ("OPTION".equals(parts[0])) {
            int option = Integer.parseInt(parts[1]);
            logger.info("CLIENT", "OPTION_RECEIVED", "Opción recibida: " + option, "SERVER");
            return option;
        }

        logger.warn("CLIENT", "INVALID_COMMAND", "Esperaba OPTION pero recibió: " + parts[0], "SERVER");
        throw new IllegalArgumentException("Comando inválido: " + parts[0]);
    }

    public void sendFileWithLogging(File file) throws IOException {
        try {
            logger.info("FILE", "SEND_FILE_STARTED", file.getName(), "SERVER");
            sendFile(file);
            logger.info("FILE", "SEND_FILE_COMPLETED", file.getName() + " enviado exitosamente", "SERVER");
        } catch (Exception e) {
            logger.error("FILE", "SEND_FILE_ERROR", file.getName() + " - " + e.getMessage(), "SERVER");
            throw e;
        }
    }

    public void receiveFileWithLogging(String fileName, long fileSize)
            throws IOException {
        try {
            logger.info("FILE", "RECEIVE_FILE_STARTED", fileName + " (" + fileSize + " bytes)", "SERVER");
            receiveFile(fileName, fileSize);
            logger.info("FILE", "RECEIVE_FILE_COMPLETED", fileName + " recibido exitosamente", "SERVER");
        } catch (Exception e) {
            logger.error("FILE", "RECEIVE_FILE_ERROR", fileName + " - " + e.getMessage(), "SERVER");
            throw e;
        }
    }

    // Casos Especificos del Servidor
    public void handleListFiles() throws IOException {
        String[] filesString = fileManager.getFilesStringFromMultipleDirectories(
                FileManager.Directory.FILES,
                FileManager.Directory.DOWNLOADS);

        // verificamos si hay más de 1 elemento (archivos + "Regresar")
        if (filesString.length == 1) {
            sendReply(RequestManager.CommandType.TITLE, "No hay Archivos Disponibles");
            logger.info("FILE", "LIST_EMPTY", "No hay archivos disponibles", "SERVER");
        } else {
            sendMenu("Archivos Disponibles", filesString);
            logger.info("FILE", "LIST_SENT", (filesString.length - 1) + " archivos enviados", "SERVER");
        }
    }

    public void handleDownloadRequest() throws IOException {
        // Obtener archivos de FILES y DOWNLOADS (ya incluye "Regresar")
        File[] files = fileManager.getFilesFromMultipleDirectories(
                FileManager.Directory.FILES,
                FileManager.Directory.DOWNLOADS);
        String[] filesWithReturn = fileManager.getFilesStringFromMultipleDirectories(
                FileManager.Directory.FILES,
                FileManager.Directory.DOWNLOADS);

        boolean validate = false;
        int choice = 0;

        while (!validate) {
            sendMenu("Archivos Disponibles para Descargar", filesWithReturn);
            sendOption("Elija un Archivo");
            choice = readClientOption() - 1;

            ValidationResult result = CommandValidator.validateFileSelection(choice, filesWithReturn);
            if (!result.isValid()) {
                sendMessage(result.getErrorMessage());
                logger.warn("CLIENT", "INVALID_CHOICE", "Opción inválida: " + (choice + 1), "SERVER");
                continue;
            }
            validate = true;
        }

        // Verificar si eligió "Regresar" (última opción)
        if (choice == filesWithReturn.length - 1) {
            logger.info("CLIENT", "RETURNED_TO_MENU", "Cliente regresó al menú", "SERVER");
            return;
        }

        // Obtener el archivo seleccionado y su directorio origen
        File selectedFile = files[choice];
        Directory sourceDirectory = fileManager.getDirectoryFromPath(selectedFile.getAbsolutePath());

        logger.info("FILE", "FILE_SELECTED", selectedFile.getName() + " desde " + sourceDirectory, "SERVER");
        sendFileWithLogging(selectedFile);
        sendMessage("Descarga Completada.");
    }

    public void handleUploadRequest() throws IOException {
        logger.info("CLIENT", "UPLOAD_REQUEST", "Cliente solicitó subir archivo", "SERVER");

        sendReply(RequestManager.CommandType.UPLOAD_FILE, "");

        // Esperar respuesta del cliente
        String[] response = getParts();

        switch (response[1]) {
            case "NO_FILES_TO_UPLOADS":
                sendMessage("El cliente no tiene archivos para subir");
                logger.info("CLIENT", "NO_FILES_TO_UPLOAD", "Cliente sin archivos", "SERVER");
                break;
            case "UPLOAD_CANCELLED":
                sendMessage("Subida cancelada por el cliente");
                logger.info("CLIENT", "UPLOAD_CANCELLED", "Cliente canceló subida", "SERVER");
                break;
            case "DOWNLOAD_FILE": // Cliente envía archivo
                String[] parts = getParts();
                String fileName = parts[1];
                long fileSize = Long.parseLong(parts[2]);
                receiveFileWithLogging(fileName, fileSize);
                sendMessage("Archivo subido correctamente");
                break;
            default:
                logger.warn("CLIENT", "UNKNOWN_RESPONSE", "Respuesta desconocida: " + response[0], "SERVER");
                sendMessage("Respuesta no reconocida");
        }
    }

}
