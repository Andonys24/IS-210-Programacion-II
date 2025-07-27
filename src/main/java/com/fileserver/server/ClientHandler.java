package com.fileserver.server;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import com.fileserver.utils.FileManager;
import com.fileserver.utils.Logger;
import com.fileserver.utils.RequestManager;

public class ClientHandler implements Runnable {

    private final Socket client;
    private final Logger logger;

    public ClientHandler(Socket socket, Logger logger) {
        this.client = socket;
        this.logger = logger;
    }

    @Override
    public void run() {
        boolean keepRunning = true;
        String[] options = { "Listar Archivos", "Solicitar archivo", "Subir Archivo", "Salir" };
        try {
            var rm = new RequestManager(client.getInputStream(), client.getOutputStream());

            while (keepRunning) {
                boolean validate = false;
                String[] filesString = null;
                // Enviar menu principal
                rm.sendMenu("Menu Principal", options);
                rm.sendOption("Ingrese una opcion");
                // Esperar opcion del cliente
                switch (rm.readClientOption()) {
                    case 1:
                        // Listar los archivos disponibles
                        filesString = FileManager.getFilesString("Files");

                        if (filesString.length == 0 || filesString == null) {
                            rm.sendTitle("No hay Archivos Disponibles");
                        } else {
                            rm.sendMenu("Archivos Disponibles", filesString);
                        }
                        break;
                    case 2:
                        // Listar los archivos disponibles
                        File[] files = FileManager.getFiles("Files");
                        String[] originalFiles = FileManager.getFilesString("Files");

                        // Crear arreglo con un elemento adicional
                        filesString = Arrays.copyOf(originalFiles, originalFiles.length + 1);
                        filesString[originalFiles.length] = "Regresar"; // Agregar en la última posición

                        if (filesString.length == 0 || filesString == null) {
                            rm.sendTitle("No hay Archivos Disponibles");
                        } else {
                            int choice = 0;
                            while (!validate) {
                                rm.sendMenu("Archivos Disponibles para Descargar", filesString);
                                rm.sendOption("Elija un Archivo");
                                choice = rm.readClientOption() - 1;

                                if (choice < 0 || choice > filesString.length) {
                                    rm.sendMessage("Opcion NO valida");
                                    continue;
                                }

                                validate = true;
                            }

                            if (choice == originalFiles.length) {
                                break;
                            }
                            // Cuando sea valida la opcion enviar archivo
                            rm.sendFile(files[choice].getName());
                            rm.sendMessage("Archivo Descargado Correctamente");
                        }
                        break;
                    case 3:
                        // El cliente subira un archivo
                        rm.sendMessage("Subir archivo al Servidor");
                        break;
                    case 4:
                        // El cliente se saldra
                        rm.sendExit();
                        keepRunning = false;

                        break;

                    default:
                        rm.sendMessage("La opcion ingresada no es valida.");
                        break;
                }

                if (keepRunning)
                    rm.sendPause("continuar");

            }
            // Liberar memoria y cerrar conexion
            rm.closeRequestManager();
            cleanUp();
        } catch (IOException e) {
            cleanUp();
            System.out.println("Error en el hilo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error el tipo de dato de la opcion no es valida: " + e.getMessage());
        }
    }

    private void cleanUp() {
        try {
            client.close();
        } catch (IOException e) {
            System.out.println("Error al cerrar la conexion al cliente:" + e.getMessage());
            logger.error("CLIENT", "DISCONNECT_ERROR", "Error al cerrar conexión: " + e.getMessage(),
                    client.getInetAddress().toString());
        }
    }
}
