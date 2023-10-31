package com.heissen.cragexplorer.models;

import java.io.Serializable;

public class Zona implements Serializable {

    private int id;

    private int idSector;
    private Sector sector;


    private String nombre;

    public Zona() {
    }

    public Zona(int id, int idSector, String nombre) {
        this.id = id;
        this.idSector = idSector;
        this.nombre = nombre;
    }

    public int getIdSector() {
        return idSector;
    }

    public void setIdSector(int idSector) {
        this.idSector = idSector;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Zona{" +
                "id=" + id +
                ", idSector=" + idSector +
                ", sector=" + sector +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
