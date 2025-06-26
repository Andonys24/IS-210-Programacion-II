package genericos;

/**
 *
 * @author Lab Sistemas
 */
public class Genericos {
    public static void main(String[] args) {
        var cajaInt = new Caja<Integer>(45);
        var cajaFloat = new Caja<Float>(45.65F);
        var cajaString = new Caja<String>("Maria Castillo");
        var libro = new Libro("Programacion en Java", "Deitel y Deitel", 456);
        var cajaLibro = new Caja<Libro>(libro);
        var vec = new Vector<Integer>();

        System.out.println("\nObjeto caja de tipo Generico\n");

        System.out.println("Obtenido de la cajaInt ----> " + cajaInt.getContenido());
        System.out.println("Obtenido de la cajaFloat ----> " + cajaFloat.getContenido());
        System.out.println("Obtenido de la String ----> " + cajaString.getContenido());
        System.out.println("Obtenido de la Libro ----> " + cajaLibro.getContenido());

        System.out.println("\n\n*** Objeto Vector de tipo Generico (Integert) ***\n");

        System.out.println("\nVector despues de Agregar elementos con pushBack");
        for (int i = 0; i < 17; i++) {
            vec.pushBack(i * i);
        }
        vec.imprimir();

        System.out.println("\nVector despues de usar insert");
        vec.insert(0, 1000);
        vec.insert(12, 1200);
        vec.insert(20, 2000);
        vec.insert(28, 2800); // Fuera de Rango
        vec.imprimir();

        System.out.println("\nVector despues de Eliminar elementos con popBack");
        System.out.println("Nueva tamano: " + vec.size() + "\n");
        vec.popBack();
        vec.popBack();
        vec.imprimir();

        System.out.println("\nVector despues de Eliminar elementos con erase");
        vec.erase(8);
        vec.erase(3);
        vec.erase(13);
        vec.erase(30); // Fuera de Rango
        vec.imprimir();

        System.out.println("\nVector despues de usar shinkToFit");
        vec.shinkToFit();
        vec.imprimir();

        System.out.println("\nVector despues de usar clear y pushBack");
        vec.clear();
        vec.pushBack(314);
        vec.pushBack(200);
        vec.imprimir();
    }

}
