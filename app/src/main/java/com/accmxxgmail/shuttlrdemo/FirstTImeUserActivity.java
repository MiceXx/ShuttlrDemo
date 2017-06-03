package com.accmxxgmail.shuttlrdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstTImeUserActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference mChildReference = mRootReference.child("message");

    private EditText editTextName, editTextEmail, editTextPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_user);

        editTextName = (EditText) findViewById(R.id.EditTextName);
        editTextEmail = (EditText) findViewById(R.id.EditTextEmail);
        editTextPhone = (EditText) findViewById(R.id.EditTextPhone);

    }

    @Override
    protected void onStart(){
        super.onStart();
        mChildReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameString = dataSnapshot.getValue(String.class);
                editTextName.setText(nameString);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
