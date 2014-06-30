package com.mygdx.radar.android;

import java.util.Date;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class BubbleStop {
	public Vector2		position;
	//public List<StopTimes>	listStopTimes;
	private Vector2		_direction;
    public Vector2      pAffichage, pSave;
	public	float		slide;
	public Station		station;
	public int			order;
	private float		_inertie;
	//public	float		distance;
	public boolean		touch;
	private float		_timeTouch;
	private float		_timeSelect;
	//public MyTimes		nextTime;
	public boolean		select;
	private Date		_today;
    private BubbleDrawer    bd;
	private SoundManager	soundManager;
	//public float		distanceTemps;
	private Vector2		_goTo;

	
	public BubbleStop(Station s) {

        bd = BubbleDrawer.instance();

        pAffichage = null;
        pSave = null;
		soundManager = SoundManager.instance();
		soundManager.get("pop.mp3").play();
		order = -1;
		_goTo = null;
		_today = new Date();
		station = s;
		slide = 0;
		//listStopTimes = Territory.instance().getListStopTimesByStopId(station.stops.get(0).stop_id);
		//distance = (float) Territory.distanceAB(You.instance().coordinate, station.stops.get(0).coord);
		//distanceTemps = distance / 5;
		position = new Vector2();
		touch = false;
		select = true;
		_timeTouch = 0;
		_timeSelect = 0;
		check();
		//refreshNextTime();
	}

    public void setpAffichage(Vector2 pa)
    {
        pAffichage = pa;
    }

	public void	check() {
		_direction = new Vector2(GUIController.random(-10, 10), GUIController.random(-10, 10));
		_inertie = 2f;
	}
	
	public void initPosition(Vector2 goTo) {
		_goTo = goTo;
	}
	
	public boolean collision(float x, float y) {
		if (!select)
			return (x >= position.x && x <= position.x + 40 * station.name.length() && y >= position.y - 50 && y <= position.y + 50);
		else
			return (x >= position.x - Gdx.graphics.getWidth() / 2 - 160 && x <= position.x + 800 && y >=  position.y + ((station.stops.size() + 5.5f) * slide) && y <= position.y - Gdx.graphics.getHeight() / 2 + 20);
	}
	
	public void		move(float deltaX, float deltaY)
	{
		_timeTouch = 0;
		_inertie = 1;
		_direction.x = deltaX;
		_direction.y = deltaY;
	}

	public void select()
	{
		slide = 0;
		select = true;
        pSave = new Vector2(position.x, position.y);
	}
	
	public void unSelect()
	{
		slide = 50;
		select = false;
	}
	
	private	void goTo() {
		position.x += (_goTo.x - position.x) * 0.1;
		position.y += (_goTo.y - position.y) * 0.1;
		if (position.x == _goTo.x && position.y == _goTo.y)
			_goTo = null;
	}
	
	public void update() {

        station.update();
		if (_goTo != null)
			goTo();

		position.x += _direction.x * _inertie;
		position.y += _direction.y * _inertie;

		if (_goTo == null)
		{
			if (position.x > Gdx.graphics.getWidth() - 40 * station.name.length() / 2 || position.x < 40)
				_direction.x *= -1;
			if (position.y > Gdx.graphics.getHeight() || position.y < 5)
				_direction.y *= -1;
		}


		_inertie /= 1.1;

		if (select)
		{
			if (slide < 50)
				slide += 5;
            position.x += (pAffichage.x - position.x) * 0.1;
            position.y += (pAffichage.y - position.y) * 0.1;
		}
		else
		{
			if (slide > 0)
				slide -= 5;
            if (pSave != null)
            {
                position.x += (pSave.x - position.x) * 0.1;
                position.y += (pSave.y - position.y) * 0.1;
                if ((int) position.x  / 100 == (int) pSave.x / 100 && (int) position.y / 100 == (int) pSave.y / 100)
                    pSave = null;
            }
		}
		if (touch)
		{
			_timeTouch += Gdx.graphics.getDeltaTime();
			if (_timeTouch > 0.2)
			{
				_timeTouch = 0;
				touch = false;
			}
		}
	}

    public void scroll(float deltaY) {
        pAffichage.y -= deltaY * 2;
    }
    public void render(GUIController guiController) {
        bd.fontNum.setColor(Color.WHITE);
        bd.myBatch.begin();
        Gdx.gl20.glLineWidth(10);

        //Enable transparency
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        bd.shapeDebugger.setProjectionMatrix(guiController.camera.combined);
        bd.shapeDebugger.begin(ShapeRenderer.ShapeType.Line);
        bd.shapeDebugger.setColor(0f, 0f, 0f, 1f);
        bd.shapeDebugger.end();
        bd.shapeDebugger.begin(ShapeRenderer.ShapeType.Filled);
        bd.shapeDebugger.rect(position.x - Gdx.graphics.getWidth() / 2, position.y - Gdx.graphics.getHeight() / 2, 800, (-73 - (station.stops.size()) * 76) * slide / 50);
        bd.shapeDebugger.end();
        bd.myBatch.end();

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


        bd._spriteBatch.begin();
        Date d = new Date();
        int nb, walkingTime;
        bd.fontStationName.draw(bd._spriteBatch, station.name, 159 + position.x - 138, -17 + position.y);
        //_font.draw(_spriteBatch, toto.x + " " + toto.y, 400, 400);
        bd.fontNum.setColor(bd.green);
        bd.fontNum.draw(bd._spriteBatch, ((walkingTime = (int) (station.distanceTemps * 60)) <= 9 ? "0" + walkingTime : walkingTime) + " mn", -45 + 662 + position.x - (50 - slide) * 2, -17 + position.y - 10);
        bd.sWalking.setPosition(position.x + 610 + -45 - (50 - slide) * 2, -20 + position.y - 35 - 10);
        bd.sWalking.draw(bd._spriteBatch);
        bd._spriteBatch.end();
        Iterator<Stop> i = station.stops.iterator();
        nb = 0;

        while (i.hasNext()) {
            bd.fontNum.setColor(Color.WHITE);
            int time1, time2, time3;
            int t = 999;

            Stop stop = i.next();
            nb++;
            time1 = (stop.list_time.size() < 1 ? 999 : stop.list_time.get(0).diff(new Date()));
            time2 = (stop.list_time.size() < 2 ? 999 : stop.list_time.get(1).diff(new Date()));
            time3 = (stop.list_time.size() < 3 ? 999 : stop.list_time.get(2).diff(new Date()));

            bd.myBatch.begin();
            bd.shapeDebugger.setProjectionMatrix(guiController.camera.combined);
            bd.shapeDebugger.begin(ShapeRenderer.ShapeType.Filled);
            bd.shapeDebugger.setColor(bd.grey);
            bd.shapeDebugger.rect(position.x - Gdx.graphics.getWidth() / 2 + 612 + 0 * 62, -141 + position.y - Gdx.graphics.getHeight() / 2 + (nb - 1) * -77, 60, 60);
            bd.shapeDebugger.rect(position.x - Gdx.graphics.getWidth() / 2 + 612 + 1 * 62, -141 + position.y - Gdx.graphics.getHeight() / 2 + (nb - 1) * -77, 60, 60);
            bd.shapeDebugger.rect(position.x - Gdx.graphics.getWidth() / 2 + 612 + 2 * 62, -141 + position.y - Gdx.graphics.getHeight() / 2 + (nb - 1) * -77, 60, 60);
            bd.shapeDebugger.end();
            bd.myBatch.end();

            bd._spriteBatch.begin();
            bd.sprite.setPosition(159 + position.x - 141, -21 + position.y - 40 - nb * (slide + 28));
            bd.sprite.draw(bd._spriteBatch);
            bd.fontNum.draw(bd._spriteBatch, "10", 87 + position.x, -21 + 5 + position.y - nb * (slide + 28));
            bd._font.draw(bd._spriteBatch, "Aiguelong", 152 + position.x, -21 + -17 + -23 + 40 + position.y - nb * (slide + 28));

            t = 999;
            String n = new String();

            String str = new String();

            str = "" + (stop.list_time.size() < 1 ? "-" : ((n = ((t = stop.list_time.get(0).diff(new Date())) <= 9 ? "0" + t : "" + t))).length() >= 3 ? n = "*" : n);

            if (t - walkingTime <= 0)
                bd.fontNum.setColor(bd.red);
            else if (t - walkingTime <= 3)
                bd.fontNum.setColor(bd.orange);
            else
                bd.fontNum.setColor(bd.green);
            bd.fontNum.draw(bd._spriteBatch, str, 159 + position.x + 480 - 20 * (n.length() - 1), -21 + 6 + position.y - nb * (slide + 28));

            str = "" + (stop.list_time.size() < 2 ? "-" : ((n = ((t = stop.list_time.get(1).diff(new Date())) <= 9 ? "0" + t : "" + t))).length() >= 3 ? n = "*" : n);
            if (t - walkingTime <= 0)
                bd.fontNum.setColor(bd.red);
            else if (t - walkingTime <= 3)
                bd.fontNum.setColor(bd.orange);
            else
                bd.fontNum.setColor(bd.green);
            bd.fontNum.draw(bd._spriteBatch, str, 159 + position.x + 540 - 20 * (n.length() - 1), -21 + 6 + position.y - nb * (slide + 28));

            str = "" + (stop.list_time.size() < 3 ? "-" : ((n = ((t = stop.list_time.get(2).diff(new Date())) <= 9 ? "0" + t : "" + t))).length() >= 3 ? n = "*" : n);
            if (t - walkingTime <= 0)
                bd.fontNum.setColor(bd.red);
            else if (t - walkingTime <= 3)
                bd.fontNum.setColor(bd.orange);
            else
                bd.fontNum.setColor(bd.green);
            bd.fontNum.draw(bd._spriteBatch, str, 159 + position.x + 600 - 20 * (n.length() - 1), -21 + 6 + position.y - nb * (slide + 28));
            bd._spriteBatch.end();
        }
    }
}
