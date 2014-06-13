package com.mygdx.game.android;

//import android.location.Location;
//import android.util.Log;



/*public class MyGdxGame implements ApplicationListener {
	//private OrthographicCamera camera;
	
	private Territory territory;
	private List<Stop> listStop;
	private SpriteBatch batch;
	private Texture texture;
	private CoordinateGPS me;
	private CoordinateGPS start;;
	private float w;
	private float h;
	private int F;
	private boolean ok;
	private DrawStop drawStop;
	
	@Override
	public void create() {

		DetecteurGeste monDetecteurGeste = new DetecteurGeste();
		Gdx.input.setInputProcessor(new GestureDetector (monDetecteurGeste));
		
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		F = 100000;
		me = new CoordinateGPS(48.09275716032735, -1.64794921875);
		start = new CoordinateGPS();
		territory = Territory.instance();
		listStop = territory.getListStopByDistance(100, me);
		drawStop = DrawStop.instance();
		drawStop.setCenter(w / 2,  h / 2);
		drawStop.setZoom(100000);
		double gps, km;
		
		km = territory.distanceAB(me, listStop.get(0).coord);
		Log.d("galaksy", "galasky distance km = " + km);
		double X, Y, x;
		X = me.latitude - listStop.get(0).coord.latitude;
		Y = me.longitude - listStop.get(0).coord.longitude;
		gps = Math.sqrt(X * X + Y * Y);
		x = gps / km;
		//camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		ok = false;
	}

	public void setPosition(Location location) {
		if (ok == false)
		{
			start.latitude = location.getLatitude();
			start.longitude = location.getLongitude();
			ok = true;
		}
		else
		{
			me.latitude += location.getLatitude()- start.latitude;
			me.longitude += location.getLongitude() - start.longitude;
		}
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
		drawStop.dispose();
	}

	@Override
	public void render() {
		drawStop.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
        //batch.draw(texture, 0, 0);
        drawStop.draw(batch, listStop, me);
        batch.end();
        
        // Log.d("galasky", "galasky " + Gdx.input.getAccelerometerX());
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	
}
*/