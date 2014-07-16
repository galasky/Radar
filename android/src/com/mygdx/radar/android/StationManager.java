package com.mygdx.radar.android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class StationManager {
	private boolean					_loadFinish;
	private You						_you;
	private List<Station>			listStation;
	private List<BubbleStop>		listTmp;
    public  ArrayList<BubbleStop>   listLoaded;
	public boolean					endDraw;

	private StationManager () {
        listLoaded = new ArrayList<BubbleStop>();
		listTmp = new ArrayList<BubbleStop>();
		listStation = null;
		_you = You.instance();
		_loadFinish = false;
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
	
	public boolean loadFinish() {
		return _loadFinish;
	}

    public boolean stopExist(Stop stop) {
        return (World.instance().mapStop.get(stop.stop_id) != null);
    }

    public void addListStation(List<Station> listStation) {
        Iterator<Station> i = listStation.iterator();
        listStation = new ArrayList<Station>();
        while (i.hasNext())
        {
            Station station = i.next();
            station.calcDistance();
            listStation.add(station);
            loadInstance(station);
            loadBubble(station);
        }
    }

	public void add(List<Stop> listStop) {
		Iterator<Stop> i = listStop.iterator();
		listStation = new ArrayList<Station>();
        Log.d("ok", "galasky " + listStop.size());
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
                //station.distanceAffichage = distance;
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
                //showStation(station);
                loadInstance(station);
                loadBubble(station);
                LoadStation load = new LoadStation();
                load.filtre = true;
                load.start();
            }
		}
		_loadFinish = true;
        //filtreDistance();
	}

    public void filtreDistance() {
        if (World.instance().listBubbleStop == null)
            return ;
        for (int i = 0; i < listLoaded.size(); i++)
        {
            BubbleStop bubbleStop = listLoaded.get(i);
            if (bubbleStop.station.distance <= Config.instance().distance)
            {
                addBubble(bubbleStop);
                listLoaded.remove(i);
                i--;
                Game3D.instance().instances.add(bubbleStop.station.instance);
            }
        }

        for (int i = 0; i < World.instance().listBubbleStop.size(); i++)
        {
            BubbleStop bubbleStop = World.instance().listBubbleStop.get(i);
            Log.d("ioj", "STATION FILTRE NAME " + bubbleStop.station.name + " DISTANCE "+ bubbleStop.station.distance);
            if (bubbleStop.station.distance > Config.instance().distance) {
                listLoaded.add(bubbleStop);
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

    public void initPoseBubble() {
    }

    private void showStation(Station station)
    {
        Log.d("ok", "station name : " + station.name);
        Log.d("ok", "station position : latitude : " + station.coord.latitude + " longitude : " + station.coord.longitude);
        Log.d("ok", "station ListStop :");
        for (int i = 0; i < station.stops.size(); i++)
        {
            Stop stop = station.stops.get(i);
            Log.d("ok", "stop name : " + stop.destination);
        }
        Log.d("ok", "station");
    }

	public void loadInstance(Station station) {
  //      ModelBuilder modelBuilder = new ModelBuilder();

		Vector3 decal = new Vector3();

//        Texture tstation = new Texture("texture/info_icon.png");
        //Model model = modelBuilder.createBox(0.5f, 2f, .01f, new Material(TextureAttribute.createDiffuse(tstation)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        //model = Game3D.instance().assets.get("data/steve/steve.obj", Model.class);
    	decal.x = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(station.coord.latitude, _you.coordinate.longitude)) * 10 * (station.coord.latitude > _you.coordinate.latitude ? 1 : -1);
    	decal.z = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(_you.coordinate.latitude, station.coord.longitude)) * 10 *(station.coord.longitude > _you.coordinate.longitude ? 1 : -1);
        ModelInstance instance = new ModelInstance(Game3D.instance().modelStation);
	    station.position.x = decal.z;
	    station.position.y = decal.x;
       // Log.d("ok", "galasky distance reel = " + (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(station.coord.latitude, station.coord.longitude)) + " distance gdx = " + Math.sqrt(decal.x * decal.x + decal.z * decal.z));
	    station.instance = instance;
	    station.instance.transform.setTranslation(decal.x, 0.971f, decal.z);
	    station.instance.transform.scale(0.35f, 0.35f, 0.35f);
        //station.instance.transform.rotate(new Vector3(0, 1, 0), -43.39995f);
	    //Game3D.instance().instances.add(instance);
	}
	
	public void loadBubble(Station station) {
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
/*                if (b.station.distance <= Config.instance().distance)
    				addBubble(b);
                else*/
                    listLoaded.add(b);
			}
			listTmp.clear();
		}
		else
			listTmp.add(bubble);
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
