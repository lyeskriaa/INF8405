package com.inf8405.projet_inf8405.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.inf8405.projet_inf8405.R;

public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    private Button creerProfileBtn;
    private EditText email;
    private EditText mdpasse;
    private EditText description;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        creerProfileBtn = (Button) findViewById(R.id.creerProButton);
        email = (EditText) findViewById(R.id.userEmailRegister);
        mdpasse = (EditText) findViewById(R.id.userMdpasseRegister);
        description = (EditText) findViewById(R.id.user_description);

        creerProfileBtn.setOnClickListener(this);


        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.sexe_option, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
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

        if(TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userMdp)) {
            // email empty or pwd empty

        }

        progressDialog.setMessage("Création du profile...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userMdp)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // user is successfully registered and logged in
                            // we will start the profile activity here
                            // right now lets display a toast only
                            progressDialog.hide();
                            Toast.makeText(InscriptionActivity.this, "Céation du profile avec succès !", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(InscriptionActivity.this, "Échec de la céation du profile, veuillez reéssayer !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
