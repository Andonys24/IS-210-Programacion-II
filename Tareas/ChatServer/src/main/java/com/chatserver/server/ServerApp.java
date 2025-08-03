package com.chatserver.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.chatserver.utils.Config;
import com.chatserver.utils.UI;

public class ServerApp {
    private static volatile boolean running = false;
    private static ServerSocket server;

    public static void main(String[] args) {
        Scanner input;

        UI.cleanConsole();
        startServer();

        // Salir si el servidor no esta iniciado
        if (!running)
            return;

        input = new Scanner(System.in);

        // Creacion de hilo para escribir en consola
        var consolethread = new Thread(() -> {
            System.out.println("Hilo de monitoreo inicializado");

            while (running) {
                System.out.println("Ingrese 'exit' para apagar el servidor.");
                String command = input.nextLine();

                if (!"exit".equals(command.trim().toLowerCase())) {
                    if (!command.trim().isEmpty()) {
                        System.out.println("Comando Invalido: '" + command.trim() + "'");
                    }
                    continue; // continuar esperando entrada
                }

                running = false;

                try {
                    server.close();
                    System.out.println("ServerSocket Cerrado exitosamente");
                } catch (IOException e) {
                    System.err.println("Error al cerrar el servidor.");
                }
            }

            System.out.println("Hilo de monitoreo de consola terminado");
        });

        consolethread.setDaemon(true);
        consolethread.start();

        // Logica para manejar a los clientes
        while (running) {
            try {
                Socket client = server.accept();

                Thread.ofVirtual().start(new ConnectionHandler(client));
            } catch (IOException e) {
                System.err.println("Error al aceptar el cliente: " + e.getMessage());
            }
        }

        // cerrar Scanner
        input.close();
    }

    private static void startServer() {
        final int PORT = Integer.parseInt(Config.get("PORT"));

        try {
            server = new ServerSocket(PORT);
            UI.cleanConsole();
            System.out.println("Servidor Corriendo en: " + serverInfo());
            running = true;
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            running = false;
        }
    }

    private static String serverInfo() throws UnknownHostException {

        if (server == null)
            return null;

        var host = InetAddress.getLocalHost().getHostAddress();
        var port = server.getLocalPort();
        var info = new StringBuilder();
        info.append(host).append(":").append(port);

        return info.toString();
    }

}
