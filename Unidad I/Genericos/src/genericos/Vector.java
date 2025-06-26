package genericos;

public class Vector<T> {
    private T[] vector;
    private int catElementos;
    private static final int MAX_CAPACIDAD = 25;

    // Constructores
    @SuppressWarnings("unchecked")
    public Vector() {
        vector = (T[]) new Object[MAX_CAPACIDAD];
        catElementos = 0;
    }

    // Metodos Getters y Setters

    // Agregar Elemento al final
    protected void pushBack(T elemento) {
        if (catElementos >= MAX_CAPACIDAD) {
            System.out.println("Vector completamente lleno.");
            return;
        }

        vector[catElementos++] = elemento;
    }

    // Eliminar el ultimo elemento
    protected void popBack() {
        if (catElementos <= 0) {
            System.out.println("No hay elementos para eliminar.");
            return;
        }

        erase(--catElementos);
    }

    // Obtener tamano del vector
    protected int size() {
        return catElementos;
    }

    // Obtener capacidad del vector
    protected int capacity() {
        return MAX_CAPACIDAD;
    }

    // Acceder a un elemento
    protected T at(int indice) {
        if (!validarIndice(indice)) {
            return null;
        }

        return vector[indice];
    }

    // Devuelve el primer elemento
    protected T front() {
        return vector[0];
    }

    // Devuelve el ultimo elemento
    protected T back() {
        return vector[catElementos - 1];
    }

    // Insertar un elemento en una posicion
    protected void insert(int posicion, T valor) {
        if (!validarIndice(posicion))
            return;

        vector[posicion] = valor;

        if (catElementos < posicion)
            catElementos = posicion + 1;
    }

    // Eliminar el elemento de una posicion
    protected void erase(int posicion) {

        if (!validarIndice(posicion))
            return;

        vector[posicion] = null;
    }

    // Limpiar todos los elementos
    protected void clear() {
        for (int i = 0; i < MAX_CAPACIDAD; i++) {
            erase(i);
        }
        catElementos = 0;
    }

    // Reducir la capacidad del tamano actual
    protected void shinkToFit() {
        int posicion = 0;

        for (int i = 0; i < MAX_CAPACIDAD; i++) {
            if (vector[i] != null) {
                vector[posicion++] = vector[i];
            }
        }

        catElementos = posicion;

        while (posicion < MAX_CAPACIDAD) {
            erase(posicion++);
        }

    }

    protected void imprimir() {
        for (int i = 0; i < catElementos; i++) {
            System.out.println("[" + i + "]: " + at(i));
        }
        System.out.println();
    }

    protected boolean validarIndice(int indice) {

        if (indice < 0 || indice >= MAX_CAPACIDAD) {
            System.out.println("Indice Fuera de rango.");
            return false;
        }

        return true;
    }

}
