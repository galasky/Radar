package com.mygdx.game.android;

import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class BubbleStop {
	public Vector2		position;
	//public List<StopTimes>	listStopTimes;
	private Vector2		_direction;
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
			return (x >= position.x && x <= position.x + 650 && y >= position.y - (station.stops.size() + 1.5f) * slide && y <= position.y + 50);
	}
	
	public void		move(float deltaX, float deltaY)
	{
		_timeTouch = 0;
		_inertie = 1;
		_direction.x = deltaX;
		_direction.y = deltaY;
	}
	
	/*public void	refreshNextTime() {
		Date d = new Date();
		MyTimes tmp = null;
		
		Iterator<StopTimes> i = listStopTimes.iterator();
		StopTimes stopTimes = null;
		
		while (i.hasNext())
		{
			stopTimes = i.next();
			if (!stopTimes.departure_time.isBeforeTo(d))// && Territory.instance().isServiceAvailableByDate(Territory.instance().getTripsByTripId(stopTimes.trip_id).service_id, _today))
			{
				if (tmp == null || stopTimes.departure_time.isBeforeTo(tmp))
					tmp = stopTimes.departure_time;
			}
		}
		distanceTemps = distance / 5;
		nextTime =  tmp;
	}*/
	
	public void select()
	{
		slide = 0;
		select = true;
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
	
		
		if (_inertie > 0)
		{
			_inertie -= 0.01;
			if (_inertie < 0)
				_inertie = 0;
		}
		
		if (select)
		{
			if (slide < 50)
				slide += 5;
		}
		else
		{
			if (slide > 0)
				slide -= 5;
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
