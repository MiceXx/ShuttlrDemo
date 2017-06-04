package com.accmxxgmail.shuttlrdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirstTimeUserActivity extends AppCompatActivity implements ValueEventListener{

    private DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mNameReference = mRootReference.child("profilenamekey");
    private DatabaseReference mEmailReference = mRootReference.child("profileemailkey");
    private DatabaseReference mPhoneReference = mRootReference.child("profilephonekey");
    private DatabaseReference mAddressReference = mRootReference.child("profileaddresskey");
    private DatabaseReference mCompanyReference = mRootReference.child("profilecompanykey");

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

        Button saveButton = (Button) findViewById(R.id.button_save_info);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String nameText = editTextName.getText().toString();
                String emailText = editTextEmail.getText().toString();
                String phoneText = editTextPhone.getText().toString();
                String addressText = editTextAddress.getText().toString();

                if(!nameText.isEmpty()){
                mNameReference.setValue(nameText);}

                if(!emailText.isEmpty()){
                    mEmailReference.setValue(emailText);}

                if(!phoneText.isEmpty()){
                    mPhoneReference.setValue(phoneText);}

                if(!addressText.isEmpty()){
                    mAddressReference.setValue(addressText);}

                String companyText= String.valueOf(spinner.getSelectedItem());
                if(!companyText.equals("Select")){
                    mCompanyReference.setValue(companyText);}
                finish();
            }
        });
    }

    protected void onStart(){
        super.onStart();
        mNameReference.addListenerForSingleValueEvent(this);
        mEmailReference.addListenerForSingleValueEvent(this);
        mPhoneReference.addListenerForSingleValueEvent(this);
        mAddressReference.addListenerForSingleValueEvent(this);
        mCompanyReference.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        editTextName = (EditText) findViewById(R.id.EditTextName);
        editTextEmail = (EditText) findViewById(R.id.EditTextEmail);
        editTextPhone = (EditText) findViewById(R.id.EditTextPhone);
        editTextAddress = (EditText) findViewById(R.id.EditTextAddress);
        spinner = (Spinner) findViewById(R.id.SpinnerCompany);

        if(dataSnapshot.getValue(String.class)!=null){
            String key = dataSnapshot.getKey();
            if(key.equals("profilenamekey")){
                String profileTextRetrieved = dataSnapshot.getValue(String.class);
                editTextName.setHint(profileTextRetrieved);
            }
            else if(key.equals("profileemailkey")){
                String emailTextRetrieved = dataSnapshot.getValue(String.class);
                editTextEmail.setHint(emailTextRetrieved);
            }
            else if(key.equals("profilephonekey")){
                String phoneTextRetrieved = dataSnapshot.getValue(String.class);
                editTextPhone.setHint(phoneTextRetrieved);
            }
            else if(key.equals("profileaddresskey")){
                String addressTextRetrieved = dataSnapshot.getValue(String.class);
                editTextAddress.setHint(addressTextRetrieved);
            }
            else if(key.equals("profilecompanykey")){
                String addressTextRetrieved = dataSnapshot.getValue(String.class);
                spinner.setSelection(getIndex(spinner,addressTextRetrieved));
            }
        }
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


    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
