package com.iutbmteprow.shootingarchery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.Diametre;
import com.iutbmteprow.shootingarchery.dbman.Partie;

import java.util.ArrayList;
import java.util.Date;

public class IndividualConfigActivity extends Activity {
    RadioButton classiqueRadio = null;
    RadioButton trispotRadio = null;
    private SeekBar volees = null;
    private TextView textProgressVolees;
    int progressChanged = 0;
    int progressChangedVolees = 0;
    private RadioButton fleche3;
    private RadioButton fleche6;
    private SeekBar manches = null;
    private TextView textProgressManches;
    int progressChangedManches = 0;
    private ImageView lastSeekBarVolee;
    private ImageView nextSeekBarVolee;
    boolean isEntrainement = false;
    int lastPlayer;
    int nPlayers;
    private ArrayList<Diametre> listDiametre;
    private Spinner spinner;
    private LinearLayout contentTaille;
    private DBHelper db;
    private TextView txtActualPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_config);

        setupActionBar();
        loadAttributes();
        readPreferences();
    }

    private void loadAttributes() {
        SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
        if (preferences.getBoolean("RadioEntrainement", true)) {
            isEntrainement = true;
            ((View) findViewById(R.id.config3_manches_layout)).setVisibility(View.GONE);
            ((View) findViewById(R.id.config3_volees_layout)).setVisibility(View.GONE);
        }

        db = new DBHelper(this);
        contentTaille = (LinearLayout) findViewById(R.id.contentTaille);
        spinner = (Spinner) findViewById(R.id.config2_spinnertaille);

        CompoundButton.OnCheckedChangeListener occl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    return;
                updateDiametres((((View) buttonView).getId() == R.id.config2_radioCibleBlason));
                switch (buttonView.getId()) {
                    case R.id.config2_radioCibleBlason: {
                        contentTaille.setVisibility(View.VISIBLE);
                        break;
                    }

                    case R.id.config2_radioCibleTrispot: {
                        contentTaille.setVisibility(View.VISIBLE);
                        break;
                    }

                }
            }
        };

        classiqueRadio = (RadioButton) findViewById(R.id.config2_radioCibleBlason);
        trispotRadio = (RadioButton) findViewById(R.id.config2_radioCibleTrispot);
        classiqueRadio.setOnCheckedChangeListener(occl);
        trispotRadio.setOnCheckedChangeListener(occl);

        volees = (SeekBar) findViewById(R.id.config3_seekNbVolees);
        textProgressVolees = (TextView) findViewById(R.id.config3_progressVolees);

        manches = (SeekBar) findViewById(R.id.config3_seekNbManches);
        textProgressManches = (TextView) findViewById(R.id.config3_progressManches);

        fleche3 = (RadioButton) findViewById(R.id.config3_radio3fleches);
        fleche6 = (RadioButton) findViewById(R.id.config3_radio6fleches);
        lastSeekBarVolee = (ImageView) findViewById(R.id.config3_prec_volee);
        nextSeekBarVolee = (ImageView) findViewById(R.id.config3_suiv_volee);

        txtActualPlayer = (TextView) findViewById(R.id.txtJoueurCourante);

        setupSeekBar();

    }

    private void updateDiametres(boolean isBlason) {
        listDiametre = null;
        listDiametre = db.getDiametres((isBlason) ? 1 : 2);
        ArrayList<String> listStringDiametre = new ArrayList<String>();
        for (Diametre d : listDiametre) {
            listStringDiametre.add(String.valueOf(d.getDiametre()) + " cm");
        }

        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listStringDiametre);
        spinner.setAdapter(adapt);
    }

    private void setupSeekBar() {
        //SEEKBAR VOLEES
        volees.setMax(50);
        //lastSeekBarVolee button
        lastSeekBarVolee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                volees.setProgress(volees.getProgress() - 1);

            }
        });
        //nextSeekBarVolee button
        nextSeekBarVolee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                volees.setProgress(volees.getProgress() + 1);

            }
        });

        /*
         * SEEKBAR DES VOLEES
		 */
        volees.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String intProgressChangedVolee = String.valueOf(progressChangedVolees);
                textProgressVolees.setText(intProgressChangedVolee);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar volees, int progress, boolean fromUser) {
                progressChangedVolees = progress;
                String intProgressChangedVolee = String.valueOf(progressChangedVolees);
                textProgressVolees.setText(intProgressChangedVolee);

            }
        });
        /*
         * FIN SEEKBAR DES VOLEES
		 */

        /*
		 * SEEKBAR DES MANCHES
		 */
        manches.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String intProgressChangedManches = String.valueOf(progressChangedManches);
                textProgressManches.setText(intProgressChangedManches);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar manches, int progress,
                                          boolean fromUser) {
                progressChangedManches = progress;

            }
        });
		/*
		 * FIN SEEKBAR DES MANCHES
		 */
    }

    private void readPreferences() {
        SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);

        nPlayers = preferences.getInt("nPlayers", 0);
        lastPlayer = preferences.getInt("lastPlayer", 0);

        txtActualPlayer.setText((lastPlayer + 1) + " de " + nPlayers);

        //lecture choix nombre de fleches
        if (preferences.getBoolean("RadioFleche3", false))
            fleche3.setChecked(true);
        if (preferences.getBoolean("RadioFleche6", false))
            fleche6.setChecked(true);
        //fin lecture choix nombre de fl�che

        if (preferences.getBoolean("RadioCibleBlason", false)) {
            trispotRadio.setChecked(true);
        }
        if (preferences.getBoolean("RadioCibleTrispot", false)) {
            trispotRadio.setChecked(true);
        }

        //lecture vol�es
        String TextVolee = preferences.getString("textProgressVolee", "0");
        textProgressVolees.setText(TextVolee);
        int SeekBarVolee = preferences.getInt("progressVolee", 0);
        volees.setProgress(SeekBarVolee);
        //fin lecture vol�es

        //lecture manches
        String TextManche = preferences.getString("textProgressManches", "0");
        textProgressManches.setText(TextManche);
        int SeekBarManche = preferences.getInt("progressManche", 0);
        manches.setProgress(SeekBarManche);
        //fin lecture manches
    }

    private boolean checkInputs() {
        if (progressChangedVolees == 0 && !isEntrainement) {
            makeAlert(getString(R.string.erreur), getString(R.string.volees_error));
            return false;
        }
        if (progressChangedManches == 0 && !isEntrainement) {
            makeAlert(getString(R.string.erreur), getString(R.string.manches_error));
            return false;
        }
        if (!fleche3.isChecked() && !fleche6.isChecked()) {
            makeAlert(getString(R.string.erreur), getString(R.string.nbfleche_error));
            return false;
        }

        return true;
    }

    private void makeAlert(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.error));
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.warning_dark);
        alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Nothing here
            }
        });
        alertDialog.show();
    }

    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                //Erreur de saisie ?
                if (!checkInputs())
                    return true;

                savePreferences();
                if (lastPlayer == nPlayers) {
                    Intent intent = new Intent(this, InGameMultiActivity.class);
                    startActivity(intent);
                } else if (lastPlayer < nPlayers) {
                    Intent intent = new Intent(this, IndividualConfigActivity.class);
                    startActivity(intent);
                }

                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePreferences() {
        SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        lastPlayer += 1;
        editor.putInt("lastPlayer", lastPlayer);

        //Taille Cible
        editor.putInt("p" + lastPlayer + "idDiametre", listDiametre.get(spinner.getSelectedItemPosition()).getIdDiametre());

        editor.putInt("p" + lastPlayer + "currentManche", 1);
        editor.putInt("p" + lastPlayer + "currentVolee", 1);
        Log.e("dev", "player: " + lastPlayer + " set.");

        //Choix cible
        editor.putBoolean("p" + lastPlayer + "ImageClassique", classiqueRadio.isChecked());
        editor.putBoolean("p" + lastPlayer + "ImageTrispot", trispotRadio.isChecked());

        editor.putInt("p" + lastPlayer + "currentManche", 1);
        editor.putInt("p" + lastPlayer + "currentVolee", 1);

        //choix nombre de fl�ches
        editor.putBoolean("p" + lastPlayer + "RadioFleche3", fleche3.isChecked());
        editor.putBoolean("p" + lastPlayer + "RadioFleche6", fleche6.isChecked());
        //fin choix nombre de fl�che

        //choix nombre vol�es
        editor.putString("p" + lastPlayer + "textProgressVolee", textProgressVolees.getText().toString());
        int progressVolee = volees.getProgress();
        editor.putInt("p" + lastPlayer + "progressVolee", (isEntrainement) ? 1 : progressVolee);
        //fin choix nombre vol�es

        //choix nombre manches
        editor.putString("p" + lastPlayer + "textProgressManches", textProgressManches.getText().toString());
        int progressManche = manches.getProgress();
        editor.putInt("p" + lastPlayer + "progressManche", (isEntrainement) ? 1 : progressManche);
        //fin choix nombre de manches.

        editor.commit();
        savePartie();
    }

    private void savePartie() {
        SharedPreferences sp = getSharedPreferences("partie", Context.MODE_PRIVATE);
        String joueurs = "";
        if (sp.getInt("nPlayers", 1) > 1) {
            for (int i = 0; i < sp.getInt("nPlayers", 1); i++) {
                joueurs += sp.getInt("idUtilisateur" + i, 1) + ",";
            }
            joueurs = joueurs.substring(0, joueurs.length() - 1);
        }

        int idPartie = new DBHelper(this).addPartie(new Partie(0, //idPartie
                sp.getInt("idUtilisateur" + (lastPlayer - 1), 0), //idUtilisateur
                false, //partieFinie
                sp.getBoolean("p" + lastPlayer + "ImageClassique", true) ? 1 : 2, //idCible
                sp.getInt("progressDistance", 0), //distanceCible
                new Date(), //datePartie
                sp.getInt("p" + lastPlayer + "progressManche", 0), //nbManches
                sp.getInt("p" + lastPlayer + "progressVolee", 0), //nbVolees
                (sp.getBoolean("p" + lastPlayer + "RadioFleche3", true)) ? 3 : 6, //nbFleches
                sp.getBoolean("RadioCompetition", false),//competition
                sp.getBoolean("RadioExterieur", true),//exterieur
                sp.getInt("p" + lastPlayer + "idDiametre", 0),
                sp.getString("p" + lastPlayer + "NomArc", "test"),
                joueurs
        ) //nom arc
        );
        sp.edit().putInt("p" + lastPlayer + "idPartie", idPartie).commit();
        Log.e("p" + lastPlayer + "idPartie", "" + idPartie);
    }
}
