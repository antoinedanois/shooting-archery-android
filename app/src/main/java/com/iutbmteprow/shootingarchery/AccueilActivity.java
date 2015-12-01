package com.iutbmteprow.shootingarchery;


import java.util.ArrayList;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AccueilActivity extends Activity {

	private DBHelper db;
	private int idPartieResumable;
    private int idCampagneResumable;
    Button btnnew;
    Button btnscore;
    Button btnarc;
    Button btnconseil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);
		
		db = new DBHelper(this);
		
		//Créer les fragments une seule fois
		/*if (savedInstanceState == null) {
			makeLastGamesFragments();
		}*/
        btnnew = (Button)  findViewById(R.id.nouveau);
        btnscore = (Button) findViewById(R.id.score);
        btnarc = (Button) findViewById(R.id.monarc);
        btnconseil = (Button) findViewById(R.id.conseil);
        btnnew.setOnClickListener(handleClick);
        btnscore.setOnClickListener(handleClick);
        btnarc.setOnClickListener(handleClick);
        btnconseil.setOnClickListener(handleClick);

        //getSharedPreferences("partie", Context.MODE_PRIVATE).edit().clear().commit();

		checkOngoingGame();
        checkOngoingGameCampagne();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		FragmentManager fragmentManager = getFragmentManager();
		int i = 0;
		ScoreTableFragment fragment = null;
		do {
			fragment = (ScoreTableFragment) fragmentManager.findFragmentByTag("stf" + i++);
			if (fragment != null) {
				fragment.refresh();
			}
		} while (fragment != null);
	}

	private void makeLastGamesFragments() {
		ArrayList<Integer> parties = db.getLastParties();
		if (parties.size() == 0) {
			((TextView) findViewById(R.id.accueil_noDataText)).setVisibility(View.VISIBLE);
			return;
		}
		
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		for (int i=0;i<parties.size();i++) {
			ScoreTableFragment stf = new ScoreTableFragment();
			Bundle arguments = new Bundle();
			arguments.putInt("idPartie", parties.get(i));
			stf.setArguments(arguments);
			fragmentTransaction.add(R.id.accueil_fragments, stf, "stf" + i);
			
			if (i != parties.size() - 1) {
				SeparatorFragment sf = new SeparatorFragment();
				fragmentTransaction.add(R.id.accueil_fragments, sf);
			}
		}
		fragmentTransaction.commit();
	}

	private void checkOngoingGame() 
	{
		idPartieResumable = db.checkOngoingGame();

		if(idPartieResumable==0 )
			return;
		
		 new AlertDialog.Builder(this)
	        .setTitle(R.string.attention)
	        .setMessage(R.string.checkOngoingGame)
	        .setIcon(R.drawable.warning_dark)
	        .setCancelable(false)
	        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {

                        db.supprimerPartie(idPartieResumable);
                        idPartieResumable = 0;


					getSharedPreferences("partie", Context.MODE_PRIVATE).edit().clear().commit();
					arg0.dismiss();
					finish();
					Intent thisActivity = getIntent();
					thisActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(thisActivity);
				}

			})
	        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {

                    resumeOngoingGame(0);
                    arg0.dismiss();
	            }
	        }).create().show();

	}
    private void checkOngoingGameCampagne()
    {

        idCampagneResumable = db.checkOngoingGameCampagne();

        if(idCampagneResumable==0)
            return;

        new AlertDialog.Builder(this)
                .setTitle(R.string.attention)
                .setMessage(R.string.checkOngoingGame)
                .setIcon(R.drawable.warning_dark)
                .setCancelable(false)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                            db.supprimerCampagne(idCampagneResumable);
                            idCampagneResumable = 0;


                        getSharedPreferences("partie", Context.MODE_PRIVATE).edit().clear().commit();
                        arg0.dismiss();
                        finish();
                        Intent thisActivity = getIntent();
                        thisActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(thisActivity);
                    }

                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        resumeOngoingGame(1);
                        arg0.dismiss();
                    }
                }).create().show();

    }

	protected void trashOngoingGame() {
		//Supprimer partie non termin�e
	}


	protected void resumeOngoingGame(int type) {
        Intent intent;
        switch(type){
            case 0:
                intent = new Intent(this, InGameActivity.class);
                break;
            case 1:
                intent = new Intent(this, InGameActivityCampagne.class);
                break;
            default:
                intent = new Intent(this, InGameActivity.class);
                break;
        }

    	startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.accueil, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {

	        case R.id.action_settings:
	        	Intent intent2 = new Intent(this, SettingsActivity.class);
            	startActivity(intent2);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

    protected View.OnClickListener handleClick = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.nouveau:
                    Intent intent = new Intent(view.getContext(), ConfigNPlayersActivity.class);
                    startActivity(intent);
                    break;
                case R.id.score:
                    Intent intent2 = new Intent(view.getContext(), SortActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.monarc:
                    Intent intent3 = new Intent(view.getContext(), ArcActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.conseil:
                    Intent intent4 = new Intent(view.getContext(), ConseilActivity.class);
                    startActivity(intent4);
                    break;

            }
        }
    };

   }



