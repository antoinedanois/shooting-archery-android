package com.iutbmteprow.shootingarchery.dbman;

public class Graduation {
    private int idArc;
    private int idGraduation;
    private int Distance;
    private String Remarque;
    private float horizontal;
    private float vertical;
    private float profondeur;


    public Graduation(int idArc, int idGraduation, int Distance, String Remarque, float horizontal, float vertical, float profondeur) {
        super();
        this.idArc = idArc;
        this.idGraduation = idGraduation;
        this.Distance = Distance;
        this.Remarque = Remarque;
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.profondeur = profondeur;
    }

    public int getIdArc() {
        return idArc;
    }

    public int getIdGraduation() {
        return idGraduation;
    }
    public void setIdGraduation(int idGraduation) {
        this.idGraduation = idGraduation;
    }
    public int getDistance() {
        return Distance;
    }
    public void setDistance(int Distance) {
        this.Distance = Distance;
    }
    public String getRemarque(){return Remarque;}
    public void setRemarque(String Remarque){this.Remarque = Remarque; }
    public double getHorizontal() {return horizontal;}
    public void setHorizontal(int horizontal) {this.horizontal = horizontal;}
    public double getVertical() {return vertical;}
    public void setVertical(int vertical) {this.vertical = vertical;}
    public double getProfondeur() {return profondeur;}
    public void setProfondeur(int pro4) {this.profondeur = profondeur;}
}