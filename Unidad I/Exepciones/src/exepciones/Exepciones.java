/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package exepciones;

public class Exepciones {

    static int num1 = 123;
    static int num2 = 0;
    static long resultado = 0;

    public static void main(String[] args) {

        try {
            resultado = num1 / num2;
        } catch (ArithmeticException e) {
            System.out.println("Error: Division entre 0: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error: " + e.getMessage());
        } finally {
            System.out.println("Este codigo siempre se ejecutara...");
        }

        System.out.println("Resutltado de la Division: " + resultado);
    }

}
