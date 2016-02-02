package com.iutbmteprow.shootingarchery.dbman;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static int DB_VERSION = 1;
	private static String DB_NAME = "tiralarc";
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		//Script SQL pour creer la base
		arg0.execSQL("CREATE TABLE Utilisateur (" +
				"idUtilisateur INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
				"idGrade INTEGER NOT NULL  REFERENCES Grade (idGrade)," +
				"NomUtilisateur TEXT(16) NOT NULL ," +
				"PrenomUtilisateur TEXT(16) NOT NULL " +
				");");
		
		arg0.execSQL("CREATE TABLE Grade (" +
				"idGrade INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
				"LibGrade TEXT(16) NOT NULL" +
				");");
		
		arg0.execSQL("CREATE TABLE Cible ("+
				"idCible INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
				"LibCible TEXT(16) NOT NULL " +
				");");
		
		arg0.execSQL("CREATE TABLE Partie (" +
				"idPartie INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
				"idUtilisateur INTEGER NOT NULL  REFERENCES Utilisateur (idUtilisateur)," +
				"partieFini TEXT(1) NOT NULL  DEFAULT 'F'," +
				"idCible INTEGER NOT NULL  REFERENCES Cible (idCible)," +
				"distanceCible INTEGER NOT NULL ," +
				"datePartie TEXT NOT NULL ," +
				"nbreManches INTEGER NOT NULL  DEFAULT 1," +
				"nbreVolees INTEGER NOT NULL  DEFAULT 3," +
				"nbreFleches INTEGER NOT NULL ," +
				"comp_entrain TEXT(1) NOT NULL  DEFAULT 'E'," +
				"ext_int TEXT(1) NOT NULL  DEFAULT 'E'," +
				"idDiametre INTEGER NOT NULL  REFERENCES Diametre (idDiametre)," +
                "NomArc TEXT(20) NOT NULL ," +
				"joueurs TEXT NOT NULL" +
				");");
		
		arg0.execSQL("CREATE TABLE Tirer (" +
				"idTirer INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
				"idPartie INTEGER NOT NULL  REFERENCES Partie (idPartie)," +
				"noManche INTEGER NOT NULL  DEFAULT 1," +
				"noVolee INTEGER NOT NULL  DEFAULT 1," +
				"ordreLancer INTEGER NOT NULL  DEFAULT 1," +
				"score INTEGER NOT NULL  DEFAULT 0" +
				");");
		
		arg0.execSQL("CREATE TABLE Diametre (" +
				"idDiametre INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
				"idCible INTEGER NOT NULL  REFERENCES Cible (idCible)," +
				"diametreCible NUMERIC NOT NULL" +
				");");

        arg0.execSQL("CREATE TABLE TypeArc (" +
                "idTypeArc INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
                "NomType TEXT(20) NOT NULL" +
                ");");

        arg0.execSQL("CREATE TABLE Arc (" +
                "idArc INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
                "idUtilisateur INTEGER NOT NULL  REFERENCES Utilisateur (idUtilisateur)," +
                "NomArc TEXT(20) NOT NULL," +
                "idTypeArc INTEGER NOT NULL  REFERENCES TypeArc (idTypeArc)" +
                ");");

        arg0.execSQL("CREATE TABLE Graduation (" +
                "idGraduation INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
                "idArc INTEGER NOT NULL  REFERENCES Arc (idArc)," +
                "Distance INTEGER(3) NOT NULL," +
                "Remarque TEXT NULL  ," +
                "Horizontal REAL(6) NOT NULL,"+
                "Vertical REAL(6) NOT NULL,"+
                "Profondeur REAL(6) NOT NULL"+
                ");");


        arg0.execSQL("CREATE TABLE Campagne (" +
                "idCampagne INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
                "idUtilisateur INTEGER NOT NULL  REFERENCES Utilisateur (idUtilisateur)," +
                "partieFini TEXT(1) NOT NULL  DEFAULT 'F'," +
                "datePartie TEXT NOT NULL ," +
                "nbCible INTEGER NOT NULL  ," +
                "competition TEXT(1) NOT NULL  DEFAULT 'E'," +
                "NomArc TEXT(20) NOT NULL" +
                ");");

        arg0.execSQL("CREATE TABLE TirerCampagne (" +
                "idTirerCampagne INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT," +
                "idCampagne INTEGER NOT NULL  REFERENCES Campagne (idCampagne)," +
                "noVolee INTEGER NOT NULL  DEFAULT 1," +
                "score INTEGER NOT NULL  DEFAULT 1," +
                "idCible INTEGER NOT NULL  DEFAULT 1," +
                "connu TEXT(1) NOT NULL  DEFAULT 'F'," +
                "Distance INTEGER NOT NULL  DEFAULT 0," +
                "ordreLancer INTEGER NOT NULL  DEFAULT 1" +
                ");");
		
		//Insertion de donnees statiques
		// -- Grade
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Poussin');");
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Benjamin');");
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Minime');");
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Cadet');");
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Junior');");
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Senior');");
		arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Veteran');");
        arg0.execSQL("INSERT INTO 'Grade' VALUES(null,'Super-Veteran');");

		// -- Cible
		arg0.execSQL("INSERT INTO 'Cible' VALUES(null,'Blason');");
		arg0.execSQL("INSERT INTO 'Cible' VALUES(null,'Trispot');");
        arg0.execSQL("INSERT INTO 'Cible' VALUES(null,'Birdee');");
        arg0.execSQL("INSERT INTO 'Cible' VALUES(null,'Gazinière');");
        arg0.execSQL("INSERT INTO 'Cible' VALUES(null,'60');");
        arg0.execSQL("INSERT INTO 'Cible' VALUES(null,'80');");

		// -- Diametre
		arg0.execSQL("INSERT INTO 'Diametre' VALUES(null,1,40);");
		arg0.execSQL("INSERT INTO 'Diametre' VALUES(null,1,60);");
		arg0.execSQL("INSERT INTO 'Diametre' VALUES(null,1,80);");
		arg0.execSQL("INSERT INTO 'Diametre' VALUES(null,1,122);");
		
		arg0.execSQL("INSERT INTO 'Diametre' VALUES(null,2,40);");
		arg0.execSQL("INSERT INTO 'Diametre' VALUES(null,2,60);");

        // -- Type d'Arc
        arg0.execSQL("INSERT INTO 'TypeArc' VALUES(null,'Classique');");
        arg0.execSQL("INSERT INTO 'TypeArc' VALUES(null,'Poulie');");
        arg0.execSQL("INSERT INTO 'TypeArc' VALUES(null,'Nu');");
        arg0.execSQL("INSERT INTO 'TypeArc' VALUES(null,'Poulie Nue');");
        arg0.execSQL("INSERT INTO 'TypeArc' VALUES(null,'BareBow');");
        arg0.execSQL("INSERT INTO 'TypeArc' VALUES(null,'Droit');");
        arg0.execSQL("INSERT INTO 'TypeArc' VALUES(null,'Chasse');");

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO : Script d'upgrade
	}

	//---------------------------------------------------------------
	//Utilisateur
	public void addUtilisateur(Utilisateur user) throws UserAlreadyRegisteredException {
		if (getUtilisateurFromName(user.getNom(), user.getPrenom()) != null) {
			throw new UserAlreadyRegisteredException();
		}
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		//Remplir la requete
		ContentValues values = new ContentValues();
		values.put("nomUtilisateur", user.getNom());
		values.put("prenomUtilisateur", user.getPrenom());
		values.put("idGrade", user.getIdGrade());
		//Insert dans BDD
		db.insert("Utilisateur", null, values);
		db.close();
	}
	
	public Utilisateur getUtilisateur(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query("Utilisateur", new String[] { "idUtilisateur", "idGrade",
	            "nomUtilisateur", "prenomUtilisateur" }, "idUtilisateur" + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
				cursor.moveToFirst();
		
		Utilisateur user = new Utilisateur(
				cursor.getInt(0),
				cursor.getInt(1),
				cursor.getString(2), 
				cursor.getString(3));
		
		cursor.close();
		db.close();
		return user;
	}
	
	public Utilisateur getUtilisateurFromName(String nom,String prenom)
	{
		String selectQuery = "SELECT * FROM Utilisateur WHERE NomUtilisateur='"+nom+"' and PrenomUtilisateur='"+prenom+ "';";
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  
		  Utilisateur user = null;
		  
		  if(cursor.moveToFirst())
		  {
			  user = new Utilisateur(Integer.parseInt(cursor.getString(0)),
	            		Integer.parseInt(cursor.getString(1)),
	            		getGrade(Integer.parseInt(cursor.getString(1))).getLibelle(),
	            		cursor.getString(2),
	            		cursor.getString(3));
		  }
		  cursor.close();
		  db.close();
		  return user;
	}
	
	public List<Utilisateur> getUtilisateurs() {
		List<Utilisateur> usersList = new ArrayList<Utilisateur>();
		
		String selectQuery = "SELECT * FROM Utilisateur;";
		 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Utilisateur user = new Utilisateur(Integer.parseInt(cursor.getString(0)),
	            		Integer.parseInt(cursor.getString(1)),
	            		cursor.getString(2),
	            		cursor.getString(3));
	            // Adding contact to list
	            usersList.add(user);
	        } while (cursor.moveToNext());
	    }
		
	    cursor.close();
	    db.close();
	    
		return usersList;
	}
	
	public List<String> getUtilisateursForSpinner() {
		List<String> listUsers = new ArrayList<String>();
		
		for (Utilisateur user : getUtilisateurs()) {
			listUsers.add(user.getPrenom() + " " + user.getNom());
		}
		
		return listUsers;
	}
	
	public int getUtilisateurCounts() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM Utilisateur;", null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}
	
	public void updateUtilisateur(Utilisateur user) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("NomUtilisateur", user.getNom());
		values.put("PrenomUtilisateur", user.getPrenom());
		values.put("idGrade", user.getIdGrade());
		
		db.update("Utilisateur", values, "idUtilisateur="+user.getId(), null);
		db.close();
	}
	
	public void deleteUtilisateur(Utilisateur user) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.rawQuery("DELETE FROM Tirer WHERE idPartie in (SELECT idPartie FROM Partie WHERE idUtilisateur="+user.getId()+");", null);
		db.delete("Partie", "idUtilisateur="+user.getId(), null);

        Cursor cursor = db.rawQuery("SELECT idArc FROM Arc WHERE idUtilisateur ="+user.getId()+";", null);
        if (cursor.moveToFirst()) {
            do {
                db.delete("Graduation", "idArc="+cursor.getInt(0), null);
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.delete("Arc", "idUtilisateur="+user.getId(), null);
		db.delete("Utilisateur", "idUtilisateur="+user.getId(), null);
		db.close();
	}
	
	//---------------------------------------------------------------
	//Partie
	
	public int addPartie(Partie partie) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("idUtilisateur", partie.getIdUtilisateur());
		values.put("partieFini", partie.isPartieFini());
		values.put("idCible", partie.getIdCible());
		values.put("distanceCible", partie.getDistanceCible());
		values.put("datePartie", partie.getDatePartie().getTime());  
		values.put("nbreManches", partie.getNbManches());
		values.put("nbreVolees", partie.getNbVolees());
		values.put("nbreFleches", partie.getNbFleches());
		values.put("comp_entrain", ((partie.isCompetition())?"C":"E"));
		values.put("ext_int", ((partie.isExterieur())?"E":"I"));
		values.put("idDiametre",partie.getIdDiametre());
        values.put("NomArc",partie.getNomArc());
		values.put("joueurs",partie.getJoueurs());
		
		int idPartie = Integer.valueOf(String.valueOf(db.insert("Partie", null, values)));
		db.close();
		return idPartie;
	}
	
	public int checkOngoingGame() {
		String selectQuery = "SELECT count(partieFini),idPartie FROM Partie WHERE partieFini != 'T' ;";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		int idPartie = 0;

		if(cursor.moveToFirst()) {
			if (cursor.getInt(0) != 0)
				idPartie=cursor.getInt(1);
		}
		
		cursor.close();
		db.close();
		return idPartie;
	}
	
	public void incVoleesPartie(int idPartie) {
		Partie partie = getPartie(idPartie);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("nbreVolees", partie.getNbVolees() + 1);
		db.update("Partie",values, "idPartie =" + idPartie, null);
		db.close();
	}
	
	public void terminerPartie(int idPartie)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values=new ContentValues();
		values.put("partieFini", "T");
		db.update("Partie",values, "idPartie =" + idPartie, null);
		db.close();
	}
	
	public void supprimerPartie(int idPartie)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Partie","idPartie ="+idPartie, null);
		db.delete("Tirer","idPartie ="+idPartie, null);
		db.close();
	}
	
	public Partie getPartie(int idPartie) {
		String selectQuery= "SELECT * FROM PARTIE WHERE idPartie =" + idPartie + ";";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		Partie partie = null;
		
		if(cursor.moveToFirst()) {
			partie = new Partie(
					cursor.getInt(0), 
					cursor.getInt(1), 
					(cursor.getString(2).equals("T")?true:false),//boolean partieFini,
					cursor.getInt(3), 
					cursor.getInt(4), 
					new Date(cursor.getLong(5)), 
					cursor.getInt(6),
					cursor.getInt(7),
					cursor.getInt(8),
					(cursor.getString(9).equals("C")?true:false),
					(cursor.getString(10).equals("E")?true:false),
					cursor.getInt(11),
                    cursor.getString(12),
					cursor.getString(13)
			);
		}
		
		cursor.close();
		db.close();
		return partie;
	}
	
	public ArrayList<Integer> getLastParties() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		String selectQuery= "SELECT idPartie FROM PARTIE ORDER BY datePartie DESC LIMIT 5;";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
	        do {
	            result.add(cursor.getInt(0));
	        } while (cursor.moveToNext());
	    }
		cursor.close();
		db.close();
		return result;
	}

	public void normalizeVolees(int idPartie) {
		Partie partie = getPartie(idPartie);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("nbreVolees", partie.getNbVolees() - 1);
		db.update("Partie",values, "idPartie =" + idPartie, null);
		db.close();
	}
	public ArrayList<Partie> sortPartie(Integer idUtilisateur, Boolean typeCible, Boolean entr_comp, Boolean environnement) {
		ArrayList<Partie> parties = new ArrayList<Partie>();
		
		SQLiteDatabase db = getReadableDatabase();
		String SQLQuery = "SELECT idPartie FROM Partie WHERE idUtilisateur=" + idUtilisateur;
		
		if (typeCible != null)
			SQLQuery += " AND idCible=" + String.valueOf(typeCible==true?1:2);

		if (entr_comp != null)
			SQLQuery += " AND comp_entrain='" + (entr_comp==true?"C":"E") + "'";
		
		if (environnement != null)
			SQLQuery += " AND ext_int='" + (environnement==true?"E":"I") + "'";
		
		SQLQuery += " ORDER BY datePartie DESC;";
		Cursor cursor = db.rawQuery(SQLQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				parties.add(getPartie(cursor.getInt(0)));
			} while (cursor.moveToNext());
		}
		
		db.close();
		return parties;
	}

    // Campagne


    public int addCampagne(Campagne campagne) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("idUtilisateur", campagne.getIdUtilisateur());
        values.put("partieFini", campagne.isPartieFini());
        values.put("datePartie", campagne.getDatePartie().getTime());
        values.put("nbCible", campagne.getNbCibles());
        values.put("competition", ((campagne.isCompetition())?"C":"E"));
        values.put("NomArc",campagne.getNomArc());

        int idCampagne = Integer.valueOf(String.valueOf(db.insert("Campagne", null, values)));
        db.close();
        return idCampagne;
    }

    public int checkOngoingGameCampagne() {
        String selectQuery = "SELECT count(partieFini),idCampagne FROM Campagne WHERE partieFini != 'T' ;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int idCampagne = 0;

        if(cursor.moveToFirst()) {
            if (cursor.getInt(0) != 0)
                idCampagne=cursor.getInt(1);
        }

        cursor.close();
        db.close();
        return idCampagne;
    }

    public void incCibleCampagne(int idCampagne) {
        Campagne campagne = getCampagne(idCampagne);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("nbCible", campagne.getNbCibles() + 1);
        db.update("Campagne",values, "idCampagne =" + idCampagne, null);
        db.close();
    }

    public void terminerCampagne(int idCampagne)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("partieFini", "T");
        db.update("Campagne",values, "idCampagne =" + idCampagne, null);
        db.close();
    }

    public void supprimerCampagne(int idCampagne)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Campagne","idCampagne ="+idCampagne, null);
        db.delete("TirerCampagne","idCampagne ="+idCampagne, null);
        db.close();
    }

    public Campagne getCampagne(int idCampagne) {
        String selectQuery= "SELECT * FROM Campagne WHERE idCampagne =" + idCampagne + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Campagne campagne = null;

        if(cursor.moveToFirst()) {
            campagne = new Campagne(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    (cursor.getString(2).equals("T")?true:false),//boolean partieFini,
                    new Date(cursor.getLong(3)),
                    cursor.getInt(4),
                    (cursor.getString(5).equals("C")?true:false),
                    cursor.getString(6));
        }

        cursor.close();
        db.close();
        return campagne;
    }

    public ArrayList<Integer> getLastCampagne() {
        ArrayList<Integer> result = new ArrayList<Integer>();

        String selectQuery= "SELECT idCampagne FROM Campagne ORDER BY datePartie DESC LIMIT 5;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public void normalizeCibles(int idCampagne) {
        Campagne campagne = getCampagne(idCampagne);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("nbCible", campagne.getNbCibles() - 1);
        db.update("Campagne",values, "idCampagne =" + idCampagne, null);
        db.close();
    }


    public ArrayList<Campagne> sortCampagne(Integer idUtilisateur, Boolean entr_comp) {
        ArrayList<Campagne> partiesC = new ArrayList<Campagne>();

        SQLiteDatabase db = getReadableDatabase();
        String SQLQuery = "SELECT idCampagne FROM Campagne WHERE idUtilisateur=" + idUtilisateur;

        if (entr_comp != null)
            SQLQuery += " AND competition='" + (entr_comp==true?"C":"E") + "'";

        SQLQuery += " ORDER BY datePartie DESC;";
        Cursor cursor = db.rawQuery(SQLQuery, null);

        if (cursor.moveToFirst()) {
            do {
                partiesC.add(getCampagne(cursor.getInt(0)));
            } while (cursor.moveToNext());
        }

        db.close();
        return partiesC;
    }
    public ArrayList<PartieGlobal> sortAll(Integer idUtilisateur, Boolean entr_comp, Integer typeCible, Boolean environnement) {
        ArrayList<PartieGlobal> parties = new ArrayList<PartieGlobal>();

        if (typeCible == 0 || typeCible == 1 || typeCible == 2){
            SQLiteDatabase db = getReadableDatabase();
            String SQLQuery = "SELECT idPartie FROM Partie WHERE idUtilisateur=" + idUtilisateur;

            if (typeCible != 0)
                SQLQuery += " AND idCible=" + typeCible;

            if (entr_comp != null)
                SQLQuery += " AND comp_entrain='" + (entr_comp==true?"C":"E") + "'";

            if (environnement != null)
                SQLQuery += " AND ext_int='" + (environnement==true?"E":"I") + "'";

            SQLQuery += " ORDER BY datePartie DESC;";
            Cursor cursor = db.rawQuery(SQLQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    parties.add(new PartieGlobal(getPartie(cursor.getInt(0)).getIdPartie(),getPartie(cursor.getInt(0)).getDatePartie(),"Classique"));
                } while (cursor.moveToNext());
            }
            db.close();
        }
        if (typeCible == 0 || typeCible == 3 ){
            SQLiteDatabase db2 = getReadableDatabase();
            String SQLQuery2 = "SELECT idCampagne FROM Campagne WHERE idUtilisateur=" + idUtilisateur;

            if (entr_comp != null)
                SQLQuery2 += " AND competition='" + (entr_comp==true?"C":"E") + "'";

            SQLQuery2 += " ORDER BY datePartie DESC;";
            Cursor cursor2 = db2.rawQuery(SQLQuery2, null);

            if (cursor2.moveToFirst()) {
                do {
                    parties.add(new PartieGlobal(getCampagne(cursor2.getInt(0)).getIdCampagne(),getCampagne(cursor2.getInt(0)).getDatePartie(),"Campagne"));
                } while (cursor2.moveToNext());
            }


            db2.close();
        }
        Collections.sort(parties, DatePartieComparator);
        return parties;
    }
    public static Comparator<PartieGlobal> DatePartieComparator
            = new Comparator<PartieGlobal>() {

        public int compare(PartieGlobal partie1, PartieGlobal partie2) {

            Date partieDate1 = partie1.getDatePartie();
            Date partieDate2 = partie2.getDatePartie();

            //ascending order
            //return partieDate1.compareTo(partieDate2);

            //descending order
            return partieDate2.compareTo(partieDate1);
        }

    };

   /// Tirer Campagne
   public void addTirerCampagne(int idCampagne, int noVolee, int score, int idCible, boolean connu, int distance, int ordreLancer) {
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues values = new ContentValues();
       values.put("idCampagne", idCampagne);
       values.put("idCible", idCible);
       values.put("noVolee", noVolee);
       values.put("connu", connu?"T":"F");
       values.put("Distance", distance);
       values.put("ordreLancer", ordreLancer);
       values.put("score", score);

       db.insert("TirerCampagne", null, values);
       db.close();
   }
    public ArrayList<TirerCampagne> getTirerOfCamp(int idCampagne) {
        ArrayList<TirerCampagne> resultat = new ArrayList<TirerCampagne>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TirerCampagne WHERE idCampagne =" + idCampagne +
                " ORDER BY noVolee;", null);
        if (cursor.moveToFirst()) {
            do {
                resultat.add(new TirerCampagne(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        (cursor.getString(5).equals("T")?true:false),
                        cursor.getInt(6),
                        cursor.getInt(7))
                );
            } while (cursor.moveToNext());
        }

        db.close();
        return resultat;
    }

    public ArrayList<TirerCampagne> getTirerOfCamp(int idCampagne, int nbVolee) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<TirerCampagne> resultat = new ArrayList<TirerCampagne>();
        String selectQuery = "SELECT * FROM TirerCampagne WHERE idCampagne=" + idCampagne + " AND noVolee=" + nbVolee + " ORDER BY ordreLancer;";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                resultat.add(new TirerCampagne(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        (cursor.getString(5).equals("T")?true:false),
                        cursor.getInt(6),
                        cursor.getInt(7)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return resultat;
    }


	//---------------------------------------------------------------
	// Grade
	
	public Grade getIdGradeFromName(String libelleGrade){
		String selectQuery = "SELECT * FROM Grade WHERE LibGrade='"+libelleGrade+"';";
		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery(selectQuery, null);
		  
		  Grade grade = null;
		  
		  if(cursor.moveToFirst())
		  {
			  grade = new Grade(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
		  }
		  cursor.close();
		  db.close();
		  return grade;
	}
	
	public int findGradeByUser(int idUser){
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT idGrade FROM Utilisateur WHERE idUtilisateur="+idUser+";";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			cursor.moveToFirst();
		
		int idGrade = cursor.getInt(0);
		
		cursor.close();
		db.close();
		
		return idGrade;
	}
	
	public List<String> getGradeForSpinner() {
		List<String> listGrades = new ArrayList<String>();
		
		for (Grade grade : getGrades()) {
			listGrades.add(grade.getLibelle());
		}
		
		return listGrades;
	}
	

	public List<Grade> getGrades() 
	{
		List<Grade> gradeList = new ArrayList<Grade>();
		String selectQuery = "SELECT * FROM Grade;";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		 // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Grade grade = new Grade(Integer.parseInt(cursor.getString(0)),
	            		 cursor.getString(1));
	            // Adding contact to list
	            gradeList.add(grade);
	        } while (cursor.moveToNext());
	    }
		cursor.close();
		db.close();
		return gradeList;
	}

	public Grade getGrade(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query("Grade", new String[] { "idGrade", "LibGrade" }, "idGrade="+id , null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		
		Grade grade = new Grade(cursor.getInt(0), cursor.getString(1));
		
		cursor.close();
		db.close();
		return grade;
	}
	//---------------------------------------------------------------
	// Tirer
	
	public void addTirer(int idPartie, int noManche, int noVolee, int score, int ordreLancer) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("idPartie", idPartie);
		values.put("noManche", noManche);
		values.put("noVolee", noVolee);
		values.put("ordreLancer", ordreLancer);
		values.put("score", score);
		
		db.insert("Tirer", null, values);		
		db.close();
	}
	
	public ArrayList<Tirer> getTirerOf(int idPartie) {
		ArrayList<Tirer> resultat = new ArrayList<Tirer>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM Tirer WHERE idPartie =" + idPartie +
				" ORDER BY noManche,noVolee,ordreLancer;", null);
		if (cursor.moveToFirst()) {
			do {
				resultat.add(new Tirer(cursor.getInt(0),
						cursor.getInt(1),
						cursor.getInt(2),
						cursor.getInt(3),
						cursor.getInt(4),
						cursor.getInt(5)));
			} while (cursor.moveToNext());
		}
		
		db.close();
		return resultat;
	}
	
	public ArrayList<Tirer> getTirerOf(int idPartie, int noManche, int nbVolee) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		ArrayList<Tirer> resultat = new ArrayList<Tirer>();
		String selectQuery = "SELECT * FROM Tirer WHERE idPartie=" + idPartie + " AND noManche=" + noManche + " AND noVolee=" + nbVolee + " ORDER BY ordreLancer;";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				resultat.add(new Tirer(cursor.getInt(0),
						cursor.getInt(1),
						cursor.getInt(2),
						cursor.getInt(3),
						cursor.getInt(4),
						cursor.getInt(5)));
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		return resultat;
	}
	//---------------------------------------------------------------
	// Cible
	
	public Cible getCible(int idCible) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query("Cible", new String[] { "idCible", "LibCible" }, "idCible="+idCible , null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		
		Cible cible = new Cible(cursor.getInt(0), cursor.getString(1));
		
		cursor.close();
		db.close();
		return cible;
	}
    public Cible getIdCibleFromName(String nomCible) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Cible", new String[] { "idCible", "LibCible" }, "LibCible='"+nomCible+"'" , null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Cible cible = new Cible(cursor.getInt(0), cursor.getString(1));

        cursor.close();
        db.close();
        return cible;
    }

	//---------------------------------------------------------------
	// Diametre
	
	public ArrayList<Diametre> getDiametres(int i) {
		ArrayList<Diametre> result = new ArrayList<Diametre>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query("Diametre", new String[] { "idDiametre" , "diametreCible" }, "idCible="+i, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			do {
				result.add(new Diametre(cursor.getInt(0), i, cursor.getInt(1)));
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		return result;
	}
	
	public Diametre getDiametreFromId(int id)
	{
		Diametre result= null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor= db.query("Diametre", new String[] { "idCible" , "diametreCible" }, "idDiametre="+id, null, null, null, null);
		if (cursor.moveToFirst()) 
		{
			result= new Diametre(id,cursor.getInt(0), cursor.getInt(1));
		}
		
		return result;
	}

    //---------------------------------------------------------------
    // Type Arc


    public TypeArc getTypeArc(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("TypeArc", new String[] { "idTypeArc", "NomType" }, "idTypeArc" + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        TypeArc typearc = new TypeArc(
                cursor.getInt(0),
                cursor.getString(1));
        cursor.close();
        db.close();
        return typearc;
    }

    public ArrayList<TypeArc> getTypeArc() {
        ArrayList<TypeArc> result = new ArrayList<TypeArc>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM TypeArc;";


        Cursor cursor = db.rawQuery(selectQuery, null);
        //Cursor cursor = db.query("TypeArc", new String[] { "idTypeArc" , "NomType" }, "idTypeArc=?", null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                result.add(new TypeArc(cursor.getInt(0),cursor.getString(1)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return result;
    }

    public TypeArc getTypeArcFromId(int id)
    {
        TypeArc result= null;
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor= db.query("TypeArc", new String[] {"idTypeArc", "NomType" }, "idTypeArc="+id, null, null, null, null);
        Cursor cursor= db.rawQuery("SELECT * FROM TypeArc WHERE idTypeArc =" + id +";", null);
        if (cursor.moveToFirst())
        {
            result= new TypeArc(cursor.getInt(0),cursor.getString(1));
        }

        return result;
    }
    public TypeArc getTypeArcFromName(String nom)
    {
        TypeArc result= null;
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor= db.query("TypeArc", new String[] {"idTypeArc", "NomType" }, "NomType="+nom, null, null, null, null);
        Cursor cursor = db.rawQuery("SELECT * FROM TypeArc WHERE NomType ='" + nom +"';", null);
        if (cursor.moveToFirst())
        {
            result= new TypeArc(cursor.getInt(0),cursor.getString(1));
        }

        return result;
    }


    //---------------------------------------------------------------
    // Arc

    public void addArc(Arc arc)  {

        SQLiteDatabase db = this.getWritableDatabase();

        //Remplir la requete
        ContentValues values = new ContentValues();
        values.put("idUtilisateur", arc.getIdUtilisateur());
        values.put("NomArc", arc.getNomArc());
        values.put("idTypeArc", arc.getIdTypeArc());
        //Insert dans BDD
        db.insert("Arc", null, values);
        db.close();
    }

    public Arc getArc(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Arc", new String[] { "idUtilisateur", "idArc",
                        "NomArc", "idTypeArc" }, "idUtilisateur" + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Arc arc = new Arc(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getString(2),
                cursor.getInt(3));

        cursor.close();
        db.close();
        return arc;
    }

    public ArrayList<Integer> getListnbrArc() {
        ArrayList<Integer> result = new ArrayList<Integer>();

        String selectQuery= "SELECT idArc FROM Arc;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }
    public ArrayList<Integer> getListnbrArcByUser(int id) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        String selectQuery= "SELECT idArc FROM Arc where idUtilisateur ="+id+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public Arc getArcFromName(String nom)
    {
        String selectQuery = "SELECT * FROM Arc WHERE NomArc='"+nom+"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Arc arc = null;

        if(cursor.moveToFirst())
        {
            arc = new Arc(Integer.parseInt(cursor.getString(0)),
                    Integer.parseInt(cursor.getString(1)),
                    cursor.getString(2),
                    cursor.getInt(3));
        }
        cursor.close();
        db.close();
        return arc;
    }

    public List<Arc> getArcs() {
        List<Arc> arcsList = new ArrayList<Arc>();

        String selectQuery = "SELECT * FROM Arc;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Arc arc = new Arc(Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)));
                // Adding contact to list
                arcsList.add(arc);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return arcsList;
    }

    public List<Arc> getArcsByUser(int id) {
        List<Arc> arcsList = new ArrayList<Arc>();

        String selectQuery = "SELECT * FROM Arc WHERE idUtilisateur = "+id+";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Arc arc = new Arc(Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)));
                // Adding contact to list
                arcsList.add(arc);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return arcsList;
    }

    public String getNomArc(int id) {
        String selectQuery = "SELECT NomArc FROM Arc WHERE idArc='"+id+"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String nom = cursor.getString(0);
        return nom;
    }

    public List<String> getArcsForSpinner() {
        List<String> listArcs = new ArrayList<String>();

        for (Arc arc : getArcs()) {
            listArcs.add(arc.getNomArc());
        }

        return listArcs;
    }

    public int getArcsCounts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Arc;", null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public int getArcsCountByUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Arc WHERE idUtilisateur ='"+id+"';", null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public void updateArc(Arc arc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NomArc", arc.getNomArc());
        values.put("idUtilisateur", arc.getIdUtilisateur());
        values.put("idTypeArc", arc.getIdTypeArc());

        db.update("Arc", values, "idArc="+arc.getIdArc(), null);
        db.close();
    }

    public void deleteArc(int idArc) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("DELETE FROM Graduation WHERE idArc in (SELECT idGraduation FROM Graduation WHERE idArc="+idArc+");", null);
        db.delete("Graduation", "idArc="+idArc, null);
        db.delete("Arc", "idArc="+idArc, null);
        db.close();
    }

    //GRADUATION----------------------------------------------------------
    public void addGraduation(Graduation grad) {

        SQLiteDatabase db = this.getWritableDatabase();
        //Remplir la requete
        ContentValues values = new ContentValues();
        values.put("idArc", grad.getIdArc());
        values.put("Distance", grad.getDistance());
        values.put("Remarque", grad.getRemarque());
        values.put("Horizontal", grad.getHorizontal());
        values.put("Vertical", grad.getVertical());
        values.put("Profondeur", grad.getProfondeur());
        //Insert dans BDD
        db.insert("Graduation", null, values);
        db.close();
    }

    public Graduation getGraduation(int idGraduation) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Graduation", new String[] { "idGraduation", "idArc", "Distance", "Remarque", "Horizontal", "Vertical", "Profondeur" }, "idGraduation" + "=?",
                new String[] { String.valueOf(idGraduation) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Graduation grad = new Graduation(
                cursor.getInt(0),
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getString(3),
                cursor.getFloat(4),
                cursor.getFloat(5),
                cursor.getFloat(6));
        cursor.close();
        db.close();
        return grad;
    }

    public Graduation getGraduationFromDistance(int distance)
    {
        String selectQuery = "SELECT * FROM Graduation WHERE Distance='"+distance+"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Graduation grad = null;

        if(cursor.moveToFirst()) {
            grad = new Graduation(Integer.parseInt(cursor.getString(0)),
                    Integer.parseInt(cursor.getString(1)),
                    Integer.parseInt(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getFloat(4),
                    cursor.getFloat(5),
                    cursor.getFloat(6));

        }
        cursor.close();
        db.close();
        return grad;
    }

    public List<Graduation> getGraduation() {
        List<Graduation> graduationList = new ArrayList<Graduation>();

        String selectQuery = "SELECT * FROM Graduation;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Graduation grad = new Graduation(Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3),
                        cursor.getFloat(4),
                        cursor.getFloat(5),
                        cursor.getFloat(6));
                // Adding contact to list
                graduationList.add(grad);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return graduationList;
    }
    public List<Graduation> getListReglageByArc(int id) {
        List<Graduation> result = new ArrayList<Graduation>();
        String selectQuery= "SELECT * FROM Graduation where idArc ="+id+" ORDER BY Distance;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Graduation grad = new Graduation(Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        cursor.getString(3),
                        cursor.getFloat(4),
                        cursor.getFloat(5),
                        cursor.getFloat(6));
                // Adding contact to list
                result.add(grad);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    /* List<String> getTypeArcForSpinner() {
        List<String> listtypearc = new ArrayList<String>();

        for (TypeArc typearc : getTypeArc()) {
            listtypearc.add(typearc.getNomType());
        }

        return listtypearc;
    }

    public int getTypeArcCounts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TypeArc;", null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }*/

    public void updateGraduation(Graduation grad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idArc", grad.getIdArc());
        values.put("Distance", grad.getDistance());
        values.put("Remarque", grad.getRemarque());
        values.put("Horizontal", grad.getHorizontal());
        values.put("Vertical", grad.getVertical());
        values.put("Profondeur", grad.getProfondeur());

        db.update("Graduation", values, "idGraduation="+grad.getIdGraduation(), null);
        db.close();
    }

    public void deleteGraduation(int idGrad) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("DELETE FROM Graduation WHERE idGraduation="+idGrad+";", null);
        db.delete("Graduation", "idGraduation="+idGrad, null);
        db.close();
    }


	//---------------------------------------------------------------
	
	public static String getDateString(Date date, Context context) {
		long when = date.getTime();
        int flags = 0;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
        flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

        return android.text.format.DateUtils.formatDateTime(context,
        	when, flags);
	}

	public void reset() {
		dropTables();
		onCreate(getWritableDatabase());
	}

	private void dropTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL("DROP TABLE IF EXISTS Utilisateur;");
		db.execSQL("DROP TABLE IF EXISTS Grade;");
		db.execSQL("DROP TABLE IF EXISTS Cible;");
		db.execSQL("DROP TABLE IF EXISTS Tirer;");
		db.execSQL("DROP TABLE IF EXISTS Partie;");
		db.execSQL("DROP TABLE IF EXISTS Diametre;");
        db.execSQL("DROP TABLE IF EXISTS TypeArc;");
        db.execSQL("DROP TABLE IF EXISTS Arc;");
        db.execSQL("DROP TABLE IF EXISTS Graduation;");
		
		db.close();
	}
}
