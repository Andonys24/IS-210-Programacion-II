/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package poo;

/**
 *
 * @author Andoni
 */
public class POO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Estudiante ps1 = new Estudiante(); // Constructor vacio
        Estudiante ps2 = new Estudiante(4321, "Periodismo", "UNAH", 87F); // Constructor especial
        Seccion sec1 = new Seccion();
        Seccion sec2 = new Seccion(1234, 40, 7000, 8000, "Programacion II", "Lab 2", "Lunes,Martes,Miercoles,Jueves");
        ps1.imprimirAtributos();

        // Modificar datos ps1
        ps1.setNoCuenta(1234);
        ps1.setCarrera("Ing. Sistemas");
        ps1.setCentroEstudio("UNAH");
        ps1.setPromedioActual(81.6777777777777777F);

        // Modificar datos ps2
        ps2.setNoCuenta(8729);
        ps2.setCarrera("Informatica Administrativa");
        ps2.setCentroEstudio("SEUTEC");
        ps2.setPromedioActual(80.18F);
        
        ps1.imprimirAtributos();
        ps2.imprimirAtributos();

        // Compara objetos(boolean)
        System.out.println("Objeto1 == Objeto2 ?? " + ps1.equals(ps2));
        
        System.out.println(sec2);
    }
    
}
