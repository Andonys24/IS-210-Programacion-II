package com.fileserver.client;

import java.io.IOException;
import java.net.Socket;

import com.fileserver.utils.Config;
import com.fileserver.utils.FileManager;
import com.fileserver.utils.Logger;
import com.fileserver.utils.UI;

public class ClientApp {

    private static boolean connected = false;
    private static Socket socket = null;

    public static void main(String[] args) {
        var fileManager = new FileManager(FileManager.Context.CLIENT);
        var logger = new Logger(fileManager);

        ClientRequestHandler clientHandler = null;

        logger.info("CLIENT", "APP_STARTED", "Aplicación cliente iniciada", "CLIENT");

        if (!establishConnection()) {
            logger.error("CLIENT", "CONNECTION_FAILED", "No se pudo establecer conexión con el servidor", "CLIENT");
            return;
        }

        logger.info("CLIENT", "CONNECTION_SUCCESS", "Conexión establecida exitosamente", "CLIENT");

        UI.cleanConsole();
        try {
            clientHandler = new ClientRequestHandler(socket.getInputStream(), socket.getOutputStream(), fileManager,
                    logger);

            logger.info("CLIENT", "HANDLERS_CREATED", "RequestManager y ClientRequestHandler creados", "CLIENT");

            while (connected) {
                boolean shouldExit = clientHandler.processServerCommand();
                if (shouldExit) {
                    connected = false;
                    logger.info("CLIENT", "EXIT_REQUESTED", "Servidor solicitó desconexión", "CLIENT");
                }
            }
        } catch (IOException e) {
            logger.error("CLIENT", "IO_ERROR", "Error de E/S: " + e.getMessage(), "CLIENT");
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                if (clientHandler != null)
                    clientHandler.close();

                if (socket != null && !socket.isClosed())
                    socket.close();

                logger.info("CLIENT", "RESOURCES_CLOSED", "Recursos cerrados correctamente", "CLIENT");
                System.out.println("Conexion Cerrada exitosamente.");

                // Cerrar logger al final
                logger.close();
            } catch (IOException e) {
                logger.error("CLIENT", "CLOSE_ERROR", "Error al cerrar recursos: " + e.getMessage(), "CLIENT");
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    private static boolean establishConnection() {
        final String HOST = Config.get("HOST");
        final int PORT = Integer.parseInt(Config.get("PORT"));
        try {
            socket = new Socket(HOST, PORT);
            System.out.println("Conexion establecida con el servidor");
            System.out.println(socket.getInetAddress() + ":" + socket.getPort());
            connected = true;
            return true;
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor");
            return false;
        }
    }

}
