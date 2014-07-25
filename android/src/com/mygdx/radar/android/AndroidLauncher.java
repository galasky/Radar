package com.mygdx.radar.android;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AndroidLauncher extends AndroidApplication implements LocationListener {

    private LocationManager lm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        try{
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), 0);
            ZipFile zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("META-INF/MANIFEST.MF");
            long time = ze.getTime();
            SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
            formatter.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
            Config.instance().dateBuild = formatter.format(new java.util.Date(time));
            zf.close();
        }catch(Exception e){
        }

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
    public void onBackPressed() {
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