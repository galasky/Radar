package com.mygdx.game.android;

import java.util.ArrayList;


public class World {
	public ArrayList<BubbleStop> 	listBubbleStop;
	public	LoadListStop			loadListStop;
	
	private	World() {
        listBubbleStop = null;
        loadListStop = new LoadListStop();
	}
	
	public static World instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static World instance = new World();
    }
}
