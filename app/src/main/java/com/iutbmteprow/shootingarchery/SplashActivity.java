package com.iutbmteprow.shootingarchery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    private final static int DELAY = 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Un handler permet de lancer un traitement dans le futur
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeSplash();
            }
        }, DELAY);
    }

    private void closeSplash() {
        SplashActivity.this.finish();
        Intent mainIntent = new Intent(SplashActivity.this, AccueilActivity.class);
        SplashActivity.this.startActivity(mainIntent);
    }
}