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
        nbStopSelect = 0;
    }
    
    public void update() {
    	if (_world.loadListStop.loaded == false && _world.loadListStop.location != null)
    	{
    		_world.loadListStop.loaded = true;
    		_world.loadListStop.start();
    	}
    	if (stationManager.getListStation() == null)
    		return ;
    }
}
