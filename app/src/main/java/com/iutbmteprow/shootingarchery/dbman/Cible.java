package com.iutbmteprow.shootingarchery.dbman;

public class Cible {

	private int id;
	private String libelle;
	
	public Cible(int id, String libelle) {
		this.id = id;
		this.setLibelle(libelle);
	}

	public String getLibelle() {
		return libelle;
	}
	
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	public int getId() {
		return id;
	}
	
}