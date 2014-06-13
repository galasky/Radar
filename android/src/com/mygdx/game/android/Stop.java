package com.mygdx.game.android;

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
    public List<String> list_trip;

    public CoordinateGPS coord = new CoordinateGPS();

    public Stop(){
    	select = false;
    }

    public void	getListStopTimes() {
    	listStopTimes = Territory.instance().getListStopTimesByStopId(stop_id);
    	refreshNextTime();
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