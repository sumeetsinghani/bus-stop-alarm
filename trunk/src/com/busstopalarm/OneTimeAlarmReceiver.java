/**
 * Author: Pyong Byon
 * Date: 02/23/2010
 * 
 * OneTimeAlarmReceiver class which extends BroadcastReceiver to alert the user by notifying
 * with the selected ringtone / vibrate when it receives the signal from the system
 * It wakes up the phone if the phone is sleeping
 * if given ringotne is null, it does not ring
 * if given vibrate is null, it does not vibrate
 * TODO: alarm update consistently
 * 		    
 */


package com.busstopalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class OneTimeAlarmReceiver extends BroadcastReceiver {

	// this TAG is for debugging
	private static final String TAG = "inOneTimeAlarmReceiver";

	private static final int NOTIFICATION_ID2 = 1002;
	//private static final int PENDING_INTENT_REQUEST_CODE3 = 1000003;

 // this variable is for testing
	private boolean ifSuccessful;
	
	public OneTimeAlarmReceiver(){
		ifSuccessful = false; 
	}
	
	
	
	/**
	 * this is called when the BroadcastReceiver receives the intent from the system
	 * it alerts the user with the notification (possibly with ringtone and vibrate)\
	 * if it successfully goes through this method it will set ifSuccessful to true.
	 * @param context in which the work is being done
	 * @param intent which contains what to do
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
        ifSuccessful = false;
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		// this notification appears on top of the screen for a short time when you click on the OK Button.
		Notification notification = new Notification();

		//notification.setLatestEventInfo(getBaseContext(), "Bus Stop Alarm", timeConverter(), contentIntent);
		notification.flags = Notification.FLAG_INSISTENT;

		Log.v(TAG, "vibrate " + intent.getBooleanExtra("Vibration", false));
		Log.v(TAG, "ringtone " + intent.getParcelableExtra("Ringtone"));

		Uri ringtoneUri = (Uri) intent.getParcelableExtra("Ringtone");
		if (ringtoneUri != null)
			notification.sound = ringtoneUri;

		boolean vibration = intent.getBooleanExtra("Vibration", false);

		if (vibration)
			notification.defaults |= Notification.DEFAULT_VIBRATE;

		notification.defaults |= Notification.DEFAULT_LIGHTS;
		manager.notify(NOTIFICATION_ID2, notification);
		Log.v(TAG, "Alarm is ringing now! ");
		Toast.makeText(context, "Hey Wake up! (Alarm is ringing now!)", Toast.LENGTH_LONG).show();
		ifSuccessful = true;
	}
	
	
	public boolean getIfSuccessful() {
		return ifSuccessful;
	}
	
	


} // class ends
