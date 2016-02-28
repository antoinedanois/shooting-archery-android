package com.iutbmteprow.shootingarchery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;

import com.iutbmteprow.shootingarchery.dbman.Campagne;
import com.iutbmteprow.shootingarchery.dbman.DBHelper;

public class InGameActivityCampagne extends Activity {

    private int notifId;
    private Campagne partie;
    private int noVolee;
    private int firstCible;

    private ScoreTableFragmentCampagne stfc;
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
            stfc = (ScoreTableFragmentCampagne) getFragmentManager().findFragmentByTag("stfc");
        }
    }

    private void readPreferences() {
        SharedPreferences sp = getSharedPreferences("partie", Context.MODE_PRIVATE);
        noVolee = sp.getInt("currentVolee", 0);
        firstCible = sp.getInt("firstCible", 0);


        partie = db.getCampagne(sp.getInt("idCampagne", 0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        readPreferences();

        stfc.refresh();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        SharedPreferences.Editor sp = getSharedPreferences("partie", Context.MODE_PRIVATE).edit();
        sp.putInt("currentVolee", noVolee);


        sp.commit();
    }

    private void makeFragment() {
        //Preparer le fragment avec ses arguments
        stfc = new ScoreTableFragmentCampagne();
        Bundle arguments = new Bundle();
        arguments.putInt("idCampagne", partie.getIdCampagne());
        stfc.setArguments(arguments);

        //Installer le fragment dans la vue
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ingame_fragments, stfc, "stfc");
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
                if (noVolee > partie.getNbCibles()) {
                    showTooManyPlayError();
                    break;
                }
                Intent intent = new Intent(this, ManualScoreCampActivity.class);
                intent.putExtra("noVolee", noVolee);
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
        alertDialog.setMessage(getString(R.string.toomanyplaycamp));
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
                    fragmentTransaction.remove(stfc);
                    fragmentTransaction.commit();
                    makeFragment();
                }
                stfc.refresh();
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
                        db.supprimerCampagne(partie.getIdCampagne());
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
                        db.terminerCampagne(partie.getIdCampagne());
                        if (!partie.isCompetition()) {
                            db.normalizeCibles(partie.getIdCampagne());
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
        Intent intent = new Intent(InGameActivityCampagne.this, AccueilActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void makeNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.target)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notif_txt));
        Intent resultIntent = new Intent(this, InGameActivityCampagne.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(InGameActivityCampagne.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(

                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = mBuilder.build();
        notif.flags = Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(notifId, notif);
    }
}
