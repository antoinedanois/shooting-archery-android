package com.iutbmteprow.shootingarchery;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.Utilisateur;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerSelectionFragment extends Fragment {

    int noPlayer;
    Spinner playerSpinner=null;
    TextView gradeU=null;
    private DBHelper db;

    public PlayerSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_player_selection, container, false);
        // Inflate the layout for this fragment

        //Connection à la bdd
        db = new DBHelper(v.getContext());

        loadAttributes(v);
        setupSpinner(v);

        return v;
    }

    private void loadAttributes(View v){
        noPlayer=getArguments().getInt("noPlayer")-1;
        playerSpinner=(Spinner)v.findViewById(R.id.playerSpinner);
        gradeU=(TextView)v.findViewById(R.id.playerS_gradeU);
    }

    private void setupSpinner(View v) {
        //Remplissage Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(),
                android.R.layout.simple_spinner_item, db.getUtilisateursForSpinner());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerSpinner.setAdapter(adapter);
        playerSpinner.setSelection(noPlayer);

        playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String[] userName = playerSpinner.getSelectedItem().toString().split(" ");
                Utilisateur curUser = db.getUtilisateurFromName(userName[1], userName[0]);

                try {
                    gradeU.setText(curUser.getGrade());
                    savePreferences();
                } catch (NoSuchFieldException e) {
                    gradeU.setText("Grade inconnu...");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // ???
            }

        });
    }


    private void savePreferences()
    {

        SharedPreferences preferences = getActivity().getSharedPreferences("partie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NomUtilisateur"+noPlayer, playerSpinner.getSelectedItem().toString());
        String userSpinner = playerSpinner.getSelectedItem().toString();

        String[] nomUtil = userSpinner.split(" ");
        editor.putInt("idUtilisateur"+noPlayer, db.getUtilisateurFromName(nomUtil[1], nomUtil[0]).getId());
        if(noPlayer==0){
            editor.putInt("idUtilisateur", db.getUtilisateurFromName(nomUtil[1], nomUtil[0]).getId());
        }

        editor.commit();
    }
}
