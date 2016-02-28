package com.iutbmteprow.shootingarchery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class SortActivity extends Activity {

    private Spinner sortUserSpinner = null;
    private DBHelper db;
    private List<Utilisateur> listUser;

    private View sortCibleLayout;
    private View sortTypeLayout;
    private View sortTypeEnvLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        setupActionBar();
        db = new DBHelper(this);
        sortUserSpinner = (Spinner) findViewById(R.id.sort_spinner_user);
        updateUser();

        sortCibleLayout = findViewById(R.id.sort_cible);
        sortTypeLayout = findViewById(R.id.sort_type);
        sortTypeEnvLayout = findViewById(R.id.sort_environnement);
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
        getMenuInflater().inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_search:
                launchSearch();
                return true;
            case R.id.action_add:
                listSort();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchSearch() {
        if (!checkInputs())
            return;


        Intent intent = new Intent(this, SortResultActivity.class);

        //Nom utilisateur
        Object selection = sortUserSpinner.getSelectedItem();
        if (selection == null) {
            Toast.makeText(this, R.string.have_to_select_user, Toast.LENGTH_LONG).show();
            return;
        }
        String[] nomUtilisateur = selection.toString().split(" ");
        intent.putExtra("utilisateurId", db.getUtilisateurFromName(nomUtilisateur[0], nomUtilisateur[1]).getId());

        //Type de cible
        if (sortCibleLayout.getVisibility() == View.VISIBLE) {
            if (((RadioButton) findViewById(R.id.sort_blason)).isChecked()) {
                intent.putExtra("typeCible", 1);
            }
            if (((RadioButton) findViewById(R.id.sort_trispot)).isChecked()) {
                intent.putExtra("typeCible", 2);
            }
            if (((RadioButton) findViewById(R.id.sort_campagne)).isChecked()) {
                intent.putExtra("typeCible", 3);
            }
        }

        //Type comp_entra
        if (sortTypeLayout.getVisibility() == View.VISIBLE) {
            intent.putExtra("competition", ((RadioButton) findViewById(R.id.sort_competition)).isChecked());
        }

        //Type environnement
        if (sortTypeEnvLayout.getVisibility() == View.VISIBLE) {
            intent.putExtra("exterieur", ((RadioButton) findViewById(R.id.sort_exterieur)).isChecked());
        }

        startActivity(intent);
    }

    private boolean checkInputs() {
        boolean result = true;

        if (sortCibleLayout.getVisibility() == View.VISIBLE) {
            result &= ((RadioButton) findViewById(R.id.sort_blason)).isChecked() ^ ((RadioButton) findViewById(R.id.sort_trispot)).isChecked() ^ ((RadioButton) findViewById(R.id.sort_campagne)).isChecked();
        }

        if (sortTypeLayout.getVisibility() == View.VISIBLE) {
            result &= ((RadioButton) findViewById(R.id.sort_competition)).isChecked() ^ ((RadioButton) findViewById(R.id.sort_entrainement)).isChecked();

        }

        if (sortTypeEnvLayout.getVisibility() == View.VISIBLE) {
            result &= ((RadioButton) findViewById(R.id.sort_interieur)).isChecked() ^ ((RadioButton) findViewById(R.id.sort_exterieur)).isChecked();
        }

        if (!result) {
            new AlertDialog.Builder(this)
                    .setTitle("Erreur")
                    .setMessage(R.string.has_to_fill_criterias)
                    .setPositiveButton(R.string.ok, null)
                    .create().show();
        }

        return result;
    }

    private void updateUser() {
        listUser = null;
        listUser = db.getUtilisateurs();
        ArrayList<String> listStringUser = new ArrayList<String>();
        for (Utilisateur user : listUser) {
            listStringUser.add(user.getNom() + " " + user.getPrenom());
        }
        ArrayAdapter<String> adapterUser = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listStringUser);
        sortUserSpinner.setAdapter(adapterUser);
    }

    private void listSort() {
        final AlertDialog d = new AlertDialog.Builder(this)
                .setView(this.getLayoutInflater().inflate(R.layout.popup_sort_selector, null))
                .setTitle(R.string.choose_criteria)
                .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button okBtn = d.getButton(AlertDialog.BUTTON_POSITIVE);
                final CheckBox choixCible = (CheckBox) ((Dialog) d).findViewById(R.id.popup_type_cible);
                final CheckBox choixEntrainCompet = (CheckBox) ((Dialog) d).findViewById(R.id.popup_entrain_compet);
                final CheckBox choixEnvironnement = (CheckBox) ((Dialog) d).findViewById(R.id.popup_type_environnement);
                choixCible.setChecked(sortCibleLayout.getVisibility() == View.VISIBLE);
                choixEntrainCompet.setChecked(sortTypeLayout.getVisibility() == View.VISIBLE);
                choixEnvironnement.setChecked(sortTypeEnvLayout.getVisibility() == View.VISIBLE);

                okBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        changeLayout(choixCible.isChecked(), choixEntrainCompet.isChecked(), choixEnvironnement.isChecked());
                        d.dismiss();
                    }
                });
            }
        });

        d.show();
    }

    private void changeLayout(boolean choixCible, boolean choixEntrainCompet, boolean choixEnvironnement) {
        sortCibleLayout.setVisibility(choixCible ? View.VISIBLE : View.GONE);
        sortTypeLayout.setVisibility(choixEntrainCompet ? View.VISIBLE : View.GONE);
        sortTypeEnvLayout.setVisibility(choixEnvironnement ? View.VISIBLE : View.GONE);
    }

}

