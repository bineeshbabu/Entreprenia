package com.phacsin.entreprenia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Bineesh P Babu on 29-08-2016.
 */
public class Profile extends AppCompatActivity {
    @InjectView(R.id.user_profile_id) TextView  uid;
    @InjectView(R.id.user_email) TextView  email_text;
    @InjectView(R.id.user_profile_photo) ImageView qr_code;

    SharedPreferences sharedPreferences;
    String email;
    String QR_URL;


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
        ButterKnife.inject(this);
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email","");
        email_text.setText(email);
        loadUUID();
    }

    private void loadUUID() {
        String URL = "http://entreprenia.org/app/uuid_fetch.php?email="+email;
        if(!sharedPreferences.contains("uid")) {
            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                    URL, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", response.toString());
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    QR_URL = "https://chart.googleapis.com/chart?cht=qr&chl="+response.optString("uuid")+"&choe=UTF-8&chs=500x500";
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid", response.optString("uuid"));
                    editor.commit();
                    uid.setText(response.optString("uuid"));
                    Picasso.with(getApplicationContext()).load(QR_URL).into(qr_code);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Volley Error", "Error: " + error.getMessage());
                    String errorMsg;
                    if (error instanceof NoConnectionError)
                        errorMsg = "Network Error";
                    else if (error instanceof TimeoutError)
                        errorMsg = "Timeout Error";
                    else
                        errorMsg = "Unknown Error";
                    Snackbar.make(findViewById(android.R.id.content), errorMsg, Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loadUUID();
                                }
                            }).show();
                }

            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(strReq);
        }
        else {
            String id = sharedPreferences.getString("uid","");
            QR_URL = "https://chart.googleapis.com/chart?cht=qr&chl="+id+"&choe=UTF-8&chs=500x500";
            Picasso.with(getApplicationContext()).load(QR_URL).into(qr_code);
        }
    }
}