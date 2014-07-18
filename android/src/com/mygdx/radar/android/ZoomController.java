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
    private MyFont              fDistance;
	private ShapeRenderer       shapeDebugger;
    private LoadStation         loadStation;
	public boolean              zoomFinish;
	private ZoomController() {
        loadStation = new LoadStation();
        fDistance = new MyFont("font/HelveticaNeue.ttf", 30);
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		touch = false;
		cursorA = new Vector2(w / 3, h - 50);
		cursorB = new Vector2(2 * w / 3, h - 50);
		cursor = new Vector2(cursorA);
		camera = new OrthographicCamera(w, h);
	    camera.setToOrtho(true, w, h);
	    camera.update();
		myBatch = new SpriteBatch();
		shapeDebugger = new ShapeRenderer();
	}

    public void zoomIn() {
        if (!zoomFinish)
            return ;
        if (Config.instance().distance == Config.instance().distance2)
            distance1();
        else if (Config.instance().distance == Config.instance().distance3)
            distance2();
    }

    public void zoomOut() {
        if (!zoomFinish)
            return ;
        if (Config.instance().distance == Config.instance().distance1)
            distance2();
        else if (Config.instance().distance == Config.instance().distance2)
            distance3();
    }

    public void distance1() {
        Config.instance().distance = Config.instance().distance1;
        loadStation.interrupt();
        LoadStation load = new LoadStation();
        load.filtre = true;
        load.start();
        cursor.x = cursorA.x;
        MyCamera.instance().zoomTo(10);
    }

    public void distance2() {
        Config.instance().distance = Config.instance().distance2;
        LoadStation load = new LoadStation();
        load.filtre = true;
        load.start();
        cursor.x = (cursorA.x + cursorB.x) / 2;
        MyCamera.instance().zoomTo(20);
    }

    public void distance3() {
        Config.instance().distance = Config.instance().distance3;
        LoadStation load = new LoadStation();
        load.filtre = true;
        load.start();
        cursor.x = cursorB.x;
        MyCamera.instance().zoomTo(26);
    }

	public void render() {
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
        myBatch.begin();
        fDistance.draw(myBatch, (int) (Config.instance().distance1 * 1000) +" m", cursorA.x - 33, Gdx.graphics.getHeight() - (cursorA.y - 66));
        fDistance.draw(myBatch, (int) (Config.instance().distance2 * 1000) +" m", (cursorA.x + cursorB.x) / 2 - 33, Gdx.graphics.getHeight() - (cursorA.y - 66));
        fDistance.draw(myBatch, (int) (Config.instance().distance3 * 1000) +" m", cursorB.x - 33, Gdx.graphics.getHeight() - (cursorA.y - 66));
        myBatch.end();

		touch = false;
	}

    public static ZoomController instance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private final static ZoomController instance = new ZoomController();
    }

    public void tap(float x, float y) {
        y = Gdx.graphics.getHeight() - y;
        float distance = (float) Math.sqrt((cursorA.x - x) * (cursorA.x - x) + (cursorA.y - y) * (cursorA.y - y));
        if (distance < 100)
        {
            distance1();
        }
        distance = (float) Math.sqrt(((cursorA.x + cursorB.x) / 2 - x) * ((cursorA.x + cursorB.x) / 2 - x) + (cursorA.y - y) * (cursorA.y - y));
        if (distance < 100)
        {
            distance2();
        }
        distance = (float) Math.sqrt((cursorB.x - x) * (cursorB.x  - x) + (cursorB.y - y) * (cursorB.y - y));
        if (distance < 100)
        {
            distance3();
        }
    }
}
