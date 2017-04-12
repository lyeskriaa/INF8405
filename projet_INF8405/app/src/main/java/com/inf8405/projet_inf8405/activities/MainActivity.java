package com.inf8405.projet_inf8405.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inf8405.projet_inf8405.R;

public class MainActivity extends AppCompatActivity {


    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button inscriptionButton = (Button) findViewById(R.id.InscriptionButton);
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inscription = new Intent(MainActivity.this, InscriptionActivity.class);
                startActivity(inscription);
            }
        });

        final Button connexionButton = (Button) findViewById(R.id.ConnexionButton);
        connexionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText username = (EditText) findViewById(R.id.userEmailLogin);
                name = username.getText().toString();

            }
        });

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




}
