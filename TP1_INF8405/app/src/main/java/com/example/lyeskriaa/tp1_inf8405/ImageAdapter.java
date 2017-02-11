package com.example.lyeskriaa.tp1_inf8405;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by LyesKriaa on 17-01-26.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Niveaux nivGrid;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public ImageAdapter(Context c, Niveaux nGrid) {
        mContext = c;
        nivGrid = nGrid;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(160, 160));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        fillGrid(nivGrid.getGrilleNiveaux());
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = new Integer[4];

    private void fillGrid(ArrayList<Boolean> nivTab) {

        mThumbIds[0] = (nivTab.get(0)) ? R.mipmap.level_1 : R.mipmap.lock_level;
        mThumbIds[1] = (nivTab.get(1)) ? R.mipmap.level_2 : R.mipmap.lock_level;
        mThumbIds[2] = (nivTab.get(2)) ? R.mipmap.level_3 : R.mipmap.lock_level;
        mThumbIds[3] = (nivTab.get(3)) ? R.mipmap.level_4 : R.mipmap.lock_level;

//        for(int j=4; j<21; j++){
//            mThumbIds[j] = R.mipmap.lock_level;
//        }
    }
}
