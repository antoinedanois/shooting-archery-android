package com.iutbmteprow.shootingarchery;

import java.util.ArrayList;
import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.PartieGlobal;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class SortResultActivity extends ListActivity {

    DBHelper db;
    ArrayList<PartieGlobal> parties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        db = new DBHelper(this);
    }

    private void searchParties() {
        Integer typeCible = 0;
        if (getIntent().getExtras().containsKey("typeCible"))
            typeCible = getIntent().getIntExtra("typeCible", 0);
        Boolean entr_compet = null;
        if (getIntent().getExtras().containsKey("competition"))
            entr_compet = getIntent().getBooleanExtra("competition", false);

        Boolean environnement = null;
        if (getIntent().getExtras().containsKey("exterieur"))
            environnement = getIntent().getBooleanExtra("exterieur", false);
        parties =  db.sortAll(getIntent().getIntExtra("utilisateurId", 0), entr_compet, typeCible, environnement);

        if (parties.size() == 0 ) {
            setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,new String[] {}));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.no_result)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    }).show();
            return;
        }

        String[] values = new String[parties.size()];

        for (int i=0;i<parties.size();i++) {
            values[i] = DBHelper.getDateString(parties.get(i).getDatePartie(), this);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,values);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchParties();
    }

    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (parties.get(position).getId() != 0){
            if (parties.get(position).getTypePartie() == "Classique"){
                Intent intent = new Intent(this,ViewGameActivityv2.class);
                intent.putExtra("idPartie", parties.get(position).getId());
                startActivity(intent);
            }
            if (parties.get(position).getTypePartie() == "Campagne"){
                Intent intent = new Intent(this,ViewGameActivityCamp.class);
                intent.putExtra("idCampagne", parties.get(position).getId());
                startActivity(intent);
            }
        }
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

}
