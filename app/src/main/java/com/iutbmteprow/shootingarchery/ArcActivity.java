package com.iutbmteprow.shootingarchery;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import com.iutbmteprow.shootingarchery.dbman.Arc;
import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.TypeArc;
import com.iutbmteprow.shootingarchery.dbman.Utilisateur;


public class ArcActivity extends Activity {
    private DBHelper db;
    private Spinner popupTypeSpinner = null;
    private Spinner userSpinner;
    EditText EditNomArc = null;
    private List<TypeArc> TypeList;
    int idUserSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc);
        db = new DBHelper(this); //connection bdd

        setupActionBar();
        setupUser();
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

    private void setupUser() {
        userSpinner = (Spinner) findViewById(R.id.arc_spinner_user);
        //Remplissage Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, db.getUtilisateursForSpinner());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);

        //Lecture nom utilisateur
        SharedPreferences preferences = getSharedPreferences("partie", Context.MODE_PRIVATE);
        String nomUser = preferences.getString("NomUtilisateur", "");

        if (!nomUser.isEmpty()) {
            userSpinner.setSelection(findTextId(userSpinner.getAdapter(),nomUser));
        }

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String[] userName = userSpinner.getSelectedItem().toString().split(" ");
                Utilisateur curUser = db.getUtilisateurFromName(userName[0], userName[1]);
                idUserSelected = curUser.getId();
                ListArcFragments(curUser.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // ???
            }

        });
    }

    private void ListArcFragments(int id) {
        List<Arc> arc = db.getArcsByUser(id);
        if (arc.size() == 0) {
            ((TextView) findViewById(R.id.arc_noDataText)).setVisibility(View.VISIBLE);
            return;
        }
        ((TextView) findViewById(R.id.arc_noDataText)).setVisibility(View.GONE);
        LinearLayout ll = (LinearLayout) findViewById(R.id.listarc);
        ll.removeAllViews();

        for (Arc arcsolo : arc) {

            float dp = getResources().getDisplayMetrics().density;
            String nom = arcsolo.getNomArc();

            Button arcBut = new Button(this);
////TEST pour suppression
            /*LinearLayout layoutArc = new LinearLayout(this);
            layoutArc.setOrientation(LinearLayout.HORIZONTAL);

            layoutArc.setBackgroundColor(getResources().getColor(R.color.color_button));
            LinearLayout.MarginLayoutParams lamargin = new LinearLayout.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lamargin.setMargins(0,0,0,100);
            layoutArc.setLayoutParams(lamargin);


            TextView textArc = new TextView(this);
            textArc.setText(nom);
            textArc.setTextColor(getResources().getColor(R.color.color_button_text));
            textArc.setTextSize(32);
            textArc.setGravity(Gravity.CENTER_HORIZONTAL);
            LinearLayout.LayoutParams talp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textArc.setLayoutParams(talp);*/





//FIN TEST
            arcBut.setText(nom);
            arcBut.setTextColor(getResources().getColor(R.color.color_button_text_menu));
            arcBut.setHintTextColor(getResources().getColor(R.color.color_button_text_menu));
            arcBut.setId(arcsolo.getIdArc());
            arcBut.setBackgroundColor(getResources().getColor(R.color.color_button_menu));
            arcBut.setTextSize(30);

            LinearLayout.MarginLayoutParams margin = (LinearLayout.MarginLayoutParams) ll.getLayoutParams();
            margin.setMargins(0,0,0,10);
            margin.height =LinearLayout.LayoutParams.WRAP_CONTENT ;
            arcBut.setGravity(Gravity.CENTER_HORIZONTAL);
            arcBut.setLayoutParams(margin);

            arcBut.requestLayout();
            arcBut.setOnClickListener(handleClick);
            ll.addView(arcBut);
            /*layoutArc.addView(textArc);
            layoutArc.addView(arcBut);*/



        }
    }

    private void setupActionBar() {
       getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add:
                showAddArc();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void showAddArc (){
        final AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(R.string.add_arc)
                .setView(this.getLayoutInflater().inflate(R.layout.popup_add_arc, null))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Annuler", null)
                .create();

        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                popupTypeSpinner = (Spinner) ((Dialog) d).findViewById(R.id.popup_spinner_typearc);
                ListTypeArc();


                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View dialog) {
                        Object selection = popupTypeSpinner.getSelectedItem();
                        EditNomArc = (EditText) ((Dialog)d).findViewById(R.id.popup_nom_arc);
                        String nomArc = EditNomArc.getText().toString();

                        if (nomArc.isEmpty()) {
                            EditNomArc.setError(getString(R.string.nom_arc_not_null));

                        }else{
                            String nomType = selection.toString();
                            TypeArc typeSelect = db.getTypeArcFromName(nomType);
                            int idTypeSelect = typeSelect.getIdTypeArc();
                            int nbArcForUser = db.getArcsCountByUser(idUserSelected);
                            if (nbArcForUser >= 4){
                                Toast.makeText(getApplicationContext(),"Vous avez déjà quatre arcs d'enregistrés !", Toast.LENGTH_LONG).show();
                                d.dismiss();
                            }else{

                                db.addArc(new Arc( 0, idUserSelected,nomArc, idTypeSelect));
                                setupUser();
                                //lancer linsert get l'utilisateur
                                d.dismiss();
                            }
                        }
                    }
                });
            }


        });
        d.show();
    }

    private void ListTypeArc(){
        TypeList = null;
        TypeList = db.getTypeArc();

        ArrayList<String> listStringType = new ArrayList<String>();
        for (TypeArc type : TypeList){
            listStringType.add(type.getNomType());

        }

        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listStringType);
        popupTypeSpinner.setAdapter(adapterType);
    }

    protected View.OnClickListener handleClick = new View.OnClickListener() {
        public void onClick(View view) {

            Button btn = (Button) view;
                    Intent intent = new Intent(getApplicationContext(), ChoixReglage.class);
                    intent.putExtra("nomArc", btn.getText());
                    startActivity(intent);

        }
    };
}