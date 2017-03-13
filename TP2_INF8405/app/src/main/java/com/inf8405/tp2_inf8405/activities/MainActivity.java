package com.inf8405.tp2_inf8405.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
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
import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.User;

public class MainActivity extends AppCompatActivity {

    private boolean quitterApp = false;
    private User user;
    private Group group;
    private static final int REQUEST_IMAGE_CAPTURE = 111;

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
                creerProfile();
            }
        });

        final Button photoButton = (Button) findViewById(R.id.pictureButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                onLaunchCamera();
            }
        });

    }

    //verifier les permissions de l application
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onLaunchCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // lancer la camera si la permission est donne
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }

    // mettre la photo dans le cadre specifie et l encoder pour la sauvegarder sur firebase
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == this.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageLabel = (ImageView) findViewById(R.id.photoIamgeView);
            mImageLabel.setImageBitmap(imageBitmap);
            encodeBitmapAndSaveToFirebase(imageBitmap);
        }
    }

    // parcourir la bd pour stocker la reference a l image capture
    private void encodeBitmapAndSaveToFirebase(Bitmap imageBitmap) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
//        DatabaseReference ref = FirebaseDatabase.getInstance()
//                .getReference("groups")
//                .child("group2").child("users").child("user1")
//                .child("pictureURI");
//        ref.setValue(imageEncoded);
    }

    private void creerProfile() {

        user = new User();
//        user = new User(nomUtilisateur.getText().toString(), String pictureURI, boolean organisateur, double longitude, double latitude,
//        Group group, boolean writePermission);
        // get EditText by id
        EditText nomUtilisateur = (EditText) findViewById(R.id.userNameEditText);
        // Store EditText in Variable
        user.setUsername(nomUtilisateur.getText().toString());

        EditText nomGroupe = (EditText) findViewById(R.id.groupEditText);
        group = new Group();
        group.setNomGroupe(nomGroupe.getText().toString());

        ImageView photoImageView = (ImageView) findViewById(R.id.photoIamgeView);
        Bitmap imageBitmap = photoImageView.getDrawingCache();
        user.setPictureURI(imageBitmap.toString());

        if (user.getUsername() != null && group.getNomGroupe() != null && user.getPictureURI() != null) {
            // save data


            // continue to next activity with relevant data.
            Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(myIntent);
        }
        else
        {
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Erreur")
                    .setMessage("Il faut un nom d'utilisateur et une photo ainsi qu'un groupe !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        if(!quitterApp){
            Toast.makeText(this, "Appuiez une deuxième fois si vous êtes sûr de vouloir quitter.", Toast.LENGTH_SHORT).show();
            quitterApp = true;
//            mettre un timer pour remettre quitterApp a false // TODO: 17-03-12
        }
        else{
            finish();
        }
    }
}
