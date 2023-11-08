package com.heissen.cragexplorer.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Resenia implements Serializable {

    private int id;
    private Via via;

    private int idVia;
    private Usuario usuario;

    private int idUsuario;

    private String comentario;

    private double calificacion;

    private String fecha;

    public Resenia( int idVia, String comentario, double calificacion, String fecha) {
        this.idVia = idVia;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fecha = fecha;
    }

    public Resenia(int id, int idVia, int idUsuario, String comentario, double calificacion, String fecha) {
        this.id = id;
        this.idVia = idVia;
        this.idUsuario = idUsuario;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fecha = fecha;
    }

    public Resenia(int id, int idVia, String comentario, double calificacion, String fecha) {
        this.id = id;
        this.idVia = idVia;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Via getVia() {
        return via;
    }

    public void setVia(Via via) {
        this.via = via;
    }

    public int getIdVia() {
        return idVia;
    }

    public void setIdVia(int idVia) {
        this.idVia = idVia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Resenia{" +
                "id=" + id +
                ", via=" + via +
                ", idVia=" + idVia +
                ", usuario=" + usuario +
                ", idUsuario=" + idUsuario +
                ", comentario='" + comentario + '\'' +
                ", calificacion=" + calificacion +
                ", fecha=" + fecha +
                '}';
    }
}

