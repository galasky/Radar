package com.mygdx.radar.android;

import java.util.Iterator;

import android.util.Log;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Plate {
	private	World					_world;
    private MyCamera				_cam;
    public int						nbStopSelect;
    private Territory 				territory;
    private You						_you;
    public Array<ModelInstance>		Stopinstances;
    private Environment				_environment;
    private	StationManager			stationManager;
    //private	LoadListStop			_loadListStop;
    
    public Plate (Environment environment)
    {
    	stationManager = StationManager.instance();
    	_world = World.instance();
    	_cam = MyCamera.instance();
    	_environment = environment;
    	_you = You.instance();
    	//_loadListStop = new LoadListStop();
    	territory = Territory.instance();

        Stopinstances = new Array<ModelInstance>();
    	_you.load();
    	Game3D.instance().instances.add(_you.modelInstance);
        nbStopSelect = 0;
    }
    
    public void update() {
    	//_you.setRotation(_cam.getAngle());
    	if (_world.loadListStop.loaded == false && _world.loadListStop.location != null)
    	{
    		_world.loadListStop.loaded = true;
    		_world.loadListStop.start();
    	}
    	if (stationManager.getListStation() == null)
    		return ;
    }
    
    public void setListStop() {
    	//_loadListStop.start();
    	stationManager.add(territory.getListStopByDistance(Config.instance().distance, _you.coordinate));
    	//loadStopInstances();
    	Log.d("galasky", "galasky setListStop");
    }
 
    private void loadStopInstances() 
    {
     	if (stationManager.getListStation() != null)
    	{
    		Vector3 decal = new Vector3();
        	Station station = null;
        	Iterator<Station> i = stationManager.getListStation().iterator();
        	Model model;
            ModelInstance instance;
    		
   		 	model = Game3D.instance().modelBuilder.createBox(.2f, .2f, .2f, 
   		 	new Material(ColorAttribute.createDiffuse(Color.RED)),
   		 	Usage.Position | Usage.Normal);
   		 
        	while(i.hasNext())
        	{
        		station = (Station) i.next();
        		
        		decal.x = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(station.coord.latitude, _you.coordinate.longitude)) * 10 * (station.coord.latitude > _you.coordinate.latitude ? 1 : -1);
        		decal.z = (float) Territory.distanceAB(_you.coordinate, new CoordinateGPS(_you.coordinate.latitude, station.coord.longitude)) * 10 *(station.coord.longitude > _you.coordinate.longitude ? 1 : -1);

             	instance = new ModelInstance(model);
             	        
    	        instance.transform.setToTranslation(decal.x, .5f, decal.z);
    	        station.position.x = decal.z;
    	        station.position.y = decal.x;
    	        station.instance = instance;
    	        Game3D.instance().instances.add(instance);
        	}   	
    	}
    }
}
