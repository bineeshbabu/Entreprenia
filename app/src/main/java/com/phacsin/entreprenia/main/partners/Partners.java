package com.phacsin.entreprenia.main.partners;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.phacsin.entreprenia.EventAdapter;
import com.phacsin.entreprenia.Events;
import com.phacsin.entreprenia.PartnerAdapter;
import com.phacsin.entreprenia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bineesh P Babu on 19-08-2016.
 */
public class Partners extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<String> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_partners);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.partnerList);
        toolbar.setTitle("Partners");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(toolbar);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
        }
        else {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
            recyclerView.setLayoutManager(mLayoutManager);
        }
        loadPartner();

    }

    private void loadPartner() {
        String URL = "http://entreprenia.org/app/partners.php";
        JsonArrayRequest strReq = new JsonArrayRequest(Request.Method.GET,
                URL,null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.d("response", response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject json = response.getJSONObject(i);
                        String image = "http://entreprenia.org/img/logos/" + json.getString("logo_image");
                        images.add(image);
                    }
                    PartnerAdapter adapter = new PartnerAdapter(images,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }catch (JSONException e)
                {
                    Log.d("json_error", response.toString());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("vError", "Error: " + error.getMessage());
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
                                loadPartner();
                            }
                        }).show();
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }

}
