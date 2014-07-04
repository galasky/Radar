package com.mygdx.radar.android;


import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
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
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;

public class Game3D implements ApplicationListener {
	private MyCamera				_cam;
	//private SkyBox				_skyBox;
	public ModelBuilder 			modelBuilder;
    private Plate					_plate;
    private float toto;
    private	GUIController			_guiController;
    public CameraInputController	camController;
    private DirectionalLight		_light;
    public AssetManager				assets;
    public ModelBatch 				modelBatch;
    public Environment 				environment;
    public boolean 					loading;
    public Array<ModelInstance>		instances;
    public Array<ModelInstance>		perso;
    public ModelInstance            instanceSky;
    public Model                    model, modelStation, modelSky;
    private long 					diff, start;
    
    private Game3D() {
        toto = 1;
    	start = System.currentTimeMillis();
    	loading = false;
        modelStation = null;
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

        // A ModelBatch is like a SpriteBatch, just for models.  Use it to batch up geometry for OpenGL
        modelBatch = new ModelBatch();

        // Model loader needs a binary json reader to decode
        UBJsonReader jsonReader = new UBJsonReader();
        // Create a model loader passing in our json reader
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        // Now load the model by name
        // Note, the model (g3db file ) and textures need to be added to the assets folder of the Android proj

        model = modelLoader.loadModel(Gdx.files.getFileHandle("data/you/you.g3db", Files.FileType.Internal));
        modelStation = modelLoader.loadModel(Gdx.files.getFileHandle("data/station/station.g3db", Files.FileType.Internal));
        /*modelSky = modelLoader.loadModel(Gdx.files.getFileHandle("data/sky/sky_v2.g3db", Files.FileType.Internal));

        Pixmap _pixmap = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        _pixmap.setColor(Color.BLUE);
        _pixmap.fillRectangle(0, 0, 50, 50);
        Texture _pixmapTexture = new Texture(_pixmap, Pixmap.Format.RGB888, false);
        modelSky = modelBuilder.createCylinder(4f, 6f, 4f, 16,
                new Material(TextureAttribute.createDiffuse(_pixmapTexture)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        instanceSky = new ModelInstance(modelSky);
        perso.add(instanceSky);*/


        // Now create an instance.  Instance holds the positioning data, etc of an instance of your model
        ModelInstance modelInstance = new ModelInstance(model);
        modelInstance.transform.setToTranslation(0, .23f, 0);
        modelInstance.transform.scale(0.5f, 0.5f, 0.5f);
        perso.add(modelInstance);



        loading = false;
    }
	
    @Override
    public void create () {
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
        //toto += deltaX / 1000;
        //Log.d("ok", "galasky toto = " + toto);
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
