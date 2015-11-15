package com.iutbmteprow.shootingarchery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Geoffrey on 09/02/2015.
 */
public class ConseilActivity extends Activity {

    Button btntir;
    Button btnreg;
    Button btnarc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conseil_menu);
        setupActionBar();
        btntir = (Button) findViewById(R.id.btn_cons_tir);
        btnreg = (Button) findViewById(R.id.btn_cons_reg);
        btnarc = (Button) findViewById(R.id.btn_cons_arc);
        btntir.setOnClickListener(handleClick);
        btnreg.setOnClickListener(handleClick);
        btnarc.setOnClickListener(handleClick);
    }

    protected View.OnClickListener handleClick = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_cons_tir:
                    Intent intent = new Intent(view.getContext(), ConseilTirActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_cons_reg:
                    Intent intent2 = new Intent(view.getContext(), ConseilReglage.class);
                    startActivity(intent2);
                    break;
                case R.id.btn_cons_arc:
                    Intent intent3 = new Intent(view.getContext(), ConseilArcActivity.class);
                    startActivity(intent3);
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
