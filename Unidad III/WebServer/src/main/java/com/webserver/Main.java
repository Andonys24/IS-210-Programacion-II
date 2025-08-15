package com.webserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    // Configuración del servidor según especificaciones del proyecto
    final static int PORT = 8089; // Puerto específico requerido en las instrucciones
    private static volatile boolean running = false;
    private static ServerSocket server;
    private static Logger logger;

    // Contador para generar IDs únicos y legibles para hilos virtuales
    private static volatile int clientCounter = 0;

    public static void main(String[] args) {
        Scanner input;
        logger = new Logger();

        Ui.cleanConsole();
        startServer();

        if (!running) {
            return;
        }

        input = new Scanner(System.in);

        // Crear gilo para poder escribir en consola par poder apagar el servidor
        var consoleThread = new Thread(() -> {

            while (running) {
                System.out.println("Ingrese 'exit' para apagar el servidor.");
                String command = input.nextLine();

                if (!"exit".equalsIgnoreCase(command.trim())) {
                    if (!command.trim().isEmpty()) {
                        System.out.println("Comando Invalido: '" + command.trim() + "'");
                    }
                    continue; // continuar esperando entrada
                }

                running = false;
                logger.logServerEvent("Comando de cierre recibido - deteniendo servidor");

                try {
                    server.close();
                    System.out.println("ServerSocket cerrado exitosamente");
                    logger.logServerEvent("Servidor web cerrado correctamente");
                } catch (IOException e) {
                    System.err.println("Error al cerrar el servidor: " + e.getMessage());
                    logger.logError("Error al cerrar servidor: " + e.getMessage());
                }
            }

        });

        consoleThread.setDaemon(true);
        consoleThread.start();

        // Loop principal del servidor - acepta conexiones indefinidamente
        // Cada cliente se maneja en su propio hilo virtual
        try {
            var ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Servidor corriendo en http://" + ip + ":" + PORT);
            System.out.println("Usando hilos virtuales para manejar clientes");
            System.out.println("Esperando conexiones de clientes...\n");

            while (running) {
                try {
                    var client = server.accept();

                    // Generar ID único y legible para el hilo virtual
                    int currentId = ++clientCounter;
                    String threadName = "client-" + String.format("%03d", currentId);

                    Thread.ofVirtual()
                            .name(threadName)
                            .start(new ClientHandler(client, logger));
                } catch (IOException e) {
                    if (running) {
                        // Error real durante accept()
                        System.err.println("Error al aceptar cliente: " + e.getMessage());
                        logger.logError("Error aceptando cliente: " + e.getMessage());
                    } else {
                        // Socket cerrado intencionalmente por comando exit
                        logger.logServerEvent("Socket cerrado - deteniendo aceptación de clientes");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error obteniendo IP del servidor: " + e.getMessage());
            logger.logError("Error de red: " + e.getMessage());
        }

        // Cerrar el scanner y logger para permitir que el programa termine
        input.close();

        logger.logServerEvent("Servidor web detenido - fin de ejecución");

        // Cerrar logger para que el programa termine completamente
        logger.close();
    } /*
       * Inicializa el ServerSocket en el puerto especificado por las instrucciones
       */

    private static void startServer() {
        try {
            server = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en: " + serverInfo());
            logger.logServerEvent("Servidor web iniciado en puerto " + PORT);
            running = true;
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            logger.logError("Fallo al iniciar servidor: " + e.getMessage());
            running = false;
        }
    }

    private static String serverInfo() throws UnknownHostException {

        if (server == null)
            return null;

        var host = InetAddress.getLocalHost().getHostAddress();
        var port = server.getLocalPort();

        return host + ":" + port;
    }
}