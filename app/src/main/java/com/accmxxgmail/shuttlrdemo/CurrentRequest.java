package com.accmxxgmail.shuttlrdemo;

import com.google.android.gms.location.places.Place;

public class CurrentRequest {
    static int fragmentRequested = 0;
    static double lat = 0;
    static double lng = 0;
    Place mPlaceStart, mPlaceEnd;

    static public void initialize() {
        fragmentRequested = 0;
        lat = 0;
        lng = 0;
    }
    public void SetNewTripInfo(Place placeStart, Place placeEnd){
        mPlaceStart = placeStart;
        mPlaceEnd = placeEnd;
    }

}