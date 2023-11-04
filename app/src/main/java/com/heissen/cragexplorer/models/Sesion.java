package com.heissen.cragexplorer.models;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Sesion  {


    private int id;


    private int idUsuario;


    private int idVia;


    private double porcentaje;


    private String fecha; // Asegúrate de que la fecha esté en un formato adecuado


    private int idTipo;

    private int intentos;

    public Sesion() {
        // Constructor vacío requerido para deserialización
    }

    public Sesion(int idVia, double porcentaje, String fecha, int idTipo, int intentos) {
        this.idVia = idVia;
        this.porcentaje = porcentaje;
        this.fecha = fecha;
        this.idTipo = idTipo;
        this.intentos = intentos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdVia() {
        return idVia;
    }

    public void setIdVia(int idVia) {
        this.idVia = idVia;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    @Override
    public String toString() {
        return "Sesion{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", idVia=" + idVia +
                ", porcentaje=" + porcentaje +
                ", fecha=" + fecha +
                ", idTipo=" + idTipo +
                ", intentos=" + intentos +
                '}';
    }
}
