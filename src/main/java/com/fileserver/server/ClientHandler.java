package com.fileserver.server;

import java.io.IOException;
import java.net.Socket;

import com.fileserver.utils.Logger;
import com.fileserver.utils.RequestManager;

public class ClientHandler implements Runnable {

    private final Socket client;
    private final Logger logger;

    public ClientHandler(Socket socket, Logger logger) {
        this.client = socket;
        this.logger = logger;
    }

    @Override
    public void run() {
        String[] options = { "Listar Archivos", "Solicitar archivo", "Subir Archivo", "Salir" };
        try {
            var rm = new RequestManager(client.getInputStream(), client.getOutputStream());
            rm.sendMenu("Menu Principal", options);
            rm.sendMessage("Hasta Pronto");
            rm.closeRequestManager();
            disconnectClient();
        } catch (IOException e) {
            disconnectClient();
            System.out.println("Error en el hilo: " + e.getMessage());
        }
    }

    private void disconnectClient() {
        try {
            client.close();
        } catch (IOException e) {
            System.out.println("Error al cerrar la conexion al cliente:" + e.getMessage());
            logger.error("CLIENT", "DISCONNECT_ERROR", "Error al cerrar conexión: " + e.getMessage(),
                    client.getInetAddress().toString());
        }
    }
}
