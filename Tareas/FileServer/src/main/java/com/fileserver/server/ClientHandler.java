package com.fileserver.server;

import java.io.IOException;
import java.net.Socket;

import com.fileserver.utils.FileManager;
import com.fileserver.utils.Logger;
import com.fileserver.utils.RequestManager;

public class ClientHandler implements Runnable {

    private final Socket client;
    private final FileManager fileManager;
    private final Logger logger;
    private ServerRequestHandler serverHandler;

    public ClientHandler(Socket socket, FileManager fileManager, Logger logger) {
        this.client = socket;
        this.fileManager = fileManager;
        this.logger = logger;
    }

    @Override
    public void run() {
        boolean keepRunning = true;
        String[] options = { "Listar Archivos", "Solicitar archivo", "Subir Archivo", "Salir" };
        String clientInfo = client.getInetAddress().toString() + ":" + client.getPort();

        logger.info("CLIENT", "SESSION_STARTED", "Sesión iniciada para: " + clientInfo,
                client.getInetAddress().toString());

        try {
            serverHandler = new ServerRequestHandler(client.getInputStream(), client.getOutputStream(), fileManager,
                    logger);

            logger.debug("CLIENT", "REQUEST_MANAGER_CREATED", "RequestManager y ServerRequestHandler creados",
                    clientInfo);

            while (keepRunning) {
                serverHandler.sendMenu("Menu Principal", options);
                serverHandler.sendOption("Ingrese una opcion");

                int option = serverHandler.readClientOption();

                logger.debug("CLIENT", "MENU_OPTION_PROCESSING", "Procesando opción: " + option, clientInfo);

                switch (option) {
                    case 1 -> {
                        logger.info("CLIENT", "LIST_FILES_REQUEST", "Cliente solicitó listar archivos", clientInfo);
                        serverHandler.handleListFiles();
                    }
                    case 2 -> {
                        logger.info("CLIENT", "DOWNLOAD_REQUEST", "Cliente solicitó descargar archivo", clientInfo);
                        serverHandler.handleDownloadRequest();
                    }
                    case 3 -> {
                        logger.info("CLIENT", "UPLOAD_REQUEST", "Cliente solicitó subir archivo", clientInfo);
                        serverHandler.handleUploadRequest();
                    }
                    case 4 -> {
                        logger.info("CLIENT", "EXIT_REQUEST", "Cliente solicitó salir", clientInfo);
                        serverHandler.sendExit();
                        keepRunning = false;
                    }
                    default -> {
                        logger.warn("CLIENT", "INVALID_OPTION", "Opción inválida: " + option, clientInfo);
                        serverHandler.sendMessage("Opcion invalida");
                    }
                }

                if (keepRunning) {
                    serverHandler.sendReply(RequestManager.CommandType.PAUSE, "continuar");
                }
            }

        } catch (IOException e) {
            logger.error("CLIENT", "HANDLER_ERROR", "Error en manejo de cliente: " + e.getMessage(), clientInfo);
        } finally {
            logger.info("CLIENT", "SESSION_ENDING", "Finalizando sesión para: " + clientInfo,
                    client.getInetAddress().toString());
            cleanUp();
        }
    }

    private void cleanUp() {
        String clientInfo = client.getInetAddress().toString() + ":" + client.getPort();

        try {
            logger.debug("CLIENT", "CLEANUP_STARTED", "Iniciando limpieza de recursos", clientInfo);
            if (serverHandler != null)
                serverHandler.close();
            client.close();
            logger.info("CLIENT", "DISCONNECTED", "Cliente desconectado exitosamente",
                    client.getInetAddress().toString());
        } catch (IOException e) {
            System.out.println("Error al cerrar la conexion al cliente:" + e.getMessage());
            logger.error("CLIENT", "DISCONNECT_ERROR", "Error al cerrar conexión: " + e.getMessage(),
                    client.getInetAddress().toString());
        }
    }
}
