package com.iutbmteprow.shootingarchery.dbman;

import java.util.Date;

/**
 * Created by Geoffrey on 02/02/2015.
 */
public class Campagne {
    private int idCampagne;
    private int idUtilisateur;
    private boolean partieFini;
    private Date datePartie;
    private int nbCibles;
    private boolean competition;
    private String NomArc;

    public Campagne(int idCampagne, int idUtilisateur, boolean partieFini, Date datePartie, int nbCibles, boolean competition, String NomArc) {
        super();
        this.idCampagne = idCampagne;
        this.idUtilisateur = idUtilisateur;
        this.partieFini = partieFini;
        this.datePartie = datePartie;
        this.nbCibles = nbCibles;
        this.competition = competition;
        this.NomArc = NomArc;
    }


    public int getIdCampagne() {
        return idCampagne;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public boolean isPartieFini() {
        return partieFini;
    }

    public void setPartieFini(boolean partieFini) {
        this.partieFini = partieFini;
    }

    public Date getDatePartie() {
        return datePartie;
    }

    public void setDatePartie(Date datePartie) {
        this.datePartie = datePartie;
    }


    public int getNbCibles() {
        return nbCibles;
    }

    public void setNbCibles(int nbCibles) {
        this.nbCibles = nbCibles;
    }

    public boolean isCompetition() {
        return competition;
    }

    public void setCompetition(boolean comp_entrain) {
        this.competition = comp_entrain;
    }

    public String getNomArc() {
        return NomArc;
    }


}
