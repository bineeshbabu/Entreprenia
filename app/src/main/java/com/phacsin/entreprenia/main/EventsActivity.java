package com.phacsin.entreprenia.main;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.phacsin.entreprenia.R;
import com.phacsin.entreprenia.fragments.Competitions;
import com.phacsin.entreprenia.fragments.Panel_Discussion;
import com.phacsin.entreprenia.fragments.Talks;
import com.phacsin.entreprenia.fragments.Workshops;

/**
 * Created by Bineesh P Babu on 19-08-2016.
 */
public class EventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_events);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.talk_tab, Talks.class)
                .add(R.string.compet_tab, Competitions.class)
                .add(R.string.workshop_tab, Workshops.class)
                .add(R.string.panel_tab, Panel_Discussion.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

    }

}
