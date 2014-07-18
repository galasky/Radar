package com.mygdx.radar.android;

import java.util.Timer;
import java.util.TimerTask;

public class RefreshGUI extends Thread {
	private World	_world;
	
	public RefreshGUI() {
		_world = World.instance();
	}
	
	TimerTask task = new TimerTask()
	{
		@Override
		public void run() 
		{
			refreshBubbleStop();
		}
	};
	
	public void run() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 1000);
	  }
	
	
	
	public void	refreshBubbleStop() {
		if (_world.listBubbleStop == null)
			return ;
		
		for (int i = 0; i < _world.listBubbleStop.size(); i++)
		{
			BubbleStop bubble = _world.listBubbleStop.get(i);
            bubble.station.calcDistance();
			for (int u = 0; u < bubble.station.stops.size(); u++)
			{
				Stop stop = bubble.station.stops.get(u);
                stop.refreshListTime();
			}
		}
	}
}
