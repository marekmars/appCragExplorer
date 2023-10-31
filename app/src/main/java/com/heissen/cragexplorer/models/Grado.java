package com.heissen.cragexplorer.models;

public class Grado {
    public int id;
    public String gradoN;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGradoN() {
        return gradoN;
    }

    public void setGradoN(String gradoN) {
        this.gradoN = gradoN;
    }

    @Override
    public String toString() {
        return "Grado{" +
                "id=" + id +
                ", gradoN='" + gradoN + '\'' +
                '}';
    }
}
