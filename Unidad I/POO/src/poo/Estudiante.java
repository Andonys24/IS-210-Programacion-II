/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poo;

import java.util.Objects;

/**
 *
 * @author Andoni
 */
public class Estudiante {

    private int noCuenta;
    private String centroEstudio;
    private String carrera;
    private float promedioActual;

    // ----- Constructores de la clase -----
    // Constructor Estandar
    public Estudiante() {
        System.out.println("Creando el Objeto estudiante");
    }

    // Constructor Especial
    public Estudiante(int noCuenta, String centroEstudio, String carrera, float promedioActual) {
        this(); // llamar el contructor vacio
        this.noCuenta = noCuenta;
        this.centroEstudio = centroEstudio;
        this.carrera = carrera;
        this.promedioActual = promedioActual;
    }

    // ----- Metodos Getters y Setters -----
    public int getNoCuenta() {
        return noCuenta;
    }

    public void setNoCuenta(int noCuenta) {
        this.noCuenta = noCuenta;
    }

    public String getCentroEstudio() {
        return centroEstudio;
    }

    public void setCentroEstudio(String centroEstudio) {
        this.centroEstudio = centroEstudio;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public float getPromedioActual() {
        return promedioActual;
    }

    public void setPromedioActual(float promedioActual) {
        this.promedioActual = promedioActual;
    }

    // ----- Metodos de Comportamiento -----
    public void imprimirAtributos() {
        System.out.printf("""
                          %n---- Datos del estudiante ----
                           No. Cuenta: %d
                           Carrera: %s
                           Centro Estudio: %s
                           Promedio: %.2f%%
                           """, noCuenta, carrera, centroEstudio, promedioActual);

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.noCuenta;
        hash = 29 * hash + Objects.hashCode(this.centroEstudio);
        hash = 29 * hash + Objects.hashCode(this.carrera);
        hash = 29 * hash + Float.floatToIntBits(this.promedioActual);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estudiante other = (Estudiante) obj;
        if (this.noCuenta != other.noCuenta) {
            return false;
        }
        if (Float.floatToIntBits(this.promedioActual) != Float.floatToIntBits(other.promedioActual)) {
            return false;
        }
        if (!Objects.equals(this.centroEstudio, other.centroEstudio)) {
            return false;
        }
        return Objects.equals(this.carrera, other.carrera);
    }

    @Override
    public String toString() {
        return "Estudiante{" + "noCuenta=" + noCuenta + ", centroEstudio=" + centroEstudio + ", carrera=" + carrera + ", promedioActual=" + promedioActual + '}';
    }

}
