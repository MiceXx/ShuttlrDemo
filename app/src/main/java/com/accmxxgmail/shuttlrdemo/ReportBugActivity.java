package com.accmxxgmail.shuttlrdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ReportBugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_bug);

        final EditText editTextMessage = (EditText)findViewById(R.id.editTextMSG);

        Button button = (Button)findViewById(R.id.buttonSendBugReport);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String MESSAGE = editTextMessage.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"accmxx@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "ShuttlrDemo Bug Report");
                intent.putExtra(Intent.EXTRA_TEXT, MESSAGE);

                intent.setType("message/rfc822");

                startActivity(Intent.createChooser(intent, "Select Email Sending App :"));

            }
        });

    }

}