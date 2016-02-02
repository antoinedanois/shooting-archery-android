package com.iutbmteprow.shootingarchery;

import java.util.ArrayList;
import java.util.List;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.Partie;
import com.iutbmteprow.shootingarchery.dbman.Tirer;
import com.iutbmteprow.shootingarchery.dbman.Utilisateur;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScoreTableFragmentv2 extends Fragment {

    View topView;
    List<List<View>> manches;
    View bottomView;
    DBHelper db;

    Partie partie;

    boolean ready = false;
    private boolean noCumul10;

    int currentPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        noCumul10 = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("col_1010p", "per_row").equals("per_row");

        // Inflate the layout for this fragment
        LinearLayout result = new LinearLayout(getActivity());
        result.setOrientation(LinearLayout.VERTICAL);

        Bundle arguments = getArguments();
        int idPartie = arguments.getInt("p1idPartie");

        db = new DBHelper(getActivity());
        partie = db.getPartie(idPartie);

        manches = new ArrayList<List<View>>();

        makeTopView(inflater, container, result);
        makeManches(inflater, container, result);
        if (partie.isCompetition())
            makeBottomView(inflater, container, result);

        //Eviter quelques exceptions inutiles
        ready = true;
        return result;
    }

    private void makeManches(LayoutInflater inflater, ViewGroup container, LinearLayout result) {
        for (int i=1;i<=partie.getNbManches();i++) {
            makeManche(i,inflater,container,result);
        }
    }

    private void makeManche(int idManche, LayoutInflater inflater,
                            ViewGroup container, LinearLayout result) {
        ArrayList<View> views = new ArrayList<View>();

        HorizontalScrollView hsv = new HorizontalScrollView(getActivity());
        LinearLayout hsv_layout = new LinearLayout(getActivity());
        hsv_layout.setOrientation(LinearLayout.VERTICAL);
        hsv.addView(hsv_layout);

        //Définition de la taille du tableau avec un "filler" de taille minimale
        View constraint = new View(getActivity());
        constraint.setMinimumWidth(computeMinimumWidth());
        hsv_layout.addView(constraint);

        if (partie.isCompetition()) {
            View top_manche = inflater.inflate(R.layout.fragment_manchetop, container,false);
            ((TextView) top_manche.findViewById(R.id.tabtop2_manche)).setText(String.valueOf(idManche));
            result.addView(top_manche);
        }

        View ligne = inflater.inflate(R.layout.fragment_manchetop2, container, false);
        if (partie.getNbFleches() == 3) {
            ((TextView) ligne.findViewById(R.id.tabtop2_f4)).setVisibility(View.GONE);
            ((TextView) ligne.findViewById(R.id.tabtop2_f5)).setVisibility(View.GONE);
            ((TextView) ligne.findViewById(R.id.tabtop2_f6)).setVisibility(View.GONE);
        }
        if(partie.isExterieur()){
            ((TextView) ligne.findViewById(R.id.textView1)).setText(R.string.nombre_10p);
            ((TextView) ligne.findViewById(R.id.textView2)).setText(R.string.nombre_10);
        }

        hsv_layout.addView(ligne);

        for (int i=1;i<=partie.getNbVolees();i++) {
            View ligneTab = inflater.inflate(R.layout.fragment_mancherow, container, false);
            ((TextView) ligneTab.findViewById(R.id.tabmid_nomanche)).setText(String.valueOf(i));
            if (i%2 == 0) {
                ligneTab.setBackgroundColor(Color.LTGRAY);
            }

            if (partie.getNbFleches() == 3) {
                ((TextView) ligneTab.findViewById(R.id.tabmid_f4)).setVisibility(View.GONE);
                ((TextView) ligneTab.findViewById(R.id.tabmid_f5)).setVisibility(View.GONE);
                ((TextView) ligneTab.findViewById(R.id.tabmid_f6)).setVisibility(View.GONE);
            }
            hsv_layout.addView(ligneTab);
            views.add(ligneTab);
        }

        result.addView(hsv);

        View ligneTot = inflater.inflate(R.layout.fragment_manchebottom, container,false);
        if (!partie.isCompetition()) {
            ((TextView) ligneTot.findViewById(R.id.tabbot_string)).setText(R.string.total_entrainement);
        }
        if (partie.isExterieur()){
            ((TextView) ligneTot.findViewById(R.id.nombre_cumul)).setText(R.string.nb_11_total);
        }
        result.addView(ligneTot);
        views.add(ligneTot);

        manches.add(views);
    }

    private void makeBottomView(LayoutInflater inflater, ViewGroup container, LinearLayout result) {
        bottomView = inflater.inflate(R.layout.fragment_gamebottom, container, false);
        result.addView(bottomView);
    }

    private void makeTopView(LayoutInflater inflater, ViewGroup container, LinearLayout result) {
        topView = inflater.inflate(R.layout.fragment_gametop, container, false);

        Utilisateur user = db.getUtilisateur(partie.getIdUtilisateur());
        String nomUtilisateur = user.getNom() + " " + user.getPrenom();
        ((TextView) topView.findViewById(R.id.tabtop_username)).setText(nomUtilisateur);

        //Remplissage de l'entete cache
        ((TextView) topView.findViewById(R.id.tabtop_comptEnt)).setText(partie.isCompetition()?"Compétition":"Entraînement");
        ((TextView) topView.findViewById(R.id.tabtop_intExt)).setText((partie.isExterieur())?"extérieur":"intérieur");
        ((TextView) topView.findViewById(R.id.tabtop_distance)).setText(String.valueOf(partie.getDistanceCible()));
        ((TextView) topView.findViewById(R.id.tabtop_taillecible)).setText(String.valueOf(db.getDiametreFromId(partie.getIdDiametre()).getDiametre()));
        ((TextView) topView.findViewById(R.id.tabtop_typecible)).setText(db.getCible(partie.getIdCible()).getLibelle());

        long when = partie.getDatePartie().getTime();
        int flags = 0;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
        flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

        String finalDateTime = android.text.format.DateUtils.formatDateTime(getActivity(),
                when, flags);

        ((TextView) topView.findViewById(R.id.tabtop_date)).setText(finalDateTime);

        topView.findViewById(R.id.tabtop_maininfo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View extrainfo = topView.findViewById(R.id.tabtop_extrainfo);
                extrainfo.setVisibility((extrainfo.getVisibility() == View.VISIBLE)?View.GONE:View.VISIBLE);
                TextView expandtxt = (TextView) topView.findViewById(R.id.tabtop_expbtn);
                expandtxt.setText((extrainfo.getVisibility() == View.VISIBLE)?"-":"+");
            }
        });

        topView.findViewById(R.id.tabtop_extrainfo).setVisibility(View.GONE);
        result.addView(topView);
    }

    public void refresh() {
        if (!ready) {
            return;
        }

        ArrayList<Tirer> tirs = db.getTirerOf(partie.getIdPartie());
        int scoreTotal = 0;
        int scoreVolee = 0;
        int scoreManche = 0;

        int nb10p_total = 0;
        int nb10_total = 0;
        int nb9_total = 0;
        int nb10p = 0;
        int nb10 = 0;
        int nb9 = 0;

        for (int i=0;i<tirs.size();i++) {
            Tirer tirer = tirs.get(i);
            View ligne = manches.get(tirer.getNoManche() - 1).get(tirer.getNbVolee() - 1);
            TextView tx = (TextView) ligne.findViewById(getId(tirer.getOrderLancer()));
            tx.setText(getScoreString(tirer.getScore()));

            //Calcul des scores
            scoreVolee += getRealScore(tirer.getScore());
            scoreTotal += getRealScore(tirer.getScore());
            if (tirer.getScore() == 11){
                nb10p++;
            }else  if (tirer.getScore() == 10) {
                nb10++;
            } else if (tirer.getScore() == 9) {
                nb9++;
            }

            if (tirer.getOrderLancer() == partie.getNbFleches()) {
                ((TextView) ligne.findViewById(getId(7))).setText(String.valueOf(scoreVolee));
                scoreManche += scoreVolee;
                ((TextView) ligne.findViewById(getId(8))).setText(String.valueOf(scoreTotal));
                float currentMoyenne = (float) scoreVolee/partie.getNbFleches();
                ((TextView) ligne.findViewById(R.id.tabmid_moy)).setText(String.format("%.2f", currentMoyenne));
                if (partie.isExterieur()){
                    ((TextView) ligne.findViewById(R.id.tab_nb10)).setText(String.valueOf(nb10p));
                    ((TextView) ligne.findViewById(R.id.tab_nb9)).setText(String.valueOf(nb10));
                }else {
                    ((TextView) ligne.findViewById(R.id.tab_nb10)).setText(String.valueOf(nb10));
                    ((TextView) ligne.findViewById(R.id.tab_nb9)).setText(String.valueOf(nb9));
                }

                scoreVolee = 0;

                nb10p_total += nb10p;
                nb10_total += nb10;
                nb9_total += nb9;

                if (noCumul10) {
                    nb10p = 0;
                    nb10 = 0;
                    nb9 = 0;
                }

                if (tirer.getNbVolee() == partie.getNbVolees() || i == tirs.size() - 1) {

                    View ligneManche = manches.get(tirer.getNoManche() - 1).get(partie.getNbVolees());
                    TextView tx2 = (TextView) ligneManche.findViewById(getId(9));
                    tx2.setText(String.valueOf(scoreManche));

                    ((TextView) ligneManche.findViewById(R.id.tabbot_flemoy)).setText(String.format("%.2f",(float) scoreManche/(tirer.getNbVolee()*partie.getNbFleches())));
                    ((TextView) ligneManche.findViewById(R.id.tabbot_moy)).setText(String.format("%.2f",(float) scoreManche/tirer.getNbVolee()));
                    if (partie.isExterieur()){
                        ((TextView) ligneManche.findViewById(R.id.tabbot_9)).setText(String.valueOf(nb10p_total) + "/" + String.valueOf(nb10_total));
                    }else {
                        ((TextView) ligneManche.findViewById(R.id.tabbot_9)).setText(String.valueOf(nb10_total) + "/" + String.valueOf(nb9_total));
                    }

                    scoreManche = 0;
                    nb10p_total = 0;
                    nb10_total = 0;
                    nb9_total = 0;
                }

            }
        }

        //Score total compétition
        if (partie.isCompetition())
            ((TextView) bottomView.findViewById(getId(10))).setText(String.valueOf(scoreTotal));
    }

    private CharSequence getScoreString(int score) {
        if (score == 0) {
            return "M";
        } else if (score == 10) {
            return "10";
        }else if (score == 11) {
            return "10+";
        } else {
            return String.valueOf(score);
        }
    }

    private int getRealScore(int score) {
        return score==11?10:score;
    }

    private int getId(int noLancer) {
        switch (noLancer) {
            case 1 :
                return R.id.tabmid_f1;
            case 2 :
                return R.id.tabmid_f2;
            case 3 :
                return R.id.tabmid_f3;
            case 4 :
                return R.id.tabmid_f4;
            case 5 :
                return R.id.tabmid_f5;
            case 6 :
                return R.id.tabmid_f6;
            case 7 :
                return R.id.tabmid_sum1;
            case 8 :
                return R.id.tabmid_sum2;
            case 9 :
                return R.id.tabbot_sum;
            case 10:
                return R.id.tabbot2_sum;
            default:
                return -1;
        }
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public int computeMinimumWidth() {
        //Récuperer la largeur en pixel du viewport
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int horizontal_margin = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        //Retirer les marges pour obtenir la taille du viewport
        int viewport_width = (int) (metrics.widthPixels - horizontal_margin * 2 );

        if (isTablet(getActivity())) {
            return viewport_width;
        } else {
            //Calcul de la taille minimale en fonction de la masse des colonnes de "gauche"
            //Facteur = 1 / masse du conteneur
            return (int) (viewport_width * 1.66);
        }
    }

}
