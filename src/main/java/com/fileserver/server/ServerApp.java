package com.fileserver.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import com.fileserver.utils.Config;
import com.fileserver.utils.Logger;
import com.fileserver.utils.RequestManager;
import com.fileserver.utils.UI;

public class ServerApp {
    final static String HOST;
    final static int PORT;

    static {
        HOST = Config.get("HOST");
        PORT = Integer.parseInt(Config.get("PORT"));
    }

    public static void main(String[] args) {
        var logger = new Logger();
        UI.cleanConsole();

        try (var server = new ServerSocket(PORT);
                var client = server.accept()) {
            System.out.println("Servidor en espera en: " + PORT);
            System.out.println("Cliente conectado: " + client.getInetAddress());

            var rqm = new RequestManager(client.getInputStream(), client.getOutputStream(), "Server");

            rqm.sendMessage("Hola Cliente, Bienvenido");
            rqm.sendFile("Temporal.txt");
            rqm.sendMessage("Adios Cliente, Cuidate");

            rqm.closeRequestManager();
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }

    }
}
