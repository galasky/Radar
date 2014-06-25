package com.mygdx.radar.android;

public class StopTimes {
    public Trips    trip;
	public String	trip_id;
	public MyTimes	arrival_time;
	public MyTimes	departure_time;
	public String	stop_sequence;

    public StopTimes() {
        trip = null;
    }

    public void loadTrip() {
        trip = Territory.instance().getTripsByTripId(trip_id);
    }
}
