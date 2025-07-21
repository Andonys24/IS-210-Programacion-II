package sockettest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketTest {

    public static void main(String[] args) {
        // final String host = "time-a.nist.gov";
        // final int port = 13;
        final String host = "horstmann.com";
        final int port = 80;

        try {
            var socket = new Socket(host, port);
            // Canal para recibir datos del servidor
            var in = new Scanner(socket.getInputStream());
            // Canal para enviar datos al servidor
            var out = new PrintWriter(socket.getOutputStream());
            
            out.println("GET/ HTTP/1.1");
            out.flush();

            while (in.hasNextLine()) {
                System.out.println("Respuesta: " + in.nextLine());
            }

            socket.close();

        } catch (IOException e) {
            System.err.println("Error de IO: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error ineperado: " + e.getMessage());
        }
    }

}
