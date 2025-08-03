package com.chatserver.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.chatserver.protocol.Protocol;
import com.chatserver.utils.ProtocolManager;
import com.chatserver.utils.UI;
import com.chatserver.utils.Validators;

public class ClientProtocolHandler extends ProtocolManager {
    private Scanner input;

    public ClientProtocolHandler(Socket socket, Scanner input) throws IOException {
        super(socket);
        this.input = input;
    }

    private void handleInfoMessage(String[] response) {
        System.out.println(); // Línea en blanco antes
        switch (response[1]) {
            case Protocol.INFO_TYPE_SUCESS:
                System.out.println("  [OK] " + response[2]);
                break;
            case Protocol.INFO_TYPE_ERROR:
                System.out.println("  [ERROR] " + response[2]);
                break;
            case Protocol.INFO_TYPE_WARNING:
                System.out.println("  [ALERTA] " + response[2]);
                break;
            case Protocol.INTO_TYPE_ENTER:

                System.out.println("  >> " + response[2] + " se unio al chat");
                break;
            case Protocol.INFO_TYPE_EXIT:
                System.out.println("  << " + response[2] + " salio del chat");
                break;
            default:
                System.out.println("  [INFO] " + response[1]);
                break;
        }
        System.out.println(); // Línea en blanco después
    }

    @Override
    public boolean proccessResponse() throws IOException {
        String[] response = getResponse();

        switch (response[0]) {
            case Protocol.RESP_OK_ENTER:
                UI.generateTitle("SERVIDOR DE CHAT CONCURRENTE", false);
                System.out.println(response[1]);
                break;
            case Protocol.RESP_ERROR_ENTER:
                System.out.println("Cerrando Conexion: " + response[1]);
                return true;
            case Protocol.RESP_MESSAGE_FROM:
                System.out.println();
                System.out.println("  [" + response[1] + "]: " + response[2]);
                System.out.println();
                break;
            case Protocol.RESP_INFO:
                handleInfoMessage(response);
                break;
            case Protocol.RESP_USERS:
                System.out.println();
                if (response[1].startsWith("0")) {
                    System.out.println("  [USUARIOS] No hay otros usuarios conectados");
                } else {
                    System.out.println("  [USUARIOS] Conectados: " + response[1]);
                }
                System.out.println();
                break;
            case Protocol.RESP_CMD:
                System.out.println("----------------------------");
                sendReply(Validators.readValidatedLine(input, "Ingrese un comando"));
                break;
            case Protocol.RESP_WRITE_TEXT:
                sendReply(Protocol.CMD_TEXT, Validators.readValidatedLine(input, response[1]));
                break;
            case Protocol.RESP_CMD_ERROR:
                System.out.println();
                System.out.println("  [ERROR] " + response[1]);
                System.out.println();
                break;
            case Protocol.RESP_OK_EXIT:
                System.out.println("Cerrando Conexion...");
                return true;
            case Protocol.RESP_OK_CLEAN:
                UI.generateTitle("SERVIDOR DE CHAT CONCURRENTE", true);
                break;
            default:
                sendReply(Protocol.CMD_RESP_ERROR, "La respuesta '" + response[0] + "' NO es valida");
                break;
        }

        return false;
    }

}
