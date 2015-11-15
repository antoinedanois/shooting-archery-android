package com.iutbmteprow.shootingarchery.dbman;

public class Tirer {

	private int idTirer;
	private int idPartie;
	private int noManche;
	private int nbVolee;
	private int orderLancer;
	private int score;
	
	public Tirer(int idTirer, int idPartie, int noManche, int nbVolee, int orderLancer,
			int score) {
		super();
		this.setIdTirer(idTirer);
		this.idPartie = idPartie;
		this.noManche = noManche;
		this.setNbVolee(nbVolee);
		this.orderLancer = orderLancer;
		this.score = score;
	}

	public int getIdTirer() {
		return idTirer;
	}

	public void setIdTirer(int idTirer) {
		this.idTirer = idTirer;
	}

	public int getIdPartie() {
		return idPartie;
	}
	
	public int getNoManche() {
		return noManche;
	}
	
	public void setNoManche(int noManche) {
		this.noManche = noManche;
	}
	
	public int getNbVolee() {
		return nbVolee;
	}

	public void setNbVolee(int nbVolee) {
		this.nbVolee = nbVolee;
	}

	public int getOrderLancer() {
		return orderLancer;
	}

	public void setOrderLancer(int orderLancer) {
		this.orderLancer = orderLancer;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}