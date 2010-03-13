/**
 * Author: Pyong Byon
 * Date: 02/23/2010
 * 
 * OneTimeAlarmReceiver class which extends BroadcastReceiver to alert the user
 * by notifying with the selected ringtone / vibrate when it receives the signal
 * from the system. It wakes up the phone if the phone is sleeping.
 * If given ringotne is null, it does not ring.
 * If given vibrate is null, it does not vibrate.
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

 // this variable is for testing whether it successfully goes through
 // the method onCreate and alarm goes off.
	private boolean ifSuccessful;
	
	
	/**
	 * OneTimeAlarmReceiver constructor 
	 * initializes the field ifSuccessful to false
	 * since it has not gone through the method onCreate and succeeded
	 * alarming yet.
	 */
	public OneTimeAlarmReceiver(){
		ifSuccessful = false; 
	}
	
	
	/**
	 * This is called when the BroadcastReceiver receives the signal and the 
	 * intent from the system (when the device gets within the defined
	 * proximity. It alerts the user with the notification (possibly with the
	 * user-defined ringtone and vibrate) and toasts a message.
	 * If it successfully goes through this method, then
	 * it will set ifSuccessful to true (for testing purpose).
	 * @param context in which the work is being done
	 * @param intent which contains what to do
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
        ifSuccessful = false;
		NotificationManager manager = 
			(NotificationManager)context.getSystemService(
					Context.NOTIFICATION_SERVICE);

		// this notification appears on top of the screen for a short time
		// when you click on the OK Button.
		Notification notification = new Notification();
		notification.flags = Notification.FLAG_INSISTENT;

		// Logs for debugging purpose
		Log.v(TAG, "vibrate " + intent.getBooleanExtra("vibration", false));
		Log.v(TAG, "ringtone " + intent.getParcelableExtra("ringtoneUri"));

		Uri ringtoneUri = (Uri) intent.getParcelableExtra("ringtoneUri");
		if (ringtoneUri != null)
			notification.sound = ringtoneUri;

		boolean vibration = intent.getBooleanExtra("vibration", false);

		if (vibration)
			notification.defaults |= Notification.DEFAULT_VIBRATE;

		notification.defaults |= Notification.DEFAULT_LIGHTS;
		
		manager.notify(NOTIFICATION_ID2, notification);
		Log.v(TAG, "Alarm is ringing now! ");
		Toast.makeText(context, "Hey Wake up! (Alarm is ringing now!)",
				Toast.LENGTH_LONG).show();
		
		context.stopService(new Intent(context, AlarmService.class));
		ifSuccessful = true;
	}
	
	
	/**
	 * getter for the field ifSuccessful.
	 * @return boolean (true if alarm successfully goes off) 
	 */
	public boolean getIfSuccessful() {
		return ifSuccessful;
	}
	
	


} // class ends
