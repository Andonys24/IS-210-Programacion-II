/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parametrosvariados;

/**
 *
 * @author Lab Sistemas
 */
public class Operaciones {

    public Operaciones() {
    }

    public int sumatoriaEnteros(int... enteros) {
        int suma = 0;

        for (int entero : enteros) {
            suma += entero;
        }

        return suma;
    }

    public String getNombres(String... nombres) {
        String texto = "";

        for (final String nombre : nombres) {
            texto += nombre + ", ";
        }

        return texto;
    }
}
