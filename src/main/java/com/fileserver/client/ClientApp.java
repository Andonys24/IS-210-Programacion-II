package com.fileserver.client;

import java.net.Socket;

import com.fileserver.utils.Config;
import com.fileserver.utils.RequestManager;
import com.fileserver.utils.UI;

public class ClientApp {
    public static void main(String[] args) {
        Socket socket;
        String host = Config.get("HOST");
        int port = Integer.parseInt(Config.get("PORT"));

        UI.cleanConsole();

        try {
            socket = new Socket(host, port);
            System.out.println("Conectado al servidor");

            var rm = new RequestManager(socket.getInputStream(), socket.getOutputStream(), "CLIENT");
            rm.processInput();
            rm.processInput();
            rm.processInput();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
