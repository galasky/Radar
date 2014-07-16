package com.mygdx.radar.android;


import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;

public class Game3D implements ApplicationListener {
	private MyCamera				_cam;
	//private SkyBox				_skyBox;
	public ModelBuilder 			modelBuilder;
    private Plate					_plate;
    private float                   toto, tata;
    private	GUIController			_guiController;
    public CameraInputController	camController;
    private Texture                 tSky;
    private Sprite                  sSky;
    private DirectionalLight		_light;
    public AssetManager				assets;
    public ModelBatch 				modelBatch;
    public Environment 				environment;
    public boolean 					loading;
    public OrthographicCamera camera;
    public Array<ModelInstance>		instances;
    public Array<ModelInstance>		perso;
    public ModelInstance            modelInstance;
    public ModelInstance            instanceSky;
    private BubbleDrawer    bd;
    public Model                    model, modelStation, modelSky;
    private long 					diff, start;
    
    private Game3D() {
        tata = 1;
        toto = 1;
    	start = System.currentTimeMillis();
    	loading = false;
        modelStation = null;
        model = null;
    }
    
	public static Game3D instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        private final static Game3D instance = new Game3D();
    }
	
	public void loadSound() {
		SoundManager soundManager = SoundManager.instance();
		soundManager.load("pop.mp3");
	}
	
	public void loadModel() {



			assets.load("data/steve/steve.obj", Model.class);
		//assets.load("data/steve/steve.obj", Model.class);
		//assets.load("data/zombie/zombie.obj", Model.class);
		loading = true;
	}
	
    private void doneLoading() {

        modelBatch = new ModelBatch();

        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

        model = modelLoader.loadModel(Gdx.files.getFileHandle("data/you/newcoq.g3db", Files.FileType.Internal));
        modelStation = modelLoader.loadModel(Gdx.files.getFileHandle("data/station/pannel_v2.g3db", Files.FileType.Internal));
        modelInstance = new ModelInstance(model);
        modelInstance.transform.setToTranslation(0, .5f, 0);
        modelInstance.transform.scale(0.3f, 0.3f, 0.3f);
        perso.add(modelInstance);

        loading = false;
    }
	
    @Override
    public void create () {
        tSky = new Texture(Gdx.files.internal("texture/sky.png"));
        sSky = new Sprite(tSky);
        toto = 4;
        sSky.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2.07f);
        sSky.setPosition(0, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2.07f);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        bd = BubbleDrawer.instance();
    	instances = new Array<ModelInstance>();
    	perso = new Array<ModelInstance>();
    	assets = new AssetManager();
    	loadSound();
    	loadModel();
        _cam = MyCamera.instance();
    	modelBuilder = new ModelBuilder();
    	modelBatch = new ModelBatch();
    	_light = new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, -1f, 0f);
    	environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(_light);
        //_skyBox = new SkyBox(environment);
        _plate = new Plate(environment);
        BubbleDrawer.instance();
        _guiController = new GUIController();
        DetecteurGeste monDetecteurGeste = new DetecteurGeste();
		Gdx.input.setInputProcessor(new GestureDetector (monDetecteurGeste));
        modelBuilder = new ModelBuilder();
    }
    
    public void longPress()
    {
    	_guiController.invert();
    }

    public void	touchScreen(float x, float y, float deltaX, float deltaY) {
       World.instance().tata.y -= deltaY;
       toto -= deltaX / 1000;
      // sSky.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / toto);
       //sSky.setPosition(0, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / toto);
        //Log.d("ok", "toto " + toto);
       /* tata += deltaY / 10;
        Log.d("ok", "galasky toto = " + toto +  " tata = " + tata);

        modelInstance.transform.setToTranslation(0, tata, 0);
        modelInstance.transform.scale(0.3f, 0.3f, 0.3f);*/
        /*for (int i = 0; i < World.instance().listBubbleStop.size(); i++)
        {
            Station station = World.instance().listBubbleStop.get(i).station;
            station.instance.transform.setToTranslation(station.position.y, toto, station.position.x);
            station.instance.transform.scale(0.35f, 0.35f, 0.35f);
            station.instance.transform.rotate(new Vector3(0, 1, 0), tata);
        }*/
        //instanceSky.transform.scale(toto, toto, toto);
    	_guiController.touch(x, y, deltaX, deltaY);
    }
    
    public void tap(float x, float y)
    {
    	_guiController.tap(x, y);
    }
   
    public void sleep(int fps) {
        if(fps>0){
          diff = System.currentTimeMillis() - start;
          long targetDelay = 1000/fps;
          if (diff < targetDelay) {
            try{
                Thread.sleep(targetDelay - diff);
              } catch (InterruptedException e) {}
            }   
          start = System.currentTimeMillis();
        }
    }
    
    @Override
    public void render () {
    	if (loading && assets.update())
            doneLoading();
        _cam.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.graphics.getGL20().glClearColor(147 / 255f, 199 / 255f, 255 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        bd._spriteBatch.begin();
        sSky.draw(bd._spriteBatch);
       /* bd.shapeDebugger.setProjectionMatrix(camera.combined);
        bd.shapeDebugger.begin(ShapeRenderer.ShapeType.Filled);
        bd.shapeDebugger.setColor(bd.blue);
        bd.shapeDebugger.rect(Gdx.graphics.getWidth() / -2, Gdx.graphics.getHeight() / -2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);
        bd.shapeDebugger.setColor(bd.grey);
        bd.shapeDebugger.rect(Gdx.graphics.getWidth() / -2, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);
        bd.shapeDebugger.end();*/
        bd._spriteBatch.end();

        //_skyBox.render();
        _plate.update();

    	modelBatch.begin(_cam.pCam);
    	
        modelBatch.render(instances);
        modelBatch.render(perso);
        modelBatch.end();
        _guiController.render();
        sleep(60);
    }

    @Override
    public void dispose () {
        Log.d("ok", "DISPODE");
        bd.tHorloge.dispose();
        bd.texture.dispose();
        bd.tWalking.dispose();
        tSky.dispose();
    	model.dispose();
        modelStation.dispose();
        modelBatch.dispose();
    }
 
    public void resume () {
    }
 
    public void resize (int width, int height) {
    }

    public void pause () {
    }
}
