package com.mygdx.radar.android;

public class Config {

	public float	distance, distance1, distance2, distance3;
    public int      timeRed;
    public int      timeOrange;
    public float    distanceStation;

    private Config() {
        distanceStation = 0.05f;
        timeRed = 0;
    	timeOrange = 3;
        distance1 = .15f;
        distance2 = 0.5f;
        distance3 = 1;
        distance = distance1;

    }

    public static Config instance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder
    {
        /** Instance unique non préinitialisée */
        private final static Config instance = new Config();
    }
}
