package com.accmxxgmail.shuttlrdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.android.gms.location.places.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SessionManagement {
    SharedPreferences mPreferences;
    Editor mEditor;
    Context mContext;

    private static final String IS_LOGIN = "IsLoggedIn";

    public SessionManagement(Context context){
        this.mContext = context;
        mPreferences = mContext.getSharedPreferences("CHECK_LOGIN", 0);
        mEditor = mPreferences.edit();
    }

    public void createLoginSession(String email){
        // Storing login value as TRUE
        mEditor.putBoolean(IS_LOGIN, true);
        mEditor.putString("email", email);
        mEditor.commit();
    }

    public void InputSessionName(String name){
        mEditor.putString("name", name);
        mEditor.commit();
    }

    public void InputSessionAddress(String address){
        mEditor.putString("address", address);
        mEditor.commit();
    }

    public void InputSessionPhone(String phone){
        mEditor.putString("phone", phone);
        mEditor.commit();
    }

    public void InputSessionCompany(String company){
        mEditor.putString("company", company);
        mEditor.commit();
    }



    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(mContext, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }
    }

    public String getUserEmail(){
        return mPreferences.getString("email", null);
    }

    public String getUserName(){
        return mPreferences.getString("name", "");
    }
    public String getUserAddress(){
        return mPreferences.getString("address", "");
    }
    public String getUserPhone(){
        return mPreferences.getString("phone", "");
    }
    public String getCompany(){
        return mPreferences.getString("company", "");
    }

    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();

        Intent i = new Intent(mContext, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }

    public boolean isLoggedIn(){
        return mPreferences.getBoolean(IS_LOGIN, false);
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