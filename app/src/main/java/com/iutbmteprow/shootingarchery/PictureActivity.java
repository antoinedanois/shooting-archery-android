package com.iutbmteprow.shootingarchery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class PictureActivity extends Activity {

    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mediaChooser = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(mediaChooser, 1);

        setContentView(R.layout.activity_picture);

        photo = (ImageView) findViewById(R.id.photo);
    }

    private void editPicture() {
        //TODO : Mode manuel...
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                photo.setImageBitmap((Bitmap) data.getExtras().get("data"));
            }
        } else {
            onBackPressed();
        }
    }

    private void saveData() {
        //TODO : Enregistrer dans la base de donnees
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancelPicture:
                onBackPressed();
                break;
            case R.id.action_okPicture:
                saveData();
                break;
            case R.id.action_editPicture:
                editPicture();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
