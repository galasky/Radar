package com.mygdx.game.android;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


public class DrawStop {
	private double _zoom, _acZoom;
	private BitmapFont _font;
	private BitmapFont _fontYou;
	private int _nbStopVisible;
	public boolean _autoZoom, _nameVisible;
	public float _timeTap;
	private float _centerX, _centerY, _acCenterX, _acCenterY, _angleBoussole, _angle, _acAngle;
	
	private DrawStop() {
		_centerX = 0;
		_centerY = 0;
		_acCenterX = 0;
		_acCenterY = 0;
		_acZoom = 0;
		_nameVisible = true;
		_font = new BitmapFont();
		_fontYou = new BitmapFont();
        _font.setColor(Color.RED);
        _font.setScale(2F, 2F);
        _fontYou.setColor(Color.GREEN);
        _fontYou.setScale(2F, 2F);
        _angleBoussole = 0;
        _autoZoom = true;
        _nbStopVisible = 0;
        _timeTap = 0;
        _angle = 0;
        _acAngle = 0;
	}
	
	private void updateAngle() {
		
		_angleBoussole = (float) ((Math.PI * Gdx.input.getAzimuth()) / 180);
		float delta = Math.abs(_angleBoussole - _angle);
		if (delta > 0.2)
		{
			_acAngle = _angleBoussole;
		}
		if (_acAngle != _angle)
		{
			if (_acAngle > _angle)
			{
				if (delta < Math.PI * 1.5)
				{
					_angle += 0.1 * delta;
					if (_acAngle < _angle)
						_acAngle = _angle;
				}
				else
				{
					_angle += Math.PI * 1.99;
				}
			}
			if (_acAngle < _angle)
			{
				if (delta < Math.PI * 1.5)
				{
					_angle -= 0.1 * delta;
					if (_acAngle > _angle)
						_acAngle = _angle;
				}
				else
				{
					_angle -= Math.PI * 2;
				}
			}
		}
	}	
	public void update() {
		//Log.d("galasky", "galasky Zoom " + zoom);
		
		updateAngle();
		autoZoom();
		_timeTap += Gdx.graphics.getDeltaTime();
		if (!(_zoom <= 0 && _acZoom < 0))
			_zoom += _acZoom;
		_centerX += _acCenterX;
		_centerY += _acCenterY;
		deceleration();
	}
	
	private void deceleration() {
		if (_acZoom > 0) {
			_acZoom -= 100;
			if (_acZoom < 0)
				_acZoom = 0;
		}
		if (_acZoom < 0) {
			_acZoom += 100;
			if (_acZoom > 0)
				_acZoom = 0;
		}
		if (_acCenterX > 0) {
			_acCenterX -= 1;
			if (_acCenterX < 0)
				_acCenterX = 0;
		}
		if (_acCenterX < 0) {
			_acCenterX += 1;
			if (_acCenterX > 0)
				_acCenterX = 0;
		}
		if (_acCenterY > 0) {
			_acCenterY -= 1;
			if (_acCenterY < 0)
				_acCenterY = 0;
		}
		if (_acCenterY < 0) {
			_acCenterY += 1;
			if (_acCenterY > 0)
				_acCenterY = 0;
		}
	}
	
	public double getZoom() {
		return _zoom;
	}
	
	private void autoZoom() {
		if (_autoZoom)
		{
			if (_nbStopVisible < 10)
				_zoom -= 8000;
			else if (_nbStopVisible > 10)
				_zoom += 6000;
			else
				_autoZoom = false;
		}
	}
	
	public void translateX(float x, float deltaX) {
		_acCenterX = deltaX;
	}
	
	public void translateY(float y, float deltaY) {
		_acCenterY = -deltaY;
	}
	
	public void zoom(float delta) {
		_autoZoom = false;
		_acZoom = delta * 10;
	}
	
	public void latitudeTranslate(double x) {
		_centerX += x;
	}
	
	public void longitudeTranslate(double y) {
		_centerY += y;
	}
	
	public void setZoom(double z) {
		_zoom = z;
	}
	
	public static DrawStop instance() {
        return SingletonHolder.instance;
    }
	
	public void setCenter(float centerx, float centery) {
		_centerX = centerx;
		_centerY = centery;
	}
	
	public void dispose() {
		_font.dispose();
		_fontYou.dispose();
	}
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static DrawStop instance = new DrawStop();
    }
	
	public void draw(SpriteBatch batch, List<Stop> listStop, CoordinateGPS me) {
		_fontYou.draw(batch, "You", _centerX, _centerY);
		Vector3 decal = new Vector3();
		
		Iterator i = listStop.iterator();
		Stop stop = null;
		_nbStopVisible = listStop.size();
		while(i.hasNext()){
	          stop = (Stop) i.next();
	          //calcRaport(stop);
	          decal.x = (float) ((me.latitude - stop.coord.latitude));
	          decal.y = (float) ((me.longitude - stop.coord.longitude));
	          rotation_z(_angle, decal);
	          decal.x *= _zoom;
	          decal.y *= _zoom;
	          Vector3 point = new Vector3(decal.x + _centerX, decal.y + _centerY, 0);
	          if (inWindow(point)) {
	        	  _font.draw(batch, "+ " + (_nameVisible ? stop.stop_name : "") + "", point.x, point.y);
	          } else {
	        	 _nbStopVisible -= 1;
	          }  
			}
	}
	
	public boolean inWindow(Vector3 point) {
		return (point.x <= Gdx.graphics.getWidth() && point.x >= 0 && point.y <= Gdx.graphics.getHeight() && point.y >= 0);
	}
	
	void rotation_x(double a, Vector3 f)
	{
	  Vector3  tempo;

	  tempo = f;
	  f.x *= 1;
	  f.y = (float) (Math.cos(a) * tempo.y - Math.sin(a) * tempo.z);
	  f.z = (float) (Math.sin(a) * tempo.y + Math.cos(a) * tempo.z);
	}

	void rotation_y(double a, Vector3 f)
	{
	  Vector3  tempo;

	  tempo = f;
	  f.x = (float) (Math.cos(a) * tempo.x + Math.sin(a) * tempo.z);
	  f.y *= 1;
	  f.z = (float) (-Math.sin(a) * tempo.x + Math.cos(a) * tempo.z);
	}

	void rotation_z(double a, Vector3 f)
	{
	  Vector3  tempo = new Vector3();

	  tempo.x = f.x;
	  tempo.y = f.y;
	  tempo.z = f.z;
	  f.x = (float) (Math.cos(a) * tempo.x - Math.sin(a) * tempo.y);
	  f.y = (float) (Math.sin(a) * tempo.x + Math.cos(a) * tempo.y);
	  f.z = tempo.z;
	}
}
