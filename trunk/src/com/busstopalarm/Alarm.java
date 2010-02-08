package com.busstopalarm;

import android.media.Ringtone;

public class Alarm {

	private BusStop destination;
	private double proximity;
	private String proximityUnit;
	private boolean vibration;
	private Ringtone ringtone;
	private BusRoute currentBusRoute;
	
	
	// Alarm constructor
	public Alarm (BusStop destination,  double proximity,  String proximityUnit,
	 boolean vibration,  Ringtone ringtone,  BusRoute currentBusRoute) {
		this.setDestination(destination);
		this.setProximity(proximity);
		this.setProximityUnit(proximityUnit);
		this.setVibration(vibration);
		this.setRingtone(ringtone);
		this.setCurrentBusRoute(currentBusRoute);
		
		
	}


	public void setDestination(BusStop destination) {
		this.destination = destination;
	}


	public BusStop getDestination() {
		return destination;
	}


	public void setProximity(double proximity) {
		this.proximity = proximity;
	}


	public double getProximity() {
		return proximity;
	}


	public void setProximityUnit(String proximityUnit) {
		this.proximityUnit = proximityUnit;
	}


	public String getProximityUnit() {
		return proximityUnit;
	}


	public void setVibration(boolean vibration) {
		this.vibration = vibration;
	}


	public boolean isVibrate() {
		return vibration;
	}


	public void setRingtone(Ringtone ringtone) {
		this.ringtone = ringtone;
	}


	public Ringtone getRingtone() {
		return ringtone;
	}


	public void setCurrentBusRoute(BusRoute currentBusRoute) {
		this.currentBusRoute = currentBusRoute;
	}


	public BusRoute getCurrentBusRoute() {
		return currentBusRoute;
	}
	
	
	
	
	/*
	Intent intentAlarm = new Intent(this, RepeatingAlarmReceiver.class);
	PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intentAlarm, 0);

	AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
	alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * 1000), 10 * 1000, pendingIntent);
	Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show();
	*/
	
	
	
}  // ends Alarm class
