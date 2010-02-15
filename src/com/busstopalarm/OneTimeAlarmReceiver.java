package com.busstopalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class OneTimeAlarmReceiver extends BroadcastReceiver {
	private static final String TAG = "inOneTimeAlarmReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
	
		/*
		NotificationManager notificationManger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon, "Bus stop alarm", System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainPage.class), PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(context, "Context Title", "Context text", contentIntent);
	//	notification.flags = Notification.FLAG_INSISTENT;

		notification.sound = (Uri) intent.getParcelableExtra("Ringtone");
		boolean vibrate = (boolean) intent.getBooleanExtra("Vibration", false);
		
	//	if (intent.getParcelableExtra("Vibration"))
			
		if (vibrate)  // if vibrate is checked
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		
		/*  // no vibration
		else {
			long[] vib = new long[0];
			notification.vibrate = vib;
		}
		
		
		notification.flags = Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_SHOW_LIGHTS;
		notification.ledARGB = Color.GREEN;
		notification.ledOnMS = 1000;
		notification.ledOffMS = 500;

		// The PendingIntent to launch our activity if the user selects this notification
		notificationManger.notify(R.drawable.icon, notification);
*/
		
		Log.v(TAG, "does it come here? ");
	
		Toast.makeText(context, "Alarm worked.", Toast.LENGTH_LONG).show();
	}
	


}
