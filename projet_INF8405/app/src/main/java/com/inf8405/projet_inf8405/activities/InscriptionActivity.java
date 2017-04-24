package com.inf8405.projet_inf8405.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.inf8405.projet_inf8405.R;
import com.inf8405.projet_inf8405.fireBaseHelper.UserDBHelper;
import com.inf8405.projet_inf8405.model.User;
import com.inf8405.projet_inf8405.services.LocationService;
import com.inf8405.projet_inf8405.services.NetworkStatusService;

import java.io.ByteArrayOutputStream;

public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    private FloatingActionButton creerProfileBtn;
    private FloatingActionButton photoButton;
    private EditText email;
    private EditText mdpasse;
    private EditText userName;
    private EditText description;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private String itemSpinner;
    private Bitmap capturedImage;
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private static final int PERMISSION_LOCATION = 101;
    private GoogleApiClient googleApiClient;
    private LocationManager mLocationManager = null;
    boolean gps_enabled, network_enabled = false;
    private Location lastLocation;
    private static final String TAG = "INSCRIPTION ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Firebase.setAndroidContext(this);
        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        creerProfileBtn = (FloatingActionButton) findViewById(R.id.creerProButton);
        photoButton = (FloatingActionButton) findViewById(R.id.pictureButton);
        email = (EditText) findViewById(R.id.userEmailRegister);
        mdpasse = (EditText) findViewById(R.id.userMdpasseRegister);
        description = (EditText) findViewById(R.id.user_description);
        userName = (EditText) findViewById(R.id.user_name);

        creerProfileBtn.setOnClickListener(this);
        photoButton.setOnClickListener(this);

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if(view == creerProfileBtn) {
            creerProfile();
        }

        if(view == photoButton) {
            onLaunchCamera();
        }
    }


    //verifier les permissions de l application
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onLaunchCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // lancer la camera si la permission est donne
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close and crash
            }
        }
        if (requestCode == PERMISSION_LOCATION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                initialiserLocationManager();
                startService(new Intent(this, LocationService.class));
            }
        } else {
            //on a pas acces a la localisation (probleme)
            Toast.makeText(this, "Nous avons besoin de votre localisation!", Toast.LENGTH_SHORT).show();
        }
    }

    private void creerProfile() {
        String userEmail = email.getText().toString().trim();
        String userMdp = mdpasse.getText().toString().trim();
        String nom = userName.getText().toString().trim();

        if(userEmail.isEmpty() || userMdp.isEmpty() ||  capturedImage == null || nom.isEmpty()) { //
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
                                User user = new User(userId,userName.getText().toString().trim(), desc, imageURI, lastLocation.getLongitude(), lastLocation.getLatitude(), itemSpinner);
                                UserDBHelper.getInstance().setCurrentUser(user);
                                UserDBHelper.getInstance().setUserProfileRef(userId);
                                UserDBHelper.getInstance().addUserChild(user);

                                Toast.makeText(InscriptionActivity.this, "Céation du profile avec succès !", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(InscriptionActivity.this, MapsActivity.class));
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

    @Override
    protected void onStart() {
        super.onStart();
        NetworkStatusService.checkConnectivity(getApplicationContext());
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "Connected to Google Play Services!");
        initialiserLocationManager();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void initialiserLocationManager() {
        // appel au service de localisation si on a la permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // You don't have the permission you need to request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
        } else {
            // You have the permission.
            if (mLocationManager == null) {
                mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            }
            try {
                gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                Log.e(TAG, "fail to request location via GPS", ex);
            }

            try {
                network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
                Log.e(TAG, "fail to request location via NETWORK", ex);
            }

            if (!gps_enabled && !network_enabled) {
                checkUserLocationEnabled();
            }

            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            Log.e(TAG, "Connected to Google Play Services!"+lastLocation);
        }
    }

    private void checkUserLocationEnabled() {
        // notify user
        AlertDialog.Builder dialog = new AlertDialog.Builder(InscriptionActivity.this);
        dialog.setMessage(InscriptionActivity.this.getResources().getString(R.string.gps_network_not_enabled));
        dialog.setPositiveButton(InscriptionActivity.this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                InscriptionActivity.this.startActivity(myIntent);
                //get gps
            }
        });
        dialog.setNegativeButton(getApplicationContext().getString(R.string.Cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                checkUserLocationEnabled();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

}
