package com.phacsin.entreprenia16;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    @InjectView(R.id.package_text) TextView  package_text;
    @InjectView(R.id.selec) TextView  selec_;
    @InjectView(R.id.user_profile_photo) ImageView qr_code;
    @InjectView(R.id.logout) Button logout;

    SharedPreferences sharedPreferences;
    String email;
    String QR_URL;
    RecyclerView recyclerView;
    LinearLayout rootView;
    int layoutList[] = {R.layout.card_basic,R.layout.card_ecocnomy,R.layout.card_business,R.layout.card_iedc,R.layout.card_nss,R.layout.card_exe};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_profile);
        final CarouselLayoutManager layoutManager;
        recyclerView= (RecyclerView) findViewById(R.id.packageList);
        rootView = (LinearLayout) findViewById(R.id.rootView);
        recyclerView.addOnScrollListener(new CenterScrollListener());
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            layoutManager =  new CarouselLayoutManager(CarouselLayoutManager.VERTICAL);
        else
            layoutManager =  new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        ButterKnife.inject(this);
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email","");
        email_text.setText(email);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("email");
                editor.remove("package_id");
                editor.remove("uuid");
                editor.commit();
                startActivity(new Intent(getApplicationContext(),PieChartActivity.class));
                finish();
            }
        });
        if(!sharedPreferences.contains("package_id"))
        {
            notSelectedPackage();
        }
        else
        {
            int package_id = sharedPreferences.getInt("package_id",0);
            int Package_sel[] = new int[1];
            Package_sel[0]=layoutList[package_id];
            package_text.setText("Selected Package");
            selec_.setText("Click the Card For Registration");
            PackageAdapter adapter = new PackageAdapter(Package_sel,getApplicationContext(),rootView,Profile.this,"Package");
            recyclerView.setAdapter(adapter);
        }
        loadUUID();

    }

    private void notSelectedPackage() {
        String URL = "http://entreprenia.org/app/package_return.php?email="+email;
        Log.d("url",URL);
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                URL,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("response_json", response.toString());
                String package_id = response.optString("package_id");
                if(!package_id.equals("0"))
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("package_id", Integer.parseInt(package_id)-1);
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(),Profile.class));
                    finish();
                }
                else
                {
                    PackageAdapter adapter = new PackageAdapter(layoutList, getApplicationContext(), rootView, Profile.this,"Package");
                    recyclerView.setAdapter(adapter);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Verror", "Error: " + error.getMessage());
                String errorMsg;
                if(error instanceof NoConnectionError)
                    errorMsg = "Network Error";
                else if(error instanceof TimeoutError)
                    errorMsg = "Timeout Error";
                else
                    errorMsg = "Unknown Error";
                Snackbar.make(findViewById(android.R.id.content), errorMsg, Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getApplicationContext(),Profile.class));
                                finish();
                            }
                        }).show();
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }


    private void loadUUID() {
        String URL = "http://entreprenia.org/app/uuid_fetch.php?email="+email;
        Log.d("UUID_url",URL);
            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                    URL, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response UUID", response.toString());
                    QR_URL = "https://chart.googleapis.com/chart?cht=qr&chl="+response.optString("uuid")+"&choe=UTF-8&chs=500x500";
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
}