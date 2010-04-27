/**
 * Service that runs in the background and listens to the GPS.
 * When the user gets within the defined proximity, it sets an alarm.
 * 
 * @author Orkhan Muradov, Pyong Byon
 *
 */

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

public class AlarmService extends Service {

	private static final int NOTIFICATION_ID1 = 1001;
	private static final int PENDING_INTENT_REQUEST_CODE1 = 1000001;
	private static final int PENDING_INTENT_REQUEST_CODE2 = 1000002;
	// This is given in milliseconds
	private static final int MIN_TIME_TO_UPDATE_LOCATION = 5000; 
	// This is given in meters
	private static final int MIN_DISTANCE_TO_UPDATE_LOCATION = 5; 

	/* Conversion factors from yards to meters, and vice versa. */
	private static final float METERS_PER_YARD = 0.9144f;
	private static final float YARDS_PER_METER = 1.0936133f;

	private static final float METERS_PER_KILOMETER = 1000f;
	private static final float YARDS_PER_MILE = 1760f;
	
	private LocationManager lm;
	private Location currentLoc;
	private NotificationManager mNtf;
	private Notification ntf;

	private int proximity;
	private String proximityUnit;
	
	private float distanceToShow;
	private String unitToShow;
	
	private BusStop busStop;
    

	/**
	 * Sets up GPS locations and alarms service managers.
	 */
	public void onCreate() {
		super.onCreate();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
				MIN_TIME_TO_UPDATE_LOCATION, MIN_DISTANCE_TO_UPDATE_LOCATION, 
				new AlarmLocationListener());

		mNtf = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		ntf = new Notification(R.drawable.busstopicon, "Alarm Set!", 
				System.currentTimeMillis());
	}

	/**
	 * When "Cancel" button is pressed on the confirmation page, this method
	 * will be called and cancel the current alarm set and the notification.
	 */
	public void onDestroy(){
		super.onDestroy();
		mNtf.cancel(NOTIFICATION_ID1);

		Intent alarmIntent = new Intent(getApplicationContext(), 
				OneTimeAlarmReceiver.class);

		PendingIntent pendingIntentAlarm = PendingIntent.getBroadcast(
				getApplicationContext(), PENDING_INTENT_REQUEST_CODE1, 
				alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		pendingIntentAlarm.cancel();

		Log.d("ALARMSERVICE", "current alarm is destroyed");

		// this is for performance testing, (author: Pyong Byon)
		//Debug.stopMethodTracing();

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
		ntf.setLatestEventInfo(getApplicationContext(), 
				"Bus Stop: " + busStop.getName(),
				"acquiring location...", pi);

		mNtf.notify(NOTIFICATION_ID1, ntf);

		// testing purpose
//		Log.d(LOG, "proximity: " + proximity);
//		Log.d(LOG, "proximityUnit: " + proximityUnit);
//		Log.d(LOG, "busStop: " + busStop);
//		Log.d(LOG, "ringtoneUri: " + ringtoneUri);
//		Log.d(LOG, "vibration: " + vibration);

		Intent alarmIntent = new Intent(getApplicationContext(), 
				OneTimeAlarmReceiver.class);

		alarmIntent.putExtra("ringtoneUri", ringtoneUri);
		alarmIntent.putExtra("vibration", vibration);
		PendingIntent pendingIntentAlarm = PendingIntent.getBroadcast(
				getApplicationContext(), PENDING_INTENT_REQUEST_CODE1, 
				alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

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
	 * Service requires that this function must be overridden, but we don't 
	 * need it.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * {@link AlarmLocationListener} listens to the GPS, and updates the 
	 * user's location
	 * 
	 * @author David Nufer
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
			distanceToShow = currentLoc.distanceTo(target);  // in meters
			
			if (proximityUnit.equals("Yards"))
				convertBetweenYardsMiles();
			else // proximityUnit.equals("Meters")
                convertBetweenMetersKilometers();
				
			PendingIntent pi = PendingIntent.getBroadcast(
					getApplicationContext(), 0, 
					new Intent(getApplicationContext(), AlarmService.class), 
					PendingIntent.FLAG_UPDATE_CURRENT);

			ntf.setLatestEventInfo(getApplicationContext(), 
					"Bus Stop: " + busStop.getName(), distanceToShow + " " + 
					unitToShow + " away", pi); 

			ntf.when = System.currentTimeMillis();
			mNtf.notify(NOTIFICATION_ID1, ntf);
			Log.d("ALARMSERVICE", "location updated");
		}
		

		/**
		 * It converts units between Meters and Kilometers according to the
		 * remaining distance to the destination. 
		 * If distance is greater than 500 Meters, it changes it to Kilometers
		 * for a better readability.
		 * Otherwise it is viewed in Meters. (when destination is near). 
		 */
		private void convertBetweenMetersKilometers() {
			if (distanceToShow > 500) {
			  distanceToShow = distanceToShow / METERS_PER_KILOMETER;
			  unitToShow = "Kilometers";
			} else {
			  unitToShow = "Meters";
			}
		}

		/**
		 * It converts units between Yards and Miles according to the
		 * remaining distance to the destination. 
		 * If distance is greater than 500 Yards, it changes it to Miles
		 * for a better readability.
		 * Otherwise it is viewed in Yards. (when destination is near). 
		 */
		private void convertBetweenYardsMiles() {
			distanceToShow = convertMetersToYards(distanceToShow);
			if (distanceToShow > 500) {
				  distanceToShow = distanceToShow / YARDS_PER_MILE;
				  unitToShow = "Miles";
				} else {
				  unitToShow = "Yards";
				}
			}
			

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, 
				Bundle extras) {
		}
	}

}
