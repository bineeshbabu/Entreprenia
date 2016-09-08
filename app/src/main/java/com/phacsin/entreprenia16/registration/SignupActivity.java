package com.phacsin.entreprenia16.registration;

/**
 * Created by Bineesh P Babu on 04-07-2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phacsin.entreprenia16.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_fname) EditText fname;
    @InjectView(R.id.input_lname) EditText lname;
    @InjectView(R.id.input_cname) AutoCompleteTextView college;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.input_confirmpassword) EditText conf_passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;
    @InjectView(R.id.male_radio) RadioButton  male;
    @InjectView(R.id.input_cnum) EditText phone;
    @InjectView(R.id.accomodation) CheckBox accomodation;
    @InjectView(R.id.female_radio) RadioButton female;
    SharedPreferences sharedpreferences;
    List<String> colleges = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        ButterKnife.inject(this);
        sharedpreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(male.isChecked())
                    female.setChecked(false);
            }
        });
        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(female.isChecked())
                    male.setChecked(false);
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
       accomodation.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (accomodation.isChecked()) {
                   SweetAlertDialog sDialog = new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.WARNING_TYPE);
                   sDialog.setTitleText("Are you sure?");
                   sDialog.setContentText("Additional charges will be applied for accommodation.");
                   sDialog.setConfirmText("OK");
                   sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                       @Override
                       public void onClick(SweetAlertDialog sDialog) {
                           sDialog.dismissWithAnimation();
                       }
                   });
                   sDialog.show();
               }
           }
       });
        college.setThreshold(2);
        college.setDropDownBackgroundResource(R.color.black);
        college.addTextChangedListener(new TextWatcher() {
                                           @Override
                                           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                           }

                                           @Override
                                           public void onTextChanged(CharSequence s, int start, int before, int count) {
                                               loadResults(s);
                                           }

                                           @Override
                                           public void afterTextChanged(Editable s) {

                                           }
                                       }
        );
    }

    private void loadResults(CharSequence s) {
        String URL = "http://entreprenia.org/app/fetch_college.php?text=" + s;
        JsonArrayRequest strReq = new JsonArrayRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    colleges.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject json = response.getJSONObject(i);
                        colleges.add(json.getString("institute_name"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.custom_layout_college,colleges);
                    college.setAdapter(adapter);
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("vError", "Error: " + error.getMessage());
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Registering..");
        pDialog.setCancelable(false);
        pDialog.show();
        String gender,accom;
        if(male.isChecked())
            gender="Male";
        else
            gender="Female";
        if(accomodation.isChecked())
            accom="Yes";
        else
            accom="No";
        String URL = null;
        try {
             URL = "http://entreprenia.org/app/register.php?fname=" + URLEncoder.encode(fname.getText().toString(), "UTF-8") + "&lname=" + URLEncoder.encode(lname.getText().toString(), "UTF-8") + "&email=" + _emailText.getText().toString() + "&password=" + URLEncoder.encode(_passwordText.getText().toString(), "UTF-8") + "&phone=" + URLEncoder.encode(phone.getText().toString(), "UTF-8") + "&college=" + URLEncoder.encode(_passwordText.getText().toString(), "UTF-8") + "&gender=" + gender + "&accom=" + accom;
        }
        catch (Exception e)
        {

        }
        Log.d("reg_URL",URL);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                if(response.equals("Success"))
                    onSignupSuccess();
                else
                {
                    pDialog.hide();
                    /*Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();*/
                    new SweetAlertDialog(SignupActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("error")
                            .setContentText(response)
                            .setConfirmText("Ok")
                            .show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
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
                                signup();
                            }
                        }).show();
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }


    public void onSignupSuccess() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job")
                .setContentText("Verification mail has been sent to your mail")
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                })
                .show();
        _signupButton.setEnabled(true);

    }

    public void onSignupFailed() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Info")
                .setContentText("Registration Failed")
                .setConfirmText("Try again")
                .show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String fname_text = fname.getText().toString();

        String email_text = _emailText.getText().toString();
        String password_text = _passwordText.getText().toString();
        String confirmpass = conf_passwordText.getText().toString();
        String clg = college.getText().toString();
        String mobile = phone.getText().toString();


        if (fname_text.isEmpty() || fname_text.length() < 3 ) {
            fname.setError("at least 3 characters");
            valid = false;
        } else {
            fname.setError(null);
        }

        if(fname.getText().toString().contains(" "))
         fname.getText().toString().replace(" ","%20");

        if (email_text.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email_text).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password_text.isEmpty() || password_text.length() < 4) {
            _passwordText.setError("greater than 4 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
            if(!password_text.equals(confirmpass)){
                conf_passwordText.setError("password mismatch");
                valid = false;
            }
            else {
                conf_passwordText.setError(null);
            }
        }

        if (clg.isEmpty()) {
            college.setError("select college");
            valid = false;
        } else {
            college.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()<10||mobile.length()>10) {
            phone.setError("enter a valid mobile number");
            valid = false;
        } else {
            phone.setError(null);
        }

        return valid;
    }
}
