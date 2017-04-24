package com.inf8405.projet_inf8405.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inf8405.projet_inf8405.R;
import com.inf8405.projet_inf8405.fireBaseHelper.ChatDBHelper;
import com.inf8405.projet_inf8405.fireBaseHelper.UserDBHelper;
import com.inf8405.projet_inf8405.model.User;
import com.inf8405.projet_inf8405.utils.Enum;

//login activity
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
            //finish();
            //startActivity(new Intent(MainActivity.this, MapsActivity.class));
        }

        btnInscription = (Button) findViewById(R.id.InscriptionButton);
        btnConnexion = (Button) findViewById(R.id.ConnexionButton);
        emailLogin = (EditText) findViewById(R.id.userEmailLogin);
        mdpLogin = (EditText) findViewById(R.id.userMdpasseLogin);

        btnConnexion.setOnClickListener(this);
        btnInscription.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

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
                    Toast.makeText(MainActivity.this,"Connexion réussie ! ",Toast.LENGTH_SHORT).show();
                    final String userID = firebaseAuth.getCurrentUser().getUid().toString();

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Enum.USERS.toString());//.push().getRef();
                    final Query query = reference.getParent().getRef();
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //Log.e("MAIN ACTIVITY ", " DATA CHANGE: " + snapshot.getKey());

                            if (snapshot.child(Enum.USERS.toString()).hasChildren()) {
                               // Log.e("HAS CHILDREN ", " DATA CHANGE: " + snapshot.getKey());
                                for (DataSnapshot child : snapshot.child(Enum.USERS.toString()).getChildren()) {
                                    Log.e("MAIN ACTIVITY ", " child : " + child.getKey());
                                    User user = UserDBHelper.getInstance().readData(child);
                                    if(user.getUsername() != null && user.getId().equals(userID)) {
                                        UserDBHelper.getInstance().setCurrentUser(user);
                                    }
                                }
                                for (DataSnapshot chat : snapshot.child(Enum.CHATS.toString()).getChildren()) {
                                    ChatDBHelper.getInstance().readData(chat);
                                }
                                if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // nothing here
                        }
                    });
                    UserDBHelper.getInstance().setUserProfileRef(firebaseAuth.getCurrentUser().getUid());
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
