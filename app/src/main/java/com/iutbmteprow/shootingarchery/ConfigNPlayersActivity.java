//package com.iutbmteprow.shootingarchery;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.app.Activity;
//import android.support.v4.app.NavUtils;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.SpinnerAdapter;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ConfigNPlayersActivity extends Activity {
//
//    private Spinner nPlayerSpinner=null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_config_nplayers);
//
//        // Show the Up button in the action bar.
//        setupActionBar();
//
//        loadAttributes();
//        setupSpinner();
//    }
//
//    private void loadAttributes() {
//        nPlayerSpinner = (Spinner) findViewById(R.id.config1_userQtySpin);
//    }
//
//    private int findTextId(SpinnerAdapter adapter, String nomUser) {
//        int retour = 0;
//        for (int i=0; i<adapter.getCount();i++) {
//            if (nomUser.equals(adapter.getItem(i).toString())) {
//                return i;
//            }
//        }
//        return retour;
//    }
//
//    private void setupSpinner() {
//        //Remplissage Spinner
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, getSpinnerElements());
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        nPlayerSpinner.setAdapter(adapter);
//
//    }
//
//    private List<String> getSpinnerElements(){
//        List<String> players=new ArrayList<>();
//        players.add("1");
//        players.add("2");
//        players.add("3");
//        players.add("4");
//
//        return players;
//    }
//
//    /**
//     * Set up the {@link android.app.ActionBar}.
//     */
//    private void setupActionBar() {
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.config, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_next:
//
//                Intent intent = new Intent(this, MultiplayerSelectionActivity.class);
//                intent.putExtra("playersQty",nPlayerSpinner.getSelectedItem().toString());
//                startActivity(intent);
//                return true;
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}
