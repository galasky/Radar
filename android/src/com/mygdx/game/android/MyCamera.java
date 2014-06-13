package com.mygdx.game.android;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;



public class MyCamera {
	private float				_angleBoussole;
	private float				_angleFiltre;
	public PerspectiveCamera	pCam;
	private float				_zoom;
	public double				width;
	public double				height;
	private Vector3				_look;
	public boolean				firstPerson;
	private ArrayList<Float>	_tab;
	private float 				_zoomTo;
	
	private	MyCamera() {
		_zoomTo = 10;
		firstPerson = false;
		_tab = new ArrayList<Float>();
		_zoom = 10;
        _angleFiltre = 0;
        pCam = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        width = 2 * Math.tan(pCam.fieldOfView) * (pCam.viewportHeight / pCam.viewportWidth);
        height = 2 * Math.tan(pCam.fieldOfView);
        _look = new Vector3(1, 0, 0);
        pCam.lookAt(0,0,0);
        pCam.near = 1f;
        pCam.far = 3000f;
        pCam.update();
	}
	
	public Vector3	transorm(Vector2 point) {
		Vector2 p = new Vector2(point);
		Vector3 position = new Vector3(pCam.position);
		Vector3 right = new Vector3(pCam.up);
		right = right.rotate(pCam.direction, 90);
		Vector3 up = new Vector3(pCam.up);
		
		p.x -= Gdx.graphics.getWidth() / 2;
		p.y -= Gdx.graphics.getHeight() / 2;
		
		p.x /= 3.1;
		p.y /= 1.5;
		
		p.x = (float) ((p.x / Gdx.graphics.getWidth()) * width);
		p.y = (float) ((p.y / Gdx.graphics.getHeight()) * height);

		right.x *= p.x;
		right.y *= p.x;
		right.z *= p.x;
		up.x *= p.y;
		up.y *= p.y;
		up.z *= p.y;
		
		position = position.add(pCam.direction);
		position = position.add(up);
		position = position.add(right);
		
		return position;
	}
	
	public static MyCamera instance() {
        return SingletonHolder.instance;
    }
	
	private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static MyCamera instance = new MyCamera();
    }
	
	private float moyenne()
	{
		int i = 0;
		float	m = 0;
		
		while (i < _tab.size())
		{
			m += _tab.get(i);
			i++;
		}
		return m / _tab.size();
	}
	
	public void	zoomTo(float zoomTo) {
		_zoomTo = zoomTo;
	}
	
	private void filtre() {
		if (Math.abs(_angleBoussole + Math.PI * 2 - _angleFiltre) < 1)
			_angleBoussole += Math.PI * 2;
		if (Math.abs(_angleBoussole - Math.PI * 2 - _angleFiltre) < 1)
			_angleBoussole -= Math.PI * 2;
		if (Math.abs(_angleFiltre - Math.PI * 2) < 0.1 || Math.abs(_angleFiltre + Math.PI * 2) < 0.1)
		{
			_angleFiltre = 0;
			_tab.clear();
			return ;
		}
			_tab.add(new Float(_angleBoussole));
		if (_tab.size() >= 20)
		{
			
			_angleFiltre = moyenne();
			_tab.remove(0);
		}
		else
		{
		}
	}
	
	public void update() {
		_angleBoussole = (float) ((Math.PI * Gdx.input.getAzimuth()) / 180);
		if (Math.abs(_zoomTo - _zoom) < 1)
			_zoom = _zoomTo;
		else
			_zoom += (_zoomTo > _zoom ? 0.5 : -0.5);
		filtre();
		if (firstPerson == true)
			firstPerson();
		else
			thirdPerson();
		You.instance().setRotation(_angleFiltre);
	}
	
	private void	thirdPerson() {
		pCam.position.x = (float) Math.sin(-_angleFiltre - Math.PI / 2) * _zoom;
		pCam.position.y = 0.5f * _zoom;
		pCam.position.z = (float) Math.cos(-_angleFiltre - Math.PI / 2) * _zoom;
		pCam.position.x += You.instance().position.x;
		pCam.position.z += You.instance().position.z;
		
		pCam.lookAt(You.instance().position);
		pCam.up.x = 0;
		pCam.up.y = 1;
		pCam.up.z = 0;
		pCam.update();
	}
	
	private void	firstPerson() {
		_look.x = (float) Math.sin(-_angleFiltre + Math.PI / 2);
		_look.y *= 1;
		_look.z = (float) Math.cos(-_angleFiltre + Math.PI / 2);

		pCam.position.x = You.instance().position.x;
		pCam.position.y = You.instance().position.y;
		pCam.position.z = You.instance().position.z;
		
		pCam.lookAt(_look);
		pCam.up.x = 0;
		pCam.up.y = 1;
		pCam.up.z = 0;
		pCam.update();
	}
	
	public float	getAngle() {
		return _angleFiltre;
	}
	
    public void zoom(float DistanceInitial, float DistanceActuel) {
    	//_position.y += pCam.position.y * (DistanceInitial - DistanceActuel) / 10000;
    	if (firstPerson == true)
    		return ;
    	_zoom += (DistanceInitial - DistanceActuel) / 10000;
    	pCam.lookAt(0,0,0);
    	pCam.update();
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
