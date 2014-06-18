package com.mygdx.radar.android;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;

public class Station {

	public Vector2			position;
	public ArrayList<Stop>	stops;
	public String			name;
	public CoordinateGPS	coord;
	public ModelInstance	instance;
	public float			distance;
	public float			distanceTemps;
	
	public Station() {
	   	instance = null;
    	position = new Vector2();
    	stops = new ArrayList<Stop>();
	}
	
	public void	getListStopTimes() {
		Iterator<Stop> i = stops.iterator();
		while (i.hasNext())
		{
			Stop stop = i.next();
			stop.getListStopTimes();
		}
	}
	
	
	public boolean isFasterTo(Station other) {
		return  (distanceTemps < other.distanceTemps);
	}
	
	public void calcDistance() {
		distance = (float) Territory.distanceAB(You.instance().coordinate, coord);
		distanceTemps = distance / 5;
	}
	
	public void moyCoord() {
		coord = new CoordinateGPS();
		Iterator<Stop> i = stops.iterator();
		while (i.hasNext())
		{
			Stop stop = i.next();
			coord.latitude += stop.coord.latitude;
			coord.longitude += stop.coord.longitude;
		}
		coord.latitude /= stops.size();
		coord.longitude /= stops.size();
		calcDistance();
	}
	
}
