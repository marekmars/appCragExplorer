package com.heissen.cragexplorer.models;

public class Foto {

    private int id;


    private Via via;


    private int idVia;


    private String url;



    private Usuario usuario;


    private int idUsuario;

    public Foto() {
    }

    public Foto(Via via, int idVia, String url, Usuario usuario, int idUsuario) {
        this.via = via;
        this.idVia = idVia;
        this.url = url;
        this.usuario = usuario;
        this.idUsuario = idUsuario;
    }

    public Foto(int idVia, String url) {
        this.idVia = idVia;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Override
    public String toString() {
        return "Foto{" +
                "id=" + id +
                ", via=" + via +
                ", idVia=" + idVia +
                ", url='" + url + '\'' +
                ", usuario=" + usuario +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
