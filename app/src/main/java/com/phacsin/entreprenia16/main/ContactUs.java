package com.phacsin.entreprenia16.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.phacsin.entreprenia16.R;

/**
 * Created by Bineesh P Babu on 19-08-2016.
 */
public class ContactUs extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_contact_us);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contact");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(toolbar);
        LinearLayout lt=(LinearLayout)findViewById(R.id.lay_kiron);
        LinearLayout lt2=(LinearLayout)findViewById(R.id.lay_mail_jyo);
        LinearLayout lt3=(LinearLayout)findViewById(R.id.lay_mail_febin);
        LinearLayout lt4=(LinearLayout)findViewById(R.id.lay_mail_hari);

        lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{  "kiron@entreprenia.org"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello Sir");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I'm Ready for Entreprenia");


                emailIntent.setType("message/rfc822");

                try {
                    startActivity(Intent.createChooser(emailIntent,
                            "Send email using..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),
                            "No email clients installed.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        lt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{  "jyothish@entreprenia.org"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello Sir");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I'm Ready for Entreprenia");


                emailIntent.setType("message/rfc822");

                try {
                    startActivity(Intent.createChooser(emailIntent,
                            "Send email using..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),
                            "No email clients installed.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        lt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{  "febin@entreprenia.org"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello Febin");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I'm Ready for Entreprenia");


                emailIntent.setType("message/rfc822");

                try {
                    startActivity(Intent.createChooser(emailIntent,
                            "Send email using..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),
                            "No email clients installed.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        lt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{  "hari@entreprenia.org"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello Hari");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I'm Ready for Entreprenia");


                emailIntent.setType("message/rfc822");

                try {
                    startActivity(Intent.createChooser(emailIntent,
                            "Send email using..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),
                            "No email clients installed.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

}

