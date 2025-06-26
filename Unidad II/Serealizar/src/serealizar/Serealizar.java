/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package serealizar;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author UNAH-IS
 */
public class Serealizar {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        var escribir = new Escritura();
        var lectura = new Lectura();
        var puntosGeograficos = new ArrayList<PuntoGeografico>();
        List pgDes = null;

        puntosGeograficos.add(new PuntoGeografico(56.7890F, 76.6765F, "Puente"));
        puntosGeograficos.add(new PuntoGeografico(78.6035F, 87.5785F, "Montana"));
        puntosGeograficos.add(new PuntoGeografico(67.7893F, 76.6855F, "Carretera"));

        escribir.escribirEnArchivo(puntosGeograficos);
        pgDes = lectura.leerArchivo();

        if (pgDes != null) {
            for (final var pg : pgDes) {
                System.out.println(pg);
            }
        } else {
            System.out.println("NO hay Elementos en el arreglo");
        }
    }

}
