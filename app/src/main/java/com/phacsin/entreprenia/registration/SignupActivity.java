package com.phacsin.entreprenia.registration;

/**
 * Created by Bineesh P Babu on 04-07-2016.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phacsin.entreprenia.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_fname) EditText fname;
    @InjectView(R.id.input_lname) EditText lname;
    @InjectView(R.id.input_cname) EditText college;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;
    @InjectView(R.id.male_radio) RadioButton  male;
    @InjectView(R.id.input_cnum) EditText phone;
    @InjectView(R.id.accomodation) CheckBox accomodation;
    @InjectView(R.id.female_radio) RadioButton female;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        ButterKnife.inject(this);
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
        String URL = "http://entreprenia.org/app/register.php?fname="+fname.getText().toString()+"&lname="+lname.getText().toString()+"&email="+_emailText.getText().toString()+"&password"+_passwordText.getText().toString()+"&phone="+phone.getText().toString()+"&college="+college.getText().toString()+"&gender="+gender+"&accom="+accom;
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
                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
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
                .setContentText("You are Registered")
                .show();
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String fname_text = fname.getText().toString();
        String mobile = phone.getText().toString();

        String email_text = _emailText.getText().toString();
        String password_text = _passwordText.getText().toString();

        if (fname_text.isEmpty() || fname_text.length() < 3) {
            fname.setError("at least 3 characters");
            valid = false;
        } else {
            fname.setError(null);
        }

        if (email_text.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email_text).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password_text.isEmpty() || password_text.length() < 4 || password_text.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (mobile.isEmpty()&&password_text.length()==10) {
            _passwordText.setError("eenter a valid mobile number");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
