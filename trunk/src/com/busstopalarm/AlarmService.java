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
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {
	
	private static final int NOTIFICATION_ID1 = 1001;
	
	private LocationManager lm;
	private Location currentLoc;
	private NotificationManager mNtf;
	private Notification ntf;
	
	private int proximity;
	private String units;
	private BusStop busStop;
	
	public void onCreate() {
		super.onCreate();
		
		// setup location and notification managers.
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new AlarmLocationListener());
		mNtf = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		ntf = new Notification(R.drawable.icon, "Alarm Set!", System.currentTimeMillis());
	}
	
	public void onStart(Intent intent, int startId) {
		proximity = intent.getIntExtra("proximity", 1);
		units = intent.getStringExtra("units");
		busStop = intent.getParcelableExtra("busstop");
		PendingIntent pi = PendingIntent.getBroadcast(
				getApplicationContext(), 0, new Intent(getApplicationContext(), AlarmService.class), 
				PendingIntent.FLAG_UPDATE_CURRENT);
		ntf.setLatestEventInfo(getApplicationContext(), "Bus Stop: " + busStop.getName(), "acquiring location...", pi);
		
		mNtf.notify(NOTIFICATION_ID1, ntf);
	}
	
	/**
	 * Service requires that this function must be overwritten, but we don't need it.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private class AlarmLocationListener implements LocationListener {
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
