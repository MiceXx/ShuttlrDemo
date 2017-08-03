package com.accmxxgmail.shuttlrdemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Button;

import com.google.android.gms.location.places.ui.PlacePicker;


public class RequestRideActivity extends AppCompatActivity {

    static RequestRideActivity requestRideActivity;

    private ToggleButton mToggleOneTime, mToggleRecurring, mQuestionMark;
    private Button mButtonBookTrip;
    private TextView mTotalCost, mStartAddress, mEndAddress;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);

        session = new SessionManagement(getApplicationContext());

        requestRideActivity = this;

        mToggleOneTime = (ToggleButton)findViewById(R.id.toggle_one_time);
        mToggleRecurring = (ToggleButton)findViewById(R.id.toggle_recurring);
        mTotalCost = (TextView)findViewById(R.id.tv_total_cost);
        mQuestionMark = (ToggleButton)findViewById(R.id.toggle_question_mark);
        mButtonBookTrip = (Button)findViewById(R.id.button_book_trip);
        mStartAddress = (TextView)findViewById(R.id.tv_start_address);
        mEndAddress = (TextView)findViewById(R.id.tv_end_address);

        mToggleOneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToggleOneTime.setChecked(true);
                mToggleOneTime.setPressed(true);
                if(mToggleRecurring.isChecked()){
                    mToggleRecurring.setChecked(false);
                }

                ComputeCost();
            }
        });

        mToggleRecurring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToggleRecurring.setChecked(true);
                mToggleRecurring.setPressed(true);
                if(mToggleOneTime.isChecked()){
                    mToggleOneTime.setChecked(false);
                }
                ComputeCost();
            }
        });

        mStartAddress.setText(session.getUserAddress());
    }

    public void ComputeCost() {
        int costType;
        String costAddon;
        if (mToggleOneTime.isChecked()) {
            costType = 7;
            costAddon = ".00 single trip";

        } else {
            costType = 140;
            costAddon = ".00 per month";
        }
        String cost = String.valueOf(costType);

        int c = cost.length();
        cost = "$" + cost + costAddon;

        SpannableString ss1 = new SpannableString(cost);
        ss1.setSpan(new RelativeSizeSpan(1.5f),1,1+c,0);
        ss1.setSpan(new ForegroundColorSpan(Color.GREEN),1,1+c,0);
        ss1.setSpan(new RelativeSizeSpan(0.6f),1+c,4+c,0);
        mTotalCost.setText(ss1);

        mButtonBookTrip.setEnabled(true);
        mQuestionMark.setVisibility(View.VISIBLE);
    }

    public void SuccessfullyBooked(View view){
        Intent intent = new Intent(this, SuccessfullyBookedActivity.class);
        startActivity(intent);

    }

    public static RequestRideActivity getInstance(){
        return requestRideActivity;
    }

    public void GoBack(View view){
        finish();
    }

    public void MapPlacePicker(View view){
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(intentBuilder.build(this),1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
