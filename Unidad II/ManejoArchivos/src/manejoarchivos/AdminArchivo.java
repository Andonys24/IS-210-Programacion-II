/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manejoarchivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author Andonys24
 */
public class AdminArchivo {
    File archivo;

    public AdminArchivo() {
        this.archivo = new File("mi_archivo.txt");
    }

    public AdminArchivo(String nombreArchivo) {
        this.archivo = new File(nombreArchivo);
    }

    public void escribirArchivo(String contenido) {

        var archivoDisponible = archivo.exists();

        try {
            var archivoEscritor = new FileWriter(archivo, archivoDisponible);
            var escritorBuffer = new BufferedWriter(archivoEscritor);

            System.out.println(((archivoDisponible) ? "Agregando" : "Creando") + " Contenido...");
            escritorBuffer.write(contenido + "\n");

            escritorBuffer.close();
            archivoEscritor.close();
        } catch (IOException ex) {
            System.out.println("Error al guardar el archivo." + archivo.getName() + ": " + ex.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e);
        }
    }

    public String leerArchivo() {

        StringBuilder contenidoArchivo = new StringBuilder();

        try {
            var archivoLector = new FileReader(archivo);
            var archivoBuffer = new BufferedReader(archivoLector);

            while (archivoBuffer.ready()) {
                contenidoArchivo.append(archivoBuffer.readLine()).append("\n");
            }

            archivoBuffer.close();

        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error Inesperado: " + e.getMessage());
        }

        return contenidoArchivo.toString();
    }

    public String getNombre() {
        return archivo.getName();
    }
}
