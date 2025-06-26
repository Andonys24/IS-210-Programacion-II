package genericos;

/**
 *
 * @author Lab Sistemas
 */
public class Caja<T> {

    private T contenido;

    // Contructres
    public Caja() {
    }

    public Caja(T contenido) {
        this.contenido = contenido;
    }

    // Getters y Setters
    public T getContenido() {
        return contenido;
    }

    public void setContenido(T contenido) {
        this.contenido = contenido;
    }

}
