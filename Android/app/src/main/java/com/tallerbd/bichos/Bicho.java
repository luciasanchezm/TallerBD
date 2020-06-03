package com.tallerbd.bichos;

public class Bicho {
    public int id = 0;
    public String name = "";
    public int salud = 0;
    public String tipo = "";
    public String especie = "";
    public String propietario = "";

    public Bicho(int id, String name, int salud, String tipo, String especie, String propietario) {
        this.id = id;
        this.name = name;
        this.salud = salud;
        this.tipo = tipo;
        this.especie = especie;
        this.propietario = propietario;
    }
}
