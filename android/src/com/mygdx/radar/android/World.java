package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class World {
	public  ArrayList<BubbleStop> 	listBubbleStop;
	public	LoadListStop			loadListStop;
    public  HashMap<String, Route>       routes;
    public Vector2 toto;
	
	private	World() {
        toto = new Vector2();
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
        float start = Gdx.graphics.getHeight() - 185;


        while (i < listBubbleStop.size())
        {
            BubbleStop b = listBubbleStop.get(i);
            if (i == 0)
            {
                start = b.pAffichage.y;
            }
            if (!find && bubbleStop.station.distance < b.station.distance)
            {
                bubbleStop.setpAffichage(new Vector2(Gdx.graphics.getWidth() / 2 - 400,  start + ecart));
                listBubbleStop.add(i, bubbleStop);
                find = true;
            }

            if (find)
            {
                b.setpAffichage(new Vector2(Gdx.graphics.getWidth() / 2 - 400, start + ecart));
            }
            ecart += (-73 - (b.station.stops.size()) * 76) - 10;
            i++;

        }
        if (find)
            return ;
        bubbleStop.setpAffichage(new Vector2(Gdx.graphics.getWidth() / 2 - 400, start + ecart));
        listBubbleStop.add(bubbleStop);
    }
}
