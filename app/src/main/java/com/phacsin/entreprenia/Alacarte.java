package com.phacsin.entreprenia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;

/**
 * Created by Bineesh P Babu on 05-09-2016.
 */
public class Alacarte extends Activity {
    FloatingGroupExpandableListView list;
    String events[][];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sample_activity);

        list  = (FloatingGroupExpandableListView) findViewById(R.id.sample_activity_list);

        final LayoutInflater inflater = getLayoutInflater();

        final View header = inflater.inflate(R.layout.sample_activity_list_header, list, false);
        list.addHeaderView(header);

        RelativeLayout rl_f=(RelativeLayout) findViewById(R.id.lay_foot);
        rl_f.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.entreprenia.org"));
                startActivity(intent);
            }
        });

        list.setChildDivider(new ColorDrawable(Color.BLACK));
        loadEvents();
        showDropdownList();
}

    private void loadEvents() {

    }

    private void showDropdownList() {
        final SampleAdapter adapter = new SampleAdapter(this,events);
        final WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(adapter);
        list.setAdapter(wrapperAdapter);

        for(int i = 0; i < wrapperAdapter.getGroupCount(); i++) {
            list.expandGroup(i);
        }

        list.setOnScrollFloatingGroupListener(new FloatingGroupExpandableListView.OnScrollFloatingGroupListener() {

            @Override
            public void onScrollFloatingGroupListener(View floatingGroupView, int scrollY) {
                float interpolation = - scrollY / (float) floatingGroupView.getHeight();

                // Changing from RGB(162,201,85) to RGB(255,255,255)
                final int greenToWhiteRed = (int) (162 + 93 * interpolation);
                final int greenToWhiteGreen = (int) (201 + 54 * interpolation);
                final int greenToWhiteBlue = (int) (85 + 170 * interpolation);
                final int greenToWhiteColor = Color.argb(255, greenToWhiteRed, greenToWhiteGreen, greenToWhiteBlue);

                // Changing from RGB(255,255,255) to RGB(0,0,0)
                final int whiteToBlackRed = (int) (255 - 255 * interpolation);
                final int whiteToBlackGreen = (int) (255 - 255 * interpolation);
                final int whiteToBlackBlue = (int) (255 - 255 * interpolation);
                final int whiteToBlackColor = Color.argb(255, whiteToBlackRed, whiteToBlackGreen, whiteToBlackBlue);

                final ImageView image = (ImageView) floatingGroupView.findViewById(R.id.sample_activity_list_group_item_image);
                image.setBackgroundColor(greenToWhiteColor);

                final Drawable imageDrawable = image.getDrawable().mutate();
                imageDrawable.setColorFilter(whiteToBlackColor, PorterDuff.Mode.SRC_ATOP);

                final View background = floatingGroupView.findViewById(R.id.sample_activity_list_group_item_background);
                background.setBackgroundColor(greenToWhiteColor);

                final TextView text = (TextView) floatingGroupView.findViewById(R.id.sample_activity_list_group_item_text);
                text.setTextColor(whiteToBlackColor);

              /*  final ImageView expanded = (ImageView) floatingGroupView.findViewById(R.id.sample_activity_list_group_expanded_image);
                final Drawable expandedDrawable = expanded.getDrawable().mutate();
                expandedDrawable.setColorFilter(whiteToBlackColor, PorterDuff.Mode.SRC_ATOP);*/
            }
        });
    }
    }
