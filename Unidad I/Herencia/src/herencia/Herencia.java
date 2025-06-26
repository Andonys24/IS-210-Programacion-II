package herencia;

/**
 * @author Andonys24
 */
public class Herencia {

    public static final double PI = 3.141516;

    public static void main(String[] args) {
        Perro perro = new Perro("Rex", "Marron", 5, 20.5, "Labrador", 4);
        Gato gato = new Gato("Miau", "Blanco", 3, 4.2, "activo", "Siasmes");

        System.out.println("\n***Objeto de tipo Perro ***\n");
        perro.hablar();
        perro.jugar();
        perro.dormir();
        System.out.println("\n***Objeto de tipo Gato ***\n");
        gato.hablar();
        gato.cazar();
        gato.acariciar();
        gato.dormir();
    }

    private static void variablesStaticas() {
        // Ternarios y variables constantes
        char dependiendo = 'Z';
        double variable = (dependiendo == 'Z') ? 10.56789 : 20.443242;
        final double CONSTANTE = (dependiendo == 'Z') ? 10.56789 : 20.443242;
        double numRandom = Math.random();

        System.out.println("Valor de PI estatic: " + PI);
        System.out.println("El valor de la variable es: " + variable);
        System.out.println("El valor de CONSTANTE: " + CONSTANTE);
        System.out.println("Valor de Numero Aleatorio: " + numRandom);
    }

}
