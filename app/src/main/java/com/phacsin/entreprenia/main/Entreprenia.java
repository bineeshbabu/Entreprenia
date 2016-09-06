package com.phacsin.entreprenia.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.phacsin.entreprenia.R;


/**
 * Created by Bineesh P Babu on 19-08-2016.
 */
public class Entreprenia extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_entreprenia);
        RelativeLayout r = (RelativeLayout)findViewById(R.id.lay_details);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.entreprenia.org"));
                startActivity(intent);
            }
        });
    }
}
