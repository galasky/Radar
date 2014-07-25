package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Date;

public class GUIController {
	private	GUI		        _gui;
    private PushButton      _homeButton;
	private float		    _time;
	private SpriteBatch	    _spriteBatch;
    private Menu            menu;
	private ZoomController	_zoomController;
	private MyFont	        _font;

    public OrthographicCamera camera;

    public class ActionHome implements IAction {
        public void exec() {
            active_menu();
        }
    }

    public GUIController() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menu = new Menu(this);
        _homeButton = new PushButton(new ActionHome(), new Texture(Gdx.files.internal("texture/user-home.png")), new Vector2(50, Gdx.graphics.getHeight() - 50), new Vector2(80, 80));
		_zoomController = ZoomController.instance();
		_time = 0;
		_gui = new GUI(this);
		_spriteBatch = new SpriteBatch();
		_font = new MyFont("font/HelveticaNeue.ttf", 40);
        new RefreshGUI().start();
	}

    public void active_menu()
    {
        if (!menu.opened)
            menu.active();
        else
            menu.desactive();
    }

	static float random(float Min, float Max) {
		return (float) (Min + (Math.random() * ((Max - Min) + 1)));
	}

	private void updateTime() {
		_time += Gdx.graphics.getDeltaTime();
		if (_time > 1)
		{
			_time = 0;
		}
	}

    private int nbUp() {
        int nb = 0;

        if (World.instance().listBubbleStop == null)
            return 0;
        for (int i = 0; i < World.instance().listBubbleStop.size(); i++)
        {
            BubbleStop bubbleStop = World.instance().listBubbleStop.get(i);
            if (bubbleStop.statu == 1)
                nb++;
        }
        return nb;
    }

    private int nbDown() {
        int nb = 0;
        if (World.instance().listBubbleStop == null)
            return 0;
        for (int i = 0; i < World.instance().listBubbleStop.size(); i++)
        {
            BubbleStop bubbleStop = World.instance().listBubbleStop.get(i);
            if (bubbleStop.statu == -1)
                nb++;
        }
        return nb;
    }

	public void	render() {
		updateTime();
		MyTimes time = new MyTimes(new Date());
        _gui.render();
		_spriteBatch.begin();
        int nbup = nbUp();
        if (nbup > 0)
            _font.draw(_spriteBatch, ""+nbup, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 40);
        int nbdown = nbDown();
        if (nbdown > 0)
            _font.draw(_spriteBatch, ""+nbdown, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		_font.draw(_spriteBatch, time.hours + ":" + (time.minutes < 10 ? "0" + time.minutes : time.minutes), Gdx.graphics.getWidth() - 130, Gdx.graphics.getHeight() - 40);
        _spriteBatch.end();

		_zoomController.render();
        menu.render();
        _homeButton.draw();
	}
	
	public void touch(float x, float y, float deltaX, float deltaY) {
        World.instance().toto.x += deltaX;
        World.instance().toto.y -= deltaY;
        menu.touch(x, y, deltaX, deltaY);
		_gui.touch(deltaX, deltaY);
	}
	
	public void tap(float x, float y) {
		_homeButton.tap(x, y);
        menu.tap(x, y);
        _gui.tap(x, y);
        _zoomController.tap(x, y);
	}
}
