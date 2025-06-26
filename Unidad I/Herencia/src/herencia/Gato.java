package herencia;

public class Gato extends Animal {
    private String raza;
    private String temperamento;

    public Gato() {
    }

    public Gato(String temperamento, String raza) {
        this.temperamento = temperamento;
        this.raza = raza;
    }

    public Gato(String nombre, String color, int edad, double peso, String temperamento, String raza) {
        super(nombre, color, "Gato", edad, peso);
        this.temperamento = temperamento;
        this.raza = raza;
    }

    @Override
    public void hablar() {
        System.out.println(nombre + " dice: Miau!");
    }

    public void acariciar() {
        System.out.println(nombre + " disfruta ser acariciado.");
    }

    public void cazar() {
        System.out.println(nombre + " esta cazando un raton.");
    }

}
