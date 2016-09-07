package com.phacsin.entreprenia16;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
    private final int package_id;
    private LayoutInflater mInflator;
    SweetAlertDialog sDialog;
    Activity activity;
    SharedPreferences sharedPreferences;
    List<Events> workshop,panel;
    private String price;


    public ExpandableAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList, List<String> registered_events, Activity activity, List<Events> workshop, List<Events> panel,int package_id) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
        this.registered_events=registered_events;
        this.activity=activity;
        this.workshop=workshop;
        this.panel=panel;
        this.package_id=package_id;
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
            Log.d("registered_events",registered_events.toString());
            if(registered_events.contains(event.id))
                checkbox.setChecked(true);
            else
                checkbox.setChecked(false);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    final TextView price_text=(TextView) activity.findViewById(R.id.price);;
                    sDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                    switch(package_id)
                    {
                        case 0:
                           if(checkbox.isChecked()) {
                               if (!checkBasePackage(event)) {
                                   sDialog.setTitleText("Base Package Limit Exceeded");
                                   sDialog.setConfirmText("Yes");
                                   sDialog.setCancelText("No");
                                   sDialog.setContentText("You can either choose 1 Workshop or any of the panel discussions, This event costs 100Rs. Continue? ");
                                   sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                       @Override
                                       public void onClick(SweetAlertDialog sDialog) {
                                           String url = "http://entreprenia.org/app/add_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                           registerEvent(url);
                                           int current_price = Integer.parseInt(sharedPreferences.getString("total_price", ""));
                                           price = String.valueOf(current_price + 100);
                                           editor.putString("total_price", price);
                                           editor.commit();
                                           price_text.setText("₹ " + price);
                                           registered_events.add(event.id);
                                           updatePrice();
                                           sDialog.dismissWithAnimation();
                                       }
                                   });
                                   sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                       @Override
                                       public void onClick(SweetAlertDialog sDialog) {
                                           checkbox.setChecked(false);
                                           sDialog.dismissWithAnimation();
                                       }
                                   });
                                   sDialog.show();
                               } else {
                                   sDialog.setCancelText("No");
                                   sDialog.setConfirmText("Yes");
                                   sDialog.setTitleText("Are you sure?");
                                   sDialog.setContentText("Do you want to register for " + event.name);
                                   sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                       @Override
                                       public void onClick(SweetAlertDialog sDialog) {
                                           String url = "http://entreprenia.org/app/add_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                           registerEvent(url);
                                           registered_events.add(event.id);
                                           sDialog.dismissWithAnimation();
                                       }
                                   });
                                   sDialog.show();
                               }
                           }

                        else
                        {
                                if (!checkBasePackage(event)) {
                                    sDialog.setContentText("Do you want to unregister for " + event_name.getText().toString());
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            String url = "http://entreprenia.org/app/remove_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                            registerEvent(url);
                                            registered_events.remove(event.id);
                                            int current_price = Integer.parseInt(sharedPreferences.getString("total_price", ""));
                                            price=String.valueOf(current_price);
                                            if(!checkBasePackage(event))
                                            {
                                                price = String.valueOf(current_price - 100);
                                                Log.d("price", price);
                                            }
                                            editor.putString("total_price", price);
                                            editor.commit();
                                            price_text.setText("₹ " + price);
                                            updatePrice();
                                            sDialog.dismissWithAnimation();
                                        }
                                    });
                                    sDialog.show();
                                }
                                else {
                                    sDialog.setContentText("Do you want ro unregister for " + event_name.getText().toString());
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            String url = "http://entreprenia.org/app/remove_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                            registerEvent(url);
                                            registered_events.remove(event.id);
                                            sDialog.dismissWithAnimation();
                                        }
                                    });
                                    sDialog.show();
                                }

                        }
                            sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    if (checkbox.isChecked())
                                        checkbox.setChecked(false);
                                    else
                                        checkbox.setChecked(true);

                                }
                            });


                            break;


                        case 1:
                            if(checkbox.isChecked()) {
                                if (!checkEconomyPackage(event)) {
                                    sDialog.setTitleText("Base Package Limit Exceeded");
                                    sDialog.setConfirmText("Yes");
                                    sDialog.setCancelText("No");
                                    sDialog.setContentText("You can either choose 1 Workshop or any of the panel discussions, This event costs 100Rs. Continue? ");
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            String url = "http://entreprenia.org/app/add_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                            registerEvent(url);
                                            int current_price = Integer.parseInt(sharedPreferences.getString("total_price", ""));
                                            price = String.valueOf(current_price + 100);
                                            editor.putString("total_price", price);
                                            editor.commit();
                                            price_text.setText("₹ " + price);
                                            registered_events.add(event.id);
                                            updatePrice();
                                            sDialog.dismissWithAnimation();
                                        }
                                    });
                                    sDialog.show();
                                } else {
                                    sDialog.setCancelText("No");
                                    sDialog.setConfirmText("Yes");
                                    sDialog.setTitleText("Are you sure?");
                                    sDialog.setContentText("Do you want to register for " + event.name);
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            String url = "http://entreprenia.org/app/add_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                            registerEvent(url);
                                            registered_events.add(event.id);
                                            sDialog.dismissWithAnimation();
                                        }
                                    });
                                    sDialog.show();
                                }
                            }

                            else
                            {
                                if (!checkEconomyPackage(event)) {
                                    sDialog.setCancelText("No");
                                    sDialog.setConfirmText("Yes");
                                    sDialog.setContentText("Do you want to unregister for " + event_name.getText().toString());
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            String url = "http://entreprenia.org/app/remove_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                            registerEvent(url);
                                            registered_events.remove(event.id);
                                            int current_price = Integer.parseInt(sharedPreferences.getString("total_price", ""));
                                            price = String.valueOf(current_price - 100);
                                            Log.d("price", price);
                                            editor.putString("total_price", price);
                                            editor.commit();
                                            price_text.setText("₹ " + price);
                                            updatePrice();
                                            sDialog.dismissWithAnimation();
                                        }
                                    });
                                    sDialog.show();
                                }
                                else {
                                    sDialog.setCancelText("No");
                                    sDialog.setConfirmText("Yes");
                                    sDialog.setContentText("Do you want to unregister for " + event_name.getText().toString());
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            String url = "http://entreprenia.org/app/remove_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                            registerEvent(url);
                                            registered_events.remove(event.id);
                                            sDialog.dismissWithAnimation();
                                        }
                                    });
                                    sDialog.show();
                                }

                            }
                            sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    if (checkbox.isChecked())
                                        checkbox.setChecked(false);
                                    else
                                        checkbox.setChecked(true);

                                }
                            });


                            break;
                        case 3:
                            if(checkbox.isChecked()) {
                                if (!checkBusinessPackage(event)) {
                                    sDialog.setTitleText("Base Package Limit Exceeded");
                                    sDialog.setConfirmText("Yes");
                                    sDialog.setCancelText("No");
                                    sDialog.setContentText("You can either choose 1 Workshop or any of the panel discussions, This event costs 100Rs. Continue? ");
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            String url = "http://entreprenia.org/app/add_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                            registerEvent(url);
                                            int current_price = Integer.parseInt(sharedPreferences.getString("total_price", ""));
                                            price = String.valueOf(current_price + 100);
                                            editor.putString("total_price", price);
                                            editor.commit();
                                            price_text.setText("₹ " + price);
                                            registered_events.add(event.id);
                                            updatePrice();
                                            sDialog.dismissWithAnimation();
                                        }
                                    });
                                    sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            checkbox.setChecked(false);
                                            sDialog.dismissWithAnimation();
                                        }
                                    });
                                    sDialog.show();
                                } else {
                                    sDialog.setCancelText("No");
                                    sDialog.setConfirmText("Yes");
                                    sDialog.setTitleText("Are you sure?");
                                    sDialog.setContentText("Do you want to register for " + event.name);
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            String url = "http://entreprenia.org/app/add_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                            registerEvent(url);
                                            registered_events.add(event.id);
                                            sDialog.dismissWithAnimation();
                                        }
                                    });
                                    sDialog.show();
                                }
                            }

                            else
                            {
                                if (!checkBusinessPackage(event)) {
                                    sDialog.setContentText("Do you want to unregister for " + event_name.getText().toString());
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            String url = "http://entreprenia.org/app/remove_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                            registerEvent(url);
                                            registered_events.remove(event.id);
                                            int current_price = Integer.parseInt(sharedPreferences.getString("total_price", ""));
                                            price = String.valueOf(current_price - 100);
                                            Log.d("price", price);
                                            editor.putString("total_price", price);
                                            editor.commit();
                                            price_text.setText("₹ " + price);
                                            updatePrice();
                                            sDialog.dismissWithAnimation();
                                        }
                                    });
                                    sDialog.show();
                                }
                                else {
                                    sDialog.setContentText("Do you want ro unregister for " + event_name.getText().toString());
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            String url = "http://entreprenia.org/app/remove_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                            registerEvent(url);
                                            registered_events.remove(event.id);
                                            sDialog.dismissWithAnimation();
                                        }
                                    });
                                    sDialog.show();
                                }
                            }
                            sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    if (checkbox.isChecked())
                                        checkbox.setChecked(false);
                                    else
                                        checkbox.setChecked(true);

                                }
                            });

                            break;
                        case 4:
                            if(checkbox.isChecked()) {
                                sDialog.setCancelText("No");
                                sDialog.setConfirmText("Yes");
                                sDialog.setTitleText("Are you sure?");
                                sDialog.setContentText("Do you want to register for " + event.name);
                                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        String url = "http://entreprenia.org/app/add_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                        registerEvent(url);
                                        registered_events.add(event.id);
                                        sDialog.dismissWithAnimation();
                                    }
                                });
                                sDialog.show();
                            }
                            else
                            {
                                sDialog.setContentText("Do you want ro unregister for " + event_name.getText().toString());
                                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        String url = "http://entreprenia.org/app/remove_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                        registerEvent(url);
                                        registered_events.remove(event.id);
                                        sDialog.dismissWithAnimation();
                                    }
                                });
                                sDialog.show();
                            }
                            sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    if (checkbox.isChecked())
                                        checkbox.setChecked(false);
                                    else
                                        checkbox.setChecked(true);

                                }
                            });
                            break;
                        case 5:
                            if(checkbox.isChecked()) {
                                sDialog.setCancelText("No");
                                sDialog.setConfirmText("Yes");
                                sDialog.setTitleText("Are you sure?");
                                sDialog.setContentText("Do you want to register for " + event.name);
                                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        String url = "http://entreprenia.org/app/add_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                        registerEvent(url);
                                        registered_events.add(event.id);
                                        sDialog.dismissWithAnimation();
                                    }
                                });
                                sDialog.show();
                            }
                            else
                            {
                                sDialog.setContentText("Do you want ro unregister for " + event_name.getText().toString());
                                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        String url = "http://entreprenia.org/app/remove_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                        registerEvent(url);
                                        registered_events.remove(event.id);
                                        sDialog.dismissWithAnimation();
                                    }
                                });
                                sDialog.show();
                            }
                            sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    if (checkbox.isChecked())
                                        checkbox.setChecked(false);
                                    else
                                        checkbox.setChecked(true);

                                }
                            });
                            break;
                        case 6:
                            if(checkbox.isChecked()) {
                                sDialog.setCancelText("No");
                                sDialog.setConfirmText("Yes");
                                sDialog.setTitleText("Are you sure?");
                                sDialog.setContentText("Do you want to register for " + event.name);
                                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        String url = "http://entreprenia.org/app/add_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                        registerEvent(url);
                                        registered_events.add(event.id);
                                        sDialog.dismissWithAnimation();
                                    }
                                });
                                sDialog.show();
                            }
                            else
                            {
                                sDialog.setContentText("Do you want ro unregister for " + event_name.getText().toString());
                                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        String url = "http://entreprenia.org/app/remove_event.php?email=" + sharedPreferences.getString("email", "") + "&event_id=" + event.id;
                                        registerEvent(url);
                                        registered_events.remove(event.id);
                                        sDialog.dismissWithAnimation();
                                    }
                                });
                                sDialog.show();
                            }
                            sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    if (checkbox.isChecked())
                                        checkbox.setChecked(false);
                                    else
                                        checkbox.setChecked(true);

                                }
                            });
                            break;

                }
                }});

        }

        private boolean checkBasePackage(Events event) {
            Log.d("category",event.category);

            if(event.category.equals("panel"))
            {
                for(Events panel_event : panel)
                    if(registered_events.contains(panel_event.id))
                        return true;
                for(Events work_event : workshop)
                    if(registered_events.contains(work_event.id))
                        return false;
                return true;
            }
            else if(event.category.equals("workshop"))
            {
                for(Events work_event : workshop)
                    if(registered_events.contains(work_event.id))
                        return false;
                for(Events panel_event : panel)
                    if(registered_events.contains(panel_event.id))
                        return false;
            }
            else if(event.category.equals("competition"))
                return false;
            return true;
        }
    }

    private boolean checkEconomyPackage(Events event) {
        Log.d("category",event.category);
        if(event.category.equals("workshop"))
                    return false;
        return true;
    }

    private boolean checkBusinessPackage(Events event) {
        Log.d("category",event.category);
        if(event.category.equals("competition"))
            return false;
        return true;
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

    private void updatePrice() {
        String url = "http://entreprenia.org/app/update_price.php?email=" + sharedPreferences.getString("email", "") + "&price=" + price;

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response.toString());
                if(response.equals("Success")) {
                    Toast.makeText(activity, "Price Updated", Toast.LENGTH_LONG).show();
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
                                updatePrice();
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