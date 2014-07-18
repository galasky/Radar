package com.mygdx.radar.android;

public class LoadStation extends Thread {
    public boolean filtre;

    public LoadStation() {
        filtre = false;
    }

    @Override
    public void run() {
        while (Game3D.instance().modelStation == null || You.instance().coordinate == null)
            ;
        if (filtre) {
            StationManager.instance().unSelectBubble();
            StationManager.instance().filtreDistance();
        }
        else
            StationManager.instance().addListStop(Territory.instance().getListStopByDistance(Config.instance().distance3, You.instance().coordinate));
    }
}
