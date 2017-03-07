package com.inf8405.tp2_inf8405.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.inf8405.tp2_inf8405.R;
import com.inf8405.tp2_inf8405.dao.ProfileDao;
import com.inf8405.tp2_inf8405.model.User;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private boolean quitterApp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ProfileDao profileDao = new ProfileDao(this);
        Firebase.setAndroidContext(this);

        if (profileDao.isEmpty()) {
            Toast.makeText(this, "Veuillez créer votre profile", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Veuillez choisir une action", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, MenuActivity.class);
//            startActivity(intent);
        }


        final Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
<<<<<<< HEAD
                creerProfile();
=======
                // get EditText by id
                EditText inputTxt = (EditText) findViewById(R.id.userNameEditText);

                // Store EditText in Variable
                User.username = inputTxt.getText().toString();

                if (User.username != null && User.pictFile != null) {
                    // save data
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(USERNAME_PREF, User.username);
                    editor.putString(PICTFILE_PREF, User.pictFile);

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
>>>>>>> eadb1117de0e808070e20b9687938ed968defd1f
            }
        });

        final Button photoButton = (Button) findViewById(R.id.pictureButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
<<<<<<< HEAD
//                String file = PHOTO_FILENAME;
//                File newfile = new File(file);
//                if (newfile.exists()) {
//                    newfile.delete();
//                    newfile = new File(file);
//                }
//                try {
//                    newfile.createNewFile();
//                }
//                catch (IOException e)
//                {
//                }
//
//                Uri outputFileUri = Uri.fromFile(newfile);
//
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//
//                startActivityForResult(cameraIntent, 1);
//                Profile.pictFile = PHOTO_FILENAME;
//
//                if(newfile.exists()){
//
//                    Bitmap myBitmap = BitmapFactory.decodeFile(newfile.getAbsolutePath());
//
//                    ImageView myImage = (ImageView) findViewById(R.id.photoIamgeView);
//                    myImage.setImageBitmap(myBitmap);
//                }
=======
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
                User.pictFile = PHOTO_FILENAME;

                if(newfile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(newfile.getAbsolutePath());

                    ImageView myImage = (ImageView) findViewById(R.id.photoIamgeView);
                    myImage.setImageBitmap(myBitmap);
                }
>>>>>>> eadb1117de0e808070e20b9687938ed968defd1f
            }
        });

    }

    private void creerProfile() {
        // get EditText by id
        EditText inputTxt = (EditText) findViewById(R.id.userNameEditText);

        // Store EditText in Variable
        Profile.username = inputTxt.getText().toString();

        if (Profile.username != null && Profile.pictFile != null) {
            // save data
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(USERNAME_PREF, Profile.username);
            editor.putString(PICTFILE_PREF, Profile.pictFile);

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

    @Override
    public void onBackPressed() {
        if(!quitterApp){
            Toast.makeText(this, "Appuiez une deuxième fois si vous êtes sûr de vouloir quitter.", Toast.LENGTH_SHORT).show();
            quitterApp = true;
//            mettre un timer pour remettre quitterApp a false
        }
        else{
            finish();
        }
    }
}
