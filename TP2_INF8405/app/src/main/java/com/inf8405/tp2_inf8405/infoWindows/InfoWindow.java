package com.inf8405.tp2_inf8405.infoWindows;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.inf8405.tp2_inf8405.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by Louise on 2017-03-16.
 */

public class InfoWindow implements GoogleMap.InfoWindowAdapter {

    private final Context context;

    public InfoWindow(Context context){
       this.context = context;
    }

    /*
    data for marker :
    [type]:[name]image:[image]
     */
    @Override
    public View getInfoContents(Marker marker) {
        String[] strings = marker.getSnippet().split(":image:");
        String[] params = strings[0].split("::");
        String type = params[0];
        String image = strings[1];

        if (type.equals("user"))     { return createUserInfoWindow(marker, image); }
        if (type.equals("location")) { return createLocationInfoWindow(marker, image); }
        if (type.equals("event"))    { return createEventInfoWindow(marker, image, params[1], params[2]); }

        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }

    private View createUserInfoWindow(Marker marker, String image){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View myContentsView = inflater.inflate(R.layout.user_info_contents, null);

        TextView txt = ((TextView)myContentsView.findViewById(R.id.user_title));
        txt.setText(marker.getTitle());

        ImageView img = ((ImageView)myContentsView.findViewById(R.id.user_image));
        byte[] decodedString = Base64.decode(image,Base64.NO_WRAP);
        InputStream inputStream  = new ByteArrayInputStream(decodedString);
        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
        img.setImageBitmap(bitmap);

        return myContentsView;
    }

    private View createLocationInfoWindow(Marker marker, String image){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View myContentsView = inflater.inflate(R.layout.location_info_contents, null);

        TextView txt = ((TextView)myContentsView.findViewById(R.id.location_title));
        txt.setText(marker.getTitle());

        ImageView img = ((ImageView)myContentsView.findViewById(R.id.location_image));
        byte[] decodedString = Base64.decode(image,Base64.NO_WRAP);
        InputStream inputStream  = new ByteArrayInputStream(decodedString);
        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
        img.setImageBitmap(bitmap);

        return myContentsView;
    }

    private View createEventInfoWindow(Marker marker, String image, String dateStart, String dateEnd){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View myContentsView = inflater.inflate(R.layout.event_info_contents, null);

        TextView txt = ((TextView)myContentsView.findViewById(R.id.event_title));
        txt.setText(marker.getTitle());

        ImageView img = ((ImageView)myContentsView.findViewById(R.id.event_image));
        byte[] decodedString = Base64.decode(image,Base64.NO_WRAP);
        InputStream inputStream  = new ByteArrayInputStream(decodedString);
        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
        img.setImageBitmap(bitmap);

        //TextView txt2 = ((TextView)myContentsView.findViewById(R.id.event_date));
        //txt2.setText(date);

        Button button = (Button) myContentsView.findViewById(R.id.event_go_button);
        //button.setOnClickListener(new GoOnclickListener());

        Button button2 = (Button) myContentsView.findViewById((R.id.event_maybe_go_button));
        //button2.setOnClickListener(new MaybeOnClickListener());

        Button button3 = (Button) myContentsView.findViewById(R.id.event_no_go_button);
        //button3.setOnClickListener(new NoGoOnclickListener());

        return myContentsView;
    }

}
