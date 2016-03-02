package com.iutbmteprow.shootingarchery.weblink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iutbmteprow.shootingarchery.R;
import com.iutbmteprow.shootingarchery.dbman.DBHelper;
import com.iutbmteprow.shootingarchery.dbman.Partie;
import com.iutbmteprow.shootingarchery.dbman.Tirer;

import java.util.ArrayList;
import java.util.HashMap;

public class WeblinkActivity extends Activity {

    public static final String WEB_LINK_SETTINGS = "com.iutbmteprow.shootingarchery.WEB_LINK_SETTINGS";
    public static final String CURRENT_USER_EMAIL = "current_user_email";
    public static final String CURRENT_USER_TOKEN = "current_user_token";
    public static final String WEB_URL = "web_url";
    public static final String DEFAULT_URL = "http://76394721.ngrok.io";

    DBHelper db;
    SharedPreferences sp;
    String current_user_token;
    int idPartie;
    Partie partie;
    String payload;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_weblink);
        sp = getSharedPreferences(WEB_LINK_SETTINGS, Context.MODE_PRIVATE);
        String current_user_email = sp.getString(CURRENT_USER_EMAIL, "");
        current_user_token = sp.getString(CURRENT_USER_TOKEN, "");

        TextView current_user_text = (TextView) findViewById(R.id.current_user_view);

        current_user_text.setText("Vous êtes connecté en tant que " + current_user_email);


        if (current_user_email.isEmpty() || current_user_token.isEmpty()) {
            Intent loginIntent = new Intent(WeblinkActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        }

        db = new DBHelper(getApplicationContext());
        gson = new Gson();
        idPartie = getIntent().getIntExtra("idPartieToUpload", 0);
        partie = db.getPartie(idPartie);
        ArrayList<Tirer> tirs = db.getTirerOf(partie.getIdPartie());
        HashMap<String, Object> data = new HashMap<>();
        data.put("partie", partie);
        data.put("tirs", tirs);
        payload = gson.toJson(data);


        Button publishButton = (Button) findViewById(R.id.button_publish);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.web_link_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Log.e("CASE", "EXECUTED");
                NavUtils.navigateUpFromSameTask(this);

                return true;
            case R.id.disconnect:
                Log.e("CASE", "EXECUTED");
                disconnect();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        sp = getSharedPreferences(WEB_LINK_SETTINGS, Context.MODE_PRIVATE);
        String current_user_email = sp.getString(CURRENT_USER_EMAIL, "");
        String current_user_token = sp.getString(CURRENT_USER_TOKEN, "");
        if (current_user_email.isEmpty() || current_user_token.isEmpty()) {
            onBackPressed();
        }

    }

    private void disconnect() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(WeblinkActivity.CURRENT_USER_EMAIL);
        editor.remove(WeblinkActivity.CURRENT_USER_TOKEN);
        editor.apply();
        onBackPressed();
    }

    private void publish() {
        Log.e("TEST", "TEST");
        new sendData().execute(DEFAULT_URL, current_user_token, payload);
    }

    public class sendData extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            PostJson post = new PostJson();
            String url = params[0];
            String api_token = params[1];
            String payload = params[2];
            String formatedJson = "{\"api_token\":\"" + api_token + "\", \"payload\":" + payload + "}";
            Log.e("FORMATED", formatedJson);
            Boolean response = post.post(url + "/api/game/create", formatedJson);
            return response;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Toast toast = Toast.makeText(getApplicationContext(),"Résultats publiés", Toast.LENGTH_LONG);
            if (aBoolean){
                toast.show();
                onBackPressed();
            } else {
                toast.setText("Une erreur est survenue");
                toast.show();
                onBackPressed();
            }
        }
    }

}
