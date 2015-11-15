package com.iutbmteprow.shootingarchery;

import java.util.Date;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.Partie;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class Config3Activity extends Activity  {

	private SeekBar distance = null;
	private SeekBar volees = null;
	private TextView textProgressDistance;
	private TextView textProgressVolees;
	private ImageView lastSeekbar;
	private ImageView nextSeekbar;
	int progressChanged = 0;
	private RadioButton interieurRadio;
	private RadioButton exterieurRadio;
	int progressChangedVolees = 0;
	private RadioButton fleche3;
	private RadioButton fleche6;
	private SeekBar manches = null;
	private TextView textProgressManches;
	int progressChangedManches = 0;
	boolean isEntrainement = false;
	private ImageView lastSeekBarVolee;
	private ImageView nextSeekBarVolee;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config3);
		setupActionBar();
		
		SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
		if (preferences.getBoolean("RadioEntrainement", true)){
			isEntrainement = true;
			((View) findViewById(R.id.config3_manches_layout)).setVisibility(View.GONE);
			((View) findViewById(R.id.config3_volees_layout)).setVisibility(View.GONE);
		}

		//DECLARATION
		distance = (SeekBar) findViewById(R.id.config3_seekDistance);
		textProgressDistance = (TextView) findViewById(R.id.config3_progressDistance);

		volees = (SeekBar) findViewById(R.id.config3_seekNbVolees);
		textProgressVolees = (TextView) findViewById(R.id.config3_progressVolees);

		manches = (SeekBar) findViewById(R.id.config3_seekNbManches);
		textProgressManches = (TextView) findViewById(R.id.config3_progressManches);
		
		lastSeekbar = (ImageView) findViewById(R.id.config3_prec);
		nextSeekbar = (ImageView) findViewById(R.id.config3_suiv);
		interieurRadio = (RadioButton) findViewById(R.id.config3_radioInterieur);
		exterieurRadio = (RadioButton) findViewById(R.id.config3_radioExterieur);
		fleche3 = (RadioButton) findViewById(R.id.config3_radio3fleches);
		fleche6 = (RadioButton) findViewById(R.id.config3_radio6fleches);
		lastSeekBarVolee = (ImageView) findViewById(R.id.config3_prec_volee);
		nextSeekBarVolee = (ImageView) findViewById(R.id.config3_suiv_volee);
		//FIN DECLARATION


		/*
		 * PROGRESSION SEEKBAR WITH BUTTON
		 */
		
		//SEEKBAR DISTANCE
		distance.setMax(100);
		//lastSeekBar button
		lastSeekbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				distance.setProgress(distance.getProgress()-1);
			}
		});
		//nextSeekBar button
		nextSeekbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				distance.setProgress(distance.getProgress()+1);
			}
		});
		
		//SEEKBAR VOLEES
		volees.setMax(50);
		//lastSeekBarVolee button
		lastSeekBarVolee.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				volees.setProgress(volees.getProgress()-1);
				
			}
		});
		//nextSeekBarVolee button
		nextSeekBarVolee.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				volees.setProgress(volees.getProgress()+1);
				
			}
		});
		/*
		 * END OF PROGRESSION SEEKBAR WITH BUTTON
		 */

		//INITIALISATION DES VALEURS DE DISTANCE
		distance.setProgress(50);
		textProgressDistance.setText("50");

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
		fillAttributes();

	}

	private void fillAttributes() 
	{
		SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);

		//lecture choix interieur/exterieur
		if(preferences.getBoolean("RadioInterieur", false))
			interieurRadio.setChecked(true);
		if(preferences.getBoolean("RadioExterieur", false))
			exterieurRadio.setChecked(true);
		//fin lecture choix interieur/exterieur
		
		//lecture choix nombre de fleches
		if(preferences.getBoolean("RadioFleche3", false))
			fleche3.setChecked(true);
		if(preferences.getBoolean("RadioFleche6", false))
			fleche6.setChecked(true);
		//fin lecture choix nombre de fl�che
		
		
		//lecture distance
		String TextDistance=preferences.getString("textProgressDistance", "50");
		textProgressDistance.setText(TextDistance);
		int SeekBarDistance=preferences.getInt("progressDistance", 50);
		distance.setProgress(SeekBarDistance);
		//fin lecture distance
		
		//lecture vol�es
		String TextVolee=preferences.getString("textProgressVolee", "0");
		textProgressVolees.setText(TextVolee);
		int SeekBarVolee=preferences.getInt("progressVolee", 0);
		volees.setProgress(SeekBarVolee);
		//fin lecture vol�es
		
		//lecture manches
		String TextManche = preferences.getString("textProgressManches","0");
		textProgressManches.setText(TextManche);
		int SeekBarManche = preferences.getInt("progressManche", 0);
		manches.setProgress(SeekBarManche);
		//fin lecture manches
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
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
			if(!checkForm()){
				return true;			
			}else{
				savePreferences();
				Intent intent = new Intent(this, InGameActivity.class);
				startActivity(intent);
				return true;
			}
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void savePreferences() {
		//code pour garder les valeurs en m�moire
		SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();

		editor.putInt("currentManche", 1);
	    editor.putInt("currentVolee", 1);
		
		//choix int�rieur/exterieur
		editor.putBoolean("RadioInterieur", interieurRadio.isChecked());
		editor.putBoolean("RadioExterieur", exterieurRadio.isChecked());
		//Fin choix int�rieur/exterieur

		//choix nombre de fl�ches
		editor.putBoolean("RadioFleche3", fleche3.isChecked());
		editor.putBoolean("RadioFleche6",fleche6.isChecked());
		//fin choix nombre de fl�che

		//choix distance
		editor.putString("textProgressDistance",textProgressDistance.getText().toString());
		int progressDistance= distance.getProgress();
		editor.putInt("progressDistance", progressDistance);
		//fin choix distance
		
		//choix nombre vol�es
		editor.putString("textProgressVolee", textProgressVolees.getText().toString());
		int progressVolee=volees.getProgress();
		editor.putInt("progressVolee", (isEntrainement)?1:progressVolee);
		//fin choix nombre vol�es
		
		//choix nombre manches
		editor.putString("textProgressManches", textProgressManches.getText().toString());
		int progressManche = manches.getProgress();
		editor.putInt("progressManche", (isEntrainement)?1:progressManche);
		//fin choix nombre de manches.
		
		editor.commit();
		savePartie();
	}
	
	private void savePartie() {
    	SharedPreferences sp = getSharedPreferences("partie", Context.MODE_PRIVATE);
    	int idPartie = new DBHelper(this).addPartie(new Partie(0, //idPartie 
    			sp.getInt("idUtilisateur", 0), //idUtilisateur
    			false, //partieFinie
    			sp.getBoolean("ImageClassique", true)?1:2, //idCible
    			sp.getInt("progressDistance", 0), //distanceCible
    			new Date(), //datePartie
    			sp.getInt("progressManche", 0), //nbManches
    			sp.getInt("progressVolee", 0), //nbVolees
    			(sp.getBoolean("RadioFleche3", true))?3:6, //nbFleches
    			sp.getBoolean("RadioCompetition", false),//competition
    			sp.getBoolean("RadioExterieur", true),//exterieur
    			sp.getInt("idDiametre",0),
                sp.getString("NomArc", "test")) //nom arc
    			);
    	sp.edit().putInt("idPartie", idPartie).commit();
	}

	private boolean checkForm() {
		if(!interieurRadio.isChecked() && !exterieurRadio.isChecked()){
			makeDialog(getString(R.string.erreur),getString(R.string.int_ext_error));
			return false;
		}else if(textProgressDistance.equals("0")){
			makeDialog(getString(R.string.erreur),getString(R.string.distance_error));
			return false;
		}else if(progressChangedVolees == 0 && !isEntrainement){
			makeDialog(getString(R.string.erreur),getString(R.string.volees_error));
			return false;
		} else if (progressChangedManches == 0 && !isEntrainement) {
			makeDialog(getString(R.string.erreur),getString(R.string.manches_error));
			return false;
		}else if(!fleche3.isChecked() && !fleche6.isChecked()){
			makeDialog(getString(R.string.erreur),getString(R.string.nbfleche_error));
			return false;
		}else{
			return true;	
		}
	}

	private void makeDialog(String title, String message){
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
