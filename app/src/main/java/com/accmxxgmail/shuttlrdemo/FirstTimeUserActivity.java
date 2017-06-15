package com.accmxxgmail.shuttlrdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstTimeUserActivity extends AppCompatActivity{

    SessionManagement session;

    String url = "https://active-mountain-168417.firebaseio.com/users/";
    private DatabaseReference mRootReference;

    private EditText editTextName, editTextEmail, editTextPhone, editTextAddress;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_user);

        editTextName = (EditText) findViewById(R.id.EditTextName);
        editTextEmail = (EditText) findViewById(R.id.EditTextEmail);
        editTextPhone = (EditText) findViewById(R.id.EditTextPhone);
        editTextAddress = (EditText) findViewById(R.id.EditTextAddress);

        spinner = (Spinner) findViewById(R.id.SpinnerCompany);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.company_list,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        session = new SessionManagement(getApplicationContext());

        final String email = session.getUserEmail();

        editTextEmail.setHint(email);
        url = url + email;
        mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);

        Button saveButton = (Button) findViewById(R.id.button_save_info);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String nameText = editTextName.getText().toString();
                String phoneText = editTextPhone.getText().toString();
                String addressText = editTextAddress.getText().toString();

                if(!nameText.isEmpty()){
                    session.InputSessionName(nameText);
                    mRootReference.child("name").setValue(nameText);}

                if(!phoneText.isEmpty()){
                    session.InputSessionPhone(phoneText);
                    mRootReference.child("phone").setValue(phoneText);}

                if(!addressText.isEmpty()){
                    session.InputSessionAddress(addressText);
                    mRootReference.child("address").setValue(addressText);}

                String companyText= String.valueOf(spinner.getSelectedItem());
                if(!companyText.equals("Select")){
                    mRootReference.child("company").setValue(companyText);}

                session.setFirebaseSession(email);
                finish();
            }
        });

        editTextName.setHint(session.getUserName());
        editTextPhone.setHint(session.getUserPhone());
        editTextAddress.setHint(session.getUserAddress());
        spinner.setSelection(getIndex(spinner,session.getCompany()));
    }

    private int getIndex(Spinner spinner, String s) {
        int index = 0;
        for(int i=0;i<spinner.getCount();i++){
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(s)){
                index=i;
                break;
            }
        }
        return index;
    }

    public static String EncodeEmail(String string){
        return string.replace(".",",");
    }
}
