package com.fileserver.client;

import java.io.IOException;
import java.net.Socket;

import com.fileserver.utils.RequestManager;
import com.fileserver.utils.UI;

public class ClientApp {

    private static boolean connected = false;
    private static Socket socket = null;
    private static RequestManager rm;

    public static void main(String[] args) {
        UI.cleanConsole();

        if (!establishConnection()) {
            return;
        }

        try {
            rm = new RequestManager(socket.getInputStream(), socket.getOutputStream());

            while (connected) {
                // Decidir si el cliente cierra conexion
                connected = !rm.processInput();
            }
            // Liberar memoria al cerrar la conexion
            cleanUp();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    private static boolean establishConnection() {
        try {
            socket = new Socket("localhost", 8080);
            System.out.println("Conexion establecida con el servidor");
            System.out.println(socket.getInetAddress() + ":" + socket.getPort());
            connected = true;
            return true;
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor");
            return false;
        }
    }

    private static void cleanUp() throws IOException {
        rm.closeRequestManager();
        socket.close();
        System.out.println("Conexion Cerrada exitosamente.");
    }

}
