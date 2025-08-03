package com.chatserver.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public abstract class ProtocolManager {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    protected ProtocolManager(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
        this.writer = new PrintWriter(this.socket.getOutputStream(), true);
    }

    public void sendReply(String header, String content) {
        content = ((content == null) ? "" : content).trim();
        writer.println(header + "|" + content);
    }

    public void sendReply(String header) {
        sendReply(header, null);
    }

    protected String[] getResponse() throws IOException {
        String response = reader.readLine();

        if (response == null)
            throw new IOException("Conexion cerrada - no hay mas datos disponibles");

        return response.split("\\|");
    }

    protected abstract boolean proccessResponse() throws IOException;

    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }

        } catch (IOException e) {
            System.out.println("Erro al cerrar la E/S: " + e.getMessage());
        }
    }
}
