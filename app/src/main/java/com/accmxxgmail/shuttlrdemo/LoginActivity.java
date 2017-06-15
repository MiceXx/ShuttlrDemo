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

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;

    SessionManagement session;

    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;
    TextView _forgotpasswordLink;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManagement(getApplicationContext());

        _emailText = (EditText)findViewById(R.id.text_username);
        _passwordText = (EditText)findViewById(R.id.text_password);
        _loginButton = (Button)findViewById(R.id.button_login);
        _signupLink = (TextView)findViewById(R.id.link_signup);
        _forgotpasswordLink = (TextView)findViewById(R.id.link_forgot_password);

        _loginButton.setEnabled(true);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validate();
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

        email = EncodeEmail(_emailText.getText().toString());
        if(session.getUserPhone().equals("") && session.getUserAddress().equals("")) {
                startActivity(new Intent(LoginActivity.this, FirstTimeUserActivity.class));
        }
    }

    public boolean validate() {
        boolean valid = false;

        email = EncodeEmail(_emailText.getText().toString());
        password = _passwordText.getText().toString();
        String url = "https://active-mountain-168417.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String s) {
                if(s.equals("null")){
                }
                else{
                    try {
                        JSONObject obj = new JSONObject(s);
                        if(!obj.has(email)){
                            Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_SHORT).show();
                        }
                        else if(obj.getJSONObject(email).getString("password").equals(password)){
                            session.createLoginSession(email);
                            session.setFirebaseSession(email);
                            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                                    R.style.Theme_AppCompat_DayNight);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Authenticating...");
                            progressDialog.show();

                            String email = _emailText.getText().toString();
                            String password = _passwordText.getText().toString();

                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            onLoginSuccess();
                                            progressDialog.dismiss();
                                        }
                                    }, 3000);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "incorrect password", Toast.LENGTH_SHORT).show();
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
        return valid;

    }

    public static String EncodeEmail(String string){
        return string.replace(".",",");
    }
}
