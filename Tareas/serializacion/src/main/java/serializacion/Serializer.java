package serializacion;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Serializer {
    private static final String RUTA_BASE = "serializados/";

    public static void serializar(Object obj, String nombreArchivo) {
        crearDirectorio();

        try {
            var archivo = new FileOutputStream(RUTA_BASE + nombreArchivo + ".ser");
            var salida = new ObjectOutputStream(archivo);
            salida.writeObject(obj);
            System.out.println("Objeto Serializado exitosamente: " + archivo);
            salida.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error al serializar el objeto: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al serializar el objeto: " + e.getMessage());
        }

    }

    public static Object deserializar(String nombreArchivo) {

        try {
            var archivo = new FileInputStream(RUTA_BASE + nombreArchivo + ".ser");
            var entrada = new ObjectInputStream(archivo);
            Object obj = entrada.readObject();
            System.out.println("Objeto Deserealizado con exito: " + archivo);

            entrada.close();
            archivo.close();
            return obj;
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Clase No encontrada.");
        } catch (Exception e) {
            System.out.println("Error No esperado: " + e.getMessage());
        }

        return null;

    }

    public static void serializacionMultiple(List<Object> objetos, String nombreArchivo) {
        crearDirectorio();

        try {
            var archivo = new FileOutputStream(RUTA_BASE + nombreArchivo + ".ser");
            var salida = new ObjectOutputStream(archivo);

            for (Object obj : objetos) {
                salida.writeObject(obj);
                System.out.println("Objeto serializado: " + obj);
            }

            System.out.println("Todos los objetos han sido serealizados correctamente.");
            salida.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error al serializar los objetos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al serializar los objetos: " + e.getMessage());
        }
    }

    public static List<Object> deserializacionMultiple(String nombreArchivo) {
        List<Object> objetos = new ArrayList<>();

        try {
            var archivo = new FileInputStream(RUTA_BASE + nombreArchivo + ".ser");
            var entrada = new ObjectInputStream(archivo);

            while (true) {
                try {
                    objetos.add(entrada.readObject());

                } catch (EOFException e) {
                    break; // Fin del archivo
                }
            }

            System.out.println("Todos los objetos han sido deserealizados con exito.");

            entrada.close();
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Clase no encontrada.");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }

        return objetos;
    }

    private static void crearDirectorio() {
        Path directorio = Paths.get(RUTA_BASE);
        if (!Files.exists(directorio)) {
            try {
                Files.createDirectories(directorio);
                System.out.println("Direcotio Creado: " + RUTA_BASE);
            } catch (IOException e) {
                System.out.println("Error al crear el directorio: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
    }
}
