package com.phacsin.entreprenia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

/**
 * Created by Bineesh P Babu on 27-08-2016.
 */
public class GridDynamicAdapter extends BaseDynamicGridAdapter {
    List<?> val;
    public static final int p[]={R.drawable.visited,R.drawable.speekers,R.drawable.dignitaries,R.drawable.rocket,R.drawable.visited,R.drawable.rocket};

    public GridDynamicAdapter(Context context, List<?> items, int columnCount) {
        super(context, items, columnCount);
        val=items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheeseViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid, null);
            holder = new CheeseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CheeseViewHolder) convertView.getTag();
        }
        holder.build(getItem(position).toString());
        return convertView;
    }

    private class CheeseViewHolder {
        private TextView titleText;
        private ImageView image;
        private CheeseViewHolder(View view) {
            titleText = (TextView) view.findViewById(R.id.item_title);
            image = (ImageView) view.findViewById(R.id.item_img);
        }

        void build(String title) {
            if(title.equals("ENTREPRENIA")) {
                titleText.setText(title);
                image.setImageResource(R.drawable.rocket);
            }
            else  if(title.equals("SPEEKERS")) {
                titleText.setText(title);
                image.setImageResource(R.drawable.speekers);
            }
            else  if(title.equals("TALKS")) {
                titleText.setText(title);
                image.setImageResource(R.drawable.dignitaries);
            }
            else  if(title.equals("COMPETITIONS")) {
                titleText.setText(title);
                image.setImageResource(R.drawable.rocket);
            }
            else  if(title.equals("TALKS")) {
                titleText.setText(title);
                image.setImageResource(R.drawable.dignitaries);
            }
            else  if(title.equals("WORKSHOPS")) {
                titleText.setText(title);
                image.setImageResource(R.drawable.speekers);
            }
            else  if(title.equals("PANEL DISCUSSION")) {
                titleText.setText(title);
                image.setImageResource(R.drawable.dignitaries);
            }
            else  if(title.equals("REGISTER")) {
                titleText.setText(title);
                image.setImageResource(R.drawable.visited);
            }
            else  if(title.equals("CONTACT")) {
                titleText.setText(title);
                image.setImageResource(R.drawable.rocket);
            }
            else {
                titleText.setText(title);
                image.setImageResource(R.drawable.ic_launcher);
            }
        }
    }
}