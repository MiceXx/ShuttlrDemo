package com.accmxxgmail.shuttlrdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    EditText mNameText, mEmailText, mPasswordText,mConfirmPassword;
    Button mSignupButton;
    TextView mLoginLink;
    private boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEmailText = (EditText)findViewById(R.id.text_create_email);
        mNameText = (EditText)findViewById(R.id.text_create_name);
        mPasswordText = (EditText)findViewById(R.id.text_create_password);
        mSignupButton = (Button)findViewById(R.id.button_create_account);
        mLoginLink = (TextView)findViewById(R.id.link_login);
        mConfirmPassword = (EditText)findViewById(R.id.text_confirm_password);

        Firebase.setAndroidContext(this);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        mLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d("SignupActivity", "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        mSignupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String email = EncodeEmail(mEmailText.getText().toString());
        final String name = mNameText.getText().toString();
        final String password = mPasswordText.getText().toString();

        //Connect to Firebase

        String url = "https://active-mountain-168417.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://active-mountain-168417.firebaseio.com/users");

                if(s.equals("null")) {
                    reference.child(email).child("name").setValue(name);
                    reference.child(email).child("password").setValue(password);
                    //Toast.makeText(SignupActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                    success = true;
                }
                else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(email)) {
                            reference.child(email).child("name").setValue(name);
                            reference.child(email).child("password").setValue(password);
                            success = true;
                        } else {
                            success = false;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                progressDialog.dismiss();
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError );
                progressDialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(SignupActivity.this);
        rQueue.add(request);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if(success) {
                            Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            onSignupSuccess();
                        }
                        else{
                            Toast.makeText(SignupActivity.this, "email already exists", Toast.LENGTH_LONG).show();
                            mEmailText.setError("email already exists");
                            onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }
                }, 1000);
    }

    public void onSignupSuccess() {
        mSignupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
        finish();
    }

    public void onSignupFailed() {
        mSignupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = mNameText.getText().toString();
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();
        String confirmpassword = mConfirmPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mNameText.setError("at least 3 characters");
            valid = false;
        } else {
            mNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        if (!password.equals(confirmpassword)) {
            mConfirmPassword.setError("Passwords do not match");
            valid = false;
        } else {
            mConfirmPassword.setError(null);
        }

        return valid;
    }

    public static String EncodeEmail(String string){
        return string.replace(".",",");
    }
}

