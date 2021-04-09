/**
 * Description
 *
 * @Name: Yakoob Hayat
 * @StudentID: S1714096
 * Description: Custom Adapter for the List view
 */

package com.example.rss_feed;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class RssListAdapter extends ArrayAdapter<RssItem> {
    private Context aContext;
    int aResource;

    public RssListAdapter(Context context, int resource, ArrayList<RssItem> objects) {
        super(context, resource, objects);
        this.aContext = context;
        aResource = resource;
    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }


    @Override
    public RssItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int p, View convertView, ViewGroup parent) {
        String location = getItem(p).getLocation();
        double magnitude = getItem(p).getMagnitude();

        LayoutInflater inflater = LayoutInflater.from(aContext);
        convertView = inflater.inflate(aResource, parent, false);

        TextView loc_txt = (TextView) convertView.findViewById(R.id.row_location);
        TextView strength_txt = (TextView) convertView.findViewById(R.id.row_strength);

        loc_txt.setText("Location: " + location);
        strength_txt.setText("Strength (Magnitude): " + magnitude);

        if (magnitude <= 0) {
            ImageView lineColorCode = (ImageView) convertView.findViewById(R.id.imgcolour);
            int color = Color.parseColor("#5FBCE6");
            lineColorCode.setColorFilter(color);
        } else if (magnitude >= 0 && magnitude < 1) {
            ImageView lineColorCode = (ImageView) convertView.findViewById(R.id.imgcolour);
            int color = Color.parseColor("#6EC34A");
            lineColorCode.setColorFilter(color);
        } else if (magnitude >= 1 && magnitude < 2) {
            ImageView lineColorCode = (ImageView) convertView.findViewById(R.id.imgcolour);
            int color = Color.parseColor("#FF5722");
            lineColorCode.setColorFilter(color);
        } else if (magnitude >= 2) {
            ImageView lineColorCode = (ImageView) convertView.findViewById(R.id.imgcolour);
            int color = Color.parseColor("#CC0202");
            lineColorCode.setColorFilter(color);
        }


        return convertView;
    }
} // End of Main Activity