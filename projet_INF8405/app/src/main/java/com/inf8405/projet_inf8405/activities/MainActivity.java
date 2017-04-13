package com.inf8405.projet_inf8405.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.inf8405.projet_inf8405.R;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {

    private Button btnInscription;
    private Button btnConnexion;
    private EditText emailLogin;
    private EditText mdpLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            //appel a maps activity ou profileActivity avec les bonnes donnes
        }

        btnInscription = (Button) findViewById(R.id.InscriptionButton);
        btnConnexion = (Button) findViewById(R.id.ConnexionButton);
        emailLogin = (EditText) findViewById(R.id.userEmailLogin);
        mdpLogin = (EditText) findViewById(R.id.userMdpasseLogin);

        btnConnexion.setOnClickListener(this);
        btnInscription.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
//        final Button photoButton = (Button) findViewById(R.id.pictureButton);
//        photoButton.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            public void onClick(View v) {
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }
//            }
//        });
    }


    @Override
    public void onClick(View v) {
        if (v == btnInscription) {
            Intent inscription = new Intent(MainActivity.this, InscriptionActivity.class);
            finish();
            startActivity(inscription);
        }

        if (v == btnConnexion) {
            connecterUtilisateur();
        }
    }

    private void connecterUtilisateur() {
        String email = emailLogin.getText().toString().trim();
        String mdpasse = mdpLogin.getText().toString().trim();

        if (email.isEmpty() || mdpasse.isEmpty()) {
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Erreur")
                    .setMessage("Il faut un email et un mot de passe !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        progressDialog.setMessage("Authentification en cours...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,mdpasse).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    Toast.makeText(MainActivity.this,"Connexion réussie !",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(MainActivity.this, MapsActivity.class));
                }
                else {
                    Toast.makeText(MainActivity.this,"impossible de se connecter !",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
