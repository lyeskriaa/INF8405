package com.inf8405.projet_inf8405.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Louise on 2017-04-12.
 */

public class ChatUtils {
    static String randomMessage(int tSelf, int tFriend){
        List<String> strings = new ArrayList<String>();
        strings.add("Ça va?");
        if (tSelf < 0) strings.add("Il fait froid aujourd'hui. Tu veux aller chercher un café?");
        if (tFriend < 0) strings.add("Tu as froid?. On pourait se chercher un café?");
        //TODO add string ideas

        return strings.get(new Random().nextInt(strings.size()));

    }
}
