package com.mygdx.radar.android;

public class Config {

	public float	distance, distance1, distance2, distance3;
    public int      timeRed;
    public int      timeOrange;
    public float    distanceStation;
    public String   dateBuild, versionName;
    public String   urlAPI;
    public boolean  onLine;

    private Config() {
        onLine = true;
        versionName = " 0.0.9 'beta'";
        urlAPI = "http://212.129.40.83:5000/test";
        //urlAPI = "http://172.16.42.10:5000/test";
        distanceStation = 0.05f;
        timeRed = 0;
    	timeOrange = 3;
        distance1 = 0.25f;
        distance2 = .5f;
        distance3 = 1f;
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
