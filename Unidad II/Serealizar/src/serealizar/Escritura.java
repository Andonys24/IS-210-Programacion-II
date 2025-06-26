/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serealizar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 *
 * @author UNAH-IS
 */
public class Escritura {

    public void escribirEnArchivo(List<PuntoGeografico> puntosGeograficos) {
        var archivo = new File("PuntoGeografico.ser");

        try {
            var file = new FileOutputStream(archivo);
            var salida = new ObjectOutputStream(file);

            salida.writeObject(puntosGeograficos);

            salida.close();
            file.close();
        } catch (IOException e) {
            System.out.println("Error al Serializar el objeto:" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }
}
