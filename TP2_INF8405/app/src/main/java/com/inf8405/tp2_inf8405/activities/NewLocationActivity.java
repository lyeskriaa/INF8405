package com.inf8405.tp2_inf8405.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.inf8405.tp2_inf8405.R;
import com.inf8405.tp2_inf8405.model.Coordinate;
import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.Lieu;

import java.io.ByteArrayOutputStream;

public class NewLocationActivity extends AppCompatActivity {

    private Bitmap capturedImage;
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private double longitude;
    private double latitude;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);
        Bundle b = getIntent().getExtras();
        longitude = -1; // or other values
        if(b != null) longitude = b.getDouble("longitude");
        latitude = -1; // or other values
        if(b != null) latitude = b.getDouble("latitude");

        final Button okButton = (Button) findViewById(R.id.newloc_okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText nomLieu = (EditText) findViewById(R.id.newloc_NameEditText);
                name = nomLieu.getText().toString();
                if (capturedImage == null || name == null) {
                    AlertDialog dialog = new AlertDialog.Builder(NewLocationActivity.this)
                            .setTitle("Erreur")
                            .setMessage("Il faut un nom de lieu et une photo!")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    Group.getGroup().addLoc(new Lieu(new Coordinate(longitude, latitude), name, encodeBitmap(capturedImage),0));
                    finish();
                }
            }
        });

        final Button photoButton = (Button) findViewById(R.id.newloc_pictureButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(NewLocationActivity.this.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }



    //TODO adjust
    // mettre la photo dans le cadre specifie et l encoder pour la sauvegarder sur firebase
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == this.RESULT_OK) {
            Bundle extras = data.getExtras();
            capturedImage = (Bitmap) extras.get("data");
            ImageView mImageLabel = (ImageView) findViewById(R.id.newloc_photoIamgeView);
            mImageLabel.setImageBitmap(capturedImage);
        }
    }


    // copresser l'image en PNG et l'encoder en string
    private String encodeBitmap(Bitmap imageBitmap) {
        if (imageBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } else {
            return null;
        }
    }

}