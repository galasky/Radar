package com.mygdx.radar.android;

import android.widget.Button;

import java.lang.reflect.WildcardType;
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
    private Texture             texture;
	private SpriteBatch         myBatch;
	private Vector3 			V;
	private double				k;
	private String				_str;
    private int                 i;
    private Texture             tWalking;
    private Sprite              sWalking;
	private BubbleStop			_bubbleSelect;
	private ShapeRenderer       shapeDebugger;
	private boolean				_initPosition;
    private GUIController       guiController;
    private Color               green, orange, red, grey;
    private MyFont              fontNum, fontStationName, fontWalk;

    public class ChangeFont implements IAction {
        public void exec() {
        }
    }

	public GUI(GUIController gui) {
        green = new Color().set(129 / 255f, 215 / 255f, 89 / 255f, 1f);
        orange = new Color().set(230 / 255f, 163 / 255f, 64 / 255f, 1f);
        red = new Color().set(210 / 255f, 90 / 255f, 87 / 255f, 1f);
        grey = new Color().set(83 / 255f, 88 / 255f, 95 / 255f, 1f);
        fontNum = FontManager.instance()._listFont.get(1);
        fontNum = new MyFont("font/HelveticaNeueCondensedBold.ttf", 48);
        fontStationName = new MyFont("font/HelveticaNeue.ttf", 48);
        fontWalk = new MyFont("font/HelveticaNeueBold.ttf", 48);
        toto = new Vector2(300, 50);
        guiController = gui;
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
		//Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		for (int i = 0; i < _world.listBubbleStop.size(); i++)
		{
            bubbleStop = _world.listBubbleStop.get(i);
            bubbleStop.scroll(deltaY);
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
		BubbleStop bubbleStop = null;
		for (int i = 0; i < _world.listBubbleStop.size(); i++)
		{
			bubbleStop = _world.listBubbleStop.get(i);
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
        _bubbleSelect.render(guiController);
		
		if (_bubbleSelect.select == false && _bubbleSelect.slide <= 0)
			_bubbleSelect = null;
	}
	@Override
	public void render() {
		if (_initPosition == true)
			initPosition();
		updateBubbleStop();

        if (World.instance().listBubbleStop != null)
        {
           // Iterator<BubbleStop> it = World.instance().listBubbleStop.iterator();
            int i = 0;

            while (i < World.instance().listBubbleStop.size())
            {
                BubbleStop b = World.instance().listBubbleStop.get(i);
                if (b.position.y > 0 && b.position.y < Gdx.graphics.getHeight() && Config.instance().distance >= b.station.distanceAffichage)
                    b.render(guiController);
                i++;
            }
        }
	}

    public void changeFont() {

    }

	@Override
	public void tap(float x, float y) {

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
