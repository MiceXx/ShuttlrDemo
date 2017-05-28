package com.accmxxgmail.shuttlrdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static com.accmxxgmail.shuttlrdemo.RequestRideActivity.rractivity;

public class SuccessfullyBookedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully_booked);

    }

    public void CloseScreen(View view){
        finish();
        RequestRideActivity.getInstance().finish();
    }

    public void TrackRide(View view){
        finish();
        RequestRideActivity.getInstance().finish();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}
