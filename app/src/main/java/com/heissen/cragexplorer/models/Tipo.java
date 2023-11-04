package com.heissen.cragexplorer.models;

public class Tipo {

    private int id;
    public int TipoN;

    public Tipo() {
    }

    public Tipo(int id, int tipoN) {
        this.id = id;
        TipoN = tipoN;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipoN() {
        return TipoN;
    }

    public void setTipoN(int tipoN) {
        TipoN = tipoN;
    }
}
