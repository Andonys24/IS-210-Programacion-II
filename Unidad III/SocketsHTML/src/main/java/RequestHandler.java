import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler {
    // Enviar respuesta de error 404
    public static void sendError404(PrintWriter out) {
        out.println("HTTP/1.1 404 Not Found");
        out.println("Content-Type: text/plain; charset=UTF-8");
        out.println("");
        out.println("Error: Recurso no encontrado");
    }

    // Enviar Contenido
    private static void sendResponse(Socket client, String filePath, String contentType) {
        try (var out = new PrintWriter(client.getOutputStream(), true)) {
            if (!FileManager.fileExists(filePath)) {
                sendError404(out);
            } else {
                var content = FileManager.readFile(filePath);
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: " + contentType);
                out.println("");
                out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Enviar Archivo HTML al cliente
    public static void sendHTMLResponse(Socket client, String filePath) {
        var htmlContentType = "text/html; charset=UTF-8";
        sendResponse(client, filePath, htmlContentType);
    }

    // Enviar Archivo CSS al cliente
    public static void sendCSSResponse(Socket client, String filePath) {
        var cssContentType = "text/css; charset=UTF-8";
        sendResponse(client, filePath, cssContentType);
    }

    // Enviar imagen al cliente
    public static void sendImageResponse(Socket client, String filePath) {
        try (var out = client.getOutputStream()) {
            if (!FileManager.fileExists(filePath)) {
                var writer = new PrintWriter(out, true);
                sendError404(writer);
            } else {
                byte[] content = FileManager.readBinaryFile(filePath);
                var writer = new PrintWriter(out, true);

                // Enviar encabezados HTTP
                writer.println("HTTP/1.1 200 OK");
                writer.println("Content-Type: image/jpeg");
                writer.println("");

                // Enviar la imagen
                out.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
