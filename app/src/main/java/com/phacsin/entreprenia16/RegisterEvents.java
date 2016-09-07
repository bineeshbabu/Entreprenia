package com.phacsin.entreprenia16;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by GD on 9/6/2016.
 */
public class RegisterEvents extends AppCompatActivity {

    List<Events> talk,competition,workshop,panel;
    private List<Events> eventList;
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    TextView package_name;
    List<String> registered_events = new ArrayList<>();
    TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_events);
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) findViewById(R.id.eventList);
        package_name = (TextView) findViewById(R.id.package_name);
        price = (TextView) findViewById(R.id.price);
        package_name.setText(getIntent().getStringExtra("package_name"));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        loadEvents();
    }


    private void loadEvents() {
        String URL = "http://entreprenia.org/app/events_all.php";
        JsonArrayRequest strReq = new JsonArrayRequest(Request.Method.GET,
                URL,null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.d("response", response.toString());
                    eventList = new ArrayList<Events>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject json = response.getJSONObject(i);
                        Events event = new Events();
                        event.name = json.getString("event_name");
                        event.id = json.getString("event_id");
                        event.category = json.getString("category");
                        eventList.add(event);
                    }
                    loadPrice();
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
                                loadEvents();
                            }
                        }).show();
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }

    private void loadPrice() {
        String URL = "http://entreprenia.org/app/total_price.php?email="+sharedPreferences.getString("email","");
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                    Log.d("response", response.toString());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("total_price",response);
                    editor.commit();
                    price.setText("₹ "+response);
                    checkRegisteredEvents();
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
                                loadEvents();
                            }
                        }).show();
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }

    private void checkRegisteredEvents()
    {
        String email = sharedPreferences.getString("email","");
        String URL = "http://entreprenia.org/app/get_registered_events.php?email="+email;
        JsonArrayRequest strReq = new JsonArrayRequest(Request.Method.GET,
                URL,null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.d("response", response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject json = response.getJSONObject(i);
                        registered_events.add(json.getString("event_id"));
                    }
                    Log.d("registered_events", registered_events.toString());
                    showPackage();
                }catch (JSONException e)
                {
                    Log.d("json_error", e.toString());
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
                                loadEvents();
                            }
                        }).show();
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }

    private void showPackage() {
        int package_id = sharedPreferences.getInt("package_id",0);
        talk = new ArrayList<>();
        workshop = new ArrayList<>();
        competition = new ArrayList<>();
        panel = new ArrayList<>();
        Category talk_category,comp_category,workshop_category,panel_category;
        for(Events event:eventList)
        {
            if(event.category.equals("talks"))
                talk.add(event);
            else if(event.category.equals("panel"))
                panel.add(event);
            else if(event.category.equals("workshop"))
                workshop.add(event);
            else if(event.category.equals("competition"))
                competition.add(event);
        }
        talk_category = new Category(talk,"Talks");
        workshop_category = new Category(workshop,"Workshops");
        comp_category = new Category(competition,"Competitions");
        panel_category = new Category(panel,"Panel Discussions");
        List<Category> categories=null;
        categories = Arrays.asList(talk_category,workshop_category,comp_category,panel_category);
        ExpandableAdapter adapter = new ExpandableAdapter(this, categories,registered_events,RegisterEvents.this,workshop,panel,package_id);
        recyclerView.setAdapter(adapter);
    }


    public void moreHelp(View view) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.entreprenia.org"));
        startActivity(intent);
    }
}
