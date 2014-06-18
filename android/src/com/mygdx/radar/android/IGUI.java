package com.mygdx.radar.android;

public interface IGUI {
	public IGUI	invert();
	public void	refresh();
	public void touch(float x, float y, float deltaX, float deltaY);
	public void tap(float x, float y);
	public void render();
}
