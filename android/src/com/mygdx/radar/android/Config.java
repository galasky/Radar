package com.mygdx.radar.android;

public class Config {

	public float	distance;
    public int      timeRed;
    public int      timeOrange;

    private Config() {
        timeRed = 0;
    	timeOrange = 3;
        distance = 4f;
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
