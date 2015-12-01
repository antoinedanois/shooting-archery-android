package com.iutbmteprow.shootingarchery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.UserAlreadyRegisteredException;
import com.iutbmteprow.shootingarchery.dbman.Utilisateur;

public class MultiplayerSelectionActivity extends Activity {

    private DBHelper db;
    LinearLayout spinerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_selection);

        //Connection à la bdd
        db = new DBHelper(this);

        // Show the Up button in the action bar.
        setupActionBar();

        if (noUser()) {
            Toast.makeText(this, R.string.no_user_inside, Toast.LENGTH_LONG).show();
            makeUser();
        };

        getParameters();
    }

    private void getParameters(){
        spinerContainer=(LinearLayout)findViewById(R.id.SpinnerContainer);
        Bundle b=getIntent().getExtras();
        if(b!=null) {
            int qty = Integer.valueOf(b.getString("playersQty"));
            loadFragments(qty);
        }
    }

    private void loadFragments(int qty){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(spinerContainer.getChildCount()==0) {
            for (int i = 0; i < qty; i++) {
                PlayerSelectionFragment frag = new PlayerSelectionFragment();
                fragmentTransaction.add(R.id.SpinnerContainer, frag, "playerFrag");
            }
            fragmentTransaction.commit();
        }
    }

    private boolean noUser() {
        return db.getUtilisateurCounts() == 0;
    }

    private void makeUser() {

        final AlertDialog d = new AlertDialog.Builder(this)
                .setView(this.getLayoutInflater().inflate(R.layout.popup_newuser, null))
                .setCancelable(false)
                .setTitle(R.string.add_user)
                .setPositiveButton(android.R.string.ok,
                        new Dialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                //Do nothing here. We override the onclick
                            }
                        })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                //Population du spinner
                Spinner grade = (Spinner) ((Dialog) d).findViewById(R.id.popupusr_grade);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(d.getContext(), android.R.layout.simple_spinner_item, db.getGradeForSpinner());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                grade.setAdapter(adapter);

                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        boolean canGo = true;
                        EditText nom = (EditText) ((Dialog) d).findViewById(R.id.popupusr_name);
                        EditText prenom = (EditText) ((Dialog) d).findViewById(R.id.popupusr_prenom);
                        Spinner grade = (Spinner) ((Dialog) d).findViewById(R.id.popupusr_grade);

                        if (nom.getText().toString().isEmpty()) {
                            nom.setError(getString(R.string.name_not_be_null));
                            canGo = false;
                        }

                        if (nom.getText().toString().contains(" ")) {
                            nom.setError(getString(R.string.name_no_space));
                            canGo = false;
                        }

                        if (prenom.getText().toString().isEmpty()) {
                            prenom.setError(getString(R.string.prenom_not_null));
                            canGo = false;
                        }

                        if (prenom.getText().toString().contains(" ")) {
                            prenom.setError(getString(R.string.firstname_no_space));
                            canGo = false;
                        }

                        if (canGo) {
                            //Cr�ation de l'utilisateur (idGrade++ : Spinner d�marre � 0, SQLite d�marre � 1)
                            Utilisateur user = new Utilisateur(0, Integer.parseInt(String.valueOf(grade.getSelectedItemId())) + 1,
                                    nom.getText().toString(), prenom.getText().toString());
                            try {
                                db.addUtilisateur(user);
                            } catch (UserAlreadyRegisteredException e) {
                                Toast.makeText(((Dialog) d).getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            d.dismiss();
                            finish();
                            //Rafraichit l'activit� sans animation
                            Intent thisActivity = getIntent();
                            thisActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(thisActivity);
                        }
                    };
                });

                Button c = d.getButton(AlertDialog.BUTTON_NEGATIVE);
                c.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (db.getUtilisateurCounts() == 0) {
                            finish();
                        } else {
                            d.dismiss();
                        }
                    }
                });

            }});
        d.show();
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.config_users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                Intent intent = new Intent(this, Config2Activity.class);
                startActivity(intent);
                return true;
            case R.id.action_addperson:
                makeUser();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
