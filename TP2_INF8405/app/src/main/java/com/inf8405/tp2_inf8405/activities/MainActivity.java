package com.inf8405.tp2_inf8405.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.inf8405.tp2_inf8405.R;
import com.inf8405.tp2_inf8405.model.UserProfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_FILENAME = "TP2Files/TP2_perfs.file";
    public static final String PHOTO_FILENAME = "TP2Files/TP2_photo.pict";
    public static final String USERNAME_PREF  = "username_preference";
    public static final String PICTFILE_PREF  = "pictFile_preference";

    private SharedPreferences settings; //= getSharedPreferences(PREFS_FILENAME, 0)
    File prefsFile = new File(PREFS_FILENAME);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profileadapter = new ProfileAdapter(this);
        Firebase.setAndroidContext(this);

        //Get buttons
        Button startbutton = (Button) findViewById(R.id.but_crt_usr);
        //Set eventlistener on buttons
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileadapter.isEmpty()) {
                    Toast.makeText(TitleActivity.this, "First Use : Create your own profile", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TitleActivity.this, ProfileCreationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(TitleActivity.this, "Choose your option", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TitleActivity.this, MenuActivity.class);
                    startActivity(intent);
                }

                //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        if(!prefsFile.exists()) {

            File prefsFileDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/TP2Files/");
            prefsFileDirectory.mkdirs();
            prefsFile = new File(prefsFileDirectory, "TP2_perfs.file");
            try {
                FileOutputStream fos = new FileOutputStream(prefsFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        settings = getSharedPreferences(PREFS_FILENAME, 0);
        UserProfile.username = settings.getString(USERNAME_PREF, null);
        UserProfile.pictFile = settings.getString(PICTFILE_PREF, null);

        if (UserProfile.username != null && UserProfile.pictFile != null) {
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
                UserProfile.username = inputTxt.getText().toString();

                if (UserProfile.username != null && UserProfile.pictFile != null) {
                    // save data
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(USERNAME_PREF, UserProfile.username);
                    editor.putString(PICTFILE_PREF, UserProfile.pictFile);

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
                UserProfile.pictFile = PHOTO_FILENAME;

                if(newfile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(newfile.getAbsolutePath());

                    ImageView myImage = (ImageView) findViewById(R.id.photoIamgeView);
                    myImage.setImageBitmap(myBitmap);
                }
            }
        });

    }
}
