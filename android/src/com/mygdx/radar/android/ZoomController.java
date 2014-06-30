package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class ZoomController {
	private OrthographicCamera  camera;
	private SpriteBatch         myBatch;
	private Vector2	            cursorA;
	private Vector2             cursorB;
	private	Vector2	            cursor;
	private boolean             touch;
	private float	            len;
	private float	            direction;
	private	int		            position;
	private ShapeRenderer       shapeDebugger;
	
	public ZoomController() {
		position = 0;
		direction = 0;
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		touch = false;
		cursorA = new Vector2(w / 3, h - h / 8);
		cursorB = new Vector2(2 * w / 3, h - h / 8);
		len = new Vector2(cursorA.x - cursorB.x, cursorA.y - cursorB.y).len();
		cursor = new Vector2(cursorA);
		camera = new OrthographicCamera(w, h);
	    camera.setToOrtho(true, w, h);
	    camera.update();
		myBatch = new SpriteBatch();
		shapeDebugger = new ShapeRenderer();
	}
	
	private void move(float d) {
		direction = d;
		touch = true;
		cursor.x += direction;
		position = -1;
		if (cursor.x < cursorA.x) {
			cursor.x = cursorA.x;
			direction = 0;
		}
		if (cursor.x > cursorB.x) {
			cursor.x = cursorB.x;
			direction = 0;
		}
	}
	
	public void touch(float x, float y, float deltaX, float deltaY) {
		Vector2 v = new Vector2(x - cursor.x, y - cursor.y);
		
		if (v.len() <= 60 && deltaY > 0) {
			move(deltaX);
		}
	}
	
	private void update() {
		if (!touch)
		{
			if (position == -1)
				cursor.x += (direction < 0 ? -len / 50 : (Math.abs(cursor.x - cursorA.x) > 10 ? len / 50 : 0));
				if (Math.abs(cursor.x - (cursorA.x + cursorB.x) / 2) < len / 25)
				{
					cursor.x = (cursorA.x + cursorB.x) / 2;
					direction = 0;
					position = 1;
					MyCamera.instance().zoomTo(20);
				}
			if (cursor.x > cursorB.x)
			{
				cursor.x = cursorB.x;
				direction = 0;
				position = 2;
				MyCamera.instance().zoomTo(26);
			}
			if (cursor.x <= cursorA.x)
			{
				cursor.x = cursorA.x;
				direction = 0;
				position = 0;
				MyCamera.instance().zoomTo(10);
			}
		}

	}
	
	public void render() {
		update();
		myBatch.begin();
		Gdx.gl20.glLineWidth(10);
		shapeDebugger.setProjectionMatrix(camera.combined);
	    shapeDebugger.begin(ShapeType.Line);
	    shapeDebugger.setColor(0, 1, 1, 1);
	    shapeDebugger.line(cursorA, cursorB);
	    shapeDebugger.end();
	    shapeDebugger.begin(ShapeType.Filled);

	    shapeDebugger.circle(cursorA.x, cursorA.y, 10);
	    shapeDebugger.circle((cursorA.x + cursorB.x) / 2, (cursorA.y + cursorB.y) / 2, 10);
	    shapeDebugger.circle(cursorB.x, cursorB.y, 10);
	    if (touch)
	    	shapeDebugger.setColor(1, 1, 1, 0.5f);
	    else
	    	shapeDebugger.setColor(0, 1, 1, 0.5f);
	    shapeDebugger.circle(cursor.x, cursorA.y, 30);
	    shapeDebugger.end();
	    myBatch.end();
		touch = false;
	}
}
