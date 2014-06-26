package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

    public void addBubble(BubbleStop bubbleStop) {
        //Iterator<BubbleStop> it = listBubbleStop.iterator();
        int i = 0;
        boolean find = false;
        float ecart = 0;

        while (i < listBubbleStop.size())
        {
            BubbleStop b = listBubbleStop.get(i);
            if (find == false && bubbleStop.station.distance < b.station.distance)
            {

                bubbleStop.setpAffichage(new Vector2(98, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() - 1735) + ecart));
                listBubbleStop.add(i, bubbleStop);
                find = true;
            }

            if (find)
            {
                b.setpAffichage(new Vector2(98, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() - 1735) + ecart));
            }
            ecart += (-73 - (b.station.stops.size()) * 76) - 10;
            i++;

        }
        if (find)
            return ;
        bubbleStop.setpAffichage(new Vector2(98, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() - 1735) + ecart));
        listBubbleStop.add(bubbleStop);
    }
}
