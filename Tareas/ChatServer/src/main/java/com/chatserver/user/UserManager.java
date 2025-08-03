package com.chatserver.user;

import com.chatserver.protocol.Protocol;
import com.chatserver.utils.ProtocolManager;

public class UserManager {
    private static final int MAX_USERS = 50;
    private static final User[] connectedUsers = new User[MAX_USERS];
    private static int userCount = 0;
    private static final Object userLock = new Object();

    public static User addUser(String username, ProtocolManager protocolManager) {
        synchronized (userLock) {
            if (username == null || username.trim().isEmpty()) {
                protocolManager.sendReply(Protocol.RESP_ERROR_ENTER, "El nombre de usuario esta vacio");
                return null;
            }

            username = username.trim();

            // Verificar si el username ya existe
            for (int i = 0; i < userCount; i++) {
                if (connectedUsers[i] != null &&
                        connectedUsers[i].getUsername().equals(username)) {
                    protocolManager.sendReply(Protocol.RESP_ERROR_ENTER, "el nombre de usuario ya esta en uso");
                    return null; // Username ya existe
                }
            }

            // Verificar si hay espacio disponible
            if (userCount >= MAX_USERS) {
                protocolManager.sendReply(Protocol.RESP_ERROR_ENTER, "NO hay mas espacios disponibles");
                return null; // Sin espacio
            }

            // Agregar nuevo usuario
            User newUser = new User(username, protocolManager);
            connectedUsers[userCount] = newUser;
            userCount++;

            protocolManager.sendReply(Protocol.RESP_OK_ENTER, "Bienvenido " + newUser.getUsername());

            // Notificar a otros usuarios que entró alguien nuevo
            broadcastUserInfo(Protocol.INTO_TYPE_ENTER, newUser);

            return newUser;
        }
    }

    private static void broadcastUserInfo(String infoType, User excludeUser) {
        for (int i = 0; i < userCount; i++) {
            if (connectedUsers[i] != null && connectedUsers[i] != excludeUser) {
                connectedUsers[i].sendInfoMessage(infoType, excludeUser.getUsername());
            }
        }
    }

    public static String getUserListExcept(User excludeUser) {
        synchronized (userLock) {
            if (userCount <= 1) {
                return "0";
            }

            StringBuilder users = new StringBuilder();
            int count = 0;

            for (int i = 0; i < userCount; i++) {
                if (connectedUsers[i] != null) {
                    if (connectedUsers[i] != excludeUser) {
                        if (count > 0)
                            users.append(" ");

                        users.append(connectedUsers[i].getUsername());
                        count++;
                    }
                }
            }

            return count + " " + users.toString();
        }
    }

    public static boolean removeUser(User userToRemove) {
        synchronized (userLock) {
            for (int i = 0; i < userCount; i++) {
                if (connectedUsers[i] == userToRemove) {
                    String username = connectedUsers[i].getUsername();

                    // Notificar a otros usuarios que alguien se fue
                    broadcastUserInfo(Protocol.INFO_TYPE_EXIT, userToRemove);

                    // Mover el último usuario a esta posición
                    connectedUsers[i] = connectedUsers[userCount - 1];
                    connectedUsers[userCount - 1] = null;
                    userCount--;

                    System.out.println("Usuario " + username + " eliminado de la lista");
                    return true;
                }
            }
            return false;
        }
    }

    public static User findUser(User userToFind) {
        synchronized (userLock) {
            for (int i = 0; i < userCount; i++) {
                if (connectedUsers[i] == userToFind) {
                    return connectedUsers[i];
                }
            }
            return null;
        }
    }

    public static User findUserByUsername(String username) {
        synchronized (userLock) {
            for (int i = 0; i < userCount; i++) {
                if (connectedUsers[i] != null) {
                    if (connectedUsers[i].getUsername().equals(username))
                        return connectedUsers[i];
                }
            }
            return null;
        }
    }

    public static boolean userExists(User userToCheck) {
        synchronized (userLock) {
            for (int i = 0; i < userCount; i++) {
                if (connectedUsers[i] == userToCheck) { // ← Comparación directa
                    return true;
                }
            }
            return false;
        }
    }

    public static int broadcastMessage(User fromUser, String message) {
        synchronized (userLock) {
            int messageSent = 0;
            for (int i = 0; i < userCount; i++) {
                if (connectedUsers[i] != null) {
                    if (connectedUsers[i] != fromUser) {
                        connectedUsers[i].sendMessage(fromUser.getUsername(), message);
                        messageSent++;
                    }
                }
            }
            return messageSent;
        }
    }

    public static boolean sendPrivateMessage(User fromUser, String toUsername, String message) {
        synchronized (userLock) {
            for (int i = 0; i < userCount; i++) {
                if (connectedUsers[i] != null) {
                    if (connectedUsers[i].getUsername().equals(toUsername)) {
                        connectedUsers[i].sendMessage(fromUser.getUsername(), message);
                        return true;
                    }
                }
            }
            return false;
        }
    }

}
