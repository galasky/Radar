package com.mygdx.radar.android;

import android.location.Location;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;


public class You {
	public ModelInstance 	modelInstance;
	public Vector3			position;
	private boolean 		_loaded;
	public CoordinateGPS	coordinate;
	private ModelBuilder 	modelBuilder;
	public CoordinateGPS	start;
	public ModelInstance	steve;
    private Pixmap          _pixmap;
    private Texture         _pixmapTexture;
    private int             _x  = 0;
    private int             _y  = 0;
    private float           _w;
    private float           _h;
    private int             _width;
    private int             _height;

    private	You() {
        _width = 2000;
        _height = 2000;
		modelBuilder = new ModelBuilder();
		position = new Vector3(0, 0, 0);
		_loaded = true;
		steve = null;
		coordinate = null;
		start = null;
	}
	
	public void setRotation(float angle) {
		if (steve != null)
		{
			steve.transform.setToRotation(new Vector3(0, 1, 0), (float) (-180 * angle / Math.PI));
			steve.transform.scale(0.2f, 0.2f, 0.2f);
		}
	}
	
	public void load() {
    	Model model;
        _pixmap = new Pixmap(_width, _height, Pixmap.Format.RGBA8888);
        _pixmap.setColor(BubbleDrawer.instance().blue);
        _pixmap.fillRectangle(0, 0, _width, _height);

        _pixmapTexture = new Texture(_pixmap, Pixmap.Format.RGB888, false);

        //Texture floor = new Texture("data/cadrillage.png");
      	model = modelBuilder.createBox(50f, .5f, 50f, 
      	new Material(TextureAttribute.createDiffuse(_pixmapTexture)), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
      	modelInstance = new ModelInstance(model);
      	modelInstance.transform.setToTranslation(0, -1f, 0);
        updateFloor();
       	_loaded = true;
	}

    class toto extends Thread {
        @Override
        public void run() {

        }
    }

    public void updateFloor() {
        _pixmap.setColor(Color.GREEN);
        for (int i = 0; i < 40; i++)
            _pixmap.drawPixel(_width / 2, _width / 2 + i);
        _pixmap.setColor(Color.RED);
        for (int i = 0; i < 40; i++)
            _pixmap.drawPixel(_height / 2 + i, _height / 2);



        for (int i = 10; i > 0; i--) {
            if (i % 2 == 0)
                _pixmap.setColor(BubbleDrawer.instance().green);
            else
                _pixmap.setColor(BubbleDrawer.instance().grey);
            _pixmap.fillCircle(_width / 2, _height / 2, 40 * i);
        }
        Texture _pixmapTexture = new Texture(_pixmap, Pixmap.Format.RGB888, false);

        TextureAttribute attr = TextureAttribute.createDiffuse(_pixmapTexture);
        You.instance().modelInstance.materials.get(0).set(attr);
    }

	public void setPosition(Location location) {
		if (!_loaded || modelInstance == null)
			return ;

		if (start == null)
		{
			start = new CoordinateGPS(location.getLatitude(), location.getLongitude());
			coordinate = new CoordinateGPS(start.latitude, start.longitude);
			Vector3 p = new Vector3();
			p.x = (float) Territory.distanceAB(start, new CoordinateGPS(location.getLatitude(), start.longitude)) * 10 * (location.getLatitude() > start.latitude ? 1 : -1);
			p.z = (float) Territory.distanceAB(start, new CoordinateGPS(start.latitude, location.getLongitude())) * 10 *(location.getLongitude() > start.longitude ? 1 : -1);
			p.y = 0;
			coordinate.latitude = location.getLatitude();
			coordinate.longitude = location.getLongitude();
			modelInstance.transform.setTranslation(p);
			position.x = p.x;
			position.y = p.y;
			position.z = p.z;
            StationManager.instance().add(Territory.instance().getListStopByDistance(Config.instance().distance, You.instance().coordinate));
		}
		else
		{
			Vector3 p = new Vector3();
			p.x = (float) Territory.distanceAB(start, new CoordinateGPS(location.getLatitude(), start.longitude)) * 10 * (location.getLatitude() > start.latitude ? 1 : -1);
			p.z = (float) Territory.distanceAB(start, new CoordinateGPS(start.latitude, location.getLongitude())) * 10 *(location.getLongitude() > start.longitude ? 1 : -1);
			p.y = 0;
			coordinate.latitude = location.getLatitude();
			coordinate.longitude = location.getLongitude();
            /*if (World.instance().listBubbleStop != null) {
                for (int i = 0; i < World.instance().listBubbleStop.size(); i++) {
                    BubbleStop b = World.instance().listBubbleStop.get(i);
                    b.station.refreshInstance();
                }
            }*/

			//position.x = p.x;
			//position.y = p.y;
			//position.z = p.z;
		}
	}
	
	public static You instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static You instance = new You();
    }
}
