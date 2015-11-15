package com.iutbmteprow.shootingarchery.dbman;

public class TypeArc {
    private int idTypeArc;
    private String NomType;
    public TypeArc(int idTypeArc, String NomType) {
        super();
        this.idTypeArc = idTypeArc;
        this.NomType = NomType;
    }
    public int getIdTypeArc() {
        return idTypeArc;
    }
    public String getNomType() {
        return NomType;
    }
    public void setNomType(String NomType) {
        this.NomType = NomType;
    }
}