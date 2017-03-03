package com.inf8405.tp2_inf8405;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_FILENAME = "TP2Files/TP2_perfs.file";
    public static final String PHOTO_FILENAME = "TP2Files/TP2_photo.pict";
    public static final String USERNAME_PREF  = "username_preference";
    public static final String PICTFILE_PREF  = "pictFile_preference";

    private SharedPreferences settings = getSharedPreferences(PREFS_FILENAME, 0);
    Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserData.username = settings.getString(USERNAME_PREF, null);
        UserData.pictFile = settings.getString(PICTFILE_PREF, null);

        if (UserData.username != null && UserData.pictFile != null) {
            // continue to next activity with relevant data.
            Intent myIntent = new Intent(this, MapActivity.class);
            startActivity(myIntent);
        }

        final Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get EditText by id
                EditText inputTxt = (EditText) findViewById(R.id.userNameEditText);

                // Store EditText in Variable
                UserData.username = inputTxt.getText().toString();

                if (UserData.username != null && UserData.pictFile != null) {
                    // save data
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(USERNAME_PREF, UserData.username);
                    editor.putString(PICTFILE_PREF, UserData.pictFile);

                    // continue to next activity with relevant data.
                    Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(myIntent);
                }
                else
                {
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Erreur")
                            .setMessage("Il faut un nom d'utilisateur et une photo.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        final Button photoButton = (Button) findViewById(R.id.pictureButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String file = PHOTO_FILENAME;
                File newfile = new File(file);
                if (newfile.exists()) {
                    newfile.delete();
                    newfile = new File(file);
                }
                try {
                    newfile.createNewFile();
                }
                catch (IOException e)
                {
                }

                Uri outputFileUri = Uri.fromFile(newfile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, 1);
                UserData.pictFile = PHOTO_FILENAME;

                if(newfile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(newfile.getAbsolutePath());

                    ImageView myImage = (ImageView) findViewById(R.id.photoIamgeView);
                    myImage.setImageBitmap(myBitmap);
                }
            }
        });

    }
}
