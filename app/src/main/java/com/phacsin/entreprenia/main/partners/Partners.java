package com.phacsin.entreprenia.main.partners;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.phacsin.entreprenia.R;

/**
 * Created by Bineesh P Babu on 19-08-2016.
 */
public class Partners extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_partners);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Partners");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(toolbar);
    }

}
