package com.mygdx.radar.android;

public class CoordinateGPS {

    public CoordinateGPS() {
    	longitude = 0;
    	latitude = 0;
    }

    public CoordinateGPS(double la, double lo) {
        longitude = lo;
        latitude = la;
    }
    public double longitude;
    public double latitude;
}
