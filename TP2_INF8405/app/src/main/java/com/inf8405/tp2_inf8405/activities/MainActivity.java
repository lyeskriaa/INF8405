package com.inf8405.tp2_inf8405.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.inf8405.tp2_inf8405.R;
import com.inf8405.tp2_inf8405.dao.ProfileDao;

public class MainActivity extends AppCompatActivity {

//    public static final String PREFS_FILENAME = "TP2Files/TP2_perfs.file";
//    public static final String PHOTO_FILENAME = "TP2Files/TP2_photo.pict";
//    public static final String USERNAME_PREF  = "username_preference";
//    public static final String PICTFILE_PREF  = "pictFile_preference";

//    private SharedPreferences settings; //= getSharedPreferences(PREFS_FILENAME, 0)
//    File prefsFile = new File(PREFS_FILENAME);
    private boolean quitterApp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        final ProfileDao profileDao = new ProfileDao(this);
        Firebase.setAndroidContext(this);




//        final Button okButton = (Button) findViewById(R.id.okButton);
//        okButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // get EditText by id
//                EditText inputTxt = (EditText) findViewById(R.id.userNameEditText);
//
//                // Store EditText in Variable
//                Profile.username = inputTxt.getText().toString();
//
//                if (Profile.username != null && Profile.pictFile != null) {
//                    // save data
//                    SharedPreferences.Editor editor = settings.edit();
//                    editor.putString(USERNAME_PREF, Profile.username);
//                    editor.putString(PICTFILE_PREF, Profile.pictFile);
//
//                    // continue to next activity with relevant data.
//                    Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
//                    startActivity(myIntent);
//                }
//                else
//                {
//                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
//                            .setTitle("Erreur")
//                            .setMessage("Il faut un nom d'utilisateur et une photo.")
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
//                }
//            }
//        });

//        final Button photoButton = (Button) findViewById(R.id.pictureButton);
//        photoButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
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
//            }
//        });

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
