package com.iutbmteprow.shootingarchery.dbman;

public class Utilisateur {
	private int id;
	private int idGrade;
	private String grade;
	private String nom;
	private String prenom;
	
	public Utilisateur(int id, int idGrade, String nom, String prenom) {
		this.id = id;
		grade = "";
		this.setIdGrade(idGrade);
		this.setNom(nom);
		this.setPrenom(prenom);
	}
	
	public Utilisateur(int id, int idGrade, String grade, String nom, String prenom) {
		this.id = id;
		this.idGrade = idGrade;
		this.grade = grade;
		this.setNom(nom);
		this.setPrenom(prenom);
	}

	public int getId() {
		return id;
	}

	public int getIdGrade() {
		return idGrade;
	}

	public void setIdGrade(int idGrade) {
		this.idGrade = idGrade;
	}

	public String getGrade() throws NoSuchFieldException {
		if (grade.isEmpty())
			throw new NoSuchFieldException();
		return grade;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
}