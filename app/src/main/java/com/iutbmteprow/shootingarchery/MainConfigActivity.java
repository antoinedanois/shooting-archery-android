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
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainConfigActivity extends Activity {

    Spinner typeTirSpinner;
    private RadioButton competition;
    private RadioButton entrainement;
    private RadioButton interieurRadio;
    private RadioButton exterieurRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_config);

        // Show the Up button in the action bar.
        setupActionBar();
        loadAttributes();
        readPreferences();
    }

    private void loadAttributes(){
        typeTirSpinner=(Spinner)findViewById(R.id.typeTirSpinner);
        competition = (RadioButton) findViewById(R.id.config2_radioCompetition);
        entrainement = (RadioButton) findViewById(R.id.config2_radioEntrainement);
        interieurRadio = (RadioButton) findViewById(R.id.config3_radioInterieur);
        exterieurRadio = (RadioButton) findViewById(R.id.config3_radioExterieur);

        setupSpinner();
    }

    private void readPreferences() {
        SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);

        //lecture choix condition
        if(preferences.getBoolean("RadioEntrainement", false))
            entrainement.setChecked(true);
        if(preferences.getBoolean("RadioCompetition", false))
            competition.setChecked(true);

        //lecture choix interieur/exterieur
        if(preferences.getBoolean("RadioInterieur", false))
            interieurRadio.setChecked(true);
        if(preferences.getBoolean("RadioExterieur", false))
            exterieurRadio.setChecked(true);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getSpinnerElements());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeTirSpinner.setAdapter(adapter);
    }

    private List<String> getSpinnerElements(){
        List<String> players=new ArrayList<>();
        players.add("FITA");
        players.add("Campagne");

        return players;
    }

    private void setupActionBar()
    {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_next:
                //Erreur de saisie ?
                if (!checkInputs())
                    return true;

                if (typeTirSpinner.getSelectedItem().toString().equals("Campagne")){
                    savePreferences();
                    Intent intent = new Intent(this, Config3CampagneActivity.class);
                    startActivity(intent);
                }else{
                    savePreferences();
                    Intent intent = new Intent(this, Config3Activity.class);
                    startActivity(intent);
                }

                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkInputs(){
        if(!competition.isChecked() && !entrainement.isChecked()){
            makeAlert(getString(R.string.erreur),getString(R.string.conditions_error));
            return false;
        }

        if(!interieurRadio.isChecked() && !exterieurRadio.isChecked()){
            makeAlert(getString(R.string.erreur), getString(R.string.int_ext_error));
            return false;
        }

        return true;
    }

    private void savePreferences() {
        SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //choix condition
        editor.putBoolean("RadioEntrainement", entrainement.isChecked());
        editor.putBoolean("RadioCompetition", competition.isChecked());

        //choix int?rieur/exterieur
        editor.putBoolean("RadioInterieur", interieurRadio.isChecked());
        editor.putBoolean("RadioExterieur", exterieurRadio.isChecked());
    }

    private void makeAlert(String title,String message)
    {
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
}
