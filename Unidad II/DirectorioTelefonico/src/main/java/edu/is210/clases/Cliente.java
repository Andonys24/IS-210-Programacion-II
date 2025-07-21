package edu.is210.clases;

import edu.is210.includes.Utilidades;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cliente implements Serializable {
    private final int identificador;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String direccion;

    public Cliente() {
        identificador = generarIdentificador();
    }

    public Cliente(String nombres, String apellidos, String telefono, String direccion) {
        this();
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    private int generarIdentificador() {
        String fecha = new SimpleDateFormat("yyMMdd").format(new Date());
        String identificadorStr = String.format("%s%03d", fecha, Utilidades.generarNumRandom(0, 999));

        return Integer.parseInt(identificadorStr);
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "ID: " + identificador + "\n" +
                "Nombres: " + nombres + "\n" +
                "Apellidos: " + apellidos + "\n" +
                "Telefono: +504 " + telefono + "\n" +
                "Direccion: " + direccion;
    }

}
