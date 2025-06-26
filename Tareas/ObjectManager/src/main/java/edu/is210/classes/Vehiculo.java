package edu.is210.classes;

import java.io.Serializable;

public class Vehiculo implements Serializable {
    private static final String NOMBRE_ARCHIVO = "Vehiculos";
    private String marca;
    private String modelo;
    private int anio;

    public Vehiculo() {

    }

    public Vehiculo(String marca, String modelo, int anio) {
        this.marca = marca.strip();
        this.modelo = modelo.strip();
        this.anio = anio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca.strip();
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo.strip();
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public static String getNombreArchivo() {
        return NOMBRE_ARCHIVO;
    }

    @Override
    public String toString() {
        return "Vehiculo [marca=" + marca + ", modelo=" + modelo + ", anio=" + anio + "]";
    }

}
