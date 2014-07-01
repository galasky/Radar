package com.mygdx.radar.android;

public class Config {

	public float	distance, distance1, distance2, distance3;
    public int      timeRed;
    public int      timeOrange;

    private Config() {
        timeRed = 0;
    	timeOrange = 3;
        distance1 = .5f;
        distance2 = .7f;
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
