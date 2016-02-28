package com.iutbmteprow.shootingarchery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iutbmteprow.shootingarchery.dbman.Arc;
import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.Graduation;
import com.iutbmteprow.shootingarchery.dbman.TypeArc;

import java.util.List;


public class ChoixReglage extends Activity {
    private DBHelper db;
    EditText Editdistance;
    EditText Edithorizontal;
    EditText Editvertical;
    EditText Editprofondeur;
    EditText Editremarque;

    String nomArc;
    String nomTypeArc;
    Arc arcSelect = null;
    int idArc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        nomArc = myIntent.getStringExtra("nomArc");

        setContentView(R.layout.activity_choix_reglage);
        db = new DBHelper(this);

        setupActionBar();
        setupPage();
    }

    private void setupPage() {
        arcSelect = db.getArcFromName(nomArc);
        TypeArc typeArcIn = db.getTypeArc(arcSelect.getIdTypeArc());
        nomTypeArc = String.valueOf(typeArcIn.getNomType());
        idArc = arcSelect.getIdArc();
        ListreglageFragments(arcSelect.getIdArc());
        getActionBar().setTitle("Réglages de " + nomArc);
        getActionBar().setSubtitle(nomTypeArc);

    }

    private void ListreglageFragments(int id) {
        List<Graduation> listgrad = db.getListReglageByArc(id);
        if (listgrad.size() == 0) {
            ((TextView) findViewById(R.id.reglage_noDataText)).setVisibility(View.VISIBLE);
            return;
        }

        ((TextView) findViewById(R.id.reglage_noDataText)).setVisibility(View.GONE);
        LinearLayout ll = (LinearLayout) findViewById(R.id.listreglage);
        ll.removeAllViews();

        for (Graduation grad : listgrad) {

            float dp = getResources().getDisplayMetrics().density;
            if (TextUtils.isEmpty(grad.getRemarque()) || grad.getRemarque().equals("")) {
                String remarque = "Aucune";
            } else {
                String remarque = grad.getRemarque();
            }
            String remarque = grad.getRemarque();
            int Distance = grad.getDistance();
            double horizontal = grad.getHorizontal();
            double vertical = grad.getVertical();
            double profondeur = grad.getProfondeur();
            String textDistance = "Distance : " + Distance + "m";
            String textHorizontal = "Horizontal : " + String.format("%.3f", horizontal);
            String textVertical = "Vertical : " + String.format("%.3f", vertical);
            String textProfondeur = "Profondeur : " + String.format("%.3f", profondeur);
            String textRemarque = "Remarque : " + remarque;

            Button regBut = new Button(this);
            final LinearLayout llGrad = new LinearLayout(this);
            TextView tvHori = new TextView(this);
            TextView tvVert = new TextView(this);
            TextView tvProf = new TextView(this);
            TextView tvRemq = new TextView(this);
            Button deleteBut = new Button(this);

            ///SET DU BOUTON
            regBut.setText(Html.fromHtml(textDistance + "<br/><small> " + textRemarque + "</small>"));

            regBut.setTextColor(getResources().getColor(R.color.color_button_text_menu));
            regBut.setHintTextColor(getResources().getColor(R.color.color_button_text_menu));

            regBut.setBackgroundColor(getResources().getColor(R.color.color_button_menu));
            regBut.setTextSize(25);

           /* LinearLayout.MarginLayoutParams margin = (LinearLayout.MarginLayoutParams) ll.getLayoutParams();
            margin.setMargins(20,0,0,20);
            margin.width = LinearLayout.LayoutParams.MATCH_PARENT;
            margin.height =LinearLayout.LayoutParams.WRAP_CONTENT ;*/
            regBut.setGravity(Gravity.CENTER_HORIZONTAL);
            //regBut.setLayoutParams(margin);
            // FIN DU SET BUTTON
            //SET LL AVEC LES TEXTS VIEWS
            llGrad.setOrientation(LinearLayout.VERTICAL);


            tvHori.setText(textHorizontal);
            tvVert.setText(textVertical);
            tvProf.setText(textProfondeur);
            tvRemq.setText(textRemarque);

            tvHori.setTextSize(20);
            tvVert.setTextSize(20);
            tvProf.setTextSize(20);
            tvRemq.setTextSize(20);

            tvHori.setTextColor(getResources().getColor(R.color.color_button_menu));
            tvVert.setTextColor(getResources().getColor(R.color.color_button_menu));
            tvProf.setTextColor(getResources().getColor(R.color.color_button_menu));
            tvRemq.setTextColor(getResources().getColor(R.color.color_button_menu));

            tvHori.setPadding(50, 0, 0, 0);
            tvVert.setPadding(50, 0, 0, 0);
            tvProf.setPadding(50, 0, 0, 0);
            tvRemq.setPadding(50, 0, 0, 0);

            ///SET BUTTON DELETE
            deleteBut.setText("Supprimer ce réglage");
            deleteBut.setTextColor(Color.WHITE);
            deleteBut.setOnClickListener(showDeleteReglage(grad.getIdGraduation()));
            deleteBut.setBackgroundColor(Color.parseColor("#BF1B00"));

            llGrad.addView(tvHori);
            llGrad.addView(tvVert);
            llGrad.addView(tvProf);
            llGrad.addView(tvRemq);
            llGrad.addView(deleteBut);

            //FIN LL
            //ADD TO VIEWS
            regBut.requestLayout();
            llGrad.setVisibility(View.GONE);


            regBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (llGrad.getVisibility() == View.VISIBLE) {
                        llGrad.setVisibility(View.GONE);
                    } else {
                        llGrad.setVisibility(View.VISIBLE);
                    }
                }
            });

            ll.addView(regBut);
            ll.addView(llGrad);
        }
    }

    private View.OnClickListener showDeleteReglage(final int idGrad) {
        return new View.OnClickListener() {


            public void onClick(View v) {

                final AlertDialog d = new AlertDialog.Builder(v.getContext())
                        .setTitle(R.string.delete_reglage)
                        .setMessage("Voulez vous supprimer ce réglage définitivement ?")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteGraduation(idGrad);
                                setupPage();
                            }
                        })
                        .setNegativeButton("Annuler", null)
                        .create();

                d.show();
            }

        };
    }


    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.arc, menu);
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
                showAddReglage();
                return true;
            case R.id.action_delete:
                showDeleteArc();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteArc() {
        final AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_arc)
                .setMessage("Voulez vous supprimer l'arc " + nomArc + " et ses réglages définitivement ?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteArc(idArc);
                        Intent intent = new Intent(getApplicationContext(), ArcActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Annuler", null)
                .create();

        d.show();
    }


    protected void showAddReglage() {
        final AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(R.string.add_reg)
                .setView(this.getLayoutInflater().inflate(R.layout.popup_add_reglage, null))
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
                Editdistance = (EditText) ((Dialog) d).findViewById(R.id.popup_distance_reg);
                Edithorizontal = (EditText) ((Dialog) d).findViewById(R.id.popup_horizontal_reg);
                Editvertical = (EditText) ((Dialog) d).findViewById(R.id.popup_vertical_reg);
                Editprofondeur = (EditText) ((Dialog) d).findViewById(R.id.popup_profondeur_reg);
                Editremarque = (EditText) ((Dialog) d).findViewById(R.id.popup_remarque_reg);


                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View dialog) {
                        boolean submit = true;

                        String distance = Editdistance.getText().toString();
                        String horizontal = Edithorizontal.getText().toString();
                        String vertical = Editvertical.getText().toString();
                        String profondeur = Editprofondeur.getText().toString();
                        String remarque = Editremarque.getText().toString();

                        if (distance.isEmpty()) {
                            Editdistance.setError(getString(R.string.distance_not_null));
                            submit = false;
                        }
                        if (horizontal.isEmpty()) {
                            Edithorizontal.setError(getString(R.string.horizontal_not_null));
                            submit = false;
                        }
                        if (vertical.isEmpty()) {
                            Editvertical.setError(getString(R.string.vertical_not_null));
                            submit = false;
                        }
                        if (profondeur.isEmpty()) {
                            Editprofondeur.setError(getString(R.string.profondeur_not_null));
                            submit = false;
                        }


                        if (submit) {

                            db.addGraduation(new Graduation(arcSelect.getIdArc(), 0, Integer.valueOf(distance), remarque, Float.parseFloat(horizontal), Float.parseFloat(vertical), Float.parseFloat(profondeur)));
                            ListreglageFragments(arcSelect.getIdArc());
                            d.dismiss();
                        }
                    }
                });
            }
        });
        d.show();
    }


}
