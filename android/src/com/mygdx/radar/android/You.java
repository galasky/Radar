package com.mygdx.radar.android;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.IOException;

public class You {
	public ModelInstance 	modelInstance;
	public Vector3			position;
	public CoordinateGPS	coordinate;
	private ModelBuilder 	modelBuilder;
	public CoordinateGPS	start;
	public ModelInstance	steve;
    private Pixmap          _pixmap;
    private int             _width;
    private int             _height;

    private	You() {
        _width = 2000;
        _height = 2000;
		modelBuilder = new ModelBuilder();
		position = new Vector3(0, 0, 0);
		steve = null;
		coordinate = new CoordinateGPS();
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
        Texture pixmapTexture = new Texture(_pixmap, Pixmap.Format.RGB888, false);
        model = modelBuilder.createBox(50f, .5f, 50f,
                new Material(TextureAttribute.createDiffuse(pixmapTexture)), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        modelInstance = new ModelInstance(model);
        updateFloor();
        Game3D.instance().instances.add(modelInstance);
	}

    public void updateFloor() {
        modelInstance.materials.get(0).clear();
        _pixmap = new Pixmap(_width, _height, Pixmap.Format.RGBA8888);
        _pixmap.setColor(BubbleDrawer.instance().blue);
        _pixmap.fillRectangle(0, 0, _width, _height);

        for (int i = (int) (Config.instance().distance3 * 10) + 1; i > 0; i--) {
            if (i % 2 == 0)
                _pixmap.setColor(BubbleDrawer.instance().cercle1);
            else
                _pixmap.setColor(BubbleDrawer.instance().cercle2);
            _pixmap.fillCircle(_width / 2, _height / 2, 40 * i);
        }
        Texture _pixmapTexture = new Texture(_pixmap, Pixmap.Format.RGB888, false);

        TextureAttribute attr = TextureAttribute.createDiffuse(_pixmapTexture);
        modelInstance.materials.get(0).set(attr);
    }

    public class RequetAPI extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient httpclient = new DefaultHttpClient();
            try
            {
                HttpGet httpget = new HttpGet("http://172.16.42.10:5000/test");
                ResponseHandler responseHandler = new BasicResponseHandler();
                String responseBody = (String) httpclient.execute(httpget, responseHandler);
                Log.d("ok", "HTTP " + responseBody);
            }
            catch (ClientProtocolException e)
            {
                Log.d("ok", "HTTP 1" + e.toString());
                e.printStackTrace();
            }
            catch (IOException e)
            {
                Log.d("ok", "HTTP 2" + e.toString());
                e.printStackTrace();
            }
            finally
            {
                httpclient.getConnectionManager().shutdown();
            }
         return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

	public void setPosition(Location location) {
        coordinate.latitude = location.getLatitude();
        coordinate.longitude = location.getLongitude();
        Log.d("ok", "SETPOSITION");
        /*if (!_loaded || modelInstance == null)
			return ;*/

		if (start == null)
		{
            new RequetAPI().execute();
			start = new CoordinateGPS(location.getLatitude(), location.getLongitude());
            new LoadStation().start();
            Log.d("ok", "SETPOSITION OK");
		}
		else
		{
            if (World.instance().listBubbleStop != null) {
                for (int i = 0; i < World.instance().listBubbleStop.size(); i++) {
                    BubbleStop b = World.instance().listBubbleStop.get(i);
                    b.station.refreshInstance();
                }
            }
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
