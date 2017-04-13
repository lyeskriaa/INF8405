package com.inf8405.projet_inf8405.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.inf8405.projet_inf8405.R;
import com.inf8405.projet_inf8405.fireBaseHelper.UserDBHelper;
import com.inf8405.projet_inf8405.model.User;

import java.io.ByteArrayOutputStream;

public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    private FloatingActionButton creerProfileBtn;
    private EditText email;
    private EditText mdpasse;
    private EditText userName;
    private EditText description;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private String itemSpinner;
    private Bitmap capturedImage;
    private static final int REQUEST_IMAGE_CAPTURE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        creerProfileBtn = (FloatingActionButton) findViewById(R.id.creerProButton);
        email = (EditText) findViewById(R.id.userEmailRegister);
        mdpasse = (EditText) findViewById(R.id.userMdpasseRegister);
        description = (EditText) findViewById(R.id.user_description);
        userName = (EditText) findViewById(R.id.user_name);

        creerProfileBtn.setOnClickListener(this);


        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.sexe_option, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSpinner = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getBaseContext(), item+ "selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view == creerProfileBtn) {
            creerProfile();
        }
    }

    private void creerProfile() {
        String userEmail = email.getText().toString().trim();
        String userMdp = mdpasse.getText().toString().trim();
        String nom = userName.getText().toString().trim();

        if(userEmail.isEmpty() || userMdp.isEmpty() ||  nom.isEmpty()) { //capturedImage == null ||
            // email empty or pwd empty
            AlertDialog dialog = new AlertDialog.Builder(InscriptionActivity.this)
                    .setTitle("Erreur")
                    .setMessage("Il faut remplir les informations manquantes !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
        else {
            progressDialog.setMessage("Création du profile...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userMdp)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()) {
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                String desc = description.getText().toString().trim();
                                String imageURI = encodeBitmap(capturedImage);
                                // TODO: 17-04-12  location
                                User user = new User(userId,userName.getText().toString().trim(), desc, imageURI, -73.6146566, 45.5045971, itemSpinner);
                                UserDBHelper.getInstance().setUserProfileRef(userName.getText().toString());
                                UserDBHelper.getInstance().setCurrentUser(user);
                                UserDBHelper.getInstance().addUserChild(user);

                                Toast.makeText(InscriptionActivity.this, "Céation du profile avec succès !", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                Toast.makeText(InscriptionActivity.this, "Échec de la céation du profile, veuillez reéssayer !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    // mettre la photo dans le cadre specifie et l encoder pour la sauvegarder sur firebase
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == this.RESULT_OK) {
            Bundle extras = data.getExtras();
            capturedImage = (Bitmap) extras.get("data");
            ImageView mImageLabel = (ImageView) findViewById(R.id.photoIamgeView);
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
