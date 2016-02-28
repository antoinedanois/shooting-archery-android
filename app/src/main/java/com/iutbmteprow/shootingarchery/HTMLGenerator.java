package com.iutbmteprow.shootingarchery;

import android.app.Activity;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.Partie;
import com.iutbmteprow.shootingarchery.dbman.Tirer;
import com.iutbmteprow.shootingarchery.dbman.Utilisateur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class HTMLGenerator {

    private Partie partie;
    private DBHelper db;
    private Activity sender;
    private String output;

    private int totalPartie;
    private int nb10;
    private int nb9;
    private boolean noCalcul = false;
    private boolean noCumul10;
    private String template;

    public HTMLGenerator(int idPartie, Activity sender) {
        db = new DBHelper(sender);
        partie = db.getPartie(idPartie);
        this.sender = sender;
        output = new String();
        noCumul10 = PreferenceManager.getDefaultSharedPreferences(sender).getString("col_1010p", "per_row").equals("per_row");
    }

    public void makeHTML() {
        try {
            template = readFullAsset("html_template");
        } catch (IOException e) {
            return;
        }

        StringBuilder exportContent = new StringBuilder();
        exportContent.append(makeTop());
        exportContent.append(makeManches());
        exportContent.append(makeBottom());

        output = exportContent.toString();
    }

    private String makeBottom() {
        String section = grabSection("Fin_Jeu");
        section = section.replace("%%TOTAL_GAME%%", String.valueOf(totalPartie));
        return section;
    }

    private String makeManches() {
        //Préparation des patrons
        String tabletop = grabSection("Debut_Manche");
        tabletop = tabletop.replace("%%NBFLECHES%%", String.valueOf(partie.getNbFleches()));
        tabletop = dropItem(tabletop, "WHEN_6", partie.getNbFleches() != 6);

        String tablerow = grabSection("Ligne_Manche");
        tablerow = dropItem(tablerow, "WHEN_6", partie.getNbFleches() != 6);

        String tablebottom = grabSection("Manche_Fin");
        tablebottom = dropItem(tablebottom, "WHEN_COMPETITION", !partie.isCompetition());

        //Remplissage des patrons
        StringBuilder manches = new StringBuilder();
        for (int noManche = 1; noManche <= partie.getNbManches(); noManche++) {
            //Header du tableau
            String manchetop = tabletop;
            manchetop = manchetop.replace("%%NO_MANCHE%%", String.valueOf(noManche));
            manches.append(manchetop);

            int totalManche = 0;
            //Rows du tableau
            for (int noVolee = 1; noVolee <= partie.getNbVolees(); noVolee++) {
                int totalVolee = 0;
                String tableVolee = tablerow;
                tableVolee = tableVolee.replace("%%NO_VOLEE%%", String.valueOf(noVolee));
                ArrayList<Tirer> tirs = db.getTirerOf(partie.getIdPartie(), noManche, noVolee);

                if (noCumul10) {
                    nb10 = 0;
                    nb9 = 0;
                }

                for (int noFleche = 1; noFleche <= partie.getNbFleches(); noFleche++) {
                    try {
                        int score = tirs.get(noFleche - 1).getScore();
                        if (score == 9) {
                            nb9++;
                        } else if (score == 10) {
                            nb10++;
                        }

                        totalPartie += getRealScore(score);
                        totalVolee += getRealScore(score);
                        totalManche += getRealScore(score);
                        tableVolee = tableVolee.replace("%%SCORE_" + noFleche + "%%", getScoreString(score));
                    } catch (IndexOutOfBoundsException e) {
                        tableVolee = tableVolee.replace("%%SCORE_" + noFleche + "%%", "-");
                        noCalcul = true;
                    }
                }

                if (noCalcul) {
                    tableVolee = tableVolee.replace("%%MOY%%", "?");
                } else {
                    tableVolee = tableVolee.replace("%%MOY%%", String.format("%.2f", (float) totalVolee / partie.getNbFleches()));
                }


                tableVolee = tableVolee.replace("%%TOTAL_VOLEE%%", String.valueOf(totalVolee));
                tableVolee = tableVolee.replace("%%TOTAL_CUMULE%%", String.valueOf(totalPartie));
                tableVolee = tableVolee.replace("%%NB10%%", String.valueOf(nb10));
                tableVolee = tableVolee.replace("%%NB10p%%", String.valueOf(nb9));
                manches.append(tableVolee);
            }

            //Footer du tableau
            String mancheBottom = tablebottom;
            mancheBottom = mancheBottom.replace("%%TOTAL_MANCHE%%", String.valueOf(totalManche));
            if (!noCalcul) {
                mancheBottom = mancheBottom.replace("%%VOLEE_MOYENNE%%", String.format("%.2f", (float) totalManche / partie.getNbVolees()));
                mancheBottom = mancheBottom.replace("%%FLECHE_MOYENNE%%", String.format("%.2f", (float) totalManche / (partie.getNbVolees() * partie.getNbFleches())));
            } else {
                mancheBottom = mancheBottom.replace("%%VOLEE_MOYENNE%%", "?");
                mancheBottom = mancheBottom.replace("%%FLECHE_MOYENNE%%", "?");
            }
            manches.append(mancheBottom);
        }


        return manches.toString();
    }

    private String makeTop() {
        String section = grabSection("Header");

        section = section.replace("%%APP_NAME%%", sender.getText(R.string.app_name));

        Utilisateur user = db.getUtilisateur(partie.getIdUtilisateur());
        section = section.replace("%%GAME_PLAYER_NAME%%", user.getNom() + " " + user.getPrenom());

        section = section.replace("%%GAME_DATE%%", DBHelper.getDateString(partie.getDatePartie(), sender));
        section = section.replace("%%GAME_TYPE%%", (partie.isCompetition() ? "Compétition" : "Entrainement"));
        section = section.replace("%%GAME_LOCATION%%", (partie.isExterieur() ? "extérieur" : "intérieur"));
        section = section.replace("%%GAME_TYPE_CIBLE%%", (partie.getIdCible() == 1 ? "Blason" : "Trispot"));
        section = section.replace("%%GAME_DISTANCE%%", String.valueOf(partie.getDistanceCible()));
        section = section.replace("%%GAME_TARGET_SIZE%%", String.valueOf(db.getDiametreFromId(partie.getIdDiametre()).getDiametre()));

        return section;
    }

    private String grabSection(String sectionName) {
        String ret;
        String beginString = "<%%BEGIN(" + sectionName + ")%%>";
        String endString = "<%%END%%>";
        int beginIndex = template.indexOf(beginString);
        int endIndex = template.indexOf(endString, beginIndex);
        ret = template.substring(beginIndex + beginString.length(), endIndex);
        return ret;
    }

    private String dropItem(String source, String itemName, boolean withContent) {
        String ret = source;
        if (withContent) {
            int begin = ret.indexOf("<%%" + itemName + "%%>");
            int end = ret.indexOf("<%%END" + itemName + "%%>", begin) + ("<%%END" + itemName + "%%>").length();
            try {
                ret = ret.substring(0, begin) + ret.substring(end, ret.length());
            } catch (IndexOutOfBoundsException e) {
                return ret;
            }
        } else {
            ret = ret.replace("<%%" + itemName + "%%>", "");
            ret = ret.replace("<%%END" + itemName + "%%>", "");
        }
        return ret;
    }

    private String readFullAsset(String assetName) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(sender.getAssets().open(assetName)));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    private String getScoreString(int score) {
        if (score == 0) {
            return "M";
        } else if (score == 10) {
            return "10";
        } else if (score == 9) {
            return "9";
        } else {
            return String.valueOf(score);
        }
    }

    private int getRealScore(int score) {
        return score == 9 ? 10 : score;
    }

    public String getHTML() {
        return output;
    }

    public Uri saveHTML() {
        File root = android.os.Environment.getExternalStorageDirectory();

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File(root.getAbsolutePath() + "/shooting_archery");
        dir.mkdirs();
        File file = new File(dir, partie.getDatePartie().getTime() + ".html");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(getHTML());
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }

}
