package com.accmxxgmail.shuttlrdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;

    SessionManagement session;

    private EditText mEmailText, mPasswordText;
    private Button mLoginButton;
    private TextView mSignupLink, mForgotPasswordLink;
    private Toast mToast;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(!isNetworkConnected()) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(LoginActivity.this);
            }
            builder.setTitle("No Network")
                    .setMessage("Unable to connect to network")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        session = new SessionManagement(getApplicationContext());

        mEmailText = (EditText)findViewById(R.id.text_username);
        mPasswordText = (EditText)findViewById(R.id.text_password);
        mLoginButton = (Button)findViewById(R.id.button_login);
        mSignupLink = (TextView)findViewById(R.id.link_signup);
        mForgotPasswordLink = (TextView)findViewById(R.id.link_forgot_password);

        mLoginButton.setEnabled(true);
        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mToast != null){
                    mToast.cancel();
                }
                validate();
            }
        });

        mSignupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
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
     //   _loginButton.setEnabled(true);
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);

        email = EncodeEmail(mEmailText.getText().toString());
        if(session.getUserPhone().equals("") && session.getUserAddress().equals("")) {
                startActivity(new Intent(LoginActivity.this, FirstTimeUserActivity.class));
        }
    }

    public void validate() {
        email = EncodeEmail(mEmailText.getText().toString());
        password = mPasswordText.getText().toString();
        String url = "https://active-mountain-168417.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String s) {
                if(!s.equals("null")){
                    try {
                        JSONObject obj = new JSONObject(s);
                        if(!obj.has(email)){
                            mToast= Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_SHORT);
                            mToast.show();
                        }
                        else if(obj.getJSONObject(email).getString("password").equals(password)){
                            session.createLoginSession(email);
                            session.setFirebaseSession(email);
                            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                                    R.style.Theme_AppCompat_DayNight);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Authenticating...");
                            progressDialog.show();

                            String email = mEmailText.getText().toString();
                            String password = mPasswordText.getText().toString();

                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            onLoginSuccess();
                                            progressDialog.dismiss();
                                        }
                                    }, 3000);
                        }
                        else {
                            mToast = Toast.makeText(LoginActivity.this, "incorrect password", Toast.LENGTH_SHORT);
                            mToast.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
        rQueue.add(request);
    }

    public static String EncodeEmail(String string){
        return string.replace(".",",");
    }

    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

}
