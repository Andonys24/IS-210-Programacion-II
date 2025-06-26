/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serealizar;

import java.io.Serializable;

/**
 *
 * @author UNAH-IS
 */
public class PuntoGeografico implements Serializable{

    private double latitud;
    private double longitud;
    private String descripcion;

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
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "PuntoGeografico{"
                + "latitud=" + latitud
                + ", longitud=" + longitud
                + ", descripcion='" + descripcion + '\''
                + '}';
    }

}
