package com.accmxxgmail.shuttlrdemo;

import android.content.Intent;
import java.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class SuccessfullyBookedActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully_booked);

        getCurrentDate();
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

    public void getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat("MMMM dd hh:mma");
        String stringDate = formatDate.format(calendar.getTime());
        TextView textView = (TextView)findViewById(R.id.textViewTodayDate);
        textView.setText(stringDate);
    }

}
