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
    public float            distanceAffichage;
	public float			distance;
    private Vector2     instanceGoTo;
	public float			distanceTemps;
    private You             _you;
    public Vector2          u; // vecteur pour l'orientation du model vers la caméra
	
	public Station() {
        instanceGoTo = null;
	   	instance = null;
    	position = new Vector2();
    	stops = new ArrayList<Stop>();
        u = new Vector2(1, 0);
        _you = You.instance();

	}

    public void refreshInstance() {
        position.y = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(coord.latitude, _you.coordinate.longitude)) * 10 * (coord.latitude > _you.coordinate.latitude ? 1 : -1);
        position.x = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(_you.coordinate.latitude, coord.longitude)) * 10 *(coord.longitude > _you.coordinate.longitude ? 1 : -1);
        instance.transform.setToTranslation(new Vector3(position.y, 0.971f, position.x));
        instance.transform.scale(0.35f, 0.35f, 0.35f);
        /*instanceGoTo = new Vector2();
        instanceGoTo.y = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(coord.latitude, _you.coordinate.longitude)) * 10 * (coord.latitude > _you.coordinate.latitude ? 1 : -1);
        instanceGoTo.x = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(_you.coordinate.latitude, coord.longitude)) * 10 *(coord.longitude > _you.coordinate.longitude ? 1 : -1);
        Log.d("ok", "galasky Position  " + position.x + " " + position.y);
        Log.d("ok", "galasky goTo " + instanceGoTo.x + " " + instanceGoTo.y);*/

    }

    public void update() {
        if (instanceGoTo != null)
        {
            float x = (position.x - instanceGoTo.x) / 50;
            float y = (position.y - instanceGoTo.y) / 50;

            position.x += x;
            position.y += y;
            instance.transform.setToTranslation(new Vector3(position.y, 0.971f, position.x));
            instance.transform.scale(0.35f, 0.35f, 0.35f);
        }

        /*Vector2 cam = new Vector2(MyCamera.instance().pCam.position.z, MyCamera.instance().pCam.position.x);
        Vector2 v = cam.sub(position);

        double a = Math.acos((u.x * v.x + u.y * v.y) / (Math.sqrt(u.x * u.x + u.y * u.y) * Math.sqrt(v.x * v.x + v.y * v.y)));

        a = 180 * (a) / Math.PI;
        if (a > 5)
            a *= -1;
        if (a > 0.1 || a < -0.1) {
            instance.transform.rotate(new Vector3(0, 1, 0), (float) a);
            u = u.rotate((float) a);
        }*/
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
