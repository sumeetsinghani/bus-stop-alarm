package com.busstopalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class OneTimeAlarmReceiver extends BroadcastReceiver {
	private static final String TAG = "inOneTimeAlarmReceiver";
	private static final int NOTIFICATION_ID2 = 100002;
	//private static final int PENDING_INTENT_REQUEST_CODE3 = 1000003;
	
	@Override
	public void onReceive(Context context, Intent intent) {
	    
	
	NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		

		// this notification appears on top of the screen for a short time when you click on the OK Button.
		Notification notification = new Notification();
		
		//notification.setLatestEventInfo(getBaseContext(), "Bus Stop Alarm", timeConverter(), contentIntent);
		notification.flags = Notification.FLAG_INSISTENT;
		
		Log.v(TAG, "vibrate " + intent.getBooleanExtra("Vibration", false));
		Log.v(TAG, "ringtone " + intent.getParcelableExtra("Ringtone"));

		notification.sound = (Uri) intent.getParcelableExtra("Ringtone");
		boolean vibration = intent.getBooleanExtra("Vibration", false);
		
		if (vibration)
		  notification.defaults |= Notification.DEFAULT_VIBRATE;
		
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		
		manager.notify(NOTIFICATION_ID2, notification);

		
		
		
		Log.v(TAG, "Alarm is ringing now! ");
	
		Toast.makeText(context, "Hey Wake up! (Alarm is ringing now!)", Toast.LENGTH_LONG).show();
	}
	

}