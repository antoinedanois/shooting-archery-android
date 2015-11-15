package com.iutbmteprow.shootingarchery;

import java.util.ArrayList;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class ManualScoreActivity extends Activity implements OnFocusChangeListener{
	
	ArrayList<EditText> editTexts;

	EditText textCourant;
	boolean boolFleches;
    boolean exterieur;
	SharedPreferences preferences;
	private DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual_score);

		preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
		
		if(preferences.getBoolean("ImageTrispot", true)){
			((View) findViewById(R.id.five_row)).setVisibility(View.GONE);
			((View) findViewById(R.id.four_row)).setVisibility(View.GONE);
			((View) findViewById(R.id.manscore_buttonScore5)).setVisibility(View.GONE);
		}
		
		db = new DBHelper(this);
		
		initScoreBtns();
		initScoreViews();
		
		Button btValidateVolees = (Button) findViewById(R.id.manscore_buttonValidate);
		btValidateVolees.setOnClickListener(validateVolees);
	}

	private void initScoreViews() {
		preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
		boolFleches = preferences.getBoolean("RadioFleche3", true);
		
		editTexts = new ArrayList<EditText>();
		editTexts.add((EditText) findViewById(R.id.manscore_edittext1));
		editTexts.add((EditText) findViewById(R.id.manscore_edittext2));
		editTexts.add((EditText) findViewById(R.id.manscore_edittext3));
		
		if (!boolFleches) {
			editTexts.add((EditText) findViewById(R.id.manscore_edittext4));
			editTexts.add((EditText) findViewById(R.id.manscore_edittext5));
			editTexts.add((EditText) findViewById(R.id.manscore_edittext6));
		}
		
		for (EditText edt : editTexts) {
			edt.setOnFocusChangeListener(this);
			edt.setVisibility(View.VISIBLE);
		}
		textCourant = editTexts.get(0);
	}

	private void initScoreBtns() {
		ArrayList<Button> scoreBtns = new ArrayList<Button>();
		Button score0 = (Button) findViewById(R.id.manscore_buttonScore0);
        boolean exterieur = getIntent().getBooleanExtra("exterieur",false);
		
		scoreBtns.add(score0);
		scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore1));
		scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore2));
		scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore3));
		scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore4));
		scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore5));
		scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore6));
		scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore7));
		scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore8));
		scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore9));
		scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore10));
        if (exterieur) {
            scoreBtns.add((Button) findViewById(R.id.manscore_buttonScore10plus));
            findViewById(R.id.manscore_buttonScore10plus).setVisibility(View.VISIBLE);
        }
		
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
	    		}
	    	}

	    	int idPartie = preferences.getInt("idPartie", 0);
	    	int noManche = getIntent().getIntExtra("noManche", 0);
	    	int noVolee = getIntent().getIntExtra("noVolee", 0);
	    	
	    	for (int i = 0; i < editTexts.size(); i++) {
	    		String text = editTexts.get(i).getText().toString();
	    		//int score = text.equals("10+")?11:Integer.valueOf(text);
	    		int score = 0;
	    		if (text.equals("M")) {
	    			score = 0;
	    		} else if (text.equals("10+")) {
	    			score = 11;
	    		} else {
	    			score = Integer.valueOf(text);
	    		}
	    		db.addTirer(idPartie, noManche, noVolee, score, i+1);
	    	}
	    	getSharedPreferences("partie", Context.MODE_PRIVATE).edit().putInt("currentVolee", ++noVolee).commit();
	    	
	    	//Incrementer les vol�es en entrainement
	    	boolean competition = getIntent().getBooleanExtra("competition", true);
	    	if (!competition)
	    		db.incVoleesPartie(idPartie);
	    	
	    	setResult(RESULT_OK);
    		finish();
	    }
	};
	
	@Override
	public void onFocusChange(View v, boolean hasFocus){
		textCourant = (EditText) v;
	}
	
	protected void avancerTextCourant() 
	{
		int idCourant = editTexts.indexOf(textCourant);
		if (idCourant == editTexts.size() - 1)
			return;
		textCourant = editTexts.get(idCourant + 1);
		textCourant.requestFocus();
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
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

}
