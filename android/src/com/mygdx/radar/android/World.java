package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class World {
	public  ArrayList<BubbleStop> 	    listBubbleStop;
    public  HashMap<String, Stop>       mapStop;
    public  HashMap<String, Station>    mapStation;
    public  HashMap<String, Route>      routes;
    public  Vector2 toto, tata;
	public  int                         statu;
	private	World() {
        statu = 0;
        toto = new Vector2();
        tata = new Vector2();
        listBubbleStop = null;
        //loadListStop = new LoadListStop();
        routes = new HashMap<String, Route>();
        mapStop = new HashMap<String, Stop>();
        mapStation = new HashMap<String, Station>();
	}
	
	public static World instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static World instance = new World();
    }
}
