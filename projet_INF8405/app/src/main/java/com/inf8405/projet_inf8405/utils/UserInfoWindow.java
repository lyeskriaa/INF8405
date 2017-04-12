package com.inf8405.projet_inf8405.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.inf8405.projet_inf8405.R;
/**
 * Created by Louise on 2017-04-09.
 */

public class UserInfoWindow implements GoogleMap.InfoWindowAdapter {
    public static final int IMAGE_INDEX = 0;
    public static final int SEX_INDEX = 1;
    public static final int DESCRIPTION_INDEX = 2;
    private final Context context;

    public UserInfoWindow(Context context){
        this.context = context;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View myContentsView = inflater.inflate(R.layout.marker_user, null);

        String[] attributes = marker.getSnippet().split(":::::");

        TextView txt = ((TextView)myContentsView.findViewById(R.id.user_name));
        txt.setText(marker.getTitle());

        ImageView img = ((ImageView)myContentsView.findViewById(R.id.user_photo));
        byte[] decodedString = Base64.decode(attributes[IMAGE_INDEX],Base64.NO_WRAP);
        InputStream inputStream  = new ByteArrayInputStream(decodedString);
        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
        img.setImageBitmap(bitmap);

        TextView txt2 = ((TextView)myContentsView.findViewById(R.id.user_sexe));
        txt2.setText("sexe : " + attributes[SEX_INDEX]);

        TextView txt3 = ((TextView)myContentsView.findViewById(R.id.user_description));
        txt3.setText(attributes[DESCRIPTION_INDEX]);

        // if (current user TODO) {
        //View b = myContentsView.findViewById(R.id.user_chat_button);
        //b.setVisibility(View.GONE);
        // }
        return myContentsView;

    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }
}
