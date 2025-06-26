/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serealizar;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class Lectura {

    public List<PuntoGeografico> leerArchivo() {
        List<PuntoGeografico> puntosGeograficos = null;
        var archivo = new File("PuntoGeografico.ser");

        if (!archivo.exists()) {
            System.out.println("Archivo no encontrado");
            return puntosGeograficos;
        }

        try {
            var file = new FileInputStream(archivo);
            var entrada = new ObjectInputStream(file);

            while (true) {
                try {
                    puntosGeograficos = (ArrayList<PuntoGeografico>) entrada.readObject();
                } catch (EOFException e) {
                    break;
                }
            }

            entrada.close();
            file.close();
        } catch (IOException e) {
            System.out.println("Error al");
        } catch (ClassNotFoundException e) {
            System.out.println("Erro: La clase no fue encontrada.");
        }

        return puntosGeograficos;
    }
}
