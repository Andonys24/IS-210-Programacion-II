package herencia;

/**
 * @author Andonys24
 */
// Clase Padre
public class Animal {

    protected String nombre;
    protected String color;
    protected String especie;
    protected int edad;
    protected double peso;

    public Animal() {
    }

    public Animal(String nombre, String color, String especie, int edad, double peso) {
        this.nombre = nombre;
        this.color = color;
        this.especie = especie;
        this.edad = edad;
        this.peso = peso;
    }

    // Metodos Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    //    Metodos de comportamiento
    public void hablar() {
        System.out.println(nombre + " esta hablando...");
    }

    public void dormir() {
        System.out.println(nombre + " esta durmiendo....");
    }

    public void comer() {
        System.out.println(nombre + " esta comiendo...");
    }

}
