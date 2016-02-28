package com.iutbmteprow.shootingarchery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;

public class ViewGameActivityCamp extends Activity {

    private DBHelper db;
    private ScoreTableFragmentCampagne stf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_game);
        setupActionBar();

        db = new DBHelper(this);

        //Uniquement quand on cree la premiere fois
        if (savedInstanceState == null) {
            //Creer le fragment
            makeFragment();
        } else {
            stf = (ScoreTableFragmentCampagne) getFragmentManager().findFragmentByTag("stf");
        }

    }

    private void makeFragment() {
        //Preparer le fragment avec ses arguments
        stf = new ScoreTableFragmentCampagne();
        Bundle arguments = new Bundle();
        arguments.putInt("idCampagne", getIntent().getIntExtra("idCampagne", 0));
        stf.setArguments(arguments);

        //Installer le fragment dans la vue
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.viewgame_layout, stf, "stf");
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        stf.refresh();
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_game, menu);
        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
//		case R.id.action_save:
//			saveFile();
//			return true;
            case R.id.action_discard:
                deleteGame();
                return true;
//		case R.id.action_share:
//			shareGame();
//			return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareGame() {
        HTMLGenerator html = new HTMLGenerator(getIntent().getIntExtra("idCampagne", 0), this);
        html.makeHTML();

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, html.saveHTML());
        shareIntent.setType("text/html");
        startActivity(Intent.createChooser(shareIntent, "Partager via..."));
    }

    private void deleteGame() {
        final AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_game)
                .setMessage(R.string.delete_game_confirm)
                .setPositiveButton("Oui", new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.supprimerCampagne(getIntent().getIntExtra("idCampagne", 0));
                        onBackPressed();
                    }
                })
                .setNegativeButton("Non", null)
                .create();

        d.show();

    }

    private void saveFile() {
        HTMLGenerator html = new HTMLGenerator(getIntent().getIntExtra("idCampagne", 0), this);
        html.makeHTML();
        Uri res = html.saveHTML();

        final Dialog d = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.ok, null)
                .setMessage(getString(R.string.saved_file_message, res.getLastPathSegment(), "arrowseye"))
                .create();
        d.show();
    }

}
