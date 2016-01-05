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
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainConfigActivity extends Activity {

    private RadioButton competition;
    private RadioButton entrainement;
    private RadioButton interieurRadio;
    private RadioButton exterieurRadio;
    RadioButton classiqueRadio = null;
    RadioButton trispotRadio = null;
    RadioButton campagneRadio = null;
    private SeekBar distance = null;
    private TextView textProgressDistance;
    private ImageView lastSeekbar;
    private ImageView nextSeekbar;
    int progressChanged = 0;
    private LinearLayout distanceContainer;


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
        competition = (RadioButton) findViewById(R.id.config2_radioCompetition);
        entrainement = (RadioButton) findViewById(R.id.config2_radioEntrainement);
        interieurRadio = (RadioButton) findViewById(R.id.config3_radioInterieur);
        exterieurRadio = (RadioButton) findViewById(R.id.config3_radioExterieur);

        distanceContainer=(LinearLayout)findViewById(R.id.refresh_match_layout);

        CompoundButton.OnCheckedChangeListener occl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (!isChecked)
                    return;

                switch(buttonView.getId()) {
                    case R.id.config2_radioCibleBlason: {
                        distanceContainer.setVisibility(View.VISIBLE);
                        break;
                    }

                    case R.id.config2_radioCibleTrispot: {
                        distanceContainer.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.config2_radioCibleCampagne: {
                        distanceContainer.setVisibility(View.GONE);
                        break;
                    }
                }
            }
        };
        classiqueRadio = (RadioButton)findViewById(R.id.config2_radioCibleBlason);
        classiqueRadio.setOnCheckedChangeListener(occl);
        trispotRadio = (RadioButton)findViewById(R.id.config2_radioCibleTrispot);
        trispotRadio.setOnCheckedChangeListener(occl);
        campagneRadio = (RadioButton)findViewById(R.id.config2_radioCibleCampagne);
        campagneRadio.setOnCheckedChangeListener(occl);

        distance = (SeekBar) findViewById(R.id.config3_seekDistance);
        textProgressDistance = (TextView) findViewById(R.id.config3_progressDistance);

        lastSeekbar = (ImageView) findViewById(R.id.config3_prec);
        nextSeekbar = (ImageView) findViewById(R.id.config3_suiv);

        setupDistanceBar();

    }

    private void setupDistanceBar(){
        /*
		 * PROGRESSION SEEKBAR WITH BUTTON
		 */

        //SEEKBAR DISTANCE
        distance.setMax(100);
        //lastSeekBar button
        lastSeekbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distance.setProgress(distance.getProgress() - 1);
            }
        });
        //nextSeekBar button
        nextSeekbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distance.setProgress(distance.getProgress()+1);
            }
        });

        //INITIALISATION DES VALEURS DE DISTANCE
        distance.setProgress(50);
        textProgressDistance.setText("50");

        distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                String intProgressChanged = String.valueOf(progressChanged);
                textProgressDistance.setText(intProgressChanged);
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar distance, int progress, boolean fromUser) {
                progressChanged = progress;
                String intProgressChanged = String.valueOf(progressChanged);
                textProgressDistance.setText(intProgressChanged);
            }
        });
    }

    private void readPreferences() {
        SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);

        if(preferences.getBoolean("RadioCibleCampagne", false)) {
            classiqueRadio.setChecked(true);
        }
        if(preferences.getBoolean("RadioCibleBlason", false)){
            trispotRadio.setChecked(true);
        }
        if(preferences.getBoolean("RadioCibleTrispot", false)){
            trispotRadio.setChecked(true);
        }

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

        //lecture distance
        String TextDistance=preferences.getString("textProgressDistance", "50");
        textProgressDistance.setText(TextDistance);
        int SeekBarDistance=preferences.getInt("progressDistance", 50);
        distance.setProgress(SeekBarDistance);
        //fin lecture distance
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

                if (campagneRadio.isChecked()){
                    savePreferences();
                    Intent intent = new Intent(this, Config3CampagneActivity.class);
                    startActivity(intent);
                }else{
                    savePreferences();
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

    private boolean checkInputs(){
        if(!competition.isChecked() && !entrainement.isChecked()){
            makeAlert(getString(R.string.erreur),getString(R.string.conditions_error));
            return false;
        }

        if(!interieurRadio.isChecked() && !exterieurRadio.isChecked()){
            makeAlert(getString(R.string.erreur), getString(R.string.int_ext_error));
            return false;
        }

        if(textProgressDistance.equals("0")){
            makeAlert(getString(R.string.erreur), getString(R.string.distance_error));
            return false;
        }

        return true;
    }

    private void savePreferences() {
        SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("lastPlayer",0);

        //Choix cible
        editor.putBoolean("ImageClassique",classiqueRadio.isChecked());
        editor.putBoolean("ImageTrispot", trispotRadio.isChecked());
        editor.putBoolean("ImageCampagne", campagneRadio.isChecked());

        //choix condition
        editor.putBoolean("RadioEntrainement", entrainement.isChecked());
        editor.putBoolean("RadioCompetition", competition.isChecked());

        //choix int?rieur/exterieur
        editor.putBoolean("RadioInterieur", interieurRadio.isChecked());
        editor.putBoolean("RadioExterieur", exterieurRadio.isChecked());

        //choix distance
        editor.putString("textProgressDistance", textProgressDistance.getText().toString());
        int progressDistance= distance.getProgress();
        editor.putInt("progressDistance", progressDistance);
        //fin choix distance

        editor.commit();
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
