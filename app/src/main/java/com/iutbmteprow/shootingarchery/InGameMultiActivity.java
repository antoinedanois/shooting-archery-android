package com.iutbmteprow.shootingarchery;


import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.Partie;
import com.iutbmteprow.shootingarchery.dbman.Utilisateur;

public class InGameMultiActivity extends Activity {

    int nPlayers;
    int actualPlayerID;
    String actualPlayerNom;
    int actualPlayerNumber;
    DBHelper db;
    private int noVolee;
    private int noManche;
    private Partie partie;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game_multi);

        db = new DBHelper(this);
        loadAttributes();
        readPreferences();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actualPlayerNumber=position+1;
                Log.e("dev","actual player: "+actualPlayerNumber);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void loadAttributes(){
        actualPlayerNumber=0;
    }

    private void readPreferences() {
        SharedPreferences sp = getSharedPreferences("partie", Context.MODE_PRIVATE);
        nPlayers = sp.getInt("nPlayers", 1);
        actualPlayerID = sp.getInt("idUtilisateur" + actualPlayerNumber, 0);
        actualPlayerNom= sp.getString("NomUtilisateur" + actualPlayerNumber, null);

        SharedPreferences.Editor editor =sp.edit();
        editor.putInt("currentPlayer",0);
        editor.commit();
        readActualPlayerData();
    }

    private void readActualPlayerData(){
        SharedPreferences sp = getSharedPreferences("partie", Context.MODE_PRIVATE);
        noVolee = sp.getInt("p1currentVolee", 0);
        noManche = sp.getInt("p1currentManche", 0);
//        Log.e("p1p", "" + sp.getInt("p1idPartie", 0));

        partie = db.getPartie(sp.getInt("p1idPartie", 0));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.in_game, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_photo:
                if (noVolee > partie.getNbVolees()) {
                    if (noManche == partie.getNbManches()) {
                        showTooManyPlayError();
                        break;
                    } else {
                        noManche++;
                        noVolee = 1;
                    }
                }
                Intent intent = new Intent(this, ManualScoreActivityv2.class);
                intent.putExtra("noVolee", noVolee);
                intent.putExtra("noManche", noManche);
                intent.putExtra("exterieur", partie.isExterieur());
                intent.putExtra("competition", partie.isCompetition());
                startActivityForResult(intent, 1);
                break;
//            case R.id.action_finseance:
//                terminerPartie();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTooManyPlayError() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.error));
        alertDialog.setMessage(getString(R.string.toomanyplay));
        alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Rien...
            }
        });
        alertDialog.setIcon(R.drawable.warning_dark);
        alertDialog.show();
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            actualPlayerNumber=position;

            position+=1;

            actualPlayerNom=getName(getID(actualPlayerNumber));
            return PlaceholderFragment.newInstance(position + 1,actualPlayerNom);
        }

        public int getID(int playerNumber){
            SharedPreferences sp = getSharedPreferences("partie", Context.MODE_PRIVATE);
            int id = sp.getInt("idUtilisateur"+(playerNumber), 0);
            return id;
        }

        private String getName(int id){
            db=new DBHelper(getApplicationContext());
            Utilisateur player=db.getUtilisateur(id);
            return player.getPrenom()+" "+player.getNom();
        }

        @Override
        public int getCount() {
            // Show n total pages.
            return nPlayers;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Player 1";
                case 1:
                    return "Player 2";
                case 2:
                    return "Player 3";
                case 3:
                    return "Player 4";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_PLAYER_NAME = "playerName";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String playerName) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_PLAYER_NAME, playerName);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_in_game_multi, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.playerName);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            textView.setText(getArguments().getString(ARG_PLAYER_NAME));
            return rootView;
        }
    }
}
