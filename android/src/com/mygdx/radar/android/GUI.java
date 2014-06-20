package com.mygdx.radar.android;

import android.widget.Button;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GUI implements IGUI {
	private SpriteBatch			_spriteBatch;
	private MyFont			_font;
    private ArrayList<MyFont> _listFont;
	private	World				_world;
    private Sprite              sprite;
    private Vector2             toto;
    private Texture texture;
	private SpriteBatch myBatch;
	private Vector3 			V;
	private double				k;
	private String				_str;
    private int                 i;
    private Texture             tWalking;
    private Sprite              sWalking;
	private BubbleStop			_bubbleSelect;
	private ShapeRenderer shapeDebugger;
	private boolean				_initPosition;
    private GUIController       guiController;
    private PushButton           _button;
    private MyFont              fontNum, fontStationName, fontWalk;

    public class ChangeFont implements IAction {
        public void exec() {

        }
    }

	public GUI(GUIController gui) {
        fontNum = FontManager.instance()._listFont.get(1);
        fontNum = new MyFont("font/HelveticaNeueCondensedBold.ttf", 48);
        fontStationName = new MyFont("font/HelveticaNeue.ttf", 48);
        fontWalk = new MyFont("font/HelveticaNeueBold.ttf", 48);
        toto = new Vector2(300, 50);
        guiController = gui;
        _button = new PushButton(new ChangeFont(), "Change Font", new Vector2(20, 50));
        texture = new Texture(Gdx.files.internal("texture/bus.png"));
        tWalking = new Texture(Gdx.files.internal("texture/walking-green.png"));
        // setting a filter is optional, default = Nearest
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tWalking.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


// binding texture to sprite and setting some attributes
        sprite = new Sprite(texture);
        sWalking = new Sprite(tWalking);
        //sprite.setOrigin(texture.getWidth() / 2, texture.getHeight() / 2);
        sprite.setSize(64, 64);
        sWalking.setSize(40, 60);
		StationManager.instance().endDraw = true;
		shapeDebugger = new ShapeRenderer();
		_str = new String();
		_world = World.instance();
		myBatch = new SpriteBatch();
		_spriteBatch = new SpriteBatch();
        _listFont = FontManager.instance()._listFont;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/SIXTY.TTF"));
        FreeTypeFontParameter parametre = new FreeTypeFontParameter();
        parametre.size = 20;
        i = 0;
        _font = _listFont.get(i);
		//_font = new BitmapFont();
        _bubbleSelect = null;
	   
        initPosition();
	}

	@Override
	public void touch(float x, float y, float deltaX, float deltaY)
	{
        toto.x += deltaX;
        toto.y -= deltaY;
		//_str = "x = " + x + " y = " + y + " deltaX = " + deltaX + " deltaY = " + deltaY;
		if (_world.listBubbleStop == null)
			return ;
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		while (i.hasNext())
		{
			bubbleStop = i.next();
			if (bubbleStop.touch)
			{
				bubbleStop.move(deltaX, -deltaY);
				return;
			}
			if (bubbleStop.collision(x, Gdx.graphics.getHeight() - y))
			{
				bubbleStop.touch = true;
			}
		}
	}
	
	public void	initPosition() {
		_initPosition = true;
		if (_world.listBubbleStop == null)
			return ;
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		while (i.hasNext())
		{
			bubbleStop = i.next();
			bubbleStop.initPosition(null);
			bubbleStop.check();
		}
		_initPosition = false;
	}
	
	private void updateBubbleStop() {
		if (_world.listBubbleStop == null)
			return ;
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		while (i.hasNext())
		{
			bubbleStop = i.next();
			bubbleStop.update();
		}
	}
	
	private void renderAll() {
		if (StationManager.instance().getListStation() != null && _world.listBubbleStop != null)
		{
			StationManager.instance().endDraw = false;
			Date d = new Date();
			_spriteBatch.begin();
			Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
			BubbleStop bubbleStop = null;
			while (i.hasNext())
			{
				bubbleStop = i.next();
				_font.draw(_spriteBatch, bubbleStop.station.name, bubbleStop.position.x, bubbleStop.position.y);
			}
			StationManager.instance().endDraw = true;
			_spriteBatch.end();
		}
	}
	
	private void renderSelect() {

		myBatch.begin();
		Gdx.gl20.glLineWidth(10);
		
		//Enable transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		    
		shapeDebugger.setProjectionMatrix(guiController.camera.combined);
	    shapeDebugger.begin(ShapeType.Line);
	    shapeDebugger.setColor(0f, 0f, 0f, 1f);
	    shapeDebugger.end();
	    shapeDebugger.begin(ShapeType.Filled);
	    shapeDebugger.rect(_bubbleSelect.position.x - Gdx.graphics.getWidth() / 2, _bubbleSelect.position.y - Gdx.graphics.getHeight() / 2, 800,  (-73 -(_bubbleSelect.station.stops.size())* 76) * _bubbleSelect.slide / 50);
	    shapeDebugger.end();
	    myBatch.end();
	    
	/*	myBatch.begin();
		Gdx.gl20.glLineWidth(20);
		
		//Enable transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		    
		shapeDebugger.setProjectionMatrix(MyCamera.instance().pCam.combined);
	    shapeDebugger.begin(ShapeType.Line);
	    shapeDebugger.setColor(1f, 1f, 1f, 0.80f);
	    Vector3 A = new Vector3(_bubbleSelect.station.position.y, 0.8f, _bubbleSelect.station.position.x);
	    Vector3 B = MyCamera.instance().transorm(_bubbleSelect.position);
	    
	    shapeDebugger.line(A, B);
	    shapeDebugger.end();
	    myBatch.end();*/
	    
	    Gdx.gl.glDisable(GL20.GL_BLEND);



		_spriteBatch.begin();
		Date d = new Date();
        int nb;
		fontStationName.draw(_spriteBatch, _bubbleSelect.station.name, 159 + _bubbleSelect.position.x - 138, -17 + _bubbleSelect.position.y);
        _font.draw(_spriteBatch, toto.x + " " + toto.y, 400, 400);
        fontNum.draw(_spriteBatch,((nb = (int) (_bubbleSelect.station.distanceTemps * 60)) <= 9 ? "0" + nb : nb) + " mn", 662 + _bubbleSelect.position.x - (50 - _bubbleSelect.slide) * 2, -17 + _bubbleSelect.position.y - 10);
        sWalking.setPosition(_bubbleSelect.position.x + 610 - (50 - _bubbleSelect.slide) * 2, -20 + _bubbleSelect.position.y - 35 - 10);
        sWalking.draw(_spriteBatch);

        Iterator<Stop> i = _bubbleSelect.station.stops.iterator();
		nb = 0;
        int t;
		while (i.hasNext())
		{
			Stop stop = i.next();
			nb++;
            /*myBatch.begin();
            shapeDebugger.begin(ShapeType.Filled);
            shapeDebugger.setColor(Color.GRAY);
            shapeDebugger.rect(toto.x, toto.y, 50, 50);
            shapeDebugger.end();
            myBatch.end();*/

                sprite.setPosition(159 + _bubbleSelect.position.x - 141, -21 +  _bubbleSelect.position.y - 40 - nb * (_bubbleSelect.slide + 28));
                sprite.draw(_spriteBatch);
                 fontNum.draw(_spriteBatch, "42", 87 + _bubbleSelect.position.x, -21 + 5 + _bubbleSelect.position.y - nb * (_bubbleSelect.slide + 28));
                _font.draw(_spriteBatch, "ABLPDEXPKFESIMW", 152 + _bubbleSelect.position.x, -21 + -17 + -23 + 40 + _bubbleSelect.position.y - nb * (_bubbleSelect.slide + 28));
                String n = new String();

                fontNum.draw(_spriteBatch, "" + (stop.list_time.size() < 1 ? "-" : ((n = ((t = stop.list_time.get(0).diff(new Date())) <= 9 ? "0" + t : "" + t))).length() >= 3 ? n = "*" : n), 159 + _bubbleSelect.position.x + 480 - 20 * (n.length() - 1), -21 + 6 + _bubbleSelect.position.y - nb * (_bubbleSelect.slide + 28));
				fontNum.draw(_spriteBatch, "" + (stop.list_time.size() < 2 ? "-" : ((n = ((t = stop.list_time.get(1).diff(new Date())) <= 9 ? "0" + t : "" + t))).length() >= 3 ? n = "*" : n), 159 + _bubbleSelect.position.x + 540 - 20 * (n.length() - 1), -21 + 6 + _bubbleSelect.position.y - nb * (_bubbleSelect.slide + 28));
                fontNum.draw(_spriteBatch, "" + (stop.list_time.size() < 3 ? "-" : ((n = ((t = stop.list_time.get(2).diff(new Date())) <= 9 ? "0" + t : "" + t))).length() >= 3 ? n = "*" : n), 159 + _bubbleSelect.position.x + 600 - 20 * (n.length() - 1), -21 + 6 + _bubbleSelect.position.y - nb * (_bubbleSelect.slide + 28));

    }
		_spriteBatch.end();
		
		if (_bubbleSelect.select == false && _bubbleSelect.slide <= 0)
			_bubbleSelect = null;
	}
	@Override
	public void render() {
		if (_initPosition == true)
			initPosition();
		updateBubbleStop();
		
		if (_bubbleSelect == null)
			renderAll();
		else
			renderSelect();
        _button.draw();
	}

    public void changeFont() {

    }

	@Override
	public void tap(float x, float y) {

        if (_button.colision(x, y))
        {
            i++;
            if (i >= _listFont.size())
                i = 0;

            _font = _listFont.get(i);
            _button.setSelect(_font.font);
            return ;
        }
		if (_world.listBubbleStop == null)
			return ;
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		
		while (i.hasNext())
		{
			bubbleStop = i.next();
			if (_bubbleSelect == null && bubbleStop.collision(x, y))
			{
				bubbleStop.select();
				_bubbleSelect = bubbleStop;
				return ;
			}
		}
		if (_bubbleSelect != null)
		{
			_bubbleSelect.unSelect();
		}
	}

	@Override
	public IGUI invert() {
		return new GUI2(guiController);
	}

	@Override
	public void refresh() {
		
	}
}
