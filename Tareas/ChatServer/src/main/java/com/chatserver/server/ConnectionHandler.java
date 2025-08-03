package com.chatserver.server;

import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private final Socket client;
    private ServerProtocolHandler protocolHandler;

    public ConnectionHandler(Socket socket) {
        this.client = socket;
    }

    @Override
    public void run() {
        try {
            protocolHandler = new ServerProtocolHandler(client);

            while (true) {
                if (protocolHandler.proccessResponse()) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Desconexión abrupta detectada: " + e.getMessage());
            // Manejar desconexión abrupta
            if (protocolHandler != null) {
                protocolHandler.handleAbruptDisconnection();
            }
        } finally {
            cleanUp();
        }
    }

    private void cleanUp() {
        try {
            if (protocolHandler != null)
                protocolHandler.close();

            client.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}
