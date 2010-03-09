package com.busstopalarm;

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
 * @author David Nufer, Pyong Byon
 *
 */
public class AlarmService extends Service {

	private static final int NOTIFICATION_ID1 = 1001;
	private static final int PENDING_INTENT_REQUEST_CODE1 = 1000001;
	private static final int PENDING_INTENT_REQUEST_CODE2 = 1000002;
	private static final int MIN_TIME_TO_UPDATE_LOCATION = 5000; // in milliseconds
	private static final int MIN_DISTANCE_TO_UPDATE_LOCATION = 5; // in meters

	/* Conversion factors from yards to meters, and vice versa. */
	private static final double METERS_PER_YARD = 0.9144;
	private static final double YARDS_PER_METER = 1.0936133;

	private LocationManager lm;
	private Location currentLoc;
	private NotificationManager mNtf;
	private Notification ntf;

	private int proximity;
	private String proximityUnit;
	private BusStop busStop;


	/**
	 * Sets up GPS locations and alarms service managers.
	 */
	public void onCreate() {
		super.onCreate();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		/*
		 * TODO: by setting minTime and minDistance both to 0, the battery will
		 * drain out really fast.!!!! Set the approriate value here.
		 */
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_TO_UPDATE_LOCATION, 
				MIN_DISTANCE_TO_UPDATE_LOCATION, new AlarmLocationListener());
		mNtf = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		ntf = new Notification(R.drawable.busstopicon, "Alarm Set!", System.currentTimeMillis());
	}

	/**
	 * When "Cancel" button is pressed on the confirmation page, this method
	 * will be called and cancel the current alarm set and the notification.
	 */
	public void onDestroy(){
		super.onDestroy();
		mNtf.cancel(NOTIFICATION_ID1);
		Intent alarmIntent = new Intent(getApplicationContext(), OneTimeAlarmReceiver.class);
		PendingIntent pendingIntentAlarm = PendingIntent.getBroadcast(getApplicationContext(), 
				PENDING_INTENT_REQUEST_CODE1, alarmIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		pendingIntentAlarm.cancel();
		Log.d("ALARMSERVICE", "current alarm is destroyed");
	}


	/**
	 * When the service is started, it starts listening to the GPS.
	 */
	public void onStart(Intent intent, int startId) {
		proximity = intent.getIntExtra("proximity", 1);
		proximityUnit = intent.getStringExtra("proximityUnit");
		busStop = intent.getParcelableExtra("busstop");
		Uri ringtoneUri = intent.getParcelableExtra("ringtoneUri");
		boolean vibration = intent.getBooleanExtra("vibration", false);

		PendingIntent pi = PendingIntent.getBroadcast(
				getApplicationContext(), PENDING_INTENT_REQUEST_CODE2,
				new Intent(getApplicationContext(), AlarmService.class), 
				PendingIntent.FLAG_UPDATE_CURRENT);
		ntf.setLatestEventInfo(getApplicationContext(), "Bus Stop: " + busStop.getName(),
				"acquiring location...", pi);

		mNtf.notify(NOTIFICATION_ID1, ntf);

		Log.d("ALARMSERVICE", "prox: " + proximity + ", units: " + proximityUnit + ", stop: " + busStop +
				", ringtone: " + ringtoneUri + ", vibration: " + vibration);

		Intent alarmIntent = new Intent(getApplicationContext(), OneTimeAlarmReceiver.class);

		alarmIntent.putExtra("ringtoneUri", ringtoneUri);
		alarmIntent.putExtra("vibration", vibration);
		PendingIntent pendingIntentAlarm = PendingIntent.getBroadcast(getApplicationContext(), 
				PENDING_INTENT_REQUEST_CODE1, alarmIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		float proximityInput = (float) proximity;

		if (proximityUnit.equals("Yards"))
			proximityInput = convertYardsToMeters(proximityInput);

		lm.addProximityAlert(busStop.getLatitude(), busStop.getLongitude(),
				proximityInput, -1, pendingIntentAlarm);

	}

	/** 
	 * Convenience method for converting yards to meters.
	 * @return The given yards in meters. 
	 */
	private static float convertYardsToMeters(float yards){
		return (float) (yards * METERS_PER_YARD);
	}

	/** 
	 * Convenience method for converting meters to yards.
	 * @return The given meters in yards. 
	 */
	private static float convertMetersToYards(float meters){
		return (float) (meters * YARDS_PER_METER);
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
			float dist = currentLoc.distanceTo(target);  // in meters
			if (proximityUnit.equals("Yards"))
				dist = convertMetersToYards(dist);

			PendingIntent pi = PendingIntent.getBroadcast(
					getApplicationContext(), 0, new Intent(getApplicationContext(), AlarmService.class), 
					PendingIntent.FLAG_UPDATE_CURRENT);
			ntf.setLatestEventInfo(getApplicationContext(), "Bus Stop: " + busStop.getName(),
					dist + " " + proximityUnit + " away", pi); // TODO: convert to correct units

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
