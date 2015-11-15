package com.iutbmteprow.shootingarchery;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.UserAlreadyRegisteredException;
import com.iutbmteprow.shootingarchery.dbman.Utilisateur;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class Config1Activity extends Activity {

	private TextView nomUtil;
	
	Spinner UserSpinner = null;
	private DBHelper db;

	private TextView prenomUtil;

	private TextView gradeUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config1);

		//Connection à la bdd
		db = new DBHelper(this);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		if (noUser()) {
			Toast.makeText(this, R.string.no_user_inside, Toast.LENGTH_LONG).show();
			makeUser();
		};
		fillAttributes();
		setupSpinner();
	}

	private void fillAttributes() {
		UserSpinner = (Spinner) findViewById(R.id.config1_usersSpin);
		nomUtil = (TextView) findViewById(R.id.config1_nomUtilisateur);
		prenomUtil = (TextView) findViewById(R.id.config1_prenomUtilisateur);
		gradeUtil = (TextView) findViewById(R.id.config1_GradeUtilisateur);
	}

	private int findTextId(SpinnerAdapter adapter, String nomUser) {
		int retour = 0;
		for (int i=0; i<adapter.getCount();i++) {
			if (nomUser.equals(adapter.getItem(i).toString())) {
				return i;
			}
		}
		return retour;
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

	private void setupSpinner() {
		//Remplissage Spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, db.getUtilisateursForSpinner());
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		UserSpinner.setAdapter(adapter);
		
		//Lecture nom utilisateur
		SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
		String nomUser = preferences.getString("NomUtilisateur", "");	

		if (!nomUser.isEmpty()) {	
			UserSpinner.setSelection(findTextId(UserSpinner.getAdapter(),nomUser));
		}

		UserSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				String[] userName = UserSpinner.getSelectedItem().toString().split(" ");
				Utilisateur curUser = db.getUtilisateurFromName(userName[0], userName[1]);
				
				nomUtil.setText(curUser.getNom());
				prenomUtil.setText(curUser.getPrenom());
				
				try {
					gradeUtil.setText(curUser.getGrade());
				} catch (NoSuchFieldException e) {
					gradeUtil.setText("Grade inconnu...");
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// ???
			}

		});
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
			
			savePreferences();
					
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

	private void savePreferences() 
	{

		SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("NomUtilisateur", UserSpinner.getSelectedItem().toString());
		String userSpinner = UserSpinner.getSelectedItem().toString();
		
		String[] nomUtil = userSpinner.split(" ");
		editor.putInt("idUtilisateur", db.getUtilisateurFromName(nomUtil[0], nomUtil[1]).getId());
		
		editor.commit();
	}

}
