package com.iutbmteprow.shootingarchery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Geoffrey on 10/02/2015.
 */
public class ConseilReglage extends Activity {
    Button btntiller;
    Button btnband;
    Button btndetal;
    Button btnbb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conseil_reglage);
        btntiller = (Button) findViewById(R.id.btn_reg_tiller);
        btnband = (Button) findViewById(R.id.btn_reg_band);
        btndetal = (Button) findViewById(R.id.btn_reg_detal);
        btnbb = (Button) findViewById(R.id.btn_reg_berger);
        btntiller.setOnClickListener(handleClick);
        btndetal.setOnClickListener(handleClick);
        btnband.setOnClickListener(handleClick);
        btnbb.setOnClickListener(handleClick);
        setupActionBar();
    }

    protected View.OnClickListener handleClick = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_reg_tiller:
                    if( findViewById(R.id.layout_tiller).getVisibility()== View.VISIBLE){
                        ((LinearLayout) findViewById(R.id.layout_tiller)).setVisibility(View.GONE);
                    }
                    else if(findViewById(R.id.layout_tiller).getVisibility()== View.GONE){
                        ((LinearLayout) findViewById(R.id.layout_tiller)).setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btn_reg_band:
                    if( findViewById(R.id.layout_band).getVisibility()== View.VISIBLE){
                        ((LinearLayout) findViewById(R.id.layout_band)).setVisibility(View.GONE);
                    }
                    else if(findViewById(R.id.layout_band).getVisibility()== View.GONE){
                        ((LinearLayout) findViewById(R.id.layout_band)).setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btn_reg_detal:
                    if( findViewById(R.id.layout_detal).getVisibility()== View.VISIBLE){
                        ((LinearLayout) findViewById(R.id.layout_detal)).setVisibility(View.GONE);
                    }
                    else if(findViewById(R.id.layout_detal).getVisibility()== View.GONE){
                        ((LinearLayout) findViewById(R.id.layout_detal)).setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btn_reg_berger:
                    if( findViewById(R.id.layout_berger).getVisibility()== View.VISIBLE){
                        ((LinearLayout) findViewById(R.id.layout_berger)).setVisibility(View.GONE);
                    }
                    else if(findViewById(R.id.layout_berger).getVisibility()== View.GONE){
                        ((LinearLayout) findViewById(R.id.layout_berger)).setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };

    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

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
