package com.mygdx.radar.android;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrateur on 01/07/2014.
 */
public class LoadStation extends Thread {
    public boolean filtre;

    public LoadStation() {
        filtre = false;
    }



    @Override
    public void run() {
        while (Game3D.instance().modelStation == null || You.instance().coordinate == null)
            ;

        /*List<Station>  liststation = new ArrayList<Station>();
        Station station = new Station();
        station.name = "Alfred Nobel";
        station.coord = new CoordinateGPS();
        station.coord.latitude = 43.6164;
        station.coord.longitude = 3.91078;
//        Stop stop = new Stop
  //      station.stops.add()
        liststation.add(station);

        Log.d("ok", "station EPZFEP¨FZKPK");*/

        //StationManager.instance().addListStation(liststation);
       // You.instance().updateFloor();
        //Game3D.instance().instances.clear();
       // You.instance().updateFloor();

        if (filtre)
            StationManager.instance().filtreDistance();
        else
            StationManager.instance().add(Territory.instance().getListStopByDistance(Config.instance().distance3, You.instance().coordinate));
    }
}
