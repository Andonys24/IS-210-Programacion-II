package edu.is210.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.is210.includes.Serializer;
import edu.is210.includes.Utilidades;

public class Objetos {
    // Lista de Listas de Objetos
    private static List<PuntoGeografico> puntosGeograficos;
    private static List<Persona> personas;
    private static List<Vehiculo> vehiculos;

    public Objetos() {
        // Cargar las listas al inicializar el objeto
        cargarObjetos();
    }

    private int seleccionarClase(Scanner input) {
        String[] opciones = { "PuntoGeografico", "Persona", "Vehiculo", "Regresar" };
        return Utilidades.generarMenu(input, "Clases Disponibles", opciones, "Elija la clase del Objeto");
    }

    private <T> void printObjetos(final List<T> objetos) {

        if (objetos == null || objetos.size() < 1) {
            System.out.println("No hay objetos Registrados.");
            return;
        }

        System.out.println("Total de Objetos Encontrados: " + objetos.size() + "\n");

        for (int i = 0; i < objetos.size(); i++)
            System.out.println("[" + (i + 1) + "] - " + objetos.get(i));

        System.out.println();
    }

    public void printPuntosGeograficos() {
        printObjetos(puntosGeograficos);
    }

    public void printPersonas() {
        printObjetos(personas);
    }

    public void printVehiculos() {
        printObjetos(vehiculos);
    }

    public void verObjetos(Scanner input) {
        var titulo = "Objetos Registrados de tipo ";
        while (true) {
            switch (seleccionarClase(input)) {
                case 1 -> {
                    Utilidades.imprimirTitulo(titulo + "PuntoGeografico");
                    printPuntosGeograficos();
                }
                case 2 -> {
                    Utilidades.imprimirTitulo(titulo + "Persona");
                    printPersonas();
                }
                case 3 -> {
                    Utilidades.imprimirTitulo(titulo + "Vehiculo");
                    printVehiculos();
                }
                case 4 -> {
                    return;
                }
                default -> System.out.println("Opcion No valida");
            }

            Utilidades.pausarPrograma(input, "continuar");
        }

    }

    public void agregarObjeto(Scanner input) {
        var titulo = "Agregando Objeto de tipo ";
        while (true) {
            switch (seleccionarClase(input)) {
                case 1 -> {
                    Utilidades.imprimirTitulo(titulo + "PuntoGeografico");
                    var puntoGeografico = new PuntoGeografico();

                    puntoGeografico.setLatitud(Utilidades.validarEntrada(
                            input,
                            "Ingrese la Latitud",
                            Double::parseDouble,
                            "double"));
                    puntoGeografico.setLongitud(Utilidades.validarEntrada(
                            input,
                            "Ingrese La Longitud",
                            Double::parseDouble,
                            "double"));

                    System.out.print("Ingrese una Descripcion: ");
                    puntoGeografico.setDescripcion(input.nextLine());

                    puntosGeograficos.add(puntoGeografico);
                    System.out.println("Punto Geografico Agregado Correctamente");
                }
                case 2 -> {
                    Utilidades.imprimirTitulo(titulo + "Persona");
                    var persona = new Persona();

                    System.out.print("Ingrese el nombre de la Persona: ");
                    persona.setNombre(input.nextLine());
                    persona.setEdad(Utilidades.validarEntrada(
                            input,
                            "Ingrese la edad de la Persona",
                            Integer::parseInt,
                            "entero"));

                    personas.add(persona);
                    System.out.println("\nPersona Agregada correctamente.\n");
                }
                case 3 -> {
                    Utilidades.imprimirTitulo(titulo + "Vehiculo");
                    var vehiculo = new Vehiculo();

                    System.out.print("Ingrese la Marca del vehiculo: ");
                    vehiculo.setMarca(input.nextLine());

                    System.out.print("Ingrese el modelo del vehiculo: ");
                    vehiculo.setModelo(input.nextLine());

                    vehiculo.setAnio(Utilidades.validarEntrada(
                            input,
                            "Ingrese el Año del vehiculo",
                            Integer::parseInt,
                            "entero"));

                    vehiculos.add(vehiculo);
                    System.out.println("\nVehiculo agregado Correctamente\n");
                }
                case 4 -> {
                    return;
                }
                default -> System.out.println("Opcion No valida");
            }

            Utilidades.pausarPrograma(input, "regresar");
        }

    }

    private <T> void eliminarElemento(List<T> objetos, Scanner input) {

        int indice = Utilidades.validarEntrada(input, "Ingrese el indice del objeto", Integer::parseInt, "entero");

        if (!(indice > 0 && indice <= objetos.size())) {
            System.out.println("Indice Fuera de Rango.");
            return;
        }

        indice--;

        if (objetos.get(indice) == null) {
            System.out.println("Objeto No encontrado.");
            return;
        }

        System.out.println("Eliminando el objeto: " + objetos.get(indice));
        objetos.remove(indice);
        System.out.println("\nObjeto Eliminado Correctamente.\n");
    }

    public void eliminarObjeto(Scanner input) {
        var titulo = "Eliminando Objeto de tipo ";
        while (true) {

            switch (seleccionarClase(input)) {
                case 1 -> {
                    Utilidades.imprimirTitulo(titulo + "PuntoGeografico");
                    eliminarElemento(puntosGeograficos, input);
                }
                case 2 -> {
                    Utilidades.imprimirTitulo(titulo + "Persona");
                    eliminarElemento(personas, input);
                }
                case 3 -> {
                    Utilidades.imprimirTitulo(titulo + "Vehiculo");
                    eliminarElemento(vehiculos, input);
                }
                case 4 -> {
                    return;
                }
                default -> System.out.println("Opcion No valida");
            }

            Utilidades.pausarPrograma(input, "regresar");
        }
    }

    public void cargarObjetos() {
        var serializer = new Serializer();

        System.out.println("Cargando Datos...");

        puntosGeograficos = serializer.leerArchivo(PuntoGeografico.getNombreArchivo());
        if (puntosGeograficos == null)
            puntosGeograficos = new ArrayList<>();

        personas = serializer.leerArchivo(Persona.getNombreArchivo());
        if (personas == null)
            personas = new ArrayList<>();

        vehiculos = serializer.leerArchivo(Vehiculo.getNombreArchivo());
        if (vehiculos == null)
            vehiculos = new ArrayList<>();
    }

    public void guardarObjetos() {
        var serializer = new Serializer();

        System.out.println("Guardando los Cambios realizados...");

        serializer.escribirEnArchivo(puntosGeograficos, PuntoGeografico.getNombreArchivo());
        serializer.escribirEnArchivo(personas, Persona.getNombreArchivo());
        serializer.escribirEnArchivo(vehiculos, Vehiculo.getNombreArchivo());
    }

}
