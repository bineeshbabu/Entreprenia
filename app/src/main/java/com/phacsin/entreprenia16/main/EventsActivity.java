package com.phacsin.entreprenia16.main;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.phacsin.entreprenia16.R;
import com.phacsin.entreprenia16.fragments.EventFragment;

/**
 * Created by Bineesh P Babu on 19-08-2016.
 */
public class EventsActivity extends AppCompatActivity {
    EventFragment talk,competition,panel,workshop;
    Bundle args[] = new Bundle[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_events);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        //upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        for(int i=0;i<4;i++)
            args[i] = new Bundle(i);
        args[0].putString("category","talks");
        talk = new EventFragment();
        talk.setArguments(args[0]);

        args[1].putString("category","workshop");
        workshop = new EventFragment();
        workshop.setArguments(args[1]);

        args[2].putString("category","panel");
        panel = new EventFragment();
        panel.setArguments(args[2]);

        args[3].putString("category","competition");
        competition = new EventFragment();
        competition.setArguments(args[3]);


        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }


    public  class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return competition;
                case 1:
                    return talk;
                case 2:
                    return workshop;
                case 3:
                    return panel;
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "COMPETITION";
                case 1:
                    return "TALK";
                case 2:
                    return "WORKSHOP";
                case 3:
                    return "PANEL";
                default:
                    return null;
            }
        }

    }

}
