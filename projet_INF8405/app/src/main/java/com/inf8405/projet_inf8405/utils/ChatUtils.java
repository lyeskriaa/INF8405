package com.inf8405.projet_inf8405.utils;

import com.inf8405.projet_inf8405.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Louise on 2017-04-12.
 */

public class ChatUtils {
    public static String randomMessage(User self, User friend){
        List<String> strings = new ArrayList<String>();
        strings.add("Ça va?");
        strings.add("Hey " + friend.getUsername() + "!");
        strings.add("Le bonheur ne s'acquiert pas, il ne réside pas dans les apparences, chacun d'entre nous le construit à chaque instant de sa vie avec son coeur.");
        strings.add("Il vaut mieux se disputer avec quelqu'un d'intelligent que parler à un imbécile.");
        strings.add("J'ai rien a dire alors j'utilise les message automatique XD");
        strings.add("Les message random sont stupides non?");
        if (self.getTemperature() < 0) strings.add("Il fait froid aujourd'hui. Tu veux aller chercher un café?");
        if (friend.getTemperature() < 0) strings.add("Tu as froid?. On pourait se chercher un café?");
        //TODO add string ideas

        return strings.get(new Random().nextInt(strings.size()));

    }
}
