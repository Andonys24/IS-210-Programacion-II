
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        final int PORT = 8080;
        final int MAX_CLIENTS = 33;

        try (var server = new ServerSocket(PORT);
             var executor = Executors.newFixedThreadPool(MAX_CLIENTS)) {

            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("Servidor Corriendo en http://" + inetAddress.getHostAddress() + ":" + PORT);


            while (true) {
                var client = server.accept();

                executor.submit(() -> {
                    try (var inputStreamReader = new InputStreamReader(client.getInputStream());
                         var in = new BufferedReader(inputStreamReader);
                         var out = new PrintWriter(client.getOutputStream(), true)) {
                        System.out.println("Ejecutando Hilo: " + Thread.currentThread().getName());
                        System.out.println("Cliente conectado desde " + client.getInetAddress());

                        // Leer solicitud HTTP del cliente
                        var line = in.readLine();

                        if (line.contains("GET / ")) {
                            // Verificar si es una solicitud para el "index.html"
                            RequestHandler.sendHTMLResponse(client, "index.html");
                        } else if (line.contains("GET /css/normalize.css")) {
                            // Verificar solicitud de archivo css
                            RequestHandler.sendCSSResponse(client, "css/normalize.css");
                        } else if (line.contains("GET /css/style.css ")) {
                            // Verificar solicitud de archivo css
                            RequestHandler.sendCSSResponse(client, "css/style.css");
                        } else if (line.contains("GET /img/hero.jpg ")) {
                            // Verificar solicitud de imagen
                            RequestHandler.sendImageResponse(client, "img/hero.jpg");
                        } else {
                            RequestHandler.sendError404(out);
                            out.close();
                        }
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
