package com.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket client;
    private final Logger logger;

    public ClientHandler(Socket socket, Logger logger) {
        this.client = socket;
        this.logger = logger;
    }

    @Override
    public void run() {
        // Obtener información del cliente para logging
        String clientAddress = client.getRemoteSocketAddress().toString();
        String clientIP = client.getInetAddress().getHostAddress();
        ThreadLogger.log("Iniciado - Atendiendo cliente: " + clientAddress);

        // Log de conexión aceptada en el sistema de logs
        logger.info("CLIENT", "CONNECTION_ACCEPTED", clientAddress, clientIP);

        try (var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                var out = new PrintWriter(client.getOutputStream(), true, StandardCharsets.UTF_8)) {

            // Leer Solicitud HTTP del cliente
            var line = in.readLine();
            ThreadLogger.log("Solicitud recibida: " + line);

            // Log de la solicitud HTTP recibida
            if (line != null) {
                String[] parts = line.split(" ");
                String method = parts.length > 0 ? parts[0] : "UNKNOWN";
                String path = parts.length > 1 ? parts[1] : "/";
                logger.info("HTTP", "REQUEST_RECEIVED", method + " " + path, clientIP);
            }

            if (line != null && line.startsWith("GET ")) {
                ThreadLogger.log("Procesando solicitud GET");
                handleGetRequest(line, in, out);
            } else if (line != null && line.startsWith("POST ")) {
                ThreadLogger.log("Procesando solicitud POST");
                handlePostRequest(line, in, out);
            } else {
                ThreadLogger.log("Solicitud no válida o nula");
                logger.warn("HTTP", "INVALID_REQUEST", line != null ? line : "NULL", clientIP);
                RequestHandler.sendError404(out);
            }

        } catch (IOException e) {
            ThreadLogger.logError("ClientHandler: " + e.getMessage());
            logger.error("CLIENT", "CONNECTION_ERROR", e.getMessage(), clientIP);
        } finally {
            logger.debug("CLIENT", "CONNECTION_CLOSED", clientAddress, clientIP);
        }
    }

    /*
     * Procesa solicitudes GET: extrae la ruta y delega el manejo a RequestHandler
     * Maneja casos especiales como favicon.ico que los navegadores solicitan
     * automáticamente
     */
    private void handleGetRequest(String requestLine, BufferedReader in, PrintWriter out)
            throws IOException {
        // Extraer ruta de la línea de solicitud HTTP (formato: "GET /ruta HTTP/1.1")
        String[] parts = requestLine.split(" ");

        if (parts.length < 2) {
            ThreadLogger.logError("Solicitud GET malformada: " + requestLine);
            logger.error("HTTP", "MALFORMED_GET", requestLine, client.getInetAddress().getHostAddress());
            RequestHandler.sendError404(out);
            return;
        }

        String path = parts[1];
        ThreadLogger.log("Ruta solicitada: " + path);

        // Ignorar favicon para evitar ruido en logs (navegadores lo solicitan
        // automáticamente)
        if (path.equals("/favicon.ico")) {
            ThreadLogger.log("Ignorando solicitud de favicon");
            RequestHandler.sendError404(out);
            return;
        }

        // Log de recurso específico solicitado
        logger.debug("HTTP", "GET_RESOURCE", path, client.getInetAddress().getHostAddress());

        // Delegar todo el procesamiento a RequestHandler
        RequestHandler.handleGetRequest(path, out, client);
    }

    /*
     * Maneja la solicitudes POST
     */
    private void handlePostRequest(String requestLine, BufferedReader in, PrintWriter out) throws IOException {
        ThreadLogger.log("Procesando Solicitud POST");

        // Extraer la ruta del POST
        String[] parts = requestLine.split(" ");
        String path = parts[1];
        ThreadLogger.log("Ruta POST: " + path);

        // Log de la solicitud POST específica
        logger.info("HTTP", "POST_REQUEST", path, client.getInetAddress().getHostAddress());

        // Leer header HTTP
        Map<String, String> headers = readHeaders(in);

        // Leer el cuerpo de la solicitud
        String postData = readPostBody(in, headers);

        // Log del tamaño de datos recibidos (sin mostrar contenido por privacidad)
        if (postData != null && !postData.isEmpty()) {
            logger.debug("HTTP", "POST_DATA_SIZE", String.valueOf(postData.length()) + " chars",
                    client.getInetAddress().getHostAddress());
        }

        // Procesar los datos
        RequestHandler.handlePostRequest(path, postData, out, client, Thread.currentThread().getName());
    }

    /*
     * Lee los headers HTTP línea por línea hasta encontrar línea vacía
     * Formato esperado: "Header-Name: valor"
     */
    private Map<String, String> readHeaders(BufferedReader in) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String headerLine;

        // Leer headers hasta encontrar línea vacía (separador headers/body)
        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(": ", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
                // Log solo headers importantes para evitar spam
                if (headerParts[0].equals("Content-Length") || headerParts[0].equals("Content-Type")) {
                    ThreadLogger.log("Header importante: " + headerParts[0] + "=" + headerParts[1]);
                }
            }
        }

        return headers;
    }

    /*
     * Lee el cuerpo de la solicitud POST basándose en Content-Length
     * Esto es crítico para formularios que envían datos
     */
    private String readPostBody(BufferedReader in, Map<String, String> headers) throws IOException {
        String contentLength = headers.get("Content-Length");

        if (contentLength == null) {
            ThreadLogger.logError("POST sin Content-Length - no se pueden leer datos");
            return "";
        }

        try {
            // Parsear longitud y crear buffer del tamaño exacto
            int length = Integer.parseInt(contentLength);
            char[] body = new char[length];
            int totalRead = 0;

            // Leer exactamente Content-Length caracteres del cuerpo
            while (totalRead < length) {
                int read = in.read(body, totalRead, length - totalRead);
                if (read == -1) {
                    break; // EOF inesperado
                }
                totalRead += read;
            }

            String postData = new String(body, 0, totalRead);
            ThreadLogger.log("Datos POST leídos: " + postData.length() + " caracteres");
            return postData;
        } catch (NumberFormatException e) {
            ThreadLogger.logError("Content-Length inválido: " + contentLength);
            return "";
        }
    }

}
