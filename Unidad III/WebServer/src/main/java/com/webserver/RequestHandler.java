package com.webserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {

    // Enviar Error si la pagina no se encuentra
    public static void sendError404(PrintWriter out) {
        try {
            if (FileManager.fileExists("error404.html")) {
                // Enviar página 404 con CSS
                sendHTMLResponse(out, "error404.html");
            } else {
                // Fallback a respuesta simple
                out.println("HTTP/1.1 404 Not Found");
                out.println("Content-Type: text/plain; charset=UTF-8");
                out.println("");
                out.println("Error: Recurso no encontrado");
            }
        } catch (IOException e) {
            // Fallback en caso de error
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-Type: text/plain; charset=UTF-8");
            out.println("");
            out.println("Error: Recurso no encontrado");
        }
    }

    /**
     * Maneja las solicitudes GET y enruta según la ruta solicitada
     */
    public static void handleGetRequest(String path, PrintWriter out, Socket client)
            throws IOException {
        ThreadLogger.log("procesando ruta: " + path);

        // Normalizar la ruta
        String normalizedPath = normalizePath(path);

        // Verificar si el archivo Existe
        if (!FileManager.fileExists(normalizedPath)) {
            ThreadLogger.log("Archivo No encontrado: " + normalizedPath);
            sendError404(out);
            return;
        }

        // Determinar el tipo de archivo y servir
        String fileExtension = getFileExtension(normalizedPath);
        serveFileByExtension(normalizedPath, fileExtension, out, client);

    }

    /*
     * Maneja las solicitudes POST de los formularios
     */
    public static void handlePostRequest(String path, String postData, PrintWriter out, Socket client,
            String threadName)
            throws IOException {
        ThreadLogger.log("Procesando formulario en: " + path);

        // Parsear los datos del formulario
        Map<String, String> formData = parseFormData(postData);

        // Mostrar datos en consola
        ThreadLogger.log("Datos del formulario: ");
        formData.forEach((key, value) -> System.out.println("  " + key + " = " + value));

        // Guardar datos en archivo
        saveFormDataToFile(formData, threadName);

        // Verificar si se solicita un recurso
        String requestedResource = formData.get("recurso");
        if (requestedResource != null && !requestedResource.isEmpty()) {
            serveRequestedResource(requestedResource, out, client);
        } else {
            sendFormSuccessResponse(out);
        }
    }

    /*
     * Parsea los datos de formularios POST en formato URL-encoded
     * Ejemplo: "nombre=Juan&email=juan@email.com" -> Map{nombre: Juan, email:
     * juan@email.com}
     */
    private static Map<String, String> parseFormData(String postData) {
        Map<String, String> formData = new HashMap<>();

        if (postData != null && !postData.isEmpty()) {
            // Dividir por & para obtener pares clave=valor
            String[] pairs = postData.split("&");
            for (String pair : pairs) {
                // Dividir cada par en clave y valor (máximo 2 partes por si hay = en el valor)
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    try {
                        // Decodificar URL encoding (%20 -> espacio, %40 -> @, etc.)
                        String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                        String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                        formData.put(key, value);
                    } catch (Exception e) {
                        ThreadLogger.logError("Error decodificando par: " + pair);
                    }
                }
            }
        }
        return formData;
    }

    /*
     * Guarda los datos del formulario en un archivo único con timestamp
     * Organiza los envíos en la carpeta form_submissions/ para auditoría
     */
    private static void saveFormDataToFile(Map<String, String> formData, String threadName) {
        // Crear directorio para formularios si no existe
        FileManager.createDirectory("form_submissions");

        // Generar nombre único usando timestamp (reemplazar : para evitar problemas en
        // Windows)
        String timestamp = LocalDateTime.now().toString();
        String filename = "form_data_" + timestamp.replace(":", "-") + ".txt";

        // Construir contenido estructurado del archivo
        StringBuilder content = new StringBuilder();
        content.append("=== FORMULARIO ENVIADO ===\n");
        content.append("Timestamp: ").append(timestamp).append("\n");
        content.append("Hilo: ").append(threadName).append("\n");
        content.append("Datos:\n");

        formData.forEach((key, value) -> content.append("  ").append(key).append(" = ").append(value).append("\n"));

        // Guardar en la carpeta form_submissions
        FileManager.appendToFile("form_submissions/" + filename, content.toString());
        ThreadLogger.log("Datos de formulario guardados en: form_submissions/" + filename);
    }

    /*
     * Normalizar la ruta para poder mapearla correctamente a los recursos
     * Convierte rutas del navegador a rutas de archivos del servidor
     */
    private static String normalizePath(String path) {
        // Manejar solicitudes a la raíz del sitio
        if ("/".equals(path) || "/index.html".equals(path)) {
            return "index.html";
        }

        // Eliminar slash inicial para obtener la ruta de los recursos
        String normalized = path.startsWith("/") ? path.substring(1) : path;

        // Registrar el mapeo de rutas para debugging de ruteo complejo
        ThreadLogger.log("Ruta normalizada: " + path + " -> " + normalized);

        // Si no tiene extensión y no está en una subcarpeta, asumir que es HTML en
        // /html/

        // Esto permite URLs como /nosotros -> html/nosotros.html
        if (!normalized.contains(".") && !normalized.contains("/")) {
            return "html/" + normalized;
        }

        return normalized;
    }

    /*
     * Extraer la extension del archivo
     */
    public static String getFileExtension(String filePath) {
        int lastDot = filePath.lastIndexOf('.');
        return lastDot > 0 ? filePath.substring(lastDot + 1).toLowerCase() : "";
    }

    private static void serveRequestedResource(String resourceName, PrintWriter out, Socket client)
            throws IOException {
        ThreadLogger.log("Recurso solicitado:" + resourceName);

        if (FileManager.fileExists(resourceName)) {
            ThreadLogger.log("Enviando recurso:" + resourceName);

            // Determinar tipo MIME y enviar
            String extension = getFileExtension(resourceName);
            switch (extension) {
                case "jpg":
                case "png":
                case "webp":
                case "avif":
                case "gif":
                    sendImageResponse(client, resourceName);
                    break;
                default:
                    break;
            }

            sendResourceSentResponse(out, resourceName);
        } else {
            ThreadLogger.logError("Recuro no encontrado: " + resourceName);
            sendError404(out);
        }
    }

    /*
     * Sirve el archivo segun la extension
     */

    private static void serveFileByExtension(String filePath,
            String extension, PrintWriter out, Socket client) throws IOException {
        ThreadLogger.log("Sirviendo " + extension.toUpperCase() + ": " + filePath);

        switch (extension) {
            case "html":
                sendHTMLResponse(out, filePath);
                break;
            case "css":
                sendCSSResponse(out, filePath);
                break;
            case "js":
                sendJavaScriptResponse(out, filePath);
                break;

            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "webp":
            case "avif":
                sendImageResponse(client, filePath);
                break;
            default:
                ThreadLogger.log("Tipo de archivo no soportado: " + extension);
                sendError404(out);
                break;
        }
    }

    /*
     * Metodos de respuesta especificos
     */

    // Enviar Contenido HTML
    private static void sendResponse(PrintWriter out, String filePath, String contentType) throws IOException {
        if (!FileManager.fileExists(filePath)) {
            sendError404(out);
            return;
        }

        var content = FileManager.readFile(filePath);
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: " + contentType);
        out.println("");
        out.println(content);
    }

    // Enviar Archivo HTML al cliente
    public static void sendHTMLResponse(PrintWriter out, String filePath) throws IOException {
        var htmlContentType = "text/html; charset=UTF-8";
        sendResponse(out, filePath, htmlContentType);
    }

    // Enviar Archivo CSS al cliente
    public static void sendCSSResponse(PrintWriter out, String filePath) throws IOException {
        var cssContentType = "text/css; charset=UTF-8";
        sendResponse(out, filePath, cssContentType);
    }

    // Enviar Archivo JavaScript al cliente
    public static void sendJavaScriptResponse(PrintWriter out, String filePath) throws IOException {
        var jsContentType = "application/javascript; charset=UTF-8";
        sendResponse(out, filePath, jsContentType);
    }

    // Enviar imagen al cliente
    public static void sendImageResponse(Socket client, String filePath) throws IOException {
        try (var out = client.getOutputStream()) {
            if (!FileManager.fileExists(filePath)) {
                var writer = new PrintWriter(out, true);
                sendError404(writer);
            } else {
                byte[] content = FileManager.readBinaryFile(filePath);
                var writer = new PrintWriter(out, true);

                // Determinar tipo MIME correcto
                String mimeType = MimeTypesDetector.getMimeType(filePath);
                String fileName = java.nio.file.Paths.get(filePath).getFileName().toString();

                // Enviar encabezados HTTP
                writer.println("HTTP/1.1 200 OK");
                writer.println("Content-Type: " + mimeType);
                writer.println("Content-Disposition: attachment; filename=\"" + fileName + "\"");
                writer.println("Content-Length: " + content.length);
                writer.println("");

                // Enviar la imagen
                assert content != null;
                out.write(content);
            }
        }
    }

    private static void sendFormSuccessResponse(PrintWriter out) {
        try {
            if (FileManager.fileExists("success.html")) {
                // Enviar página de éxito con CSS
                sendHTMLResponse(out, "success.html");
            } else {
                // Fallback a respuesta simple
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html; charset=UTF-8");
                out.println("");
                out.println("<html><body>");
                out.println("<h2>Formulario enviado exitosamente</h2>");
                out.println("<p>Sus datos han sido procesados correctamente.</p>");
                out.println("<a href='/'>Volver al inicio</a>");
                out.println("</body></html>");
            }
        } catch (IOException e) {
            // Fallback en caso de error
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html; charset=UTF-8");
            out.println("");
            out.println("<html><body>");
            out.println("<h2>Formulario enviado exitosamente</h2>");
            out.println("<p>Sus datos han sido procesados correctamente.</p>");
            out.println("<a href='/'>Volver al inicio</a>");
            out.println("</body></html>");
        }

        ThreadLogger.log("Respuesta de exito enviada.");
    }

    private static void sendResourceSentResponse(PrintWriter out, String resourceName) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html; charset=UTF-8");
        out.println("");
        out.println("<html><body>");
        out.println("<h2>Recurso enviado</h2>");
        out.println("<p>El archivo " + resourceName + " ha sido enviado exitosamente.</p>");
        out.println("<a href='/'>Volver al inicio</a>");
        out.println("</body></html>");
    }
}
