package com.chatserver.user;

import java.time.LocalDateTime;

import com.chatserver.protocol.Protocol;
import com.chatserver.utils.ProtocolManager;

public class User {
    private String username;
    private final ProtocolManager protocolManager;
    private LocalDateTime loginTime;
    private boolean isConnected;

    public User(String username, ProtocolManager protocolManager) {
        this.username = username;
        this.protocolManager = protocolManager;
        this.loginTime = LocalDateTime.now();
        this.isConnected = true;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public void sendMessage(String remitent, String message) {
        if (protocolManager != null) {
            protocolManager.sendReply(Protocol.RESP_MESSAGE_FROM, remitent + "|" + message);
        }
    }

    public void sendInfoMessage(String infoType, String message) {
        if (protocolManager != null) {
            protocolManager.sendReply(Protocol.RESP_INFO, infoType + "|" + message);
        }
    }

}
