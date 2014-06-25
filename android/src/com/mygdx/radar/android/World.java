package com.mygdx.radar.android;

import java.util.ArrayList;
import java.util.HashMap;

public class World {
	public  ArrayList<BubbleStop> 	listBubbleStop;
	public	LoadListStop			loadListStop;
    public  HashMap<String, Route>       routes;
	
	private	World() {
        listBubbleStop = null;
        loadListStop = new LoadListStop();
        routes = new HashMap<String, Route>();
	}
	
	public static World instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static World instance = new World();
    }
}
