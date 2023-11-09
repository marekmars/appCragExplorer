package com.heissen.cragexplorer.models;

import java.io.Serializable;

public class Favorito implements Serializable {

    private int id;

    private Via via;

    private int idVia;
    private Usuario usuario;

    private int idUsuario;

    private String fecha;

    public Favorito() {
    }

    public Favorito(int id, Via via, int idVia, Usuario usuario, int idUsuario, String fecha) {
        this.id = id;
        this.via = via;
        this.idVia = idVia;
        this.usuario = usuario;
        this.idUsuario = idUsuario;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Favorito{" +
                "id=" + id +
                ", via=" + via +
                ", idVia=" + idVia +
                ", usuario=" + usuario +
                ", idUsuario=" + idUsuario +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
