package com.phacsin.entreprenia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by GD on 9/6/2016.
 */
public class ExpandableAdapter extends ExpandableRecyclerAdapter<ExpandableAdapter.CategoryViewHolder, ExpandableAdapter.EventViewHolder> {

    private final List<String> registered_events;
    private LayoutInflater mInflator;
    SweetAlertDialog sDialog;
    Activity activity;
    SharedPreferences sharedPreferences;


    public ExpandableAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList, List<String> registered_events,Activity activity) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
        this.registered_events=registered_events;
        this.activity=activity;
        sharedPreferences = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);

    }

    public class CategoryViewHolder extends ParentViewHolder {

        private TextView category_name;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            category_name = (TextView) itemView.findViewById(R.id.sample_activity_list_group_item_text);
        }

        public void bind(Category category) {
            category_name.setText(category.getName());
        }
    }

    public class EventViewHolder extends ChildViewHolder {

        private TextView event_name;
        private CheckBox checkbox;
        public EventViewHolder(View itemView) {
            super(itemView);
            event_name = (TextView) itemView.findViewById(R.id.sample_activity_list_child_item_text);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);

                }

        public void bind(final Events event) {

            event_name.setText(event.name);
            Log.d("event_id",event.id  + " " + getLayoutPosition());
            if(registered_events.contains(event.id))
                checkbox.setChecked(true);
            else
                checkbox.setChecked(false);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                    sDialog.setCancelText("No");
                    sDialog.setConfirmText("Yes");
                    sDialog.setTitleText("Are you sure?");
                    if (checkbox.isChecked() ) {

                        sDialog.setContentText("Do you want ro register for " + event.name);
                        sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                String url = "http://entreprenia.org/app/add_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                registerEvent(url);
                                sDialog.dismissWithAnimation();

                            }
                        });
                        sDialog.show();

                    } else {
                        sDialog.setContentText("Do you want ro unregister for " + event_name.getText().toString());
                        sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                String url = "http://entreprenia.org/app/remove_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                registerEvent(url);
                                sDialog.dismissWithAnimation();
                            }
                        });
                        sDialog.show();
                    }
                    sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            if(checkbox.isChecked())
                                checkbox.setChecked(false);
                            else
                                checkbox.setChecked(true);

                        }
                    });
                }
            });
        }
    }

    private void registerEvent(final String url) {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response.toString());
                if(response.equals("Success")) {
                    Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show();
                }
                else
                {
                    sDialog.hide();
                    Toast.makeText(activity,response,Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley Erro", "Error: " + error.getMessage());
                sDialog.hide();
                String errorMsg;
                if(error instanceof NoConnectionError)
                    errorMsg = "Network Error";
                else if(error instanceof TimeoutError)
                    errorMsg = "Timeout Error";
                else
                    errorMsg = "Unknown Error";
                Snackbar.make(activity.findViewById(android.R.id.content), errorMsg, Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                registerEvent(url);
                            }
                        }).show();
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(strReq);

    }

    // onCreate ...
    @Override
    public CategoryViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View recipeView = mInflator.inflate(R.layout.sample_activity_list_group_item, parentViewGroup, false);
        return new CategoryViewHolder(recipeView);
    }

    @Override
    public EventViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View ingredientView = mInflator.inflate(R.layout.sample_activity_list_child_item, childViewGroup, false);
        return new EventViewHolder(ingredientView);
    }

    // onBind ...
    @Override
    public void onBindParentViewHolder(CategoryViewHolder recipeViewHolder, int position, ParentListItem parentListItem) {
        Category category = (Category) parentListItem;
        recipeViewHolder.bind(category);
    }

    @Override
    public void onBindChildViewHolder(EventViewHolder ingredientViewHolder, int position, Object childListItem) {
        Events event = (Events) childListItem;
        ingredientViewHolder.bind(event);
    }
}