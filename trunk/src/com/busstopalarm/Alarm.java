/**
 * Author: Pyong Byon
 * Date: 02/25/2010
 * Alarm class
 * 
 */

package com.busstopalarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class Alarm {

	private static final String TAG = "inAlarmClass";
	
	private NotificationManager notificationManager;
	private AlarmManager alarmManager;
	
	private int time;  // time in seconds
	private boolean vibration;
	private Uri ringtoneUri;
	private int proximity;
	private String proximityUnit;
	private Context ctx;
	private boolean alarmSuccessful;
	
	private static final int NOTIFICATION_ID1 = 1001;
	private static final int PENDING_INTENT_REQUEST_CODE1 = 1000001;
	private static final int PENDING_INTENT_REQUEST_CODE2 = 1000002;
	
	
	public Alarm(int time, boolean vibration, Uri ringtoneUri, 
			int proximity, String proximityUnit, Context ctx) {
		
		this.time = setUpTime(time);
		this.vibration = vibration;
		this.ringtoneUri = ringtoneUri;
		this.proximity = setUpProximity(proximity);
		this.proximityUnit = proximityUnit;
		this.ctx = ctx;
		alarmSuccessful = false;
		
		alarmManager = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);
		notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	/**
	 * initializes proximity 
	 * if proximity is less than 0, then it will return 0
	 * @param proximityInput
	 * @return int
	 */
	private int setUpProximity(int proximityInput) {
		if (proximityInput < 0)
		  return 0;
		return proximityInput;
	}

	public boolean getAlarmSuccessful(){
		return alarmSuccessful;
	}
	
	/**
	 * getter for Context
	 * @return Context
	 */
	public Context getContext() {
		return ctx;
	}

	/**
	 * initializes time
	 * if time is less than 0, then it will return 0
	 * @param timeInput
	 * @return int
	 */
	private int setUpTime(int timeInput) {
		if (timeInput < 0)
			return 0;
		return timeInput;
	}


	/**
	 * getter for vibration
	 * @return boolean
	 */
	public boolean getVibration(){
		return vibration;
	}
	
	/**
	 * getter for ringtoneUri
	 * @return Uri
	 */
	public Uri getRingtoneUri(){
		return ringtoneUri;
	}
	
	/**
	 * setter for proximity
	 * @param proximityInput
	 */
	public void setProximity(int proximityInput){
		proximity = setUpProximity(proximityInput);;
	}
	
	/**
	 * getter for proximity
	 * @return proximity
	 */
	public int getProximity(){
		return proximity;
	}
	
	/**
	 * 
	 * getter for proximity unit
	 * @return proximity unit
	 */
	public String getProximityUnit(){
		return proximityUnit;
	}
	
	
	/**
	 * this is for the purpose of updating time
	 *
	 */
	public void setTime(int timeInput){
		time = setUpTime(timeInput);
	}

	/**
	 * getter for time
	 * @return int time
	 */
	public int getTime(){
		return time;
	}


	/**
	 * this method is called when OK Button is pushed in Confirmation page
	 * it sets an alarm in the alarm manager with the pending intent and intent which holds
	 * ringtone and vibrate to be sent over to OneTimeAlarmReceiver
	 * Then, it immediately notifies with notification up on top of the screen
	 * 
	 * 
	 */
	public void setAlarm() {
		alarmSuccessful = false;
		Intent intent = new Intent(ctx, OneTimeAlarmReceiver.class);

		intent.putExtra("Ringtone", ringtoneUri);
		intent.putExtra("Vibration", vibration);
		PendingIntent pendingIntent_alarm = PendingIntent.getBroadcast(ctx, 
				PENDING_INTENT_REQUEST_CODE1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
				(time * 1000), pendingIntent_alarm);
		Notification notification = new Notification(R.drawable.icon, "Bus Stop Alarm is set!",
				System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(ctx, PENDING_INTENT_REQUEST_CODE2, 
				new Intent(ctx, ConfirmationPage.class), PendingIntent.FLAG_CANCEL_CURRENT);

		notification.setLatestEventInfo(ctx, "Bus Stop Alarm", timeConverter(getTime()),
				contentIntent);
		notification.flags = Notification.FLAG_INSISTENT;
		notificationManager.notify(NOTIFICATION_ID1, notification);
		Log.v(TAG, "Alarm is set ");
		Log.v(TAG, "Alarm vibration: " + vibration);
		Log.v(TAG, "Alarm ringtoneUri: " + ringtoneUri);
		Log.v(TAG, "Alarm proximity: " + proximity);
		Log.v(TAG, "Alarm proximityUnit: " + proximityUnit);
		alarmSuccessful = true;
	}
	
	

	/**
	 * this method converts time (remaining) into easily readable format   
	 * 
	 * 
	 * @return String remaining time message
	 */
	public String timeConverter(int time_input) {
		if (time_input < 0)
			return ("timeConverter(): Error! time should not be negative.");
		if (time_input < 60)
			return time_input + " seconds left until alarm goes off";
		if (time_input < 120)
			return "1 minute  " + time_input%60 + " seconds left until alarm goes off";
		if (time_input < 3600) 
			return time_input/60 + " minutes  " + time_input % 60 + " seconds left until alarm goes off";
		else
			return time_input/3600 + " hour(s)  " + (time_input%3600)/60 + " minutes left until alarm goes off";
	}


	
	
}  // ends Alarm class
