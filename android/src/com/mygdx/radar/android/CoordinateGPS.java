package com.mygdx.radar.android;


/**
 * Created by Administrateur on 10/04/14.
 */
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
