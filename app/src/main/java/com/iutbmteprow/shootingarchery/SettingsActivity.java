package com.iutbmteprow.shootingarchery;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends PreferenceActivity {

    private Spinner popupUserSpinner = null;
    private List<Utilisateur> userList;
    private DBHelper db;
    EditText editName;
    EditText editFirstName;
    private List<String> gradeList;
    private Spinner popupGradeSpinner = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHelper(this);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        addPreferencesFromResource(R.xml.pref_general);

        Preference about = findPreference("about_pref");
        about.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                showAboutPopup();
                return true;
            }
        });

        Preference deleteUser = findPreference("delete_user_pref");
        deleteUser.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDeleteUserPopup();
                return true;
            }
        });

        Preference modifyUser = findPreference("modify_user_pref");
        modifyUser.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                showModifyUserPopup();
                return true;
            }
        });

        Preference resetDB = findPreference("reset_db");
        resetDB.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                showResetDatabase();
                return true;
            }
        });

        Preference changeLink = findPreference("edit_web_link");
        changeLink.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                changeWebLink();
                return true;
            }
        });
    }

    private void changeWebLink() {

    }

    protected void showResetDatabase() {
        final AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_user)
                .setMessage("Êtes-vous certain d'effacer la base de données ? Cette opération est irréversible !")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.reset();
                        finish();
                    }
                })
                .setNegativeButton("Annuler", null)
                .create();
        d.show();
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

    private void showAboutPopup() {
        final AlertDialog d = new AlertDialog.Builder(this)
                .setView(this.getLayoutInflater().inflate(R.layout.popup_about, null))
                .setCancelable(false)
                .setTitle(R.string.app_name)
                .setPositiveButton(android.R.string.ok,
                        new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                //Do nothing here. We override the onclick
                            }
                        })
                .create();
        d.show();
    }

    private void showDeleteUserPopup() {
        final AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_user)
                .setView(this.getLayoutInflater().inflate(R.layout.popup_delete_user, null))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    private Utilisateur utilisateur;

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Object selection = popupUserSpinner.getSelectedItem();
                        if (selection == null) {
                            Toast.makeText(getApplicationContext(), "Aucun utilisateur selectionné, on ne peut donc pas supprimer !", Toast.LENGTH_LONG).show();
                        } else {
                            String[] user = selection.toString().split(" ");
                            utilisateur = db.getUtilisateurFromName(user[0], user[1]);
                            db.deleteUtilisateur(utilisateur);
                            Toast.makeText(getApplicationContext(), R.string.user_has_been_deleted, Toast.LENGTH_LONG).show();
                        }
                    }

                })
                .setNegativeButton("Annuler", null)
                .create();

        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                popupUserSpinner = (Spinner) ((Dialog) d).findViewById(R.id.popup_spinner_user);
                updateUser();
            }
        });
        d.show();
    }

    private void updateUser() {
        userList = null;
        userList = db.getUtilisateurs();
        ArrayList<String> listStringUser = new ArrayList<String>();
        for (Utilisateur user : userList) {
            listStringUser.add(user.getNom() + " " + user.getPrenom());
        }
        ArrayAdapter<String> adapterUser = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listStringUser);
        popupUserSpinner.setAdapter(adapterUser);
    }

    private void updateGrade() {
        gradeList = null;
        gradeList = db.getGradeForSpinner();
        ArrayList<String> listStringGrade = new ArrayList<String>();
        for (String grade : gradeList) {
            listStringGrade.add(grade);
        }
        ArrayAdapter<String> adapterGrade = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listStringGrade);
        popupGradeSpinner.setAdapter(adapterGrade);
    }

    private void showModifyUserPopup() {

        final AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(R.string.mofidier_un_utilisateur)
                .setView(this.getLayoutInflater().inflate(R.layout.popup_modify_user, null))
                .setCancelable(false)
                .setNegativeButton("Annuler", null)
                .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        //Do nothing here. We override the onclick
                    }
                })
                .create();

        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                popupGradeSpinner = (Spinner) ((Dialog) d).findViewById(R.id.popup_modify_spinner_grade);
                popupUserSpinner = (Spinner) ((Dialog) d).findViewById(R.id.popup_user_spinner);
                updateUser();
                updateGrade();
                popupUserSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        editName = (EditText) ((Dialog) d).findViewById(R.id.popup_modify_user_nom_edit);
                        editFirstName = (EditText) ((Dialog) d).findViewById(R.id.popup_modify_user_prenom_edit);
                        String tabUser[] = popupUserSpinner.getSelectedItem().toString().split(" ");
                        editName.setText(tabUser[0]);
                        editFirstName.setText(tabUser[1]);
                        popupGradeSpinner.setSelection(db.findGradeByUser(db.getUtilisateurFromName(tabUser[0], tabUser[1]).getId()) - 1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                });

                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View dialog) {
                        popupGradeSpinner = (Spinner) ((Dialog) d).findViewById(R.id.popup_modify_spinner_grade);
                        popupUserSpinner = (Spinner) ((Dialog) d).findViewById(R.id.popup_user_spinner);

                        boolean canUpdate = true;

                        String tabUser[] = null;
                        try {
                            tabUser = popupUserSpinner.getSelectedItem().toString().split(" ");
                        } catch (NullPointerException e) {
                            Toast.makeText(getApplicationContext(), "Vous ne pouvez pas modifier un utilisateur vide.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        int id = db.getUtilisateurFromName(tabUser[0], tabUser[1]).getId();

                        int idGrade = db.getIdGradeFromName(popupGradeSpinner.getSelectedItem().toString()).getId();

                        String nomUser = editName.getText().toString();
                        String prenomUser = editFirstName.getText().toString();

                        if (nomUser.isEmpty()) {
                            editName.setError(getString(R.string.name_not_be_null));
                            canUpdate = false;
                        }

                        if (nomUser.contains(" ")) {
                            editName.setError(getString(R.string.name_no_space));
                            canUpdate = false;
                        }

                        if (prenomUser.isEmpty()) {
                            editFirstName.setError(getString(R.string.prenom_not_null));
                            canUpdate = false;
                        }

                        if (prenomUser.contains(" ")) {
                            editFirstName.setError(getString(R.string.firstname_no_space));
                            canUpdate = false;
                        }

                        if (canUpdate) {
                            Utilisateur user = new Utilisateur(id, idGrade, nomUser, prenomUser);
                            db.updateUtilisateur(user);
                            d.dismiss();
                        }
                    }
                });

            }
        });
        d.show();
    }
}
