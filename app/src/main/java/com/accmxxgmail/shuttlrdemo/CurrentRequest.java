package com.accmxxgmail.shuttlrdemo;

public class CurrentRequest {
    static int fragmentRequested = 0;
    static double lat = 0;
    static double lng = 0;

    static public void initialize() {
        fragmentRequested = 0;
        lat = 0;
        lng = 0;
    }

}