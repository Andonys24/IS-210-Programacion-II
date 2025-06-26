package edu.is210.classes;

import java.io.Serializable;

public class PuntoGeografico implements Serializable {

    private static final String NOMBRE_ARCHIVO = "PuntosGeograficos";
    private double latitud;
    private double longitud;
    private String descripcion;

    public PuntoGeografico() {
    }

    public PuntoGeografico(double latitud, double longitud, String descripcion) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion.strip();
    }

    public static String getNombreArchivo() {
        return NOMBRE_ARCHIVO;
    }

    @Override
    public String toString() {
        return "PuntoGeografico [latitud=" + latitud + ", longitud=" + longitud + ", descripcion=" + descripcion + "]";
    }

}
