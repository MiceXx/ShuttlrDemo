package com.accmxxgmail.shuttlrdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SessionManagement {
    SharedPreferences pref;
    Editor editor;
    Context _context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences("CHECK_LOGIN", 0);
        editor = pref.edit();
    }

    public void createLoginSession(String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString("email", email);
        editor.commit();
    }

    public void InputSessionName(String name){
        editor.putString("name", name);
        editor.commit();
    }

    public void InputSessionAddress(String address){
        editor.putString("address", address);
        editor.commit();
    }

    public void InputSessionPhone(String phone){
        editor.putString("phone", phone);
        editor.commit();
    }

    public void InputSessionCompany(String company){
        editor.putString("company", company);
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public String getUserEmail(){
        return pref.getString("email", null);
    }

    public String getUserName(){
        return pref.getString("name", "");
    }
    public String getUserAddress(){
        return pref.getString("address", "");
    }
    public String getUserPhone(){
        return pref.getString("phone", "");
    }
    public String getCompany(){
        return pref.getString("company", "");
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setFirebaseSession(String str) {
        String url = "https://active-mountain-168417.firebaseio.com/users/" + str;
        DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
        DatabaseReference mNameReference = mRootReference.child("name");
        DatabaseReference mPhoneReference = mRootReference.child("phone");
        DatabaseReference mAddressReference = mRootReference.child("address");
        DatabaseReference mCompanyReference = mRootReference.child("company");
        mNameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String profileTextRetrieved = dataSnapshot.getValue(String.class);
                InputSessionName(profileTextRetrieved);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mPhoneReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String profileTextRetrieved = dataSnapshot.getValue(String.class);
                InputSessionPhone(profileTextRetrieved);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAddressReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String profileTextRetrieved = dataSnapshot.getValue(String.class);
                InputSessionAddress(profileTextRetrieved);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mCompanyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String profileTextRetrieved = dataSnapshot.getValue(String.class);
                InputSessionCompany(profileTextRetrieved);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}