package com.accmxxgmail.shuttlrdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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

    EditText _nameText;
    EditText _emailText;
    EditText _passwordText;
    Button _signupButton;
    TextView _loginLink;
    EditText _confirmpassword;
    private boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        _emailText = (EditText)findViewById(R.id.text_create_email);
        _nameText = (EditText)findViewById(R.id.text_create_name);
        _passwordText = (EditText)findViewById(R.id.text_create_password);
        _signupButton = (Button)findViewById(R.id.button_create_account);
        _loginLink = (TextView)findViewById(R.id.link_login);
        _confirmpassword = (EditText)findViewById(R.id.text_confirm_password);

        Firebase.setAndroidContext(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
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

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String email = EncodeEmail(_emailText.getText().toString());
        final String name = _nameText.getText().toString();
        final String password = _passwordText.getText().toString();

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
                            _emailText.setError("email already exists");
                            onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }
                }, 1000);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
        finish();
    }

    public void onSignupFailed() {
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirmpassword = _confirmpassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!password.equals(confirmpassword)) {
            _confirmpassword.setError("Passwords do not match");
            valid = false;
        } else {
            _confirmpassword.setError(null);
        }

        return valid;
    }

    public static String EncodeEmail(String string){
        return string.replace(".",",");
    }
}

