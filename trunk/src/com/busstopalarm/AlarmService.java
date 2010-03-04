package com.busstopalarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Service that runs in the background and listens to the GPS.
 * When the user gets within the defined proximity, it sets an alarm.
 * 
 * @author David Nufer
 *
 */
public class AlarmService extends Service {
	
	private static final int NOTIFICATION_ID1 = 1001;
	private static final int PENDING_INTENT_REQUEST_CODE1 = 1000001;
	
	private LocationManager lm;
	private Location currentLoc;
	private NotificationManager mNtf;
	private Notification ntf;
	
	private int proximity;
	private String units;
	private BusStop busStop;
	
	/**
	 * setup service managers.
	 */
	public void onCreate() {
		super.onCreate();
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		/*
		 * TODO: by setting minTime and minDistance both to 0, the battery will
		 * drain out really fast.!!!! Set the approriate value here.
		 */
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new AlarmLocationListener());
		mNtf = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		ntf = new Notification(R.drawable.icon, "Alarm Set!", System.currentTimeMillis());
	}
	
	/**
	 * When the service is started, it starts listening to the GPS.
	 */
	public void onStart(Intent intent, int startId) {
		proximity = intent.getIntExtra("proximity", 1);
		units = intent.getStringExtra("units");
		busStop = intent.getParcelableExtra("busstop");
		Uri ringtoneUri = intent.getParcelableExtra("ringtoneUri");
		boolean vibration = intent.getBooleanExtra("vibration", false);
		
		PendingIntent pi = PendingIntent.getBroadcast(
				getApplicationContext(), 0, new Intent(getApplicationContext(), AlarmService.class), 
				PendingIntent.FLAG_UPDATE_CURRENT);
		ntf.setLatestEventInfo(getApplicationContext(), "Bus Stop: " + busStop.getName(), "acquiring location...", pi);
		
		mNtf.notify(NOTIFICATION_ID1, ntf);

		Log.d("ALARMSERVICE", "prox: " + proximity + ", units: " + units + ", stop: " + busStop + ", ringtone: " + ringtoneUri + ", vibration" + vibration);
		
		Intent alarmintent = new Intent(getApplicationContext(), OneTimeAlarmReceiver.class);

		alarmintent.putExtra("ringtoneUri", ringtoneUri);
		alarmintent.putExtra("vibration", vibration);
		PendingIntent pendingIntentAlarm = PendingIntent.getBroadcast(getApplicationContext(), 
				PENDING_INTENT_REQUEST_CODE1, alarmintent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		
		lm.addProximityAlert(busStop.getLatitude(), busStop.getLongitude(), proximity, -1, pendingIntentAlarm);
	}
	
	/**
	 * Service requires that this function must be overridden, but we don't need it.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * {@link AlarmLocationListener} listens to the GPS, and updates the users location
	 * 
	 * @author David Nufer
	 *
	 */
	private class AlarmLocationListener implements LocationListener {
		
		/**
		 * Updates the current Location and notification.
		 */
		public void onLocationChanged(Location location) {
			currentLoc = location;
			Location target = new Location(location);
			target.setLatitude(busStop.getLatitude());
			target.setLongitude(busStop.getLongitude());
			float dist = currentLoc.distanceTo(target);
			
			PendingIntent pi = PendingIntent.getBroadcast(
					getApplicationContext(), 0, new Intent(getApplicationContext(), AlarmService.class), 
					PendingIntent.FLAG_UPDATE_CURRENT);
			
			ntf.setLatestEventInfo(getApplicationContext(), "Bus Stop: " + busStop.getName(), dist + " " + "Meters" + " away", pi); // TODO: convert to correct units
			ntf.when = System.currentTimeMillis();
			mNtf.notify(NOTIFICATION_ID1, ntf);
			Log.d("ALARMSERVICE", "location updated");
		}
	
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
	}

}
