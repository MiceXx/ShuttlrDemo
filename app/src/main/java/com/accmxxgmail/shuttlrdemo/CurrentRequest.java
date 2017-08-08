package com.accmxxgmail.shuttlrdemo;

import com.google.android.gms.location.places.Place;

public class CurrentRequest {

    static int mFragment;
    static double mLat, mLong;
    static Place mPlaceStart, mPlaceEnd;

    static public void initialize() {
        mFragment = 0;
        mLat = 0;
        mLong = 0;
    }

}