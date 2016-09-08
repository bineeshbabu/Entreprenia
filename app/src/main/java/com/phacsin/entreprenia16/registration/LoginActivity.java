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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.phacsin.entreprenia16.Profile;
import com.phacsin.entreprenia16.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;
    @InjectView(R.id.link_forgot) TextView forgot;

    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_registration_login);
        ButterKnife.inject(this);
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Signing in");
        pDialog.setCancelable(false);
        pDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        String URL = "http://entreprenia.org/app/login.php?email="+email+"&password="+password;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                if(response.equals("Success")) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", _emailText.getText().toString());
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(),Profile.class));
                    pDialog.hide();
                }
                else
                {
                    pDialog.hide();
                    _loginButton.setEnabled(true);
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
                                login();
                            }
                        }).show();
            }

        });

// Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            _passwordText.setError("greater than alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }
}
