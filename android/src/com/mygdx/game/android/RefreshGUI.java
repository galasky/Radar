package com.mygdx.game.android;

import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;


public class RefreshGUI extends Thread {
	private IGUI	_gui;
	private	boolean	_life;
	private	float	_time;
	private World	_world;
	
	public RefreshGUI(IGUI gui) {
		_gui = gui;
		_life = true;
		_world = World.instance();
	}
	
	TimerTask task = new TimerTask()
	{
		@Override
		public void run() 
		{
			Log.d("ok", "galasky REFRESH");
			refreshBubbleStop();
			//_gui.refresh();
		}	
	};
	
	public void run() {
		_time = 0;
		Log.d("ok", "galasky RUUUUUN");
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 1000);
	  }
	
	
	
	public void	refreshBubbleStop() {
		if (_world.listBubbleStop == null)
			return ;
		
		for (int i = 0; i < _world.listBubbleStop.size(); i++)
		{
			BubbleStop bubble = _world.listBubbleStop.get(i);
			for (int u = 0; u < bubble.station.stops.size(); u++)
			{
				Stop stop = bubble.station.stops.get(u);
				stop.refreshNextTime();
			}
		}
		
		
		/*Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
		BubbleStop bubble = null;
		while (i.hasNext())
		{
			bubble = i.next();
			Iterator<Stop> u = bubble.station.stops.iterator();
			while (u.hasNext())
			{
				Stop stop = u.next();
				stop.refreshNextTime();
			}
		}*/
	}
}
