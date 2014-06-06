package com.mygdx.game.android;

import android.util.Log;

public class TestThread extends Thread {
	private long 					diff, start;
	
	public TestThread() {
		start = System.currentTimeMillis();
	}
	
	   public void sleep(int fps) {
	        if(fps>0){
	          diff = System.currentTimeMillis() - start;
	          long targetDelay = 1000/fps;
	          if (diff < targetDelay) {
	            try{
	                Thread.sleep(targetDelay - diff);
	              } catch (InterruptedException e) {}
	            }   
	          start = System.currentTimeMillis();
	        }
	    }
	
	public void run() {
		int	nb= 0;
		
		while (nb < 10000) {
			Log.d("ok",  "galasky " + nb);
			nb++;
		}
	}
	
}