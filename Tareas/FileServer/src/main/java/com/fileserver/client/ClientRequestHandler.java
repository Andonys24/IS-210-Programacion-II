package com.fileserver.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import com.fileserver.utils.CommandValidator;
import com.fileserver.utils.CommandValidator.ValidationResult;
import com.fileserver.utils.Console;
import com.fileserver.utils.FileManager;
import com.fileserver.utils.Logger;
import com.fileserver.utils.RequestManager;
import com.fileserver.utils.UI;

public class ClientRequestHandler extends RequestManager {
    private final FileManager fileManager;
    private final Logger logger;
    private Console console;

    public ClientRequestHandler(InputStream input, OutputStream output, FileManager fileManager, Logger logger) {
        super(input, output, fileManager);
        this.fileManager = fileManager;
        this.logger = logger;

        logger.info("CLIENT", "HANDLER_CREATED", "ClientRequestHandler inicializado", "CLIENT");
    }

    private void startConsole() {
        if (console == null) {
            console = new Console();
        }
    }

    private void handleUploadRequest() throws IOException {
        logger.info("CLIENT", "UPLOAD_REQUEST", "Iniciando proceso de subida de archivo", "CLIENT");
        startConsole();

        // Obtener archivos de FILES y DOWNLOADS (ya incluye "Regresar")
        File[] files = fileManager.getFilesFromMultipleDirectories(
                FileManager.Directory.FILES,
                FileManager.Directory.DOWNLOADS);
        String[] filesWithReturn = fileManager.getFilesStringFromMultipleDirectories(
                FileManager.Directory.FILES,
                FileManager.Directory.DOWNLOADS);

        // Si solo hay "Regresar" (length == 1), no hay archivos
        if (filesWithReturn.length == 1) {
            logger.warn("CLIENT", "NO_FILES_AVAILABLE", "No hay archivos disponibles para subir", "CLIENT");
            UI.generateTitle("No hay archivos disponibles para subir.", true);
            sendMessage("NO_FILES_TO_UPLOADS");
            return;
        }

        logger.info("CLIENT", "FILES_LISTED", (filesWithReturn.length - 1) + " archivos disponibles para subir",
                "CLIENT");

        boolean validate = false;
        int choice = 0;

        while (!validate) {
            UI.generateMenu("Seleccione un archivo para subir", filesWithReturn);
            choice = console.validateOption("Elije un archivo") - 1;

            ValidationResult result = CommandValidator.validateFileSelection(choice, filesWithReturn);

            if (!result.isValid()) {
                logger.warn("CLIENT", "INVALID_CHOICE", "Opción inválida: " + (choice + 1), "CLIENT");
                System.out.println(result.getErrorMessage());
                continue;
            }
            validate = true;
        }

        // Verificar si eligió "Regresar" (última opción)
        if (choice == filesWithReturn.length - 1) {
            logger.info("CLIENT", "UPLOAD_CANCELLED", "Usuario canceló la subida", "CLIENT");
            sendMessage("UPLOAD_CANCELLED");
            return;
        }

        File selectedFile = files[choice];
        logger.info("CLIENT", "FILE_SELECTED", "Archivo seleccionado: " + selectedFile.getName(), "CLIENT");

        try {
            sendMessage("DOWNLOAD_FILE");
            sendFile(selectedFile);
            logger.info("CLIENT", "FILE_SENT", "Archivo " + selectedFile.getName() + " enviado exitosamente", "CLIENT");
        } catch (Exception e) {
            logger.error("CLIENT", "FILE_SEND_ERROR",
                    "Error enviando " + selectedFile.getName() + ": " + e.getMessage(), "CLIENT");
            throw e;
        }
    }

    public boolean processServerCommand() throws IOException {
        String[] parts = getParts();
        RequestManager.CommandType header = RequestManager.CommandType.valueOf(parts[0].toUpperCase());

        logger.debug("CLIENT", "COMMAND_RECEIVED", "Comando recibido: " + header, "CLIENT");

        switch (header) {
            case MESSAGE:
                logger.info("CLIENT", "MESSAGE_RECEIVED", "Mensaje del servidor: " + parts[1], "CLIENT");
                System.out.println("Servidor: " + parts[1]);
                break;
            case TITLE:
                logger.info("CLIENT", "TITLE_RECEIVED", "Título recibido: " + parts[1], "CLIENT");
                UI.generateTitle(parts[1], true);
                break;
            case MENU:
                String title = parts[1];
                String[] options = Arrays.copyOfRange(parts, 2, parts.length);
                logger.info("CLIENT", "MENU_RECEIVED",
                        "Menú recibido: " + title + " con " + options.length + " opciones", "CLIENT");
                UI.generateMenu(title, options);
                break;
            case OPTION:
                startConsole();
                int option = console.validateOption(parts[1]);
                logger.info("CLIENT", "OPTION_SENT", "Opción enviada: " + option, "CLIENT");
                sendOption(Integer.toString(option));
                break;
            case DOWNLOAD_FILE:
                var filename = parts[1];
                var fileSize = Long.parseLong(parts[2]);

                logger.info("CLIENT", "DOWNLOAD_STARTED", "Descargando " + filename + " (" + fileSize + " bytes)",
                        "CLIENT");
                System.out.println("\nRecibiendo el archivo: " + filename);
                System.out.println("Con tamanio de: " + fileSize + " bytes");

                try {
                    receiveFile(filename, fileSize);
                    logger.info("CLIENT", "DOWNLOAD_COMPLETED", "Archivo " + filename + " descargado exitosamente",
                            "CLIENT");
                    System.out.println("Archivo Descargado Correctamente.\n");
                } catch (Exception e) {
                    logger.error("CLIENT", "DOWNLOAD_ERROR", "Error descargando " + filename + ": " + e.getMessage(),
                            "CLIENT");
                    throw e;
                }
                break;
            case UPLOAD_FILE:
                handleUploadRequest();
                break;
            case PAUSE:
                logger.info("CLIENT", "PAUSE_RECEIVED", "Pausa solicitada: " + parts[1], "CLIENT");
                startConsole();
                console.pause(parts[1]);
                break;
            case EXIT:
                logger.info("CLIENT", "EXIT_RECEIVED", "Servidor cerró la conexión", "CLIENT");
                System.out.println("Servidor Cerro la conexion");
                return true;
            default:
                logger.warn("CLIENT", "UNKNOWN_COMMAND", "Comando no reconocido: " + parts[0], "CLIENT");
                System.out.println("Comando no reconocido: " + parts[0]);
                break;
        }

        return false;
    }

    public void close() throws IOException {
        logger.info("CLIENT", "HANDLER_CLOSING", "Cerrando ClientRequestHandler", "CLIENT");

        if (console != null) {
            console.close();
            logger.debug("CLIENT", "CONSOLE_CLOSED", "Console cerrada", "CLIENT");
        }

        super.close();
        logger.debug("CLIENT", "REQUEST_MANAGER_CLOSED", "RequestManager cerrado", "CLIENT");
    }
}
