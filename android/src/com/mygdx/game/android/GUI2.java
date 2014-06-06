package com.mygdx.game.android;

import java.util.Date;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GUI2 implements IGUI {
	private SpriteBatch			_spriteBatch;
	private BitmapFont			_font;
	private boolean				_initPosition;
	private String				_str;
	private World				_world;
	private BubbleStop			_bubbleSelect;

	public GUI2() {
		_str = new String();
		_world = World.instance();
		_spriteBatch = new SpriteBatch();
    	_font = new BitmapFont();
    	_font.setColor(Color.GREEN);
        _font.setScale(3F, 3F);
        _bubbleSelect = null;
        initPosition();
	}
	
	static int random(int Min, int Max) {
		return (int) (Min + (Math.random() * ((Max - Min) + 1)));
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
			if (_bubbleSelect == null && bubbleStop.collision(x, Gdx.graphics.getHeight() - y))
			{
				bubbleStop.select = true;
				_bubbleSelect = bubbleStop;
				return ;
			}
		}
		if (_bubbleSelect != null)
		{
			_bubbleSelect.select = false;
			_bubbleSelect = null;
		}
	}
	
	@Override
	public void touch(float x, float y, float deltaX, float deltaY)
	{
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
	
	private void refreshOrder() {
		/*Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		int nb = 0;
		while (i.hasNext())
		{
			bubbleStop = i.next();
			bubbleStop.initPosition(new Vector2(50, 50 + bubbleStop.order * 50));
			nb++;
		}*/
	}

	public void	initPosition() {
		_initPosition = true;
		if (_world.listBubbleStop == null)
			return ;
		
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubbleStop = null;
		int nb = 0;
		while (i.hasNext())
		{
			bubbleStop = i.next();
			bubbleStop.initPosition(new Vector2(50, 50 + nb * 50));
			nb++;
		}
		_initPosition = false;
	}
	
	private void renderAll() {
		if (StationManager.instance().getListStation() != null && _world.listBubbleStop != null)
		{
			Date d = new Date();
			_spriteBatch.begin();
			Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
			BubbleStop bubbleStop = null;
			while (i.hasNext())
			{
				bubbleStop = i.next();
				_font.setColor(Color.WHITE);
				_font.draw(_spriteBatch, bubbleStop.station.name, bubbleStop.position.x, bubbleStop.position.y);
				//_font.draw(_spriteBatch, (int) (bubbleStop.distance * 1000) + "m", bubbleStop.position.x + 400, bubbleStop.position.y);
				//if (bubbleStop.nextTime != null)
				//	_font.draw(_spriteBatch, bubbleStop.nextTime.getString(), bubbleStop.position.x + 600, bubbleStop.position.y);
				//else
				//	_font.draw(_spriteBatch, "Fin du service", bubbleStop.position.x + 600, bubbleStop.position.y);
				//_font.draw(_spriteBatch, (int) (bubbleStop.distanceTemps * 60) + " min", bubbleStop.position.x + 900, bubbleStop.position.y);
				//_font.draw(_spriteBatch, diff + "", bubbleStop.position.x + 1100, bubbleStop.position.y);
			}
			//_font.draw(_spriteBatch,_str, 20, 100);
			_spriteBatch.end();
		}
	}
	
	@Override
	public void render() {
		if (_initPosition == true)
			initPosition();
		updateBubbleStop();
		renderAll();
	}

	@Override
	public IGUI invert() {
		return new GUI();
	}

	@Override
	public void refresh() {
		initPosition();
	}
}
