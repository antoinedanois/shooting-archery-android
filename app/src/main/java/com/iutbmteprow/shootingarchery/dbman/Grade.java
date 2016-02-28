package com.iutbmteprow.shootingarchery.dbman;

public class Grade {

    private int id;
    private String libelle;

    public Grade(int id, String libelle) {
        this.id = id;
        this.setLibelle(libelle);
    }

    public int getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }


}