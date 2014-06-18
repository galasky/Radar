package com.mygdx.radar.android;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyTimes {
	private	String _str;
	public int	minutes;
	public int	hours;
	public MyTimes(String str) {
		String[] words = str.split(":");
		minutes = Integer.parseInt(words[1]);
		hours = Integer.parseInt(words[0]);
		_str = hours + ":" + (minutes <= 9 ? "0" : "") + minutes;
	}
	
	public MyTimes(Date d) {
		GregorianCalendar calendar = new java.util.GregorianCalendar(); 
		calendar.setTime(d); 
		hours = calendar.get(Calendar.HOUR_OF_DAY);
		minutes = calendar.get(Calendar.MINUTE);
	}
	
	public boolean isBeforeTo(Date d) {
		GregorianCalendar calendar = new java.util.GregorianCalendar(); 
		calendar.setTime(d);
		if (calendar.get(Calendar.HOUR_OF_DAY) > hours)
			return true;
		if (calendar.get(Calendar.HOUR_OF_DAY) == hours)
		{
			if (calendar.get(Calendar.MINUTE) > minutes)
				return true;
		}
		return false;
	}
	
	public String getString() {
		return _str;
	}
	
	public int	diff(Date d) {
		GregorianCalendar calendar = new java.util.GregorianCalendar(); 
		// Initialis� � la date et l'heure courrante. 
		calendar.setTime(d);
		int diffHours, diffMinutes;
		
		diffHours = hours - calendar.get(Calendar.HOUR_OF_DAY);
		diffMinutes = minutes - calendar.get(Calendar.MINUTE);		
		return diffHours * 60 + diffMinutes;
	}
	
	public boolean isEqualTo(MyTimes d) {
		return (d.hours == hours && d.minutes == minutes);
	}
	
	public boolean isBeforeTo(MyTimes d) {
		if (d.hours > hours)
			return true;
		if (d.hours == hours)
		{
			if (d.minutes > minutes)
				return true;
		}
		return false;
	}
}
