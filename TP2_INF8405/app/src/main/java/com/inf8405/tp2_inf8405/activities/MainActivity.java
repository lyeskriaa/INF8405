package com.inf8405.tp2_inf8405.activities;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inf8405.tp2_inf8405.R;
import com.inf8405.tp2_inf8405.dao.GroupDao;
import com.inf8405.tp2_inf8405.dao.ProfileDao;
import com.inf8405.tp2_inf8405.model.Enum;
import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.User;
import com.inf8405.tp2_inf8405.services.LocationService;
import com.inf8405.tp2_inf8405.services.NetworkStatusService;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private boolean quitterApp = false;
    private User user;
    private Group group;
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private static final int PERMISSION_LOCATION = 101;
    private Bitmap capturedImage;
    private GoogleApiClient googleApiClient;
    private LocationManager mLocationManager = null;
    boolean gps_enabled, network_enabled = false;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        // TODO: 17-03-14 check if the profile dao is empty

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
    private String encodeBitmapAndSaveToFirebase(Bitmap imageBitmap) {
        if (imageBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } else {
            return null;
        }
    }


    private void creerProfile() {

        // get EditText by id
        EditText nomUtilisateur = (EditText) findViewById(R.id.userNameEditText);

        EditText nomGroupe = (EditText) findViewById(R.id.groupEditText);

        ImageView photoImageView = (ImageView) findViewById(R.id.photoIamgeView);
        String imageURI = encodeBitmapAndSaveToFirebase(capturedImage);

        if (nomUtilisateur != null && nomGroupe != null && imageURI != null) {
            // save data
            group = Group.createGroup(nomGroupe.getText().toString());
            // TODO: 17-03-21  remettre false
            user = new User(nomUtilisateur.getText().toString(), imageURI, false, lastLocation.getLongitude(), lastLocation.getLatitude(), group, true);
            ProfileDao.getInstance().setUserProfileRef(nomUtilisateur.getText().toString(), nomGroupe.getText().toString());
            ProfileDao.getInstance().setCurrentUser(user);
            // aller verifier dans groupsNames si le nom du groupe existe deja
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Enum.GROUPS_NAMES.toString());
            Query query = reference.orderByValue().equalTo(group.getNomGroupe());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // check if groupName already exist
                    if (!snapshot.hasChildren()) {
                        user.setAsOrganisteur();
                        reference.push().setValue(group.getNomGroupe());
                    }
                    GroupDao.getInstance().addGroupChild(group.getNomGroupe(), user.getCoordinate(), user);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // nothing here
                }
            });

            // continue to next activity with relevant data.
            Intent myIntent = new Intent(MainActivity.this, MapsActivity.class);
            //myIntent.putExtra("mainUser", user.getUsername());
            MainActivity.this.startActivity(myIntent);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Erreur")
                    .setMessage("Il faut un nom d'utilisateur et une photo ainsi qu'un groupe !")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        if (!quitterApp) {
            Toast.makeText(this, "Appuiez une deuxième fois si vous êtes sûr de vouloir quitter.", Toast.LENGTH_SHORT).show();
            quitterApp = true;
            // TODO: 17-03-12 mettre un timer pour remettre quitterApp a false
        } else {
            finish();
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
        Log.e(MainActivity.class.getSimpleName(), "Connected to Google Play Services!");
        initialiserLocationManager();
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
                Log.e("MAIN ACTIVITY", "fail to request location via GPS", ex);
            }

            try {
                network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
                Log.e("MAIN ACTIVITY", "fail to request location via NETWORK", ex);
            }

            if (!gps_enabled && !network_enabled) {
                checkUserLocationEnabled();
            }

            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            Log.e(MainActivity.class.getSimpleName(), "Connected to Google Play Services!"+lastLocation);
        }
    }

    private void checkUserLocationEnabled() {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage(MainActivity.this.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(MainActivity.this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MainActivity.this.startActivity(myIntent);
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

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(MainActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }
}
