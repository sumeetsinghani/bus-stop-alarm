package com.busstopalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class Alarm extends Activity { //extends Service {

	// this TAG is for debugging
	private static final String TAG = "inAlarmClass";
	
	private String proximityUnit;
	private boolean vibration;
	private Ringtone ringtone;
	private Uri ringtone_uri;
	private int time;
	
	/*
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
*/
	
	// Alarm constructor
	public Alarm (String proximityUnit, boolean vibration, Ringtone ringtone,
			Uri ringtone_uri ,int time) {
		
		  if (ringtone_uri == null) {               
			//  ringtone_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			  ringtone_uri = Uri.parse("file://C:/Users/byonil/Desktop/175379_KeSha_TiK_ToK.mp3");
			  
			  }
		
		this.proximityUnit = proximityUnit;
		this.vibration = vibration;
		this.ringtone = ringtone;
		this.ringtone_uri = ringtone_uri;
		this.time = time;
	
	}

	
	public void setAlarm(Context context) {
		
		
		/* to insert in Manifext.xml
		 <receiver android:name=".OneTimeAlarmReceiver"
			    android:enabled="true" >
			        </receiver>
		*/
		
		
		/*
		Intent intent = new Intent(this, RepeatingAlarmReceiver.class);

		intent.putExtra("Ringtone", Uri.parse("file:///sdcard/audiofile.mp3"));
		intent.putExtra("vibrationPatern", new long[] { 200, 300 });
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * 1000), (24 * 60 * 60 * 1000), pendingIntent);

		
		
		
		
		NotificationManager manger = (NotificationManager)     context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon, "Wake up alarm", System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
		notification.setLatestEventInfo(context, "Context Title", "Context text", contentIntent);
		notification.flags = Notification.FLAG_INSISTENT;

		notification.sound = (Uri) intent.getParcelableExtra("Ringtone");
		notification.vibrate = (long[]) intent.getExtras().get("vibrationPatern");

		// The PendingIntent to launch our activity if the user selects this notification
		manger.notify(NOTIFICATION_ID, notification);

		
	
		
		
		
	//	PendingIntent pendingIntent = PendingIntent.getBroadcast(Activity.this, 0, intent, Intent.FLAG_GRANT_READ_URI_PERMISSION);
		
	//	NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		
		Intent intent = new Intent(this, OneTimeAlarmReceiver.class);
		intent.putExtra("Ringtone", ringtone_uri);
		intent.putExtra("Vibration", vibration);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        
		Log.v(TAG, "vibrate " + intent.getBooleanExtra("Vibration", false));
		Log.v(TAG, "ringtone " + intent.getParcelableExtra("Ringtone"));
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);
		
		Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show();
		
		*/
		
	}
	
	
	
	
/*
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
*/

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


	public void setTime(int time) {
		this.time = time;
	}


	public int getTime() {
		return time;
	}


	public void setRingtone_uri(Uri ringtone_uri) {
		this.ringtone_uri = ringtone_uri;
	}


	public Uri getRingtone_uri() {
		return ringtone_uri;
	}


	

	/*
	public void setCurrentBusRoute(BusRoute currentBusRoute) {
		this.currentBusRoute = currentBusRoute;
	}


	public BusRoute getCurrentBusRoute() {
		return currentBusRoute;
	}
	
	*/
	
	
  /*
	Intent intentAlarm = new Intent(this, RepeatingAlarmReceiver.class);
	PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intentAlarm, 0);

	AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
	alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * 1000), 10 * 1000, pendingIntent);
	Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show();
	*/
	
	
}  // ends Alarm class
