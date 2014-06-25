package com.mygdx.radar.android;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Administrateur on 11/04/14.
 */
public class Territory {
    private DataBaseHelper  myDbHelper;
    private Context         myContext;

    private Territory() {
    }
    
    public String getTOTO() {
    	Cursor c = myDbHelper.execSQL("SELECT name FROM sqlite_master WHERE type='table' AND name='stops'");

    	if (c.moveToFirst()) {
    		return c.getString(0);
    	}
    	return ("vache");
    }
    
    public void setContext(Context context) {
        myContext = context;
        this.loadData();
    }

    public static Territory instance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder
    {
        private final static Territory instance = new Territory();
    }

    private void    loadRoutes() {


        Cursor cursor = myDbHelper.execSQL("SELECT * FROM routes");


        Route route = null;
        if (cursor.moveToFirst()) {
            cursor.moveToNext();
            do {
                route = new Route();
                route.route_id = cursor.getString(cursor.getColumnIndex("route_id"));
                route.route_short_name = cursor.getString(cursor.getColumnIndex("route_short_name"));
                route.route_long_name = cursor.getString(cursor.getColumnIndex("route_long_name"));
                World.instance().routes.put(route.route_id, route);
            } while (cursor.moveToNext());
        }

    }

    public List<Stop> getListStopByDistance(double distance, CoordinateGPS position) {
        loadRoutes();
    	Cursor cursor = myDbHelper.execSQL("SELECT * FROM stops");

       
        List<Stop> listStops = new ArrayList<Stop>();
        Stop stop = null;
        if (cursor.moveToFirst()) {
            cursor.moveToNext();
            do {
                stop = new Stop();
                stop.setCoord(new Double(cursor.getString(cursor.getColumnIndex("stop_lat"))), new Double(cursor.getString(cursor.getColumnIndex("stop_lon"))));
                stop.stop_id = cursor.getString(cursor.getColumnIndex("stop_id"));
                stop.stop_name = cursor.getString(cursor.getColumnIndex("stop_name"));
                // Add book to books
                if (distanceAB(stop.coord, position) < distance && stop.stop_id.charAt(0) != 'S') {
                    listStops.add(stop);
                }
            } while (cursor.moveToNext());
        }
        return listStops;
    }

    public Stop getStopById(String id) {
    	
    	Cursor cursor = myDbHelper.execSQL("SELECT * FROM stops WHERE stop_id = " + id);

        Stop stop = new Stop();
        if (cursor.moveToFirst()) {
            stop.stop_id = cursor.getString(cursor.getColumnIndex("stop_id"));
            stop.stop_code = cursor.getString(cursor.getColumnIndex("stop_code"));
            stop.stop_name = cursor.getString(cursor.getColumnIndex("stop_name"));
            stop.stop_desc = cursor.getString(cursor.getColumnIndex("stop_desc"));
            stop.coord.latitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("stop_lat")));
            stop.coord.longitude = Double.parseDouble(cursor.getString(cursor.getColumnIndex("stop_lon")));
            stop.zone_id = cursor.getString(cursor.getColumnIndex("zone_id"));
            stop.stop_url = cursor.getString(cursor.getColumnIndex("stop_url"));
            stop.location_type = cursor.getString(cursor.getColumnIndex("location_type"));
            stop.parent_station = cursor.getString(cursor.getColumnIndex("parent_station"));
            stop.stop_timezone = cursor.getString(cursor.getColumnIndex("stop_timezone"));
            stop.wheelchair_boarding = cursor.getString(cursor.getColumnIndex("weelchair_boarding"));
        }
        return stop;
    }

    public int  serviceException(String idService, Date today) {
        Cursor cursor = myDbHelper.execSQL("SELECT * FROM calendar_dates WHERE service_id = '" + idService +"'");

        if (cursor.moveToFirst()) {
            String str = new String();
            int year, month, day;
            str = cursor.getString(cursor.getColumnIndex("date"));
            year = Integer.valueOf(str.substring(0, 4));
            month = Integer.valueOf(str.substring(4, 6));
            day = Integer.valueOf(str.substring(6, 8));

            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            int year2 = cal.get(Calendar.YEAR);
            int month2 = cal.get(Calendar.MONTH) + 1;
            int day2 = cal.get(Calendar.DAY_OF_MONTH);
            if (year == year2 && month == month2 && day == day2)
                return Integer.valueOf(cursor.getString(cursor.getColumnIndex("exception_type")));
        }
        return 0;
    }

    private boolean dateExpired(String SBegin, String SEnd, Date date) {
        MyDate begin = new MyDate(SBegin);
        MyDate end = new MyDate(SEnd);
        MyDate current = new MyDate(date);

        if (current.isLowerThan(end) && current.isSuperiorThan(begin))
            return false;
        if (current.isSame(begin) || current.isSame(end))
            return false;
        return true;
    }

    public List<StopTimes>	getListStopTimesByStopId(String stop_id) {
    	Cursor cursor = myDbHelper.execSQL("SELECT * FROM stop_times WHERE stop_id = " + stop_id);
        
        List<StopTimes> listStopTimes = new ArrayList<StopTimes>();
        StopTimes stopTimes = null;
        if (cursor.moveToFirst()) {
            cursor.moveToNext();
            do {
                stopTimes = new StopTimes();
                stopTimes.trip_id = cursor.getString(cursor.getColumnIndex("trip_id"));
                stopTimes.arrival_time = new MyTimes(cursor.getString(cursor.getColumnIndex("arrival_time")));
                stopTimes.departure_time = new MyTimes(cursor.getString(cursor.getColumnIndex("departure_time")));
                stopTimes.stop_sequence = cursor.getString(cursor.getColumnIndex("stop_sequence"));
                stopTimes.loadTrip();
                listStopTimes.add(stopTimes);
            } while (cursor.moveToNext());
        }
        return listStopTimes;
    }

   public Trips getTripsByTripId(String trip_id) {
    	Cursor cursor = myDbHelper.execSQL("SELECT * FROM trips WHERE trip_id = " + trip_id);
    	Trips trips = null;
    	
    	if (cursor.moveToFirst()) {
    		trips = new Trips();
    		trips.service_id = cursor.getString(cursor.getColumnIndex("service_id"));
    		trips.route_id = cursor.getString(cursor.getColumnIndex("route_id"));
    		trips.trip_id = cursor.getString(cursor.getColumnIndex("trip_id"));
    		trips.direction_id = cursor.getString(cursor.getColumnIndex("direction_id"));
    	}
    	return trips;
    }
    
    public boolean isServiceAvailableByDate(String idService, Date date) {
        int type = serviceException(idService, date);
        switch (type) {
            case 1:
                return true;
            case 2:
                return false;
            default:
                break;
        }
        Cursor cursor = myDbHelper.execSQL("SELECT * FROM calendar WHERE service_id = '" + idService + "'");

        String available  = new String();
        if (cursor.moveToFirst()) {
            if (dateExpired(cursor.getString(cursor.getColumnIndex("start_date")), cursor.getString(cursor.getColumnIndex("end_date")), date))
                return false;


            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_WEEK);
            if (day == 1) {
                available  = cursor.getString(7);
            } else {
                available  = cursor.getString(day - 1);
            }
        }
        return (available.equals("1"));
    }

    public Path getPath(Date today, String StopId) {
        Path path = new Path();

        return path;
    }

    private void loadData() {
        myDbHelper = new DataBaseHelper(myContext);
        try {
            myDbHelper.createDataBase();

        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

        }
    }

    // lat1, lat2, lon1, lon2 in degrees
    static public double distanceAB(CoordinateGPS A, CoordinateGPS B) {
        double R = 6371; // km
        double dLat = Math.toRadians(B.latitude - A.latitude);
        double dLon = Math.toRadians(B.longitude - A.longitude);
        double lat1 = Math.toRadians(A.latitude);
        double lat2 = Math.toRadians(B.latitude);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d; // Km
    }
}
