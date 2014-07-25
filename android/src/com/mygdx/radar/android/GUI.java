package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import java.util.Iterator;

public class GUI {
	private	World				_world;
	private BubbleStop			_bubbleSelect;
	private boolean				_initPosition;
    private GUIController       guiController;

	public GUI(GUIController gui) {
        guiController = gui;
		StationManager.instance().endDraw = true;
		_world = World.instance();
        _bubbleSelect = null;
	   
        initPosition();
	}

	public void touch(float deltaX, float deltaY)
	{
        if (_world.listBubbleStop == null || _world.listBubbleStop.size() == 0)
            return ;
        if (deltaX < -40 && !StationManager.instance().hide) {
            for (int i = 0; i < _world.listBubbleStop.size(); i++) {
                BubbleStop bubbleStop = _world.listBubbleStop.get(i);
                bubbleStop.hide();
            }
            StationManager.instance().hide = true;
        }
        if (deltaX > 40 && StationManager.instance().hide) {
            for (int i = 0; i < _world.listBubbleStop.size(); i++) {
                BubbleStop bubbleStop = _world.listBubbleStop.get(i);
                bubbleStop.visible();
            }
            StationManager.instance().hide = false;
        }
		BubbleStop bubbleStop;
        if (_world.listBubbleStop.get(0).pAffichage.y < Gdx.graphics.getHeight() / 2 + 100 - _world.listBubbleStop.get(0).getHeight() && deltaY > 0)
            return ;
        if (_world.listBubbleStop.get(_world.listBubbleStop.size() - 1).pAffichage.y > Gdx.graphics.getHeight() - 100 && deltaY < 0)
            return ;
		for (int i = 0; i < _world.listBubbleStop.size(); i++)
		{
            bubbleStop = _world.listBubbleStop.get(i);
            bubbleStop.scroll(deltaY);
		}
	}
	
	public void	initPosition() {
		_initPosition = true;
		if (_world.listBubbleStop == null)
			return ;
		for (int i = 0; i < _world.listBubbleStop.size(); i++)
		{
			BubbleStop bubbleStop = _world.listBubbleStop.get(i);
			bubbleStop.initPosition(null);
			bubbleStop.check();
		}
		_initPosition = false;
	}
	
	private void updateBubbleStop() {
		if (_world.listBubbleStop == null)
			return ;
		BubbleStop bubbleStop;
		for (int i = 0; i < _world.listBubbleStop.size(); i++)
		{
            if (i < _world.listBubbleStop.size()) {
                bubbleStop = _world.listBubbleStop.get(i);
                bubbleStop.update();
            }
		}
	}

	public void render() {
		if (!_initPosition)
			initPosition();
		updateBubbleStop();
        if (World.instance().listBubbleStop != null)
        {
            int i = 0;
            while (i < World.instance().listBubbleStop.size())
            {
                if (i >= World.instance().listBubbleStop.size())
                    return ;
                BubbleStop b = World.instance().listBubbleStop.get(i);
                if (b.position.y < Gdx.graphics.getHeight() / 2 - b.getHeight())
                    b.statu = -1;
                else if (b.position.y > Gdx.graphics.getHeight())
                    b.statu = 1;
                else {
                    b.statu = 0;
                    b.render(guiController);
                }
                i++;
            }
        }
	}

	public void tap(float x, float y) {

		if (_world.listBubbleStop == null)
			return ;

        if (y > Gdx.graphics.getHeight() / 2) {
            Iterator<BubbleStop> i = _world.listBubbleStop.iterator();
            BubbleStop bubbleStop;
            while (i.hasNext()) {
                bubbleStop = i.next();
                if (_bubbleSelect == null && bubbleStop.collision(x, y)) {
                    StationManager.instance().selectBubble(bubbleStop);
                    return;
                }
            }
        }
        StationManager.instance().initUnselect();
        StationManager.instance().unSelectBubble();
        float tmp = Config.instance().distance;
        Config.instance().distance = Config.instance().distance1;
        StationManager.instance().filtreDistance();
        Config.instance().distance = tmp;
        StationManager.instance().filtreDistance();
	}
}
