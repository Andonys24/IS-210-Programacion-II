package com.chatserver.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.chatserver.protocol.Protocol;
import com.chatserver.utils.Config;
import com.chatserver.utils.UI;
import com.chatserver.utils.Validators;

public class ClientApp {
    private static Socket socket;
    private static ClientProtocolHandler protocolHandler;
    private static Scanner input;

    public static void main(String[] args) {
        UI.cleanConsole();

        if (!establishConnection()) {
            return;
        }

        try {
            input = new Scanner(System.in);
            protocolHandler = new ClientProtocolHandler(socket, input);

            // Enviar solamente la primera ves el username
            protocolHandler.sendReply(Protocol.CMD_ENTER, Validators.validateUsername(input, "Ingrese su username"));
            while (true) {
                if (protocolHandler.proccessResponse()) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Ocurrio un error: " + e.getMessage());
        } finally {
            cleanUp();
        }
    }

    private static boolean establishConnection() {
        final String HOST = Config.get("HOST");
        final int PORT = Integer.parseInt(Config.get("PORT"));

        try {
            socket = new Socket(HOST, PORT);
            System.out.println("Conexion establecida con el Servidor");
            System.out.println(socket.getInetAddress() + ":" + socket.getPort());
            return true;
        } catch (IOException e) {
            System.err.println("Error al conectar con el Servidor");
            return false;
        }
    }

    private static void cleanUp() {
        try {
            if (protocolHandler != null)
                protocolHandler.close();

            socket.close();

            if (input != null)
                input.close();

        } catch (IOException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}
