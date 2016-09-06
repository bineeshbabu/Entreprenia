package com.phacsin.entreprenia.fragments;


import android.content.res.Configuration;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.phacsin.entreprenia.EventAdapter;
import com.phacsin.entreprenia.Events;
import com.phacsin.entreprenia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


/**
 * Created by Bineesh P Babu on 19-08-2016.
 */
public class EventFragment extends android.support.v4.app.Fragment {
    protected CardView cardView;
    private RecyclerView recyclerView;
    private EventAdapter Adapter;
    private List<Events> eventList ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.eventList);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
        }
        else
        {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
            recyclerView.setLayoutManager(mLayoutManager);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadImages();
        return view;
    }

    private void loadImages() {

        String URL = "http://entreprenia.org/app/events.php?category="+getArguments().getString("category");
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
                        event.image = "http://entreprenia.org/img/events/" + json.getString("event_image");
                        Log.d("response", response.toString());
                        event.brief = json.getString("event_short_app");
                        event.rules = json.getString("rule1");
                        event.date = json.getString("date");
                        event.phone = json.getString("phone");
                        event.coordinator = json.getString("coordinator");
                        event.speaker = json.getString("speaker");
                        event.venue = json.getString("venue");
                        event.prize = json.getString("cash_prize");
                        event.time = json.getString("time");

                        eventList.add(event);
                    }
                    Adapter = new EventAdapter(eventList,getActivity());
                    recyclerView.setAdapter(Adapter);
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
                Snackbar.make(getActivity().findViewById(android.R.id.content), errorMsg, Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                loadImages();
                            }
                        }).show();
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(strReq);
    }
}
