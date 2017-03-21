package com.inf8405.tp2_inf8405.infoWindows;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.inf8405.tp2_inf8405.R;
import com.inf8405.tp2_inf8405.model.Event;
import com.inf8405.tp2_inf8405.model.Group;

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
        if (type.equals("event"))    { return createEventInfoWindow(marker, image); }

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

    private View createEventInfoWindow(Marker marker, String image){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View myContentsView = inflater.inflate(R.layout.event_info_contents, null);

        TextView txt = ((TextView)myContentsView.findViewById(R.id.event_title));
        txt.setText(marker.getTitle());

        Event ev = Group.getGroup().getEvent();

        TextView txt2 = ((TextView)myContentsView.findViewById(R.id.event_participartion));
        String participation = "Participe: ";
        for (String str : ev.getGoing()) participation = participation + str + ", ";
        participation = participation + "\n Participe peut-être: ";
        for (String str : ev.getMaybe()) participation = participation + str + ", ";
        participation = participation + "\n Participe pas: ";
        for (String str : ev.getNotGoing()) participation = participation + str + ", ";
        txt2.setText(participation);

        ImageView img = ((ImageView)myContentsView.findViewById(R.id.event_image));
        byte[] decodedString = Base64.decode(image,Base64.NO_WRAP);
        InputStream inputStream  = new ByteArrayInputStream(decodedString);
        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
        img.setImageBitmap(bitmap);

        TextView txt3 = ((TextView)myContentsView.findViewById(R.id.event_date_start));
        txt3.setText(ev.getDateStart());
        TextView txt4 = ((TextView)myContentsView.findViewById(R.id.event_date_end));
        txt4.setText(ev.getDateEnd());

        return myContentsView;
    }

}
