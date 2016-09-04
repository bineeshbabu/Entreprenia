package com.phacsin.entreprenia.main.speeker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.phacsin.entreprenia.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yalantis.flipviewpager.adapter.BaseFlipAdapter;
import com.yalantis.flipviewpager.utils.FlipSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Bineesh P Babu on 19-08-2016.
 */
public class Speakers extends AppCompatActivity {
    boolean locked;
    FlipSettings settings;
    FriendsAdapter adapter;
    List<Friend> friendList = new ArrayList<>();
    ListView friends;
    int colors[] = {R.color.blue,R.color.green,R.color.red,R.color.violet};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_speeker_activity);
        friends = (ListView) findViewById(R.id.friends);
        loadSpeakers();
        settings = new FlipSettings.Builder().defaultPage(1).build();
        friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                settings.savePageState(position,position);
            }
        });
    }

    private void loadSpeakers() {
        String URL = "http://entreprenia.org/app/speakers.php";
        JsonArrayRequest strReq = new JsonArrayRequest(Request.Method.GET,
                URL,null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.d("response", response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject json = response.getJSONObject(i);
                        String URL = "http://entreprenia.org/img/speakers/"+json.getString("speaker_image");
                        String name = json.getString("speaker_name");
                        String designation = json.getString("designation");
                        String event = json.getString("event");
                        Friend friend = new Friend(URL,name,colors[i%colors.length],designation,event);
                        friendList.add(friend);
                    }
                    adapter = new FriendsAdapter(getApplicationContext(),friendList,settings);
                    friends.setAdapter(adapter);
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
                                loadSpeakers();
                            }
                        }).show();
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);
    }


    class FriendsAdapter extends BaseFlipAdapter {

        private final int PAGES = 3;
        Context context;
        public FriendsAdapter(Context context, List<Friend> items, FlipSettings settings) {
            super(context, items, settings);
            this.context=context;
        }

        @Override
        public View getPage(int position, View convertView, ViewGroup parent, Object friend1, final Object friend2) {
            final FriendsHolder holder;

            if (convertView == null) {
                holder = new FriendsHolder();
                convertView = getLayoutInflater().inflate(R.layout.main_speekers, parent, false);
                holder.leftAvatar = (ImageView) convertView.findViewById(R.id.first);
                holder.rightAvatar = (ImageView) convertView.findViewById(R.id.second);
                holder.infoPage = getLayoutInflater().inflate(R.layout.main_speekers2, parent, false);
                holder.nickName = (TextView) holder.infoPage.findViewById(R.id.nickname);

                holder.designation= (TextView) holder.infoPage.findViewById(R.id.speaker_designation);
                holder.event = (TextView) holder.infoPage.findViewById(R.id.speaker_event);

                convertView.setTag(holder);
            } else {
                holder = (FriendsHolder) convertView.getTag();
            }

            switch (position) {
                // Merged page with 2 friends
                case 1:
                    Target left_target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            holder.leftAvatar.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };
                    Target right_target = new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            holder.rightAvatar.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    };
                    Log.d("urls",((Friend) friend1).getAvatar() + "\n" + ((Friend) friend2).getAvatar());
                    Picasso.with(context).load(((Friend) friend1).getAvatar()).into(left_target);
                    if (friend2 != null)
                        Picasso.with(context).load(((Friend) friend2).getAvatar()).into(right_target);
                    break;
                default:
                    fillHolder(holder, position == 0 ? (Friend) friend1 : (Friend) friend2);
                    holder.infoPage.setTag(holder);
                    return holder.infoPage;
            }
            return convertView;
       }

        @Override
        public int getPagesCount() {
            return PAGES;
        }

        private void fillHolder(FriendsHolder holder, Friend friend) {
            if (friend == null)
                return;

            holder.infoPage.setBackgroundColor(getResources().getColor(friend.getBackground()));
            holder.nickName.setText(friend.getNickname());
            holder.designation.setText(friend.getDesignation());
            holder.event.setText(friend.getEvent());
        }

        class FriendsHolder {
            ImageView leftAvatar;
            ImageView rightAvatar;
            View infoPage;
            TextView nickName,designation,event;
        }
    }
}