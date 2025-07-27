package com.fileserver.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.fileserver.utils.RequestManager;
import com.fileserver.utils.UI;

public class ClientApp {

    private static boolean connected = false;
    private static Socket socket = null;
    private static RequestManager rm;

    public static void main(String[] args) {
        Scanner input;
        UI.cleanConsole();

        if (!establishConnection()) {
            return;
        }

        input = new Scanner(System.in);

        try {
            rm = new RequestManager(socket.getInputStream(), socket.getOutputStream());
            rm.processInput();
            rm.processInput();
            cleanUp();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        input.close();

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
