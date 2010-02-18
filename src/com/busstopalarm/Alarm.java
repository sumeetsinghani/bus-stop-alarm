package com.busstopalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Alarm extends Activity {

	// this TAG is for debugging
	private static final String TAG = "inAlarmClass";
	//private static final int NOTIFICATION_ID1 = 100001;
	//private static final int PENDING_INTENT_REQUEST_CODE1 = 1000001;
	//private static final int PENDING_INTENT_REQUEST_CODE2 = 1000002;
	
	private String proximityUnit;
	private boolean vibration;
	private Ringtone ringtone;
	private Uri ringtone_uri;
	private static int time;
	private final Context ctx;
	
	/*
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
*/
	
	// Alarm constructor
	@SuppressWarnings("static-access")
	public Alarm (Context ctx, String proximityUnit, boolean vibration, Ringtone ringtone,
			Uri ringtone_uri ,int time) {
		
		
		  if (ringtone_uri == null) {               
			  ringtone_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			 // ringtone_uri = Uri.parse("file://C:/Users/byonil/Desktop/175379_KeSha_TiK_ToK.mp3");
			 }
		this.ctx = ctx;
		this.proximityUnit = proximityUnit;
		this.vibration = vibration;
		this.ringtone = ringtone;
		this.ringtone_uri = ringtone_uri;
		this.time = time;
	
	}
	
	public void setAlarm() {
		Log.v(TAG, "error here? 1 ");
		Intent intent = new Intent(ctx, OneTimeAlarmReceiver.class);
		
		Log.v(TAG, "error here? 2 ");
		
		intent.putExtra("Ringtone", ringtone_uri);
		intent.putExtra("Vibration", vibration);
		
	
	
	
	/*
		
		Log.v(TAG, "error here? 3 ");
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, PENDING_INTENT_REQUEST_CODE1,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		Log.v(TAG, "error here? 4 ");
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		Log.v(TAG, "error here? 5 ");
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);

		Log.v(TAG, "error here? 6 ");
		NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Log.v(TAG, "error here? 7 ");
		// this notification appears on top of the screen for a short time when you click on the OK Button.
		Notification notification = new Notification(R.drawable.icon, "Bus Stop Alarm is set!", System.currentTimeMillis());
		
		Log.v(TAG, "error here? 8 ");
		// this contentIntent is invoked when you clicked on the notification -> it goes to the confirmation page
		PendingIntent contentIntent = PendingIntent.getActivity(ctx, PENDING_INTENT_REQUEST_CODE2, 
				new Intent(ctx, ConfirmationPage.class), PendingIntent.FLAG_CANCEL_CURRENT);
		
		Log.v(TAG, "error here? 9 ");
		notification.setLatestEventInfo(ctx, "Bus Stop Alarm", timeConverter(), contentIntent);
		notification.flags = Notification.FLAG_INSISTENT;
		
		Log.v(TAG, "error here? 10 ");
		
		// The PendingIntent to launch our activity if the user selects this notification
		manager.notify(NOTIFICATION_ID1, notification);

		Log.v(TAG, "Alarm set ");
		
		Toast.makeText(this, "Bus Stop Alarm is set", Toast.LENGTH_LONG).show();
		
		
		*/
	}
	
	
/*
	
	public static String timeConverter(){
		if (time < 60)
			return time + " seconds left until alarm goes off";
		if (time < 120)
			return "1 minute  " + time%60 + " seconds left until alarm goes off";
		if (time < 3600) 
			return time/60 + " minutes  " + time % 60 + " seconds left until alarm goes off";
		else
			return time/3600 + "hour(s)  " + (time%3600)/60 + " minutes left until alarm goes off";
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
		Alarm.time = time;
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

	
}  // ends Alarm class
