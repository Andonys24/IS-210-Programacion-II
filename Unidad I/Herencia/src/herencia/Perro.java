/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herencia;

/**
 * @author Lab Sistemas
 */
public class Perro extends Animal {

    private String raza;
    private double tamanio;

    public Perro() {
    }

    public Perro(String raza, double tamanio) {
        this.raza = raza;
        this.tamanio = tamanio;
    }

    public Perro(String nombre, String color, int edad, double peso, String raza, double tamanio) {
        super(nombre, color, "perro", edad, peso);
        this.raza = raza;
        this.tamanio = tamanio;
    }

    //    Getters y Setters
    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public double getTamanio() {
        return tamanio;
    }

    public void setTamanio(double tamanio) {
        this.tamanio = tamanio;
    }

    //    Metodos de Comportamiento

    @Override
    public void hablar() {
        System.out.println(nombre + " dice Guau!");
    }

    public void jugar() {
        System.out.println(nombre + " esta jugando con la pelota.");
    }

    @Override
    public void comer() {
        System.out.println(nombre = " esta comiendo croquetas");
    }
}
