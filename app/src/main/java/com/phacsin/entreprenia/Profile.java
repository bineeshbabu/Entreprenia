package com.phacsin.entreprenia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;

/**
 * Created by Bineesh P Babu on 29-08-2016.
 */
public class Profile extends AppCompatActivity {
    int layoutList[] = {R.layout.card_basic,R.layout.card_ecocnomy,R.layout.card_business,R.layout.card_iedc,R.layout.card_nss,R.layout.card_exe};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_profile);
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.packageList);
        recyclerView.addOnScrollListener(new CenterScrollListener());
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        PackageAdapter adapter = new PackageAdapter(layoutList);
        recyclerView.setAdapter(adapter);
    }
}