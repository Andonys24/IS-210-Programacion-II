package edu.is210.includes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class Serializer {
    private static final String RUTA_BASE = "src/main/resources/";

    public Serializer() {

    }

    public <T extends Serializable> void escribirEnArchivo(List<T> objetos, final String nombreArchivo) {
        var archivo = new File(RUTA_BASE + nombreArchivo + ".ser");

        try (var file = new FileOutputStream(archivo);
                var salida = new ObjectOutputStream(file)) {

            salida.writeObject(objetos);

        } catch (IOException e) {
            System.out.println("Error al serializar el objeto: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> List<T> leerArchivo(final String nombreArchivo) {
        List<T> objetos = null;
        var archivo = new File(RUTA_BASE + nombreArchivo + ".ser");

        if (!archivo.exists()) {
            System.out.println("Archivo no encontrado");
            return objetos;
        }

        try (var file = new FileInputStream(archivo);
                var entrada = new ObjectInputStream(file)) {

            objetos = (List<T>) entrada.readObject();

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error: La clase no fue encontrada.");
        }

        return objetos;
    }
}
