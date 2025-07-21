package edu.is210.includes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.is210.clases.Cliente;

public class Serializer {
    private static final String RUTA_DIR = "src/main/resources/DIRECTORIO.DIR";

    public static void guardarObjetos(Cliente[] clientes) {
        File archivo = new File(RUTA_DIR);

        try {

            if (Utilidades.arrayVacio(clientes)) {
                archivo.delete();
                return;
            }

            FileOutputStream fileOutput = new FileOutputStream(archivo);
            ObjectOutputStream salida = new ObjectOutputStream(fileOutput);

            salida.writeObject(clientes);

            salida.close();
            fileOutput.close();
        } catch (IOException ex) {
            System.out.println("Error al serializar los clientes: " + ex.getMessage());
        }
    }

    public static Cliente[] cargarObjetos() {
        Cliente[] clientes = null;
        File archivo = new File(RUTA_DIR);

        if (!archivo.exists()) {
            System.out.println("El archivo '" + RUTA_DIR + "' No se ha encontrado...");
            return null;
        }

        try {
            FileInputStream fileInput = new FileInputStream(archivo);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);

            clientes = (Cliente[]) objectInput.readObject();

            objectInput.close();
            fileInput.close();
        } catch (IOException ex) {
            System.out.println("Error al leer el archivo: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Error, la clase no fue encontrada: " + ex.getMessage());
        }

        if (Utilidades.arrayVacio(clientes))
            return null;

        return clientes;
    }
}
