package com.fileserver.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Scanner;

import com.fileserver.utils.Config;
import com.fileserver.utils.Logger;
import com.fileserver.utils.UI;

public class ServerApp {
    private static volatile boolean running = false;

    public static void main(String[] args) {
        final var PORT = Integer.parseInt(Config.get("PORT"));
        final ServerSocket server;
        final Scanner input;
        final Logger logger = new Logger();

        UI.cleanConsole();

        try {
            server = new ServerSocket(PORT);
            var host = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Servidor Corrriendo en: " + host + ":" + PORT);

            logger.info("SERVER", "HOST_RESOLVE", "Host: " + host, null);
            logger.info("SERVER", "SOCKET_CREATED", "ServerSocket creado en puerto: " + PORT, null);
            logger.info("SERVER", "STARTED", "Servidor activo en " + host + ":" + PORT, null);

            running = true;
        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            logger.error("SERVER", "STARTUP_ERROR", "Puerto: " + PORT + " - Error: " + e.getMessage(), null);
            logger.close();
            return;
        }

        // Hilo para monitorear comandos en consola
        input = new Scanner(System.in);
        Thread consoleThread = new Thread(() -> {
            logger.info("CONSOLE", "THREAD_STARTED", "Hilo de monitoreo iniciado", null);

            while (running) {
                System.out.println("Ingrese 'exit' para poder apagar el servidor");
                String line = input.nextLine();

                if (!"exit".equals(line.trim().toLowerCase())) {
                    if (!line.trim().isEmpty()) {
                        logger.warn("CONSOLE", "COMMAND_IGNORED", "Comando inválido: '" + line.trim() + "'", null);
                    }
                    continue;
                }

                running = false;

                try {
                    server.close();
                    logger.info("SERVER", "SHUTDOWN", "ServerSocket cerrado exitosamente", null);
                } catch (IOException e) {
                    System.err.println("Error al cerrar el Servidor");
                    logger.error("SERVER", "SHUTDOWN_ERROR", "Error al cerrar: " + e.getMessage(), null);
                }
            }

            logger.info("CONSOLE", "THREAD_ENDED", "Hilo de monitoreo terminado", null);
        });
        consoleThread.setDaemon(true);
        consoleThread.start();

        while (running) {

            try {
                var client = server.accept();
                var clientInfo = client.getInetAddress().toString() + ":" + client.getPort();

                logger.info("CLIENT", "CONNECTION_ACCEPTED", clientInfo, client.getInetAddress().toString());
                logger.debug("SYSTEM", "THREAD_CREATED", "Hilo virtual para: " + clientInfo, null);

                // Hilo virtual para atender los clientes
                Thread.ofVirtual().start((new ClientHandler(client, logger)));

            } catch (IOException e) {

                if (running) {
                    System.err.println("Error al aceptar el cliente: " + e.getMessage());
                    logger.error("CLIENT", "ACCEPT_ERROR", "Error: " + e.getMessage(), null);
                }
            }
        }

        // Cerrar entrada de datos y loggers
        input.close();
        logger.close();
    }
}
