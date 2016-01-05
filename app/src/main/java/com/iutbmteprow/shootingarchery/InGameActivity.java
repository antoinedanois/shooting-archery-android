package com.iutbmteprow.shootingarchery;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.Partie;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class InGameActivity extends Activity {

	private int notifId;
	private Partie partie;
	private int noVolee;
	private int noManche;
	private ScoreTableFragment stf;
	private DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in_game);
		db = new DBHelper(this);

		//Reduit la probabilite de fermeture de l'appli
		makeNotification();
		
		readPreferences();
		
		//Uniquement quand on cree la premiere fois
		if (savedInstanceState == null) {
			//Creer le fragment
			makeFragment();
		} else {
			stf = (ScoreTableFragment) getFragmentManager().findFragmentByTag("stf");
		}
	}

	private void readPreferences() {
		SharedPreferences sp = getSharedPreferences("partie", Context.MODE_PRIVATE);
		noVolee = sp.getInt("p1currentVolee", 0);
		noManche = sp.getInt("p1currentManche", 0);
		
		partie = db.getPartie(sp.getInt("p1idPartie", 0));
	}

	@Override
	protected void onResume() {
		super.onResume();
		readPreferences();
		
		stf.refresh();
	}
	
	@Override
	public void onPause() {
	    super.onPause();  // Always call the superclass method first
	    SharedPreferences.Editor sp = getSharedPreferences("partie", Context.MODE_PRIVATE).edit();
	    sp.putInt("p1currentVolee", noVolee);
	    sp.putInt("p1currentManche", noManche);

	    sp.commit();
	}
	
	private void makeFragment() {
		//Preparer le fragment avec ses arguments
		stf = new ScoreTableFragment();
		Bundle arguments = new Bundle();
		arguments.putInt("p1idPartie",partie.getIdPartie());
		stf.setArguments(arguments);
		
		//Installer le fragment dans la vue
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.ingame_fragments, stf, "stf");
		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.in_game, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_photo:
				if (noVolee > partie.getNbVolees()) {
					if (noManche == partie.getNbManches()) {
						showTooManyPlayError();
						break;
					} else {
						noManche++;
						noVolee = 1;
					}
				}
				Intent intent = new Intent(this, ManualScoreActivity.class);
				intent.putExtra("noVolee", noVolee);
				intent.putExtra("noManche", noManche);
                intent.putExtra("exterieur", partie.isExterieur());
				intent.putExtra("competition", partie.isCompetition());
				startActivityForResult(intent, 1);
				break;
			case R.id.action_finseance:
				terminerPartie();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showTooManyPlayError() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    	alertDialog.setTitle(getString(R.string.error));
    	alertDialog.setMessage(getString(R.string.toomanyplay));
    	alertDialog.setButton(getString(R.string.ok), new OnClickListener() {
    	   public void onClick(DialogInterface dialog, int which) {
    	      // Rien...
    	   }
    	});
    	alertDialog.setIcon(R.drawable.warning_dark);
    	alertDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				//Reconstruire un fragment en entrainement
				if (!partie.isCompetition()) {
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.remove(stf);
					fragmentTransaction.commit();
					makeFragment();
				}
				stf.refresh();
			}
		}
	}
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle(R.string.exit_question)
	        .setMessage(R.string.sureToQuit)
	        .setIcon(R.drawable.action_help)
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {
	            	db.supprimerPartie(partie.getIdPartie());
	                goToAccueil();
	            }
	        }).create().show();
	}
	
	private void terminerPartie() {
		if (noVolee == 1) {
			onBackPressed();
			return;
		}
		new AlertDialog.Builder(this)
        .setTitle(R.string.exit_question)
        .setMessage(R.string.confirmGameEnd)
        .setIcon(R.drawable.action_help)
        .setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes, new OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            	db.terminerPartie(partie.getIdPartie());
            	if (!partie.isCompetition()) {
            		db.normalizeVolees(partie.getIdPartie());
            	}
            	goToAccueil();
            }
        }).create().show();
	}
	
	private void goToAccueil() {
		//Effacer les preferences
    	getSharedPreferences("partie", Context.MODE_PRIVATE).edit().clear().commit();
		//Tuer la notification
    	NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	mNotificationManager.cancel(notifId);
    	
    	//Effacer les parametre
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		preferences.edit().clear().commit();

    	//Revenir a l'accueil en vidant la pile d'appel
    	Intent intent = new Intent(InGameActivity.this, AccueilActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
	}
	
	private void makeNotification() {
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.target)
		        .setContentTitle(getString(R.string.app_name))
		        .setContentText(getString(R.string.notif_txt));
		Intent resultIntent = new Intent(this, InGameActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(InGameActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notif = mBuilder.build();
		notif.flags = Notification.FLAG_ONGOING_EVENT;
		mNotificationManager.notify(notifId, notif);
	}
}
