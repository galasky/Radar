package com.mygdx.radar.android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class StationManager {
	private You						_you;
	private List<Station>			listStation;
	private List<BubbleStop>		listTmp;
    public  ArrayList<BubbleStop>   listLoaded, listCache;
	public boolean					endDraw;
    public BubbleStop               bubbleSelected;
    public boolean                  hide;
    private Vector2                 positionFirst;

	private StationManager () {
        hide = false;
        bubbleSelected = null;
        listLoaded = new ArrayList<BubbleStop>();
        listCache = new ArrayList<BubbleStop>();
		listTmp = new ArrayList<BubbleStop>();
		listStation = null;
		_you = You.instance();
	}
	
	public static StationManager instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static StationManager instance = new StationManager();
    }
	
	public List<Station> getListStation() {
		return listStation;
	}
	
    public boolean stopExist(Stop stop) {
        return (World.instance().mapStop.get(stop.stop_id) != null);
    }

    public void   addListStop(List<Stop> listStop) {
        Iterator<Stop> i = listStop.iterator();
        listStation = new ArrayList<Station>();
        while (i.hasNext())
        {
            Stop stop = i.next();
            if (!stopExist(stop))
            {

                World.instance().mapStop.put(stop.stop_id, stop);
                Station station = new Station();
                station.stops.add(stop);
                station.name = stop.stop_name;
                station.coord = stop.coord;
                i.remove();
                Iterator<Stop> u = listStop.iterator();
                while (u.hasNext())
                {
                    Stop stop2 = u.next();
                    if (Territory.distanceAB(station.coord, stop2.coord) <= Config.instance().distanceStation)
                    {
                        World.instance().mapStop.put(stop2.stop_id, stop2);
                        station.stops.add(stop2);
                        u.remove();
                        i = listStop.iterator();
                    }
                }
                station.moyCoord();
                station.getListStopTimes();
                listStation.add(station);
                if (listStation.size() == 1)
                    DetecteurGeste.instance().run();
                loadInstance(station);
                loadBubble(station);
                if (bubbleSelected == null) {
                    LoadStation load = new LoadStation();
                    load.filtre = true;
                    load.start();
                }
            }
        }
    }

    public void filtreDistance() {
        if (World.instance().listBubbleStop == null)
            return ;
        for (int i = 0; i < listLoaded.size(); i++)
        {
            BubbleStop bubbleStop;
            if (listLoaded.size() != 0)
                bubbleStop = listLoaded.get(i);
            else
                return;
            if (bubbleStop.station.distance <= Config.instance().distance)
            {
                addBubble(bubbleStop);
                if (listLoaded.size() != 0)
                    listLoaded.remove(i);
                else
                    return;
                i--;
                Game3D.instance().instances.add(bubbleStop.station.instance);
            }
        }

        for (int i = 0; i < World.instance().listBubbleStop.size(); i++)
        {
            BubbleStop bubbleStop = World.instance().listBubbleStop.get(i);
            if (bubbleStop.station.distance > Config.instance().distance) {
                if (bubbleSelected == null)
                    listLoaded.add(bubbleStop);
                if (World.instance().listBubbleStop.size() <= i)
                    return ;
                World.instance().listBubbleStop.remove(i);
                i--;
                for (int u = 0; u < Game3D.instance().instances.size; u++)
                {
                    ModelInstance instance = Game3D.instance().instances.get(u);
                    if (instance == bubbleStop.station.instance)
                    {
                        Game3D.instance().instances.removeIndex(u);
                    }
                }
            }
           // Log.d("ok", "STATION FILTRE SIZE " + World.instance().listBubbleStop.size() + " i = " + i);
        }
    }

	public void loadInstance(Station station) {
		Vector3 decal = new Vector3();
    	decal.x = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(station.coord.latitude, _you.coordinate.longitude)) * 10 * (station.coord.latitude > _you.coordinate.latitude ? 1 : -1);
    	decal.z = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(_you.coordinate.latitude, station.coord.longitude)) * 10 *(station.coord.longitude > _you.coordinate.longitude ? 1 : -1);
        ModelInstance instance = new ModelInstance(Game3D.instance().modelStation);
	    station.position.x = decal.z;
	    station.position.y = decal.x;
	    station.instance = instance;
	    station.instance.transform.setTranslation(decal.x, 0.971f, decal.z);
	    station.instance.transform.scale(0.35f, 0.35f, 0.35f);
	}
	
	public void loadBubble(Station station) {
        Log.d("ok", "name " + station.name + " longitude " + station.coord.longitude + " latitude " + station.coord.latitude);
		BubbleStop bubble = new BubbleStop(station);
		bubble.position.x = GUIController.random(40, Gdx.graphics.getWidth() - 40 * station.name.length() / 2);
		bubble.position.y = GUIController.random(Gdx.graphics.getHeight(), Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 8);
		if (World.instance().listBubbleStop == null)
			World.instance().listBubbleStop = new ArrayList<BubbleStop>();
		if (endDraw)
		{
			listTmp.add(bubble);

			Iterator<BubbleStop> it = listTmp.iterator();
			while (it.hasNext())
			{
				BubbleStop b = it.next();
                if (bubbleSelected == null)
                    listLoaded.add(b);
                else
                    listCache.add(b);
			}
			listTmp.clear();
		}
		else
			listTmp.add(bubble);
	}

    public void selectBubble(BubbleStop bubbleStop) {
        if (bubbleSelected != null)
            return ;
        bubbleSelected = bubbleStop;
        positionFirst = World.instance().listBubbleStop.get(0).position;
        bubbleStop.pAffichage = new Vector2(Gdx.graphics.getWidth() / 2 - 400, Gdx.graphics.getHeight() - 100);
        for (int u = 0; u < Game3D.instance().instances.size; u++)
        {
            ModelInstance instance = Game3D.instance().instances.get(u);
            if (instance != bubbleStop.station.instance && instance != You.instance().modelInstance)
            {
                Game3D.instance().instances.removeIndex(u);
                u--;
            }
        }
        for (int i = 0; i < World.instance().listBubbleStop.size(); i++) {
            BubbleStop b = World.instance().listBubbleStop.get(i);
            if (bubbleStop.station != b.station) {
                listCache.add(b);
                World.instance().listBubbleStop.remove(i);
                i--;
            }
        }
    }

    public void initUnselect() {
        if (bubbleSelected != null)
        {
            bubbleSelected.pAffichage = new Vector2(positionFirst);
            if (hide)
                bubbleSelected.pAffichage.x = -400;
        }

    }

    public void unSelectBubble() {
        if (bubbleSelected == null)
            return ;
        for (int u = 0; u < listCache.size(); u++)
        {
            BubbleStop bubbleStop = listCache.get(u);
            Game3D.instance().instances.add(bubbleStop.station.instance);
            addBubble(bubbleStop);
        }
        listCache.clear();
        bubbleSelected = null;
    }

    private void addBubble(BubbleStop bubbleStop) {
        //Iterator<BubbleStop> it = listBubbleStop.iterator();
        int i = 0;
        boolean find = false;
        float ecart = 0;
        float start = Gdx.graphics.getHeight() - 185;
        ArrayList<BubbleStop> 	listBubbleStop = World.instance().listBubbleStop;

        while (i < listBubbleStop.size() && !find)
        {
            BubbleStop b = listBubbleStop.get(i);
            if (i == 0)
            {
                start = b.pAffichage.y;
            }
            if (!find && bubbleStop.station.distance < b.station.distance)
            {
                bubbleStop.setpAffichage(new Vector2(Gdx.graphics.getWidth() / 2 - 400,  start + ecart));
                if (hide)
                    bubbleStop.hide();

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
        if (hide)
            bubbleStop.hide();
        listBubbleStop.add(bubbleStop);
    }
}
