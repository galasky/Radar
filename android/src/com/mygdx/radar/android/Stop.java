package com.mygdx.radar.android;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Stop {

	public List<StopTimes>	listStopTimes;
	public MyTimes	nextTime;
	public boolean	select;
    public String stop_id;
    public String stop_code;
    public String stop_name;
    public String stop_desc;
    public String zone_id;
    public String stop_url;
    public String location_type;
    public String parent_station;
    public String stop_timezone;
    public String wheelchair_boarding;
    public ArrayList<String> list_trip;
    public List<MyTimes>    list_time;

    public CoordinateGPS coord = new CoordinateGPS();

    public Stop(){
    	select = false;
    }

    public void	getListStopTimes() {
    	listStopTimes = Territory.instance().getListStopTimesByStopId(stop_id);
        initListTime();
    }

    public void initListTime() {
        Date d = new Date();

        Iterator<StopTimes> i = listStopTimes.iterator();
        StopTimes stopTimes = null;
        list_time = new ArrayList<MyTimes>();
        while (i.hasNext())
        {
            stopTimes = i.next();
            if (!stopTimes.departure_time.isBeforeTo(d))// && Territory.instance().isServiceAvailableByDate(Territory.instance().getTripsByTripId(stopTimes.trip_id).service_id, _today))
            {
                insertTime(stopTimes.departure_time);
            }
        }
    }

    public void insertTime(MyTimes time) {
        for (int i = 0; i < list_time.size(); i++)
        {
            if (time.isBeforeTo(list_time.get(i)))
            {
                list_time.add(i, time);
                return ;
            }
        }
        list_time.add(time);
    }

    public void refreshListTime() {
        Date d = new Date();

        if (list_time.size() == 0)
            return ;
        if (list_time.get(0).isBeforeTo(d))
        {
            list_time.remove(0);
        }
    }

	public void	refreshNextTime() {
		Date d = new Date();
		MyTimes tmp = null;
		
		Iterator<StopTimes> i = listStopTimes.iterator();
		StopTimes stopTimes = null;
		
		while (i.hasNext())
		{
			stopTimes = i.next();
			if (!stopTimes.departure_time.isBeforeTo(d))// && Territory.instance().isServiceAvailableByDate(Territory.instance().getTripsByTripId(stopTimes.trip_id).service_id, _today))
			{
				if (tmp == null || stopTimes.departure_time.isBeforeTo(tmp))
					tmp = stopTimes.departure_time;
			}
		}
		nextTime =  tmp;
	}
	
    public void setCoord(CoordinateGPS c) {coord.longitude = c.longitude; coord.latitude = c.latitude;}

    public void setCoord(double lat, double lon) {coord.latitude = lat; coord.longitude = lon;}
}