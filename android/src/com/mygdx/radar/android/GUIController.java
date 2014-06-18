package com.mygdx.radar.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GUIController {
	private	IGUI		_gui;
    private PushButton  _homeButton;
	private World		_world;
	private float		_time;
	private boolean		_loadBubble;
	private SpriteBatch	_spriteBatch;
    private Menu        menu;
	private ZoomController	_zoomController;
	private MyFont	_font;
	private Date		_date;
	private String		_sDate;
	private	RefreshGUI	_refreshGUI;
    public OrthographicCamera camera;
	private StationManager	stationManager;

    public class ActionHome implements IAction {
        public void exec() {
            active_menu();
        }
    }

    public GUIController() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menu = new Menu(this);
        _homeButton = new PushButton(new ActionHome(), new Texture(Gdx.files.internal("texture/user-home.png")), new Vector2(50, Gdx.graphics.getHeight() - 50), new Vector2(80, 80));
		stationManager = StationManager.instance();
		_zoomController = new ZoomController();
		_date = new Date();
		_time = 0;
		_gui = new GUI(this);
		_world = World.instance();
		_loadBubble = true;
		_spriteBatch = new SpriteBatch();
		_font = new MyFont("font/HelveticaNeue.ttf", 40);
        //_font._font.setScale(1F, 1F);
        _refreshGUI = new RefreshGUI(_gui);
        _refreshGUI.start();
	}

    public void active_menu()
    {
        if (menu.opened == false)
            menu.active();
        else
            menu.desactive();
    }

	static float random(float Min, float Max) {
		return (float) (Min + (Math.random() * ((Max - Min) + 1)));
	}
	
	private void loadBubbleStop() {
		if (stationManager.loadFinish() && stationManager.getListStation() != null)
		{
			_world.listBubbleStop = new ArrayList<BubbleStop>();
			Iterator<Station> i = stationManager.getListStation().iterator();
	    	Station station;
	    	int	nb = 0;
	    	while (i.hasNext())
	    	{
	    		station = i.next();
	    		BubbleStop bubble = new BubbleStop(station);
	    		bubble.position.x = random(40, Gdx.graphics.getWidth() - 40 * station.name.length() / 2);
	    		bubble.position.y = random(Gdx.graphics.getHeight(), Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 8);
	    		_world.listBubbleStop.add(bubble);
	    		Log.d("ok", "galasky add bubble");
	    		_loadBubble = false;
	    		nb++;
	    	}
		}
	}

	
	private void updateTime() {
		_time += Gdx.graphics.getDeltaTime();
		if (_time > 1)
		{
			_time = 0;
			//refreshBubbleStop();
			//_gui.refresh();
		}
	}

	public void	render() {
		updateTime();
		MyTimes time = new MyTimes(new Date());
		
		_spriteBatch.begin();
		_font.draw(_spriteBatch, time.hours + ":" + (time.minutes < 10 ? "0" + time.minutes : time.minutes), Gdx.graphics.getWidth() - 130, Gdx.graphics.getHeight() - 40);
		_spriteBatch.end();
		_gui.render();
		_zoomController.render();
        menu.render();
        _homeButton.draw();
	}
	
	public void	invert() {
		_gui = _gui.invert();
	}
	
	public void touch(float x, float y, float deltaX, float deltaY) {
        menu.touch(x, y, deltaX, deltaY);
		_gui.touch(x, y, deltaX, deltaY);
		_zoomController.touch(x, y, deltaX, deltaY);
	}
	
	public void tap(float x, float y) {
		_homeButton.tap(x, y);
        menu.tap(x, y);
        _gui.tap(x, y);
	}
	public void	refreshBubbleStop() {
		if (_world.listBubbleStop == null)
			return ;
		Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
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
		}
	}
}
