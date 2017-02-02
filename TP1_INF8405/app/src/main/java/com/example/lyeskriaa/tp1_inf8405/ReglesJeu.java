package com.example.lyeskriaa.tp1_inf8405;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by NP on 2/1/2017.
 */

public class ReglesJeu extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regles_jeu);
        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setPadding(20, 40, 20, 10);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setText("The Game Rules\n" +

                "The simplified game will include a rectangular grid (of potentially variable size)." +
                "Items in the grid will be solid, red, blue, green, orange, yellow and purple circles." +
                "A sliding of the finger on the screen (generally called swipe) does the exchanges of " +
                "place of items. As a reminder, an exchange is possible only vertically or horizontally," +
                "and if the exchange makes it, possible to form a group of three (3) identical items " +
                "aligned vertically or horizontally. If not, exchange prohibited or canceled (see Candy" +
                "Crush for an example of possible behavior)When a group of three (3) is formed, it must" +
                "disappear. For the simplified version,if a group makes more than three (3) items, it " +
                "deleted in the same way (it does not form a new item)The elements that were above the" +
                "deleted group must go down and randomly selected new elements among the possible " +
                "colors will take the place they release at the top of the grid. It is not necessary" +
                "to verify that a solution found in the grid. It is necessary to count the score and" +
                "the number of shots remaining. At each exchange made by the player, his remaining " +
                "number of moves is decreased by one (1).\"\n");
    }
}
