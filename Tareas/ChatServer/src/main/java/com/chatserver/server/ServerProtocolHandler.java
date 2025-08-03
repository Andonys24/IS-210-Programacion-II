package com.chatserver.server;

import java.io.IOException;
import java.net.Socket;

import com.chatserver.protocol.Protocol;
import com.chatserver.user.User;
import com.chatserver.user.UserManager;
import com.chatserver.utils.ProtocolManager;

public class ServerProtocolHandler extends ProtocolManager {
    private User user;

    public ServerProtocolHandler(Socket socket) throws IOException {
        super(socket);
    }

    private void sendInfo(String type, String message) {
        sendReply(Protocol.RESP_INFO, type + "|" + message);
    }

    @Override
    public boolean proccessResponse() throws IOException {
        String[] response = getResponse();

        switch (response[0]) {
            case Protocol.CMD_ENTER:
                user = UserManager.addUser(response[1], this);
                break;
            case Protocol.CMD_MESSAGE:
                sendReply(Protocol.RESP_WRITE_TEXT, "Para quien (username)");
                String addressee = getResponse()[1];

                if (UserManager.findUserByUsername(addressee) != null) {
                    sendReply(Protocol.RESP_WRITE_TEXT, "Escribe tu mensaje privado");
                    String message = getResponse()[1];

                    if (UserManager.sendPrivateMessage(user, addressee, message)) {
                        sendInfo(Protocol.INFO_TYPE_SUCESS, "Mensaje privado enviado a " + addressee);
                    } else {
                        sendInfo(Protocol.INFO_TYPE_ERROR, "Error al enviar mensaje");
                    }
                } else {
                    sendInfo(Protocol.INFO_TYPE_ERROR, "Usuario '" + addressee + "' no encontrado");
                }
                break;
            case Protocol.CMD_ALL:
                sendReply(Protocol.RESP_WRITE_TEXT, "Escribe tu mensaje");
                int messagesSent = UserManager.broadcastMessage(user, getResponse()[1]);
                if (messagesSent > 0) {
                    sendInfo(Protocol.INFO_TYPE_SUCESS, "Mensaje enviado a " + messagesSent + " usuarios");
                } else {
                    sendInfo(Protocol.INFO_TYPE_ERROR, "No hay usuarios conectados");
                }
                break;
            case Protocol.CMD_USERS:
                String usersList = UserManager.getUserListExcept(user);
                sendReply(Protocol.RESP_USERS, usersList);
                break;
            case Protocol.CMD_CLEAN_CONSOLE:
                sendReply(Protocol.RESP_OK_CLEAN);
                break;
            case Protocol.CMD_RESP_ERROR:
                System.out.println(response[1]);
                break;
            case Protocol.CMD_EXIT:
                UserManager.removeUser(user);
                System.out.println("\nUsuario: " + user.getUsername() + " se desconecto\n");
                sendReply(Protocol.RESP_OK_EXIT);
                return true;
            default:
                sendReply(Protocol.RESP_CMD_ERROR, "El comando '" + response[0] + "' NO es valido");
                break;
        }

        sendReply(Protocol.RESP_CMD);

        return false;
    }

    // Método para manejar desconexiones abruptas
    public void handleAbruptDisconnection() {
        if (user != null) {
            UserManager.removeUser(user);
            System.out.println("\nUsuario: " + user.getUsername() + " se desconecto abruptamente\n");
        }
    }

}
