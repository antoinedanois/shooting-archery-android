package com.iutbmteprow.shootingarchery.dbman;

public class Arc {
    private int idUtilisateur;
    private int idArc;
    private String NomArc;
    private int idTypeArc;

    public Arc(int idArc, int idUtilisateur,
               String NomArc, int idTypeArc) {
        super();
        this.idUtilisateur = idUtilisateur;
        this.idArc = idArc;
        this.NomArc = NomArc;
        this.idTypeArc = idTypeArc;
    }

    public int getIdArc() {
        return idArc;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNomArc() {
        return NomArc;
    }

    public String setNomArc(String NomArc) {
        return this.NomArc = NomArc;
    }

    public int getIdTypeArc() {
        return idTypeArc;
    }

    public void setIdTypeArc(int idTypeArc) {
        this.idTypeArc = idTypeArc;
    }
}