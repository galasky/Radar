package com.mygdx.radar.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.WindowManager;


public class AndroidLauncher extends AndroidApplication implements LocationListener {

    private LocationManager lm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        cfg.useCompass = true;
        Territory territory = Territory.instance();
        territory.setContext(this);
        initialize(Game3D.instance(), cfg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,
                    this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0,
                this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lm.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        Log.d("ok", "longitude = " + location.getLongitude() + " latitude = " + location.getLatitude());
        if (!World.instance().loadListStop.loaded) {
            World.instance().loadListStop.location = location;
        }
        else
            You.instance().setPosition(location);
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }
}