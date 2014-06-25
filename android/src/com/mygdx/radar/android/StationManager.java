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
import com.badlogic.gdx.math.Vector3;

public class StationManager {
	private boolean					_loadFinish;
	private You						_you;
	private List<Station>			listStation;
	private List<BubbleStop>		listTmp;
	public boolean					endDraw;
	
	private StationManager () {
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
	
	public void add(List<Stop> listStop) {
		Iterator<Stop> i = listStop.iterator();
		listStation = new ArrayList<Station>();
		while (i.hasNext())
		{
			Stop stop = i.next();
			Station station = new Station();
			
			station.stops.add(stop);
			station.name = stop.stop_name;
			station.coord = stop.coord;
			i.remove();
			Iterator<Stop> u = listStop.iterator();
			while (u.hasNext())
			{
				Stop stop2 = u.next();
				if (Territory.distanceAB(station.coord, stop2.coord) <= 0.05)
				{
					station.stops.add(stop2);
					u.remove();
					i = listStop.iterator();
				}
			}
			station.moyCoord();
			station.getListStopTimes();
			listStation.add(station);
			loadInstance(station);
			loadBubble(station);
			//Log.d("galasky", "galasky ADD STATION IN LISTSTATION");
		}
		_loadFinish = true;
	}
	
	public void loadInstance(Station station) {
        ModelBuilder modelBuilder = new ModelBuilder();

		Vector3 decal = new Vector3();

        Texture tstation = new Texture("texture/info_icon.png");
        Model model = modelBuilder.createBox(0.5f, 2f, .01f,
                new Material(TextureAttribute.createDiffuse(tstation)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        //model = Game3D.instance().assets.get("data/steve/steve.obj", Model.class);
    	decal.x = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(station.coord.latitude, _you.coordinate.longitude)) * 10 * (station.coord.latitude > _you.coordinate.latitude ? 1 : -1);
    	decal.z = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(_you.coordinate.latitude, station.coord.longitude)) * 10 *(station.coord.longitude > _you.coordinate.longitude ? 1 : -1);
        ModelInstance instance = new ModelInstance(model);
	    station.position.x = decal.z;
	    station.position.y = decal.x;
	    station.instance = instance;
	    station.instance.transform.setTranslation(decal.x, 1, decal.z);
	    //station.instance.transform.scale(0.1f, 0.1f, 0.1f);
	    Game3D.instance().instances.add(instance);
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
				World.instance().listBubbleStop.add(b);
			}
			listTmp.clear();
		}
		else
			listTmp.add(bubble);
	}
}
