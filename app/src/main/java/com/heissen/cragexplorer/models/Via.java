package com.heissen.cragexplorer.models;

import java.io.Serializable;

public class Via implements Serializable {
    private int id ;

    private Zona zona;
    private int idZona;

    private int idEstilo;

    private Estilo estilo;

    private String nombre;

    private int idGrado;

    private Grado grado;

    private int chapas;

    private double altura;

    public Via() {
    }

    public Via(  int idZona, int idEstilo, String nombre, int idGrado, int chapas, double altura) {

        this.idZona = idZona;
        this.idEstilo = idEstilo;
        this.nombre = nombre;
        this.idGrado = idGrado;
        this.chapas = chapas;
        this.altura = altura;
    }

    public Via(int id, Zona zona, int idZona, int idEstilo, Estilo estilo, String nombre, int idGrado, Grado grado, int chapas, double altura) {
        this.id = id;
        this.zona = zona;
        this.idZona = idZona;
        this.idEstilo = idEstilo;
        this.estilo = estilo;
        this.nombre = nombre;
        this.idGrado = idGrado;
        this.grado = grado;
        this.chapas = chapas;
        this.altura = altura;
    }

    public int getId() {
        return id;
    }

    public Estilo getEstilo() {
        return estilo;
    }

    public void setEstilo(Estilo estilo) {
        this.estilo = estilo;
    }

    public Grado getGrado() {
        return grado;
    }

    public void setGrado(Grado grado) {
        this.grado = grado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public int getIdEstilo() {
        return idEstilo;
    }

    public void setIdEstilo(int idEstilo) {
        this.idEstilo = idEstilo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public int getChapas() {
        return chapas;
    }

    public void setChapas(int chapas) {
        this.chapas = chapas;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    @Override
    public String toString() {
        return "Via{" +
                "id=" + id +
                ", zona=" + zona +
                ", idZona=" + idZona +
                ", idEstilo=" + idEstilo +
                ", nombre='" + nombre + '\'' +
                ", idGrado=" + idGrado +
                ", chapas=" + chapas +
                ", altura=" + altura +
                '}';
    }
}
