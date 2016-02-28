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
public class ConseilTirActivity extends Activity {

    Button btn_echauf;
    Button btn_secu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_tir);
        btn_echauf = (Button) findViewById(R.id.btn_tir_echauf);
        btn_secu = (Button) findViewById(R.id.btn_tir_secu);
        btn_echauf.setOnClickListener(handleClick);
        btn_secu.setOnClickListener(handleClick);
        setupActionBar();
    }

    protected View.OnClickListener handleClick = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_tir_echauf:
                    if (findViewById(R.id.layout_echauf).getVisibility() == View.VISIBLE) {
                        ((LinearLayout) findViewById(R.id.layout_echauf)).setVisibility(View.GONE);
                    } else if (findViewById(R.id.layout_echauf).getVisibility() == View.GONE) {
                        ((LinearLayout) findViewById(R.id.layout_echauf)).setVisibility(View.VISIBLE);
                    }

                    break;
                case R.id.btn_tir_secu:
                    if (findViewById(R.id.layout_secu).getVisibility() == View.VISIBLE) {
                        ((LinearLayout) findViewById(R.id.layout_secu)).setVisibility(View.GONE);
                    } else if (findViewById(R.id.layout_secu).getVisibility() == View.GONE) {
                        ((LinearLayout) findViewById(R.id.layout_secu)).setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.monarc:
                    Intent intent3 = new Intent(view.getContext(), ArcActivity.class);
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
