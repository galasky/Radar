package com.mygdx.radar.android;


import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;

public class Game3D implements ApplicationListener {
	private MyCamera				_cam;
	public ModelBuilder 			modelBuilder;
    private Plate					_plate;
    private	GUIController			_guiController;
    private Texture                 tSky;
    private Sprite                  sSky;
    public AssetManager				assets;
    public ModelBatch 				modelBatch;
    public Environment 				environment;
    public boolean 					loading;
    public OrthographicCamera       camera;
    public Array<ModelInstance>		instances;
    public Array<ModelInstance>		perso;
    public ModelInstance            modelInstance;
    private BubbleDrawer            bd;
    public Model                    model, modelStation;
    private long 					start;
    
    private Game3D() {
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
        Log.d("ok", "LOAD CREATE");
        tSky = new Texture(Gdx.files.internal("texture/sky.png"));
        sSky = new Sprite(tSky);
        sSky.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2.07f);
        sSky.setPosition(0, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2.07f);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        bd = BubbleDrawer.instance();
    	instances = new Array<ModelInstance>();
    	perso = new Array<ModelInstance>();
    	assets = new AssetManager();
    	loadSound();
        Log.d("ok", "LOAD START");
        doneLoading();
        Log.d("ok", "LOAD END");
        _cam = MyCamera.instance();
    	modelBuilder = new ModelBuilder();
    	modelBatch = new ModelBatch();
    	environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, -1f, 0f));
        _plate = new Plate(environment);
        BubbleDrawer.instance();
        _guiController = new GUIController();
        DetecteurGeste monDetecteurGeste = new DetecteurGeste();
		Gdx.input.setInputProcessor(new GestureDetector (monDetecteurGeste));
        modelBuilder = new ModelBuilder();
        Log.d("ok", "LOAD CREATE END");
    }

    public void	touchScreen(float x, float y, float deltaX, float deltaY) {
       World.instance().tata.y -= deltaY;
       World.instance().tata.x += deltaX;
    	_guiController.touch(x, y, deltaX, deltaY);
    }
    
    public void tap(float x, float y)
    {
    	_guiController.tap(x, y);
    }
   
    public void sleep(int fps) {
        if(fps>0){
          long diff = System.currentTimeMillis() - start;
          long targetDelay = 1000/fps;
          if (diff < targetDelay) {
            try{
                Thread.sleep(targetDelay - diff);
              } catch (InterruptedException e) {
                return ;
            }
            }   
          start = System.currentTimeMillis();
        }
    }
    
    @Override
    public void render () {
    	//if (assets.update())

        _cam.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.graphics.getGL20().glClearColor(147 / 255f, 199 / 255f, 255 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        bd._spriteBatch.begin();
        sSky.draw(bd._spriteBatch);
        bd._spriteBatch.end();
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
