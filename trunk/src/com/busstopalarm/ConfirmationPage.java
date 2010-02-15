package com.busstopalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class ConfirmationPage extends Activity {
	
// this TAG is for debugging
	private static final String TAG = "inConfirmationPage";
	private static final int NOTIFICATION_ID = 111;
	private static final int PENDING_INTENT_REQUEST_CODE = 1234567;
	
    
	private BusStop destination;
	private double proximity;
	private String proximityUnit;
	private boolean vibration;
	private Ringtone ringtone;
	private BusRoute currentBusRoute;
	private static int time;      // time in seconds
	private Uri ringtone_uri;
	
	//private NotificationManager mManager;
	
	// constructor
	public ConfirmationPage() {
		destination = null;
		proximity = 0;
		proximityUnit = "";
		vibration = false;
		ringtone = null;
		currentBusRoute = null;
		time = 4;  // 4 seconds for testing
		ringtone_uri = null;
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmation);
		
		String stop = getIntent().getStringExtra("name");
		TextView stopView = (TextView) findViewById(R.id.stopname);
		stopView.setText(stop);

		
		// mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		
		// OK Button confirms the alarm setting
		// it creates Alarm object
		// then, it goes back to MainPage
		final Button OKButton = (Button)findViewById(R.id.OKButton);
		OKButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
			// call Calculate(destination, currentBusRoute, proximity, proximityUnit)
				/*
				Intent intent = new Intent(ConfirmationPage.this, OneTimeAlarmReceiver.class);
				intent.putExtra("Ringtone", ringtone_uri);
				intent.putExtra("Vibration", vibration);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		        
				
				
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);
				
				
				*/
				
	
				
				Intent intent = new Intent(ConfirmationPage.this, OneTimeAlarmReceiver.class);

				
				  if (ringtone_uri == null) {               
						//  ringtone_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
						  ringtone_uri = Uri.parse("file://Z:/cse403/175379_KeSha_TiK_ToK.mp3");
						  
						  }
				
				intent.putExtra("Ringtone", ringtone_uri);
				//intent.putExtra("vibrationPatern", new long[] { 200, 300 });
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), PENDING_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);

				NotificationManager manager = (NotificationManager) v.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
				Notification notification = new Notification(R.drawable.icon, "Bus Stop Alarm is set!", System.currentTimeMillis());
				PendingIntent contentIntent = PendingIntent.getActivity(v.getContext(), 0, new Intent(v.getContext(), ConfirmationPage.class), PendingIntent.FLAG_CANCEL_CURRENT);
				notification.setLatestEventInfo(v.getContext(), "Bus Stop Alarm Title", timeConverter(), contentIntent);
				notification.flags = Notification.FLAG_INSISTENT;
				
				//Log.v(TAG, "vibrate " + intent.getBooleanExtra("Vibration", false));
				Log.v(TAG, "ringtone " + intent.getParcelableExtra("Ringtone"));

				notification.sound = (Uri) intent.getParcelableExtra("Ringtone");
				if (vibration)
				  notification.defaults |= Notification.DEFAULT_VIBRATE;

				// The PendingIntent to launch our activity if the user selects this notification
				manager.notify(NOTIFICATION_ID, notification);

				Log.v(TAG, "Alarm set ");
				
				
				
			//	Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show();
				
				
				/*
				Intent intent = new Intent(ConfirmationPage.this, MainPage.class);
				
				Notification notification = new Notification(R.drawable.icon,
		                	"Notify", System.currentTimeMillis());
		        	notification.setLatestEventInfo(ConfirmationPage.this, "App Name", "Description of the notification",
			                PendingIntent.getActivity(ConfirmationPage.this.getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT));
			        mManager.notify(111, notification);
				*/
	
				
	//		Alarm alarmObject =	new Alarm (proximityUnit, vibration, ringtone, ringtone_uri, time);
		//	alarmObject.setAlarm(v.getContext());
			
			//Log.v(TAG, "Alarm destination: " + alarmObject.getDestination());
			//Log.v(TAG, "Alarm proximity: " + alarmObject.getProximity());
	//		Log.v(TAG, "Alarm proximityUnit: " + alarmObject.getProximityUnit());
	//		Log.v(TAG, "Alarm vibration: " + alarmObject.isVibrate());
	//		Log.v(TAG, "Alarm ringtone: " + alarmObject.getRingtone());
	//		Log.v(TAG, "Alarm ringtone_uri: " + alarmObject.getRingtone_uri());
			//Log.v(TAG, "Alarm currentBusRoute: " + alarmObject.getCurrentBusRoute());
			
			startActivity(new Intent(v.getContext(), MainPage.class));
			finish();
			}
			
		});
		
		
		// cancel button cancels the current alarm set
		final Button CancelButton = (Button)findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				/* To show how alarms are canceled we will create a new Intent and a new PendingIntent with the
			 	* same requestCode as the PendingIntent alarm we want to cancel. In this case, it is PENDING_INTENT_REQUEST_CODE.
		         	* Note: The intent and PendingIntent have to be the same as the ones used to create the alarms.
		         	*/
				Intent intent1        = new Intent(ConfirmationPage.this, OneTimeAlarmReceiver.class);
				PendingIntent sender1 = PendingIntent.getBroadcast(getBaseContext(), PENDING_INTENT_REQUEST_CODE, intent1, 0);
				AlarmManager am1 = (AlarmManager) getSystemService(ALARM_SERVICE);
				am1.cancel(sender1);
               
				
				finish();
			}	
		});
		
		
	
	    getVibrate();
		getRingtones();
		getProximity();
		getProximityUnits();
		
	
	}  // ends onCreate method

	
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
	

	public void getVibrate(){
		
		final CheckBox vib = (CheckBox) findViewById(R.id.VibrateCheckbox);
		//final Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vib.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
						
				if (isChecked) { // vibrate is checked
					//	vib.setChecked(false);    
			       //buttonView.setBackgroundResource(R.drawable.black);
				  //   vibrator.vibrate(5000);  // 5000 milliseconds = 5 seconds
					vibration = true;
				}
				if (!isChecked) {
				  //  buttonView.setBackgroundColor(R.drawable.icon);
                	// vibrator.cancel();
					vibration = false;
				}	
					
			}
			});
		
	}  // ends getVibrate method
	
	
	public void getProximity() {
		final SeekBar proximitySeekBar = (SeekBar) findViewById(R.id.ProximityBar);
		proximitySeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
			//  arg0.get
				// proximity = ...;
				
			}
			
			
			
			
		});
		
		
		
		
	} // ends getProximity method
	
	public void getProximityUnits() {
		Spinner proximityUnitsSpinner = (Spinner) findViewById(R.id.ProximityUnits);
		ArrayAdapter<CharSequence> proxSpinnerValues = ArrayAdapter.createFromResource(this, R.array.ProximityUnitList, android.R.layout.simple_spinner_item);
		proxSpinnerValues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		proximityUnitsSpinner.setAdapter(proxSpinnerValues);
		
		proximityUnitsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() { 
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					int index_prox = arg0.getSelectedItemPosition();
					CharSequence selectedUnit = (CharSequence) arg0.getSelectedItem();
					selectedUnit.toString();
					proximityUnit = (String) selectedUnit;
					Log.v(TAG, "prox.units - index: " + index_prox);
					Log.v(TAG, "prox.units - selectedUnit: " + selectedUnit);
					Log.v(TAG, "prox.units - Id: " + arg0.getId());
					
					//ringtoneCursor.get
					// adaptor.set..
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}

		
			  });
		
		
		
		
	}  // ends getProximityUnits method
	
	
	public void getRingtones() {
		
		final RingtoneManager ringtoneManager = new RingtoneManager(this);
		
		// get all types of sounds (ringtones, notifications, alarms)
		ringtoneManager.setType(RingtoneManager.TYPE_ALL); 
		
		//Ringtone ringtone = ringtoneManager.getRingtone(0);
		Cursor ringtoneCursor = ringtoneManager.getCursor();
		Log.v(TAG, "ringtones column count: " + ringtoneCursor.getColumnCount());
		
		String[] ringtoneList = new String[ringtoneCursor.getCount()];
		Log.v(TAG, "ringtones row count: " + ringtoneCursor.getCount());
		for (int i = 0; i < ringtoneCursor.getCount(); i++) {
		  ringtoneList[i] = ringtoneCursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
		  ringtoneCursor.moveToNext(); 
		}
	
		//String ringtoneList = ringtone.getTitle(this);	
		// = ringtoneCursor.getColumnNames();

		Spinner ringtoneSpinner = (Spinner) findViewById(R.id.RingtoneSelector);
		ArrayAdapter<String> ringtoneAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ringtoneList);
		//ringtoneAdapter.add(ringtoneCursor.getColumnCount() + "");
		ringtoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ringtoneSpinner.setAdapter(ringtoneAdapter);
		
		  ringtoneSpinner.setOnItemSelectedListener(new OnItemSelectedListener() { 

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//int index_ringtone = arg0.getId();
				//ringtoneCursor.get()
				// adaptor.set..
				int index_ringtone = arg0.getSelectedItemPosition();
				ringtone_uri = ringtoneManager.getRingtoneUri(index_ringtone);
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

	
		  });
		
	
		
	}  // ends getRingtones method

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0,1,1, "Exit");
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			finish();
		}
		return super.onOptionsItemSelected(item);


	}
}