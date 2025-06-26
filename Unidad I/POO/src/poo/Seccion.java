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
public class Seccion {

    private int codigo;
    private int catAlumnos;
    private int horaInicio;
    private int horaFin;
    private String materia;
    private String aula;
    private String dias;

    // ----- Constructores de la clase -----
    public Seccion() {
        System.out.println("Creando el objeto de tipo Seccion");
    }

    public Seccion(int codigo, int catAlumnos, int horaInicio, int horaFin, String materia, String aula, String dias) {
        this();
        this.codigo = codigo;
        this.catAlumnos = catAlumnos;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.materia = materia;
        this.aula = aula;
        this.dias = dias;
    }

    // ----- Getters y Setters -----
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCatAlumnos() {
        return catAlumnos;
    }

    public void setCatAlumnos(int catAlumnos) {
        this.catAlumnos = catAlumnos;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    // ----- Metodos de utilidad -----
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.codigo;
        hash = 29 * hash + this.catAlumnos;
        hash = 29 * hash + this.horaInicio;
        hash = 29 * hash + this.horaFin;
        hash = 29 * hash + Objects.hashCode(this.materia);
        hash = 29 * hash + Objects.hashCode(this.aula);
        hash = 29 * hash + Objects.hashCode(this.dias);
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
        final Seccion other = (Seccion) obj;
        if (this.codigo != other.codigo) {
            return false;
        }
        if (this.catAlumnos != other.catAlumnos) {
            return false;
        }
        if (this.horaInicio != other.horaInicio) {
            return false;
        }
        if (this.horaFin != other.horaFin) {
            return false;
        }
        if (!Objects.equals(this.materia, other.materia)) {
            return false;
        }
        if (!Objects.equals(this.aula, other.aula)) {
            return false;
        }
        return Objects.equals(this.dias, other.dias);
    }

    @Override
    public String toString() {
        return "Seccion{" + "codigo=" + codigo + ", catAlumnos=" + catAlumnos + ", horaInicio=" + horaInicio + ", horaFin=" + horaFin + ", materia=" + materia + ", aula=" + aula + ", dias=" + dias + '}';
    }

}
