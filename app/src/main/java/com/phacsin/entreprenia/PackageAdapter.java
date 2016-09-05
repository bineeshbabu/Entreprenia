package com.phacsin.entreprenia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phacsin.entreprenia.registration.ForgotPassword;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by GD on 9/5/2016.
 */
public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.MyViewHolder> {

    private final View rootView;
    private final Activity activity;
    private int[] layoutList;
    Context context;
    SweetAlertDialog sDialog;
    SharedPreferences sharedPreferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(final View view) {
            super(view);
            sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            if(!sharedPreferences.contains("package_id")) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                        sDialog.setTitleText("Are you sure?");
                        sDialog.setContentText("You won't be able to change the base package");
                        sDialog.setCancelText("No");
                        sDialog.setConfirmText("Yes");
                        sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                addPackagetoServer(getLayoutPosition()+1);
                                sDialog.dismissWithAnimation();
                            }
                        });
                        sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.dismissWithAnimation();
                            }
                        });
                        sDialog.show();
                    }
                });
            }
            else
            {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.startActivity(new Intent(context, Alacarte.class));
                    }
                });
            }
        }
    }

    private void addPackagetoServer(final int package_id) {
        String email=sharedPreferences.getString("email","");
        String URL = "http://entreprenia.org/app/addPackage.php?email="+email+"&package_id="+package_id;
        Log.d("URL",URL);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response.toString());
                if(response.equals("Success"))
                {
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("package_id", package_id-1);
                    editor.commit();
                    activity.startActivity(new Intent(context,Profile.class));
                }
                else
                    Toast.makeText(context,response,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VError", "Error: " + error.getMessage());
                String errorMsg;
                if(error instanceof NoConnectionError)
                    errorMsg = "Network Error";
                else if(error instanceof TimeoutError)
                    errorMsg = "Timeout Error";
                else
                    errorMsg = "Unknown Error";
                Snackbar.make(rootView, errorMsg, Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addPackagetoServer(package_id);
                            }
                        }).show();
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
    }


    public PackageAdapter(int [] layoutList, Context context, View rootView, Activity activity) {
        this.layoutList = layoutList;
        this.context=context;
        this.rootView=rootView;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutList[viewType], parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return layoutList.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}