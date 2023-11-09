package com.heissen.cragexplorer.models;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id ;
    private String nombre ;
    private String apellido ;

    private String correo ;
    private String clave ;
    private String avatar;
    private boolean google;

    public Usuario() {
    }

    public Usuario( String nombre, String apellido, String correo, String clave, String avatar, boolean google) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.clave = clave;
        this.avatar = avatar;
        this.google = google;
    }

    public Usuario(String nombre, String apellido, String correo, String clave) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.clave = clave;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return  nombre + ' ' + apellido ;
    }

    public boolean isGoogle() {
        return google;
    }

    public void setGoogle(boolean google) {
        this.google = google;
    }



}
