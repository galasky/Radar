package com.mygdx.game.android;

import com.badlogic.gdx.assets.AssetManager;

public class InstanceManager {
	 //public Array<ModelInstance>	instances;
	public AssetManager				assets;
	 //public ModelBuilder 			modelBuilder;
	 public boolean					loading;

	 private	InstanceManager() {
		 assets = new AssetManager();
		 loading = false;
		 //modelBuilder = new ModelBuilder();
	 }
	 
	 private static InstanceManager instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        private final static InstanceManager instance = new InstanceManager();
    }
}
