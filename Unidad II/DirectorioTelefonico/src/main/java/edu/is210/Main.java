package edu.is210;

import java.util.Scanner;
import edu.is210.clases.Logger;
import edu.is210.clases.AdminClientes;
import edu.is210.clases.Cliente;
import edu.is210.includes.Utilidades;

public class Main {
    public static void main(String[] args) {
        Logger logger = new Logger();
        Scanner input = new Scanner(System.in);
        AdminClientes adminClientes = new AdminClientes();
        boolean salir = false;
        String[] opciones = { "Agregar", "Listar", "Buscar", "Modificar", "Eliminar", "Salir" };

        while (!salir) {
            int opcion = Utilidades.generarMenu(input, "Directorio Telefonico", opciones);

            switch (opcion) {
                case 1 -> {
                    Utilidades.generarTitulo("Agregando cliente", true);
                    Cliente cliente = new Cliente();
                    cliente.setNombres(adminClientes.validarNombreCliente(input));
                    cliente.setApellidos(adminClientes.validarApellidosCliente(input));
                    cliente.setTelefono(adminClientes.validarTelefonoCliente(input));
                    cliente.setDireccion(adminClientes.validarDireccionCliente(input));
                    adminClientes.insertarCliente(cliente, logger);
                }
                case 2 -> {
                    Utilidades.generarTitulo("Listado de clientes", true);
                    adminClientes.imprimirClientes();
                    logger.registrarLog("Listar");
                }
                case 3 -> {
                    int tipoBusqueda = adminClientes.generarMenuCliente(input);

                    Utilidades.generarTitulo("Clientes encontrados", true);
                    Integer[] coindicendias;
                    if (tipoBusqueda == 1) {
                        String nombre = Utilidades.validarString(input, "Ingrese el nombre a buscar");
                        coindicendias = adminClientes.buscarPorNombre(nombre);
                    } else if (tipoBusqueda == 2) {
                        String apellido = Utilidades.validarString(input, "Ingrese el apellido a buscar");
                        coindicendias = adminClientes.buscarPorApellido(apellido);
                    } else {
                        break;
                    }

                    adminClientes.imprimirClientes(coindicendias);
                    logger.registrarLog("Busqueda");
                }
                case 4 -> {
                    Utilidades.generarTitulo("Modificar Cliente", true);
                    int tipoBusqueda = adminClientes.generarMenuCliente(input);
                    Integer[] coindicendias;

                    if (tipoBusqueda == 1) {
                        String nombre = Utilidades.validarString(input, "Ingrese el nombre a buscar");
                        coindicendias = adminClientes.buscarPorNombre(nombre);
                    } else if (tipoBusqueda == 2) {
                        String apellido = Utilidades.validarString(input, "Ingrese el apellido a buscar");
                        coindicendias = adminClientes.buscarPorApellido(apellido);
                    } else {
                        break;
                    }
                    adminClientes.realizarAccion(input, coindicendias, 0, logger);

                }
                case 5 -> {
                    Utilidades.generarTitulo("Eliminar Cliente", true);
                    int tipoBusqueda = adminClientes.generarMenuCliente(input);
                    Integer[] coincidencias;
                    if (tipoBusqueda == 1) {
                        String nombre = Utilidades.validarString(input, "Ingrese el nombre a buscar");
                        coincidencias = adminClientes.buscarPorNombre(nombre);
                    } else if (tipoBusqueda == 2) { // Apellidos
                        String apellido = Utilidades.validarString(input, "Ingrese el apellido a buscar");
                        coincidencias = adminClientes.buscarPorApellido(apellido);
                    } else {
                        break;
                    }
                    adminClientes.realizarAccion(input, coincidencias, 1, logger);
                }
                case 6 -> {
                    salir = true;
                    adminClientes.almacenarClientes();
                }
                default -> System.out.println("Opcion invalida, intente denuevo.");
            }
            Utilidades.pausarPrograma(input, (salir ? "salir" : "continuar"));
        }
        logger.cerrar();
    }
}
