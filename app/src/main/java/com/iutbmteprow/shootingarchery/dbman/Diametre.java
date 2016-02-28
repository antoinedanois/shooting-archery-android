package com.iutbmteprow.shootingarchery.dbman;

public class Diametre {

    private int idDiametre;
    private int idCible;
    private int diametre;

    public Diametre(int idDiametre, int idCible, int diametre) {
        super();
        this.idDiametre = idDiametre;
        this.idCible = idCible;
        this.diametre = diametre;
    }

    public int getIdDiametre() {
        return idDiametre;
    }

    public int getIdCible() {
        return idCible;
    }

    public int getDiametre() {
        return diametre;
    }

}
