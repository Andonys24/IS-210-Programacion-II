/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parametrosvariados;

/**
 *
 * @author Lab Sistemas
 */
public class ParametrosVariados {

    public static void main(String[] args) {
        var operaciones = new Operaciones();
        int resultado;
        String nombres;
        resultado = operaciones.sumatoriaEnteros(10, 20, 30, 40, 50);
        System.out.println("El resultado de la Sumatoria de Enteros es: " + resultado);
        resultado = operaciones.sumatoriaEnteros(10, 20);
        System.out.println("El resultado de la Sumatoria de Enteros es: " + resultado);
        
        nombres = operaciones.getNombres("Andonys", "Maria", "Jose", "Pablo");
        System.out.println("Los nombre registrados son: " + nombres);
    }

}
