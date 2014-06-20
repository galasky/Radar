package com.mygdx.radar.android;

import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class BubbleStop {
	public Vector2		position;
	//public List<StopTimes>	listStopTimes;
	private Vector2		_direction;
    private Vector2     pAffichage, pSave;
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
	private SoundManager	soundManager;
	//public float		distanceTemps;
	private Vector2		_goTo;
	
	public BubbleStop(Station s) {
        pAffichage = new Vector2(98, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() - 1735));
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
		select = false;
		_timeTouch = 0;
		_timeSelect = 0;
		check();
		//refreshNextTime();
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
}
