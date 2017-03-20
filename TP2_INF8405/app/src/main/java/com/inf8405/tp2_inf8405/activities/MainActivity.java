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
import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.User;
import com.inf8405.tp2_inf8405.services.LocationService;

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
    private final String GROUPS_NAMES = "groupsNames";

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
        String imageURI = "iVBORw0KGgoAAAANSUhEUgAAAEwAAABMCAIAAABI9cZ8AAAAA3NCSVQICAjb4U/gAAAgAElEQVR4\nnE28abBlWVYe9n1r7X3OHd788uVYmVlZQ9bY1XRXl7obEAa6kbBAElhh3HbIthShcMghJNkiLCvC\nPxwO/7Blh3DYAURgB4HCDmECIUQLRCNE0wIDPdJDdXXX0DVkVWVWDi/zvXzTvfecvdda/nFuFtwf\nFTf/vPfOPmuv9U2r+Lf+5qcYSeAWEZSw6mA4IkIDTlQzdwmxagQQERGEh0HuHB7ePCir042f/bn/\nM8wDBHDX0URJNdq2bVW2Nte6rkv9fG26IiJGuT/r396r5ycs3azaYn11zd29xmKxaNu2Sai1H69M\njo9PFETEeDqezxa91WnbzmZzIBK1uHV9LyQJK/O2Hc9mxwBIZQqw/aXPfO5f/Or/vTnVZx9+KEWQ\ncAcAEYsIhbsTEijhCIQQXmsNkuEU0sNrBEUbUYlSbBHFTp8eE4nkxNNESHgjJNlXJG1Ue6MEgkBO\neGhFa63UZpLHvUWEEiFJrWKyORIB3CZbW41mJzxidbrqbu6+tjIBAMANNTzCGFZjnR7r66vucK8A\nToof7O27+8jHEZGU4gh3F9AAE6oDZIUnsIR7UEgF3NwQEhpBILxWABE1qtVagyoggGrdIiI5mnYE\nIIDizukqsgQD5LRpplMeLWa1gOJZspk1KTmiVl+gJKMTwjjpZiIyXZm4izsBmDuHYmrAYiFirvQI\nGACaI8QNYf3du3cBtzZIJkdEBEk3AB4VFWAgUwpcwHB3ByAGE6i7QxjVQQW81soUpZSICARJC1+U\nErP5pMmABunEUOckIsI9utprajysbUTB3lAjGk2jRvtaVJgg9MBI2raNiOXbc4+IAAgAsMSoDiDE\n4YgIYyAgJID9/f2IaKgMJEA0zIGAJ4E5IwLhvcf7P7cCwxmEGchaK5RRXT0FRQRdKcMvqxajWgPa\nrm0ACJIBVUWoswokIlLSlJRktMEgU4yQGWCYA3k0YgDmgAzPNjykEyTf/yeWHUAiLIIIByCgBw3e\nh9072ReXgiJEMiuguLuEVrc0FFiA5PAGASE8hGJ0QbgDQgMAMuCo1a1UABIaEmncOkUiOgddnTDr\nNAEhgTjq5rVWATfW1wQIDoWGIEERhEcgRKjDw0QEQmt4RDhieO6hXgBAheZDKxTQDcEgGL13x3PQ\n13Ts7kkpQQAwCfjQGgiEmYkIIipMIiy0RCW8+nCcQiAkBKaIWT9fuEXUk+M5m6b0FhGjcYOQaTMB\nU7iYAoFRmkgjInBDEAEHIIEQeKUSIuIRHgDAcCdACGGGP/MhOfx+WNAJBiw8JOCMiONSqnUIERGS\nCQADAcBNkXqYeEiED2/SqaQjPGomihFwdzBQHtxkQ3SlRoS5ODg/mjkCgHkRNpM8CdhQe8ZIKsVs\ndtJFcDJpjhaziAihl7oyaqft+Ohk4dUmo6ZpkkMBB+GOQIJ49QqihgciAh4+/O0IwdB7SAClFHqY\nF1Wt5mkoCQkZDgNAEdBBLM/KDO4IZ+9FmMIJonpw+OGBCHZdd3CCo2qIVSQHkHM2hlPv9IwQ9gaJ\ncB3+FJEJgL0TD0wpgUrQTwqxIDCihJ94HNuDG1jMBVEYy28CFDMAtdaICJhYRISFn8y7yajJBb31\nIiJJVTXB6KSEe8CJMM9gidCAITyCDEM4PCBmFgF4MIgIAISF1a7r2pwOLN1anGTRUHHrEhLEAIHQ\nEP/fif8XZycNpYIKkBEhTk8QAG/N4uFJSjmCTEGDZNI9SHoEht4dQVIQcAMaCw9TArBqQ+macX1U\nw9866MwsAznB3RMj6AEyBIiqoCES6OEmErAYesyyoTkCQQboEWHmCEbMF31WrLdlqiKI3dkiiYjY\nuM0MExEnftCbEasAzQA6IkSEEUOBPTJOrYQgnAbIokZOVBoAgqB71KGdWogQQGV4sIYHUAUIhKh0\npc6qzLuqjN4NcADJEdCAI6qTUhSoDtAJcQvAGeKwCBEp5u7wgLtFRDgjDBEHBwe1t81mJJPWI7rF\nAYBM2Zi0JEnCY2eiOswAmFKWkyBkGGstA6CDpXcy7i9kOhGlkwRRg6oZYSVqoOHQbN3NIqwyjKRX\no0qEjMTn3cIimpTa1AJIJOEMc6VYuAYBursATob70F8kcO/o4CsvfnsymYB5MT+KnN1kPFphM1kc\nz8NsXvu9eTdyX52mvq8523x2lFLT1yKge22a5uTkZDqdkpzNFuNxW0oZRmJOMhqNalehqF2/M2oW\nC1Olqna9u3ujw0iMpvVSSzgBqHhOyYwpJWEAmALVbXbSZ4RSAxahyT0ENHW6DBcgCEMYh4ocYAoA\nbK1tAOi6zupiXjqUQjSqMQ4cdvMuAgaf2erWhBJtymQCAioqCYyMiaqqS9aGpLSiqk2oNKJK1dz3\ni6RZ1NJohCiCUOYwa5s0GY0B96gIEfi4ad3dBnwXnjxHOOgMRIQbDg+Pa/hIWB05pcSAuVGEgeEF\nWngAabh/GICY13CGkqy1BlJEwAl6eO9m9w/vmdcVVa61BZ6gFhGoQpWAMRgo8OJRk4hEhEfOA1qv\nYcVjpEbNxaxJowQEsoSIoNZq0Hlfaq0AyNr3fQSTMsz7vldl3/dtzrX2pRQAIvLerVthnlMm2Xdd\nGiANh8rkEjQJae6ICiazcIe7n8wOljiLlWTQAXoUd6+9jVZGewfHa2vrVjpXD3OniLhC+95Ih7lm\nuX/Ub6yOUkr37x9vbExmxyeJYuaYTgivBpL9ohuNWvMu5wz3vponjaDADSBVhDllJGhOAYwnba01\ntalxd/fD4/76nd3KEBErnWteggGIDR10+ZABJyPUzYBhhrpZGejk0DbogMCNAGZHs9I7xyuLatPR\nRCk2TOvIIpGaoJtkocSklZxGQaTUmGVJU4FYdPNi69NJ7bqjk246zn2tpBwenIwnDQWLRc2NWDUA\nKQmizrtI9FKhUqerbZtGARsquFvY/u3bUpeMRFBSEAHQJGJAAFzWujv9AfyPGK4swAdd0QKkA17h\nZX506JCNycqdg4OVEYOBcBGJ6EVzgjuCpLjlRklWq6PppFFkEQBN25IMMqW0upqVHCWBQpbs0TFd\njjGixoOpE05luDsQIBikRDXM+7K3txfhSp9ZtOYpzE2MIRiImjmU7ibBGFBSRBjESGeb0lD374Md\ntwVKzI4OWjMFz26ul242NIQQ8R6pTaV0AnRlkRt2J8XHyAmz2YGMRwLWWkmOmhxWzWujKcJCWiIM\nQY/h3If6IsCgR0AIRHUqQQ9KAOpuBp+0zf2DXfNOAlHLsfVpgGckzByISg9b9psarsEwDAiejFbl\nGDHMSpJBc2igO5rdj7Y9ttoE2U5rMQk0Oc3rfFHrvYPZ7uGhVQrc3S+d3hm3KWlrbHx+dOxITKnN\nSSNJ4wDgs24gQCHwQZGgDCcHAQRBorM4mB1NRZkGhuGJycyqsS5mKFYY88UJgOQeFcgECYMnaCBq\nWAjpdGBozY5wgqMRjk+GgokIkO5eq/ezozv3DqZrq3NbAk4FQxjVQygipzc3d6Yr0rSUEAsBKgIk\nxutTOCFmZgZDAG5mEUFnePVgmHdmYX0pxWqdl74sulKs1n7ed6WbH3Yn/Unn3fzkuN87uH/zxq35\n4T2hH90/XjSwkGSohNQoAtAFHCDO8u6Zuxvcl8epgQgnBQggGKRb1P7o/u2/9am/PnC0iCCXfFeE\nZtkJMhjhEEiQGtBo1qqOBe5IAYEw2IQm96rW5XI/ytFwXjQBHUCED91x4FkRYYBEDOgl55KilP6+\n1S44Q+B4fow5gGHiwUgNH1BEdUcAjPAwoYT4wByUTEmW7xAYvgQRUdzUFyW0iSWtraRGBJFIVERm\n6qOSAmnLdMtkIkxBuJAqpLgoMGLSqOZ64sg6n2v0gaA7GO5VBOLWhw3liQhFLwEoUncfHTqqmal3\ndLeIJy6fu3JqvQSSD0xfoHQJBYFgDQtCQ2yYKIoo1b2mYXiQImLmCGeou4tA4gZMAJHQSiMH7B2M\nJjHghCBXAGgqQigBeAy3PYRr05Wf+rs/+dZb78xm3d17x5/9d58bBvjQ6iUi0TxSeEkD4ncQ8OgN\ncHcRMasRcPj5f3Bw/aen4qmWcu7yY1/48p/wP/oPfjRiINpIVIML3CpCEAGYV4eZ9bUsFv2te3tv\n3rjxABwb3CEyna4IG6eoCANQiSWvR1AQYggFwTrQm6V6EwlB0JwycIVTm1vzxQlVPerJ8dzdGRYR\n4qg6kF4YHOag6wN1C4C7U0XTqO97YQgNKqqKKsX6JJrMQhUAl8IkaRDngO04wPUgBh2BVI9K6HKE\nkIg4OT4CSTJiWcOqOm5aIJzL45t13Xg8pnPR9+O2pWZBB+L+7HjoagBmJ/dIutdG0nQ8npWZqPZ9\nHedmSgEftAYiKAyAYSQADSHEpCKT0o5Go9IvJuMMKuGtaoqwWkEmEYJWgxFwH654VLMB89ZqEZGS\naOhABW14VzEMYgyjaJhk7hZeINRQ8ZhZbdsWZr17Vjpd63JE0CIIGe55mIECeNRFWQwodK1pjvsy\n673JOkqJxHD6YIjIUtcSAaCke3Wba0XTJngczw+rRTseJXMSBtQIWdZ30IkIX+qxy+FUAFfVZjxS\n1aOjIwYCRmE4A4H39VCEQMzBQEjtSlClLDpDqKqbifed8U9b1wA7IABkoAREKWWcs4QVt0mbzawz\n6zxSSl0tKSV1IELTso2T7LvSpJRHIzCEMI+maSZBgsm8OpCJvlQRSWANQyBAt5pS9oiD48NrN+70\nHovjk65WkffVSmB5rsCDewggEA8IgYZQgNQ24g5AGSSTSpOVkvZns2oEfOB1D5RVjlQiohY4oApV\npXtv1QlH9H3f5FEINChKMswiN2k8Hru7VTfkH/3hT6yvrr347W+d2ZikRW+NsDIoYrUahqkQDAbQ\nl+JhBwdHdw/2ETI8W62WRXuYhAxDZECWIoKQoVqZ0mjU9n3fivbu6jgzTllAKqnubuBR30lAFHAi\ngiACg4I6LzXRc5Ik6QG6QJN11hcRGecM2KKrGE3OTJtFDUDu3r8/mUy+5899/JHHHv3VX//0r//m\nb9Twebc4NZ3yh/7SX/72yy8+eeXSMMHN7LU33pzPu0sXzp86tcWor7zx7myxOJr39HBC4ACsPrAf\nHozNoU7Ho5G7q2qKfm3SJHB34SHt7PjAiUQBXUQYyKKUmBv8AQ0gmXOutbr7KKXtaXbCHF2p1cV8\n8H64qFVVSTSa/uJHrv7OV16Z9XU0Gj3++OOf+is/9tM/+zO7+/dVEJCkfOHRM3/13/sQ09o6A23W\nrpbzO2cuXzhLsu/7b736+qQdGWJ7Y/PajXcdYmYwd3GEyKDI/JmPgFkpTbvV6ulR1sQS2D+pe/My\nbbEzHR1V3tg/ZABUDvgDcHdfgpChYcfwHxGq6nDoiUKyyexdxllzzntHByoNEFfPrL/23r6Tf/s/\n/5u37937td/4dNu2dDKx6xZZNKd0enuLaXWFUHc3L6c2t7pF6a1ePHfa3e/fPyylFHjt+sEUGe6M\nQ5TBgAhOrY4f2VltVEh+/cb9J7ZXRonVMTd77fZsc33URrx3f14CKjKgfHcMoHeYt8PDOB88KAL0\ncA5Ej6QTEnQO/M9JAhxGmoiMRqN/+Pf/q1/+lX9+b+/u3uEhyY9+8OlHLl35rc/9wfPPf/iFZ579\nzO98hpyMBwAiIuGcTKaaUEqpXV8dq+PR84+e+oOXryOk1sqw90tLKR+9srU6anLWLO0Xr916cmdd\nMgHcuDfbPZzrKN076N4/mvcLOyJUluz8/Onty6fPffHb33bR1dW1c2cvbG7uQAdSHm+88Z1bt9+V\nYfzKMIHTAxonAH70hz75wgef/x/+139cw9ssXTGAD22sCSI1eu7M+a+8+tb57R1yMgYggdXp5LGN\nNBo10uSXbhwsOrPgODdGlFLMizyQd92Xbl4SFZE28/L2yuJk/vSlLW3yl17Z3ZzkjWn7pev7Ao0H\n1tgS/gMimCqfe+TsxmTcjKa9jrrj3TduHNw4to98+MOaJhUuIl6NSqX2ff+d117evXvz/ZMSkfWV\n1f/xH/2jf/u53/3MZz9HEvDKUGlSSs8v6hPEPw/7pKTPi/t0lRfPb6+M2mu7hxBGMCIcknNeTbZ/\nvLh6dmMk/urufGPcWNR5V2YlmgSGPHJ+/c692e6sHx776TPrH7xy6te/fO1DlzaevPLwCz/4qRiP\nPVSZfvtzv/Nbv/WvQPvQ5Z0nzm+kRnePus+/ujvr+ob9cc9hAJFkShHx9ONPnL/0KCEuEDAo4h6E\nEN//8Y9OpqModnv3zv/2M//76SnWxvlkEffm80Xh2rgZC+/PF0edp5SdqLV87Nmn9OzWyo3DsqgF\nlLD6/EPbT1xYXSz6/Vn/yKnp84+f2dlc2Vxt3to9+P6r5x47u/qBi6eePL9x9dx66UwSL5+a3Lh7\nQpF7x9213aMff+HhKxe2t3aujk5daNt2b9alyRSBV7/9Yjtur905eOXm/fmsPHNx+8Lm6rwrH3/y\n4qyf955s+NRqtd7evfnmG6/s3Xv34oVLTTtpclqZpPv3bn/ta1+5c2/3uaef+darb/3cP/tDw6nv\nfrQ9t9Gc3RhfOX/24vbW9hjv3D3AeNxKaprRqTE9bPfwmO36pkdNRK2eRHc2JvcOZw+d39gOv3Jh\nddJMABwdn/zG169/8pnzh9W+c+PgcF468wGjp5REJIn+yHPntjcnb7yz//vvHH/suz70ye/7wQhe\n393tuv7k4N43/93vvHEyyymRpMjgjQVQrSc5gGGSSXU2n4M+GIokLZZgWDWp6s7W9t/9239vZTw5\nni/W11fv3br++hd+jeECpcRwjxbz/t+8cvO4B4lTa9Pv+eBjXN3ceOzMele7rVFbo7x2ez7rrYar\n5FE7lUyvtXTzUkoNT6GQSCkFdRiYfd9HhDCJwiESkJwApBAAJqEPhujQGIf7s8TxgaaNBnpcilk8\nMEjSTsU7togIldyMx7Uu1dRMefyRK3//v/x7JBe1hHnXde999TfzIFfCikXf98fHJ6+9e9hMm29f\nu7NfQlXT3/nJnxKBqiTwcO/GU3t7o+nG177x9bfefnvRlX7hGlAKcm7Qq2qWPOuL134wz5wY+LSD\nqgkAQofHK91sdTo5PW2oen82n6Tmo49f2FmdJkWiuNcQ7cxQ66BlHcwW/+qr70Ypt8H/dvz0/4G3\nBFpqx6GfGiaj5rueeJT93A2r0zGkuraLjZ16sht0UqGCWDm1sX7poTOLjg9tr7xyY++VWwf8h//d\nf9+kPDT0NqewgECpjoD73Xu3vvpHvzffv9sjvXv/5KTrSarqMBXMrLcqTFkUqpRGNC+RDeiMNvoP\nPrRx+97dmwdlfSU/cmpTcjo86SF6apyvnF1fGTfwvlj0tXzljf3zp1b+6NXbB7PjR3zy55vNX+zf\nHcpYwPXpaDwaBXPTjF2oFkIczE+yRvZy0nVN07xw9eJTl3fGqTnq+ruHJ+4+afJk2vB/+Sc/c2Zn\nG+6SNOdmaTy5VzOrte/KjXfe+Plf/Nn9EwtntV4la/pTKDcMaGFSbaKdqOoSwYqKl1qLlRNhGjXp\nr33kyvmt0WJ20lcjFHQLdQszA2P/qP+tb9+cLU7MTCk54vnNLW7rKMtIVVNMR+NJ257ZObOythoR\n86MjttOVs5d2HnrCq+3fefvuzXd/5bO/f3PveDJdP3tq+9z5M+d3Lnb0r331T1Ip5dKFc/v7B93J\n8WJ+UK33GmZlkMy8+PHhvZ215tbe/ZyziIji/SccpiU5mLhFIxLZd53VRXghl5a9sPzIBy9ePLUu\ntNHGFmOQswino7j7/tHs//rct0gN99OrowuT9PaxXXhs88rZ9aHhkGyaRkRG4zE1SB3pRJL0e+98\n9ZU/iVqhky+/+vpf+MjVrY1tkSaqecyy7ObUPv8DT+szTz124523JxonJ4endjYPDg5oFm5eq5VF\n1Lq3dxezvbfu7HtwbWU6m/VADH/9cFkIJTVE3HqrXXhBuKqElW4xt9rXWl97d+/g4PDRC2dzEtEk\nosGlqPe1l6//0hdeU1UFnj8/feLc+srK9MbB4uNXL2ysrYxGo6Zdfoa+RRGAQhVl06aN1cnqZDKb\nz566dGq90exVfKFeRima3IhGV2oC0mRlYzRe07aI5mZQGgWSJbTp+0WUw0x54fKFr7x9Z//gsG3G\nG9vbt2/fEriqqmp1TtY2uvksvMKs1G5woERkmC5t1o9fvfjwma3qHmjIQQlqAXQ1PvPNN4JLsPba\ncf7W/vGY5flHT+9sbY/GSSkiCHdSc6tYylZwBJ3uXoxIGSobG2uDoqUUkhBGREqpOhJqd2pzK48n\nY42D3Tti877vWRf90dGbb75yePv6pYtnNy6fakb766vNl968uX/c3b55/eyZiznrzZvvLazPogd3\n3wOwub52dLKIiKxJVUWg5CjzqXOrZ9bGK6ujnHNQSKjmgel++dUbV89ujlJsrzRPX3molCIiTdLr\ne/PxeKwpRGRwSCXczVNKOScLhw8hxxDYmzf3Lu9s932nTWbSpGm4RIZwIIh0591rzeOXd1999eWX\nvvW1b7zU94vtST6/s375oZ3Hzm41V56bV8yOjiftwdp0VRnzsvjjV+/evnMdwBNPPTOf+cHBPS8z\nRxwcHSehpiyCicazZ1dPbU621zbW1tpx0zaT0WS8Ih5kkArg/qI7dfHRFx4/3WbuHsxefOv2td2D\n0vXTEc1R3M7vbNVaFl1/++Dk/sHscN7PzeFsmjTNbcqs5mbWW9m9snj24rlGdcgnIBLHmzq9MA+9\nd+NNfv+Hnzi+fW+SudbG1tZ0a21y9uFnnvzhv0GymGWRg913r7/8+WtvfWc142Re/s1XX787l0G8\ndkSYB5GJiBDE1jg9dmZ8emtjfToZ5zTJbNNIMjWPmvGoGa0Nd0pCequ/99WXThaubh99+pFx8vF4\nnJKk1AThhi+9+PLDly6fObW6tEPiTzm69SUAd8wX/af/+Bvf98EnlaHTTVk7+wu//KtXn37m+Ytb\nO+1idW2k2lgNfepUvrTdPvLw1uXzpy+c2X7se//jfvuRjY2NruuSqtU+5/H9o/r2d17Z2RiPRpP3\n7t2/fTA3s62xVu9V20ScmerT58YfvHzmA4+evXR2e3tlujJq18aTcds0o3Fq2raZppSE1KwkQQHw\n8pvvPXdl59mHz29trI0n05wbShochpT10vlTUcqvfPbzk7bd2lyjKEWppEpKKaemb9Lru+4rW88+\n991W+pHvbTWLpx/eevc71379j7565eGLp1ZXzaLvnf/Njz0/zqkZa5ubtQsfeOnm7K/8h3/9W9/4\n+tWnnq61Hu3fmR/uvffO22+/9qXLmysk753MX752sx1xJbfVyr294zSaXrtz/0OPX3x4Z32U2LRZ\nNCmW1H5oTpThnihFmJTUN6/f/c6tG9//zCNtM2bSZRLTIwARGTysgd/s3T/cvV9ffeftv/zv/6Vm\n8+FKHuzePnrvLfS7v//Ntx4/t/r05XPj8ThlXfpewGy+2Ds8unOCjdOnjuskbW9MNCHriggycf7M\nmePdG3u3r+uTT+7ffm++OO77en3v/ulJWplOAmyb5tzGmoUFna7hRYAS51++fu+4lpO+HNyqd/cP\nt9Y3PvzsI6NxIrIG3GtARAQqpJrZwcnJ5nS1Ga1QIAIuObUwYoiGCZM3a7r58PbFxt9750rYyy99\n8e1bv/FDz39ge9KcPtucdFuv/M4XP371TNM0KkvRIElyYjwenxk1o15en60pJLVtKyKaVCS59c8+\n9aQTL7zwke5kvy9zcePsnuy9sbk1QS2iyKPWArVWDY+koko0rZfnH1u7e3B4eBIffvLC8AYO54tv\nfef2R557kgpSJASkgA4nORk3EYHgIM0rnYFC6ZuN5tSj/Xy2+/bL8u631zeuTSajHeHWpfVS186d\nPn3vpL8wGdda37lxd2s0WV9fbdqcNJv3UYpJBNh7fW8xPbApxQSSNDXDQFNllL3Zuy9Pzj6K0YRk\nEu/ufK1l9ItuZe0MPNxrWE2iuR1FhJnBg+xTMxKRi6sj8/TSd64999TjrXAKbK6uvPLmrWcev0BV\n+iDeOICgvPz6tY219bNbW6e2Nmztckw3Fkd7d699c13vtrP3Jik/sql1/XyEN5qLh7Hf3zt+7fqN\n7/nQ1YiogX/5hVe/69Gd9ZXxYAyq5tDczRallD2bHMQ0iBRS3fVHPv64k5qFjKSCftYdXe/vvdnd\nu+YH79FtvujvHByd3twEBm8mCcFwaKTUiCZNLQKIIFQEZ05v3dk9evO93dOnNv/wG69fPLu5vram\nWQGEhWTxvH2weuW5j3zs9Lkrr7326qc/+4frcv90OmjqwdbqeDIei7Yi0ju0aRXp9t2jf/av/2Dv\nZP74pe1Lp7aSJvf4fz/7dUH/wx97atqOkwzKTJhZAIsSe/f3SI1mKgGhpKA+OF0WC4uOi/rAnBs2\nDMytLhaLNosDYsWDwiQRHq6qFNVMD3EIJzunH/3I6GD/c7/wc2vTyan16Tev3frtL359VviDn/zx\nq088uv/WK/s3vvXQ9qtpZbwa8uGHV5+78ud7j8MT35vt39i9d29v73hRI2JrbfrElXMXt7c3Nqd/\n4699gtaXUobe+4u//+Lu3uF/8r2PTMcNZND6BHBElFLm85O+7xMPtrRLqVk0W/zZ//qvBgFfxhl1\niQoFkFIKSZgfHs+++eb1Dz9+OQk1CyMNJj5C0OT5+Nw/+ae/unP23D/4yZ/U/uTaN/5wpalZCdea\nR/mh55DaW9/+0mj23vb2tG2m1ORsREQC/eI4IqDJKNWdTicU5KBluQNwryKJijA3s6+8duPXv/Di\nf/Z9zzz9yCUJG8KeCDOHUhZ9N1+U16/fSuP1jbV1sBdJ+sMfuzoYRIEUXgdgaW7hAFUj3GuTdXt9\n+tKb73zt5ZvHXbl9f3H3cD698MyFj3xicvox9ItHm5NPfOB0e/RuP3rUTrMAAAlHSURBVLsz2X5E\nzj3n0/N39k9Obr+9Vm/nw3d3pmljY1PzKFyYRyKiDCBSykA4PAlaIbO0SVOTuXQ6dWi8QCgojJv3\nDv/p737xux/d+egHrk5H49RkVQnzWk1FIMxJVLE2mtzZfe9rL37z8Gixc+oM//H/9D83KyuJycsi\nTm5x/12xEwCqmfBBJ4X3tVZCI8KjFmuJmLQj3ThX1i7KaN2sLA7vLe7ePNq9uTouOxsrqckqTSzX\nBwCRJi0TZ8uVBCDCVNURNSqri8jQeGPQjUNIOl2M7hWMg+P5T/+LP2xS/J0f/djK+qqIpMGGEYa5\n1b7WntRGxYnSzWcnixu7u9+5fi/l9/5gmXJOEjGe56msXJVmVdoWeZREAVKcIRLovPdqqVssju7s\nntyMt16nv9okjNvR2iRtN4qLY+pGzk3f94A4PUKoVIaZSbhICjoHFxdaSw8gqyKJm1FSyANvS4YU\nA4MGhpn92h+9fNTNf+qHPrq6uioBwpe2r/ngozRNU6yPClQf5XGzPVlZX7l04WxSH0Rxib5CTyaL\nE3a7riQyokQN9+oUN/HoEckQ2fvEPErI06xJJTWDCR2kB1FrdRvC8VmaUOVyYSPMLaxbqgeigKWU\nAKm1FxFNqdbaSIoYNlBgQgkMV+d3v3Htm9fufOr7nrpw/sxSRA8FluYFSUDNPWsTEsu0fu1E0urq\naqruEZEVUJiZLRchBKgCJzSE4u7etzpKjQKunEg2klmaoTUEhUHrATWoSTQYMk1WQxFAUkEYKkQV\nQLGah6AICaGqVrdhUaGUnhTNCjANYQLRF1+/9ttffunjj5370JNXBk1iSIqRQBj5IKVA8YAMMciI\nJrVDrDNl1UHXVTRgGWIdDAOVVJKNqGQZjZswj9qnZsLkwhwRxXwgxyEEIUmVrO5mNnQBM4uwrNkQ\ngcipca8RwcCSWKvKIFXlJBYeBkNErb2nJps5A9du7f0///ZPzm5O/+LHnhml7A5SIgbPJ+D0CL7P\nVIboAhRDZpCiGqnWmlIarFOFINCkDMDCBSCHFxuSBCJMCVa8Dja9EIBwad0HHEs/SBIZiNILEho1\nrwwqNRSRBOYSNDNN4tXcPWVNIUUiQUPEzOBW+wLg/kn3M5/+o5z1J77v2bW1VQiUiMF9IkUkGDL0\ntsG/RwQkohIk6WYAkoBmBlURCSJs6ToqZTCCLFyVMIUaAGomLegwujtdrRRJTCkJEEKGI0gViniE\nWLUltbDEYCwtuuFoqARQay+SkghiEC7UBWY2X3Q//1tfiIgf+9jVh86eFlEHsoS755xLKYM4Gl6F\n9ADhvswLUIYNBKq7J83JrYcLhEk0KO8rNMMvTKSZQXo4leJRGTnoFIoo4EJlwPs5MY7RsJaAMMeg\nzS6lLpiVmBe2OYkCIjpEtnxwqdyrO1QznKKkJ3P/va+/eft+/z1Xd557/FJKSWTQTAJh4S4MeDiD\nIXiQWiAcIUMNmoMIYSQrNaWmWoeiQ6fKOZv3tdSU0gMfQiMizCucJL2HpBCXoWhDqAhpwRC3IeIF\nkaWXSi4T4EPpF+ujak4p4CqJA6wZOqQuixAwyivXb//ei9cunxp/4oVnxuPxAICSMCTgAlLogbBS\nRIRMQ5JYGBSEc8gADvxLNCfzhTADQLEhXC+am1Eavg9t6YENjMHbiwiiwo0hgMMJlRAaAipMFLj1\nBV7hlWFLk/dBiI3mpTtB37lXl0GUzwM2YKAUu3N7/xc+87WVUf5PP/nC1tZmeuArA5AQclAwQSBn\nzRD0fSlFMNjRfD8bN7i9SUDRsUehJHq4u1SklExUMgRRShURmHOQWDQRjkhWSYZIFZEIpyHkAfUF\nINTcChweLqQzJKjCgHpEmFJg7nTF0PbzoG5Z+KLrf/5ffzlRfuJ7Hz+9vaYxhOONAQccInSXkBii\nr+YaFGk8+n7B3CiHPAPDKwAVpCAYEG3gYRKgW9BqURBJAWhOYd5bTRSmpZkVdGU4WKurUofdUYgI\nDS6hDgPCAIqSERKkM5Qq5u6BHAA8atQIhCBDJEVYNf+1P375fpEfePr8s1cuWqkRIUnpS349mGJc\n4iIElDAyOUPI4Sdo4p8tvRQVwvAYQDopTTxYwrLetckkmTQPenm1B3gC0CSsknQYeqRKA3e3sGV+\ngkMMfZheAjBo3vVMyge6m4Z7BRm1WBafB156684rt/DI+XM/9MKjIjLMKS/9cCfJwbuUkEgYylKw\n/LKMGlPCzAfj1BykJ2X0VlOm+zIdC/ManlJiGnaXnSRVJADVUopaQMMMogFEwFQz4F6NVEUKILjc\nBf+zQR+S0qgWdF77pBkS1GG1Rh2dd/fuLj790uHK+saPf2xzPMoiGPbKh61XGxI8jtAQoyMclD+N\nEwm5DEy411IKJalqhCRkTWKl95wVgCNEJTG8r1ABlA/eIVVUtZU2CCtVaRFZCEMNcig2oK9FhkUT\nMuhhKDLEfijukSE1IbFhreEFgFEQiIjZwn75C++2afV7Hq5n1qaD7wEYl3EKifddJncIKEn5p5E+\ncojCDHEYJR3VDE6qOCDMWRTm9GF/nMtWZl4WXfUBA4hXs1It3A2SFCrw3qMOyY5hkcqDmkXEGWCg\ndH1Uo4e6eKkScK9LnpUzVYNMCI9+trDf/NIbc9eHJscfurSjZgMYEi4XAYbvAIQDZaPValED9n4k\naFiDHYxhkgwXSSSTDJd6SJz3Zfg/DIRaSEq5cXcv1RNUhSoDlZA0lL6wSQC86+kiKQbmMMB1ijiM\nCne3Up0mIvAwD4X78tIyBGYoxs+/euPFG/vThp/42GOtpghD6b0Gc+OiIjJ42UIfwgbDbWcwPNwL\nIGRQhYPwKRFGSyZwDyb3ahHuJiCTNqq16xkKWbr8yIrqvXXteDS851qqJH2/faVRqwHr+8qWw5qy\nI6JK4tLXIyKwpAGaBQELwE0QERbx6rW9z750Az771A88d3prM2uyqAijWW+Rc0aTBvwEKkUQAYY8\nWFYXSQDcYNUp/dBBwqGazYpISl5rSklzNqtD42GTGLBah7pVEWRBqf3JPI1boQzvxN0GlhRARWjT\nBiz6Wj2JCMTDCIgK4SDk/SbfFdPMsEguJ1Zu75/80udfKaX8xJ+7fGl7W0RMkEIBQU7Jo+97NWOT\nMhxZASF1aBQY1mAYADSreAXE3aMKVdx7SgP4/w8JnZ8cm61/gwAAAABJRU5ErkJggg==\n";//encodeBitmapAndSaveToFirebase(capturedImage);

        if (nomUtilisateur != null && nomGroupe != null && imageURI != null) {
            // save data
            group = Group.createGroup(nomGroupe.getText().toString());
            ProfileDao.getInstance().setUserProfileRef(nomUtilisateur.getText().toString(), nomGroupe.getText().toString());
            user = new User(nomUtilisateur.getText().toString(), imageURI, false, lastLocation.getLongitude(), lastLocation.getLatitude(), group, true);
            // aller verifier dans groupsNames si le nom du groupe existe deja
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(GROUPS_NAMES);
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
