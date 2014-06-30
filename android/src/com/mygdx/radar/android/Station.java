package com.mygdx.radar.android;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Station {

	public Vector2			position;
	public ArrayList<Stop>	stops;
	public String			name;
	public CoordinateGPS	coord;
	public ModelInstance	instance;
	public float			distance;
	public float			distanceTemps;
    public Vector2          u;
	
	public Station() {
	   	instance = null;
    	position = new Vector2();
    	stops = new ArrayList<Stop>();
        u = new Vector2(1, 0);
	}

    public void update() {
        Vector2 cam = new Vector2(MyCamera.instance().pCam.position.z, MyCamera.instance().pCam.position.x);
        Vector2 v = cam.sub(position);

        double a = Math.acos((u.x * v.x + u.y * v.y) / (Math.sqrt(u.x * u.x + u.y * u.y) * Math.sqrt(v.x * v.x + v.y * v.y)));

        a = 180 * (a) / Math.PI;
        if (a > 5)
            a *= -1;
        if (a > 0.1 || a < -0.1) {
            instance.transform.rotate(new Vector3(0, 1, 0), (float) a);
            u = u.rotate((float) a);
        }
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
