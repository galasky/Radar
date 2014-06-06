package com.mygdx.game.android;

import android.location.Location;


public class LoadListStop extends Thread {
	private World		_world;
	private Territory	_territory;
	private	You			_you;
	public Location 	location;
	public boolean		loaded;
	private Config		_config;
	
	public LoadListStop() {
		loaded = false;
		_world = World.instance();
		_territory = Territory.instance();
		_you = You.instance();
		_config = Config.instance();
		location = null;
	}
	
	public void run() {
		_you.setPosition(location);
	}
	
}
