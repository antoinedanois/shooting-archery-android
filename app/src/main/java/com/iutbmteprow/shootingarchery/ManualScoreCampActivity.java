package com.iutbmteprow.shootingarchery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;

import java.util.ArrayList;

public class ManualScoreCampActivity extends Activity implements OnFocusChangeListener {

    RadioButton birdieRadio = null;
    RadioButton gazRadio = null;
    RadioButton soixanteRadio = null;
    RadioButton quatrevingtRadio = null;

    private RadioButton inconnu;
    private RadioButton connu;

    private Spinner spinner;
    //
    ArrayList<EditText> editTexts;

    EditText textCourant;
    SharedPreferences preferences;
    private DBHelper db;


    private SeekBar distance = null;
    private TextView textProgressDistance;
    int ProgressChanged = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_score_camp);

        // Show the Up button in the action bar.
        setupActionBar();


        preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);


        db = new DBHelper(this);

        initScoreBtns();
        initScoreViews();

        Button btValidateVolees = (Button) findViewById(R.id.manscore_buttonValidate);
        btValidateVolees.setOnClickListener(validateVolees);
        //Seekbar


        distance = (SeekBar) findViewById(R.id.Seekbar_score_2);
        textProgressDistance = (TextView) findViewById(R.id.Distance_score_2);


        distance.setMax((65 - 5));
        textProgressDistance.setText("5");

        distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            public void onProgressChanged(SeekBar distance, int progress, boolean fromUser) {
                ProgressChanged = progress;
                String intProgressChanged = String.valueOf(ProgressChanged);
                textProgressDistance.setText(intProgressChanged);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

                String intProgressChanged = String.valueOf(ProgressChanged);
                textProgressDistance.setText(intProgressChanged);
            }

        });


    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        textCourant = (EditText) v;
    }

    protected void avancerTextCourant() {
        int idCourant = editTexts.indexOf(textCourant);
        if (idCourant == editTexts.size() - 1)
            return;
        textCourant = editTexts.get(idCourant + 1);
        textCourant.requestFocus();
    }

    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //The score things ....


    private void initScoreViews() {

        editTexts = new ArrayList<EditText>();
        editTexts.add((EditText) findViewById(R.id.manscore_edittext1_2));
        editTexts.add((EditText) findViewById(R.id.manscore_edittext2_2));
        editTexts.add((EditText) findViewById(R.id.manscore_edittext3_2));

        for (EditText edt : editTexts) {
            edt.setOnFocusChangeListener(this);
            edt.setVisibility(View.VISIBLE);
        }
        textCourant = editTexts.get(0);
    }


    private void initScoreBtns() {
        ArrayList<Button> scoreBtns = new ArrayList<Button>();
        Button score0 = (Button) findViewById(R.id.manscore_buttonScore0_2);


        CompoundButton.OnCheckedChangeListener occl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    return;
                switch (buttonView.getId()) {
                    case R.id.cibleBirdee: {
                        gazRadio.setChecked(false);
                        soixanteRadio.setChecked(false);
                        quatrevingtRadio.setChecked(false);
                        break;
                    }

                    case R.id.cibleGaz: {
                        birdieRadio.setChecked(false);
                        soixanteRadio.setChecked(false);
                        quatrevingtRadio.setChecked(false);
                        break;
                    }
                    case R.id.cible60: {
                        gazRadio.setChecked(false);
                        birdieRadio.setChecked(false);
                        quatrevingtRadio.setChecked(false);
                        break;
                    }
                    case R.id.cible80: {
                        gazRadio.setChecked(false);
                        soixanteRadio.setChecked(false);
                        birdieRadio.setChecked(false);
                        break;
                    }
                }
            }
        };
        CompoundButton.OnCheckedChangeListener occlConnu = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                    return;
                switch (buttonView.getId()) {
                    case R.id.connu: {
                        inconnu.setChecked(false);
                        break;
                    }

                    case R.id.inconnu: {
                        connu.setChecked(false);
                        break;
                    }

                }
            }
        };

        birdieRadio = (RadioButton) findViewById(R.id.cibleBirdee);
        birdieRadio.setOnCheckedChangeListener(occl);
        gazRadio = (RadioButton) findViewById(R.id.cibleGaz);
        gazRadio.setOnCheckedChangeListener(occl);
        soixanteRadio = (RadioButton) findViewById(R.id.cible60);
        soixanteRadio.setOnCheckedChangeListener(occl);
        quatrevingtRadio = (RadioButton) findViewById(R.id.cible80);
        quatrevingtRadio.setOnCheckedChangeListener(occl);

        connu = (RadioButton) findViewById(R.id.connu);
        connu.setOnCheckedChangeListener(occlConnu);
        inconnu = (RadioButton) findViewById(R.id.inconnu);
        inconnu.setOnCheckedChangeListener(occlConnu);

        scoreBtns.add(score0);
        scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore1_2));
        scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore2_2));
        scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore3_2));
        scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore4_2));
        scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore5_2));
        scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore6_2));


        OnClickListener ocl = new OnClickListener() {
            @Override
            public void onClick(View v) {
                textCourant.setText(((Button) v).getText());
                avancerTextCourant();
            }
        };

        for (Button btn : scoreBtns)
            btn.setOnClickListener(ocl);

        score0.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                textCourant.setText("M");
                avancerTextCourant();
            }
        });
    }

    OnClickListener validateVolees = new OnClickListener() {
        public void onClick(View v) {
            for (EditText edt : editTexts) {
                if (edt.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), R.string.have_to_fill_fields, Toast.LENGTH_SHORT).show();
                    return;
                } else if (!checkForm()) {
                    return;
                }
            }

            int idCible = 0;
            if (birdieRadio.isChecked()) {
                idCible = db.getIdCibleFromName("Birdee").getId();
            } else if (gazRadio.isChecked()) {
                idCible = db.getIdCibleFromName("Gazinière").getId();
            } else if (soixanteRadio.isChecked()) {
                idCible = db.getIdCibleFromName("60").getId();
            } else if (quatrevingtRadio.isChecked()) {
                idCible = db.getIdCibleFromName("80").getId();
            }

            boolean typeCible = false;
            if (connu.isChecked()) {
                typeCible = true;
            }


            int idCampagne = preferences.getInt("idCampagne", 0);
            int noVolee = getIntent().getIntExtra("noVolee", 0);

            int progressDistance = distance.getProgress();


            for (int i = 0; i < editTexts.size(); i++) {
                String text = editTexts.get(i).getText().toString();
                //int score = text.equals("10+")?11:Integer.valueOf(text);
                int score = 0;
                if (text.equals("M")) {
                    score = 0;
                } else {
                    score = Integer.valueOf(text);
                }
                db.addTirerCampagne(idCampagne, noVolee, score, idCible, typeCible, progressDistance, i + 1);
            }
            getSharedPreferences("partie", Context.MODE_PRIVATE).edit().putInt("currentVolee", ++noVolee).commit();

            //Incrementer les vol�es en entrainement
            boolean competition = getIntent().getBooleanExtra("competition", true);

            if (!competition)
                db.incCibleCampagne(idCampagne);

            setResult(RESULT_OK);
            finish();
        }
    };


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


    private boolean checkForm() {
        if (!birdieRadio.isChecked() && !gazRadio.isChecked() && !soixanteRadio.isChecked() && !quatrevingtRadio.isChecked()) {
            makeDialog(getString(R.string.erreur), getString(R.string.erreur_radio_non_check));
            return false;
        } else if (!connu.isChecked() && !inconnu.isChecked()) {
            makeDialog(getString(R.string.erreur), getString(R.string.type_cible_error));
            return false;
        } else if (ProgressChanged == 0) {
            makeDialog(getString(R.string.erreur), getString(R.string.distance_error));
            return false;
        } else {
            return true;
        }
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


}
