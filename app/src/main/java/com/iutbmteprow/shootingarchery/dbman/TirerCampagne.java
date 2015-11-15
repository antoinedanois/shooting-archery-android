package com.iutbmteprow.shootingarchery.dbman;

/**
 * Created by Geoffrey on 02/02/2015.
 */
public class TirerCampagne {
    private int idTirerCampagne;
    private int idCampagne;
    private int noVolee;
    private int score;
    private int idCible;
    private boolean connu;
    private int Distance;
    private int orderLancer;

    public TirerCampagne(int idTirerCampagne, int idCampagne, int noVolee, int score,int idCible, boolean connu,int distance, int orderLancer) {
        super();
        this.idTirerCampagne =idTirerCampagne;
        this.idCampagne = idCampagne;
        this.noVolee = noVolee;
        this.score = score;
        this.idCible = idCible;
        this.connu =connu;
        this.Distance = distance;
        this.orderLancer = orderLancer;
    }

    public int getIdTirerCampagne() {
        return idTirerCampagne;
    }

    public void setIdTirerCampagne(int idTirerCampagne) {
        this.idTirerCampagne = idTirerCampagne;
    }

    public int getIdCampagne() {
        return idCampagne;
    }

    public int getNoVolee() {
        return noVolee;
    }

    public void setNoVolee(int noVolee) {
        this.noVolee = noVolee;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getIdCible() {
        return idCible;
    }

    public boolean getConnu() {
        return connu;
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int Distance) {
        this.Distance = Distance;
    }

    public int getOrderLancer() {
        return orderLancer;
    }

    public void setOrderLancer(int orderLancer) {
        this.orderLancer = orderLancer;
    }
}
