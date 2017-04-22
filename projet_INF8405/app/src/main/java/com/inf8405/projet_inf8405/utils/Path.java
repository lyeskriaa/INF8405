package com.inf8405.projet_inf8405.utils;

import com.google.android.gms.maps.model.LatLng;
import com.inf8405.projet_inf8405.fireBaseHelper.UserDBHelper;
import com.inf8405.projet_inf8405.model.Coordinate;
import com.inf8405.projet_inf8405.model.ListeChatsCurrentUser;
import com.inf8405.projet_inf8405.model.ListeUsers;
import com.inf8405.projet_inf8405.model.User;

/**
 * Created by Louise on 2017-04-22.
 */

public class Path {
    private static String dest;

    static public boolean isSet(){
        return dest != null;
    }

    static public void setDestination(String id){
        dest = id;
    }

    static public LatLng getStart() {
        Coordinate coor = UserDBHelper.getInstance().getCurrentUser().getCoordinate();
        return new LatLng(coor.getLatitude(), coor.getLongitude());
    }
    static public LatLng getDest() {
        if (isSet()) {
            Coordinate coor = ListeUsers.getInstance().findUserById(dest).getCoordinate();
            return new LatLng(coor.getLatitude(), coor.getLongitude());
        }
        return null;
    }

}
