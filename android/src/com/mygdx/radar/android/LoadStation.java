package com.mygdx.radar.android;

/**
 * Created by Administrateur on 01/07/2014.
 */
public class LoadStation extends Thread {
    public LoadStation() {

    }

    @Override
    public void run() {
        StationManager.instance().add(Territory.instance().getListStopByDistance(Config.instance().distance, You.instance().coordinate));
    }
}
