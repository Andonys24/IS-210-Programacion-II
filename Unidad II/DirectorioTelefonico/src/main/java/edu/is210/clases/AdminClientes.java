package edu.is210.clases;

import java.util.Scanner;

import edu.is210.includes.Serializer;
import edu.is210.includes.Utilidades;

public class AdminClientes {
    private int catClientes;
    private Cliente[] clientes;

    public AdminClientes() {
        cargarClientes();
    }

    private void cargarClientes() {
        clientes = Serializer.cargarObjetos();

        if (Utilidades.arrayVacio(clientes)) {
            clientes = new Cliente[4];
            catClientes = 0;
            return;
        }

        catClientes = clientes.length;
    }

    public void almacenarClientes() {
        reducirArreglo();
        Serializer.guardarObjetos(clientes);
    }

    private boolean validarIndice(final int indice) {
        return indice >= 0 && indice < catClientes;
    }

    private void redimencionarArreglo() {
        if (catClientes < clientes.length) {
            return;
        }

        // Inicializar nuevo arreglo
        Cliente[] arregloTemp = new Cliente[clientes.length * 2];

        // Copiar elementos del arreglo antiguo al nuevo
        System.arraycopy(clientes, 0, arregloTemp, 0, clientes.length);

        // Pasar propiedad del viejo arreglo al nuevo
        clientes = arregloTemp;
    }

    private void moverElementosIzq(final int indice) {
        for (int i = indice; i < catClientes - 1; i++) {
            clientes[i] = clientes[i + 1];
        }
        clientes[catClientes - 1] = null;
    }

    // Reducir capacidad del arreglo
    private void reducirArreglo() {
        Cliente[] arregloTemp = new Cliente[catClientes];
        System.arraycopy(clientes, 0, arregloTemp, 0, catClientes);
        clientes = arregloTemp;

    }

    public void imprimirClientes() {
        if (Utilidades.arrayVacio(clientes)) {
            System.out.println("No hay Clientes Disponibles");
            return;
        }

        for (int i = 0; i < clientes.length; i++) {
            if (clientes[i] == null)
                return;
            String titulo = "Cliente N." + (i + 1);
            String asteriscos = "*".repeat(titulo.length() * 3);
            Utilidades.generarTitulo(titulo, false);
            System.out.println(clientes[i]);
            System.out.println(asteriscos + "\n");
        }
    }

    public void imprimirClientes(Integer[] indices) {

        if (Utilidades.arrayVacio(indices)) {
            System.out.println("No hay Clientes Disponibles");
            return;
        }

        for (Integer i : indices) {
            if (i == null)
                continue;

            String titulo = "Cliente N." + (i + 1);
            String asteriscos = "*".repeat(titulo.length() * 3);
            Utilidades.generarTitulo(titulo, false);
            System.out.println(clientes[i]);
            System.out.println(asteriscos + "\n");
        }
    }

    public int generarMenuCliente(final Scanner input) {
        String[] opciones = {"Nombres", "Apellidos", "Regresar"};
        return Utilidades.generarMenu(input, "Busqueda de cliente por:", opciones);
    }

    // Inserta Cliente
    public void insertarCliente(Cliente cliente, final Logger logger) {
        if (catClientes >= clientes.length) {
            redimencionarArreglo();
        }

        clientes[catClientes++] = cliente;
        logger.registrarLog("Creacion");
        System.out.println("Cliente Agregado Exitosamente");
    }

    // Eliminar cliente
    public void eliminarCliente(final int indice, final Logger logger) {

        if (!validarIndice(indice)) {
            return;
        }

        if (catClientes <= 0) {
            System.out.println("No hay mas clientes para eliminar.");
            return;
        }

        logger.registrarLog("Eliminacion");
        moverElementosIzq(indice);
        System.out.println("Cliente eliminado.");
        catClientes--;

    }

    public String validarStringCliente(Scanner input, int min, int max, String tipo) {
        String texto;
        while (true) {
            texto = Utilidades.validarString(input, "Ingrese el " + tipo + " del cliente");

            if (texto.length() < min) {
                System.out.println("El texto no puede tener menos de " + min + " caracteres.");
                continue;
            }

            if (texto.length() > max) {
                System.out.println("El texto no puede tener mas de " + max + " caracteres.");
                continue;
            }
            break;
        }
        return texto;
    }

    public String validarNombreCliente(Scanner input) {
        return validarStringCliente(input, 2, 30, "nombres");
    }

    public String validarApellidosCliente(Scanner input) {
        return validarStringCliente(input, 2, 30, "apellidos");
    }

    public String validarTelefonoCliente(Scanner input) {
        String telefono;

        while (true) {
            System.out.print("Ingrese el telefono del cliente: ");
            telefono = input.nextLine().trim();

            // Solo acepta exactamente 8 dígitos
            if (!telefono.matches("\\d{8}")) {
                System.out.println("El teléfono debe tener exactamente 8 dígitos numéricos.");
                continue;
            }
            break;
        }
        return telefono;
    }

    public String validarDireccionCliente(Scanner input) {
        return validarStringCliente(input, 6, 80, "direccion");
    }

    public Integer[] buscarPorNombre(String nombre) {
        nombre = nombre.toLowerCase();
        int[] indicesTemp = new int[catClientes];
        int contador = 0;

        for (int i = 0; i < catClientes; i++) {
            if (clientes[i] == null)
                continue;
            String nombreCliente = clientes[i].getNombres().toLowerCase();
            if (nombreCliente.equals(nombre)) {
                indicesTemp[contador++] = i;
            }
        }
        // Redimensionar el arreglo al tamaño real de coincidencias
        Integer[] indices = new Integer[contador + 1];
        for (int i = 0; i < contador; i++) {
            indices[i] = indicesTemp[i];
        }
        return indices;
    }

    public Integer[] buscarPorApellido(String apellidos) {
        apellidos = apellidos.toLowerCase();
        int[] indicesTemp = new int[catClientes];
        int contador = 0;

        for (int i = 0; i < catClientes; i++) {
            if (clientes[i] == null)
                continue;
            String apellidoCliente = clientes[i].getApellidos().toLowerCase();
            if (apellidoCliente.equals(apellidos)) {
                indicesTemp[contador++] = i;
            }
        }
        Integer[] indices = new Integer[contador + 1];
        for (int i = 0; i < contador; i++) {
            indices[i] = indicesTemp[i];
        }
        return indices;
    }

    public void modificarCliente(Scanner input, int indice, Logger logger) {
        Cliente cliente = clientes[indice];
        Utilidades.generarTitulo("Modificando Cliente", true);
        System.out.println(cliente + "\n");

        // Modificar nombres (no se puede dejar vacío)
        String nuevoNombre = validarNombreCliente(input);
        cliente.setNombres(nuevoNombre);

        // Modificar apellidos
        String nuevoApellido = validarApellidosCliente(input);
        cliente.setApellidos(nuevoApellido);

        // Modificar teléfono
        String nuevoTelefono = validarTelefonoCliente(input);
        cliente.setTelefono(nuevoTelefono);

        // Modificar dirección
        String nuevaDireccion = validarDireccionCliente(input);
        cliente.setDireccion(nuevaDireccion);

        System.out.println("Cliente modificado correctamente.");
        logger.registrarLog("Modificacion");
    }

    public void realizarAccion(Scanner input, Integer[] indices, int tipo, Logger logger) {
        if (indices.length == 0) {
            System.out.println("\nNo se encontraron coincidencias.");
            return;
        }

        // Crear menú de opciones
        String[] opciones = new String[indices.length];
        for (int i = 0; i < indices.length - 1; i++) {
            opciones[i] = clientes[indices[i]].toString();
        }
        opciones[indices.length - 1] = "Retroceder";

        int opcion;
        while (true) {
            opcion = Utilidades.generarMenu(input, "Clientes encontrados", opciones);
            if (opcion == indices.length) {
                break;
            }

            if (opcion < 1 || opcion > indices.length) {
                System.out.println("Opción inválida. Intente de nuevo.");
                continue;
            }

            int indiceReal = indices[opcion - 1];

            if (tipo == 1) {
                eliminarCliente(indiceReal, logger);

            } else {
                modificarCliente(input, indiceReal, logger);
            }
            break;
        }
    }

}
