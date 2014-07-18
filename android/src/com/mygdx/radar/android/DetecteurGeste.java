package com.mygdx.radar.android;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class DetecteurGeste implements GestureListener {
	public DetecteurGeste() {
	}
	
    @Override
    public boolean zoom (float DistanceInitial, float DistanceActuel) {
        if (DistanceActuel - DistanceInitial > 200)
        {
            ZoomController.instance().zoomIn();
        }
        else if (DistanceActuel - DistanceInitial < -200)
        {
            ZoomController.instance().zoomOut();
        }
    	return false;
    }

    @Override
    public boolean pinch (Vector2 posInitialPremierDoigt, Vector2 posInitialDeuxiemeDoigt, Vector2 posActuelPremierDoigt, Vector2 posActuelDeuxiemeDoigt) {
    	return false;
    }

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		Game3D.instance().tap(x, Gdx.graphics.getHeight() - y);
		return false;
	}



	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		Game3D.instance().touchScreen(x, y, deltaX, deltaY);
		return false;
	}

	@Override
	public boolean panStop(float arg0, float arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
 }