package com.iutbmteprow.shootingarchery.dbman;


import java.util.Date;

public class Partie {

	private int idPartie;
	private int idUtilisateur;
	private boolean partieFini;
	private int idCible;
	private int distanceCible;
	private Date datePartie;
	private boolean exterieur;
	private int nbVolees;
	private int nbManches;
	private int nbFleches;
	private boolean competition;
	private int idDiametre;
    private String NomArc;
	
	public Partie(int idPartie, int idUtilisateur, boolean partieFini,
			int idCible, int distanceCible,  Date datePartie,
			int nbManches, int nbVolees, int nbFleches, 
			boolean comp_entrain, boolean ext_int, int idDiametre, String NomArc) {
		super();
		this.idPartie = idPartie;
		this.idUtilisateur = idUtilisateur;
		this.partieFini = partieFini;
		this.idCible = idCible;
		this.distanceCible = distanceCible;
		this.datePartie = datePartie;
		this.exterieur = ext_int;
		this.nbVolees = nbVolees;
		this.setNbManches(nbManches);
		this.nbFleches = nbFleches;
		this.competition = comp_entrain;
		this.idDiametre = idDiametre;
        this.NomArc = NomArc;
	}

	public int getIdPartie() {
		return idPartie;
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
	
	public int getIdCible() {
		return idCible;
	}
	public void setIdCible(int idCible) {
		this.idCible = idCible;
	}
	
	public int getDistanceCible() {
		return distanceCible;
	}
	public void setDistanceCible(int distanceCible) {
		this.distanceCible = distanceCible;
	}
	
	public Date getDatePartie() {
		return datePartie;
	}
	public void setDatePartie(Date datePartie) {
		this.datePartie = datePartie;
	}
	
	public boolean isExterieur() {
		return exterieur;
	}
	public void setExterieur(boolean ext_int) {
		this.exterieur = ext_int;
	}
	
	public int getNbVolees() {
		return nbVolees;
	}
	public void setNbVolees(int nbVolees) {
		this.nbVolees = nbVolees;
	}
	
	public int getNbManches() {
		return nbManches;
	}

	public void setNbManches(int nbManches) {
		this.nbManches = nbManches;
	}

	public int getNbFleches() {
		return nbFleches;
	}
	public void setNbFleches(int nbFleches) {
		this.nbFleches = nbFleches;
	}
	
	public boolean isCompetition() {
		return competition;
	}
	public void setCompetition(boolean comp_entrain) {
		this.competition = comp_entrain;
	}

	public int getIdDiametre() {
		return idDiametre;
	}
    public String getNomArc() {
        return NomArc;
    }
	
}
