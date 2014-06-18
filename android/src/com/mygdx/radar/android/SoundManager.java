package com.mygdx.radar.android;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	HashMap<String, Sound> map;
	private SoundManager() {
		map = new HashMap<String, Sound>();
	}
	
	public static SoundManager instance() {
        return SingletonHolder.instance;
    }
	
	public void load(String name) {
		Sound mp3Sound = Gdx.audio.newSound(Gdx.files.internal("sound/" + name));
		map.put(name, mp3Sound);
	}

	public Sound get(String name) {
		return map.get(name);
	}
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static SoundManager instance = new SoundManager();
    }
}
