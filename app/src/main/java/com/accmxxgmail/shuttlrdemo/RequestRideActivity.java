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


public class RequestRideActivity extends AppCompatActivity {

    static RequestRideActivity rractivity;

    private ToggleButton toggleOneTime, toggleRecurring, questionMark;
    private Button buttonBookT;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);

        rractivity = this;

        toggleOneTime = (ToggleButton)findViewById(R.id.toggle_one_time);
        toggleRecurring = (ToggleButton)findViewById(R.id.toggle_recurring);
        textView = (TextView)findViewById(R.id.text_total_cost);
        questionMark = (ToggleButton)findViewById(R.id.toggle_question_mark);
        buttonBookT = (Button)findViewById(R.id.button_book_trip);

        toggleOneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleOneTime.setChecked(true);
                toggleOneTime.setPressed(true);
                if(toggleRecurring.isChecked()){
                    toggleRecurring.setChecked(false);
                }

                ComputeCost();
            }
        });

        toggleRecurring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRecurring.setChecked(true);
                toggleRecurring.setPressed(true);
                if(toggleOneTime.isChecked()){
                    toggleOneTime.setChecked(false);
                }
                ComputeCost();
            }
        });


    }

    public void ComputeCost() {
        int costType;
        String costAddon;
        if (toggleOneTime.isChecked()) {
            costType = 9;
            costAddon = ".00 single trip";

        } else {
            costType = 150;
            costAddon = ".00 per month";
        }
        String cost = String.valueOf(costType);

        int c = cost.length();
        cost = "$" + cost + costAddon;

        SpannableString ss1 = new SpannableString(cost);
        ss1.setSpan(new RelativeSizeSpan(1.5f),1,1+c,0);
        ss1.setSpan(new ForegroundColorSpan(Color.GREEN),1,1+c,0);
        ss1.setSpan(new RelativeSizeSpan(0.6f),1+c,4+c,0);
        textView.setText(ss1);

        buttonBookT.setEnabled(true);
        questionMark.setVisibility(View.VISIBLE);
    }

    public void SuccessfullyBooked(View view){
        Intent intent = new Intent(this, SuccessfullyBookedActivity.class);
        startActivity(intent);

    }

    public static RequestRideActivity getInstance(){
        return rractivity;
    }

    public void GoBack(View view){
        finish();
    }


}
