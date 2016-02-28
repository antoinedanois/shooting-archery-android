package com.iutbmteprow.shootingarchery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.iutbmteprow.shootingarchery.dbman.Campagne;
import com.iutbmteprow.shootingarchery.dbman.DBHelper;

import java.util.Date;

public class Config3CampagneActivity extends Activity {
    boolean isEntrainement = false;
    private RadioButton piquet1;
    private RadioButton piquet2;
    private RadioButton piquet3;
    private RadioButton cible12;
    private RadioButton cible16;
    private RadioButton cible20;
    private RadioButton cible24;
    private EditText num1erecible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config3campagne);

        SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
        if (preferences.getBoolean("RadioEntrainement", true)) {
            isEntrainement = true;
            ((View) findViewById(R.id.config3Campagnelayout_libel_nbr_cible)).setVisibility(View.GONE);
            //((View) findViewById(R.id.config3Campagnelayout_nbr_cible)).setVisibility(View.GONE);
            ((View) findViewById(R.id.config3Campagnelayout_num_cible)).setVisibility(View.GONE);
        }

        //DECLARATION
        num1erecible = (EditText) findViewById(R.id.config3Campagne_editnum1erecible);
        piquet1 = (RadioButton) findViewById(R.id.config3Campagne_radiopiquet1);
        piquet2 = (RadioButton) findViewById(R.id.config3Campagne_radiopiquet2);
        piquet3 = (RadioButton) findViewById(R.id.config3Campagne_radiopiquet3);
        cible12 = (RadioButton) findViewById(R.id.config3Campagne_radio12);
        cible16 = (RadioButton) findViewById(R.id.config3Campagne_radio16);
        cible20 = (RadioButton) findViewById(R.id.config3Campagne_radio20);
        cible24 = (RadioButton) findViewById(R.id.config3Campagne_radio24);
        //FIN DECLARATION

        fillAttributes();
    }

    private void fillAttributes() {
        SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);

        //lecture choix piquet
        if (preferences.getBoolean("RadioPiquet1", false))
            piquet1.setChecked(true);
        if (preferences.getBoolean("RadioPiquet2", false))
            piquet2.setChecked(true);
        if (preferences.getBoolean("RadioPiquet3", false))
            piquet3.setChecked(true);
        //fin lecture choix piquet

        //lecture choix nombre de cibles
        if (preferences.getBoolean("RadioCible12", false))
            cible12.setChecked(true);
        if (preferences.getBoolean("RadioCible16", false))
            cible16.setChecked(true);
        if (preferences.getBoolean("RadioCible20", false))
            cible20.setChecked(true);
        if (preferences.getBoolean("RadioCible24", false))
            cible24.setChecked(true);
        //fin lecture choix nombre de cible


        //lecture distance
        String Numero1erecible = preferences.getString("num1erecible", "");
        num1erecible.setText(Numero1erecible);
        //fin lecture distance


    }

    private void savePreferences() {
        //code pour garder les valeurs en m�moire
        SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        //choix piquet
        editor.putBoolean("RadioPiquet1", piquet1.isChecked());
        editor.putBoolean("RadioPiquet2", piquet2.isChecked());
        editor.putBoolean("RadioPiquet3", piquet3.isChecked());
        //Fin choix piquet

        //choix nombre de cibles
        if (cible12.isChecked()) {
            editor.putInt("nbCibles", 12);
        } else if (cible16.isChecked()) {
            editor.putInt("nbCibles", 16);
        } else if (cible20.isChecked()) {
            editor.putInt("nbCibles", 20);
        } else if (cible24.isChecked()) {
            editor.putInt("nbCibles", 24);
        } else {
            editor.putInt("nbCibles", 1);
        }
//        editor.putBoolean("RadioCible12", cible12.isChecked());
//        editor.putBoolean("RadioCible24",cible24.isChecked());
        //fin choix nombre de cibles
        editor.putInt("currentVolee", 1);
        //choix numéro 1ère cible
        if (preferences.getBoolean("RadioCompetition", false)) {
            int currentcible = Integer.parseInt(num1erecible.getText().toString());
            editor.putInt("firstCible", currentcible);
        }
        //fin choix


        editor.commit();
        savePartie();
    }

    private void savePartie() {
        SharedPreferences sp = getSharedPreferences("partie", Context.MODE_PRIVATE);
        int idCampagne = new DBHelper(this).addCampagne(new Campagne(0, //idPartie
                sp.getInt("idUtilisateur", 0), //idUtilisateur
                false, //partieFinie
                new Date(), //datePartie
                sp.getInt("nbCibles", 0), //nbCibles
                sp.getBoolean("RadioCompetition", false),//competition
                sp.getString("NomArc", "test")) //nom arc
        );
        sp.edit().putInt("idCampagne", idCampagne).commit();
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
                if (!checkForm()) {
                    return true;
                } else {
                    savePreferences();
                    Intent intent = new Intent(this, InGameActivityCampagne.class);
                    startActivity(intent);
                    return true;
                }
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkForm() {
        int nbCibles = 0;
        if (cible12.isChecked()) {
            nbCibles = 12;
        } else if (cible16.isChecked()) {
            nbCibles = 16;
        } else if (cible20.isChecked()) {
            nbCibles = 20;
        } else if (cible24.isChecked()) {
            nbCibles = 24;
        }
        if (!piquet1.isChecked() && !piquet2.isChecked() && !piquet3.isChecked()) {
            makeDialog(getString(R.string.erreur), getString(R.string.piquet_error));
            return false;
        } else if (!cible24.isChecked() && !cible12.isChecked() && !cible20.isChecked() && !cible16.isChecked() && !isEntrainement) {
            makeDialog(getString(R.string.erreur), getString(R.string.nbcible_error));
            return false;
        } else if (num1erecible.getText().toString().equals("") && !isEntrainement) {
            makeDialog(getString(R.string.erreur), getString(R.string.numcible_error) + " " + nbCibles);
            return false;
        } else if (!isEntrainement && (Integer.parseInt(num1erecible.getText().toString()) < 1 || Integer.parseInt(num1erecible.getText().toString()) > nbCibles)) {
            makeDialog(getString(R.string.erreur), getString(R.string.numcible_error) + " " + nbCibles);
            return false;
        } else {
            return true;
        }
    }

    private void makeDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.error));
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.warning_dark);
        alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Add your code for the button here.
            }
        });
        // Set the Icon for the Dialog
        alertDialog.setIcon(R.drawable.warning_dark);
        alertDialog.show();
    }

}
