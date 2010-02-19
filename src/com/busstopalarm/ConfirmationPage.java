package com.busstopalarm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ConfirmationPage extends Activity {
	
// this TAG is for debugging
	private static final String TAG = "inConfirmationPage";
    private static final int NOTIFICATION_ID1 = 1001;
	private static final int PENDING_INTENT_REQUEST_CODE1 = 1000001;
	private static final int PENDING_INTENT_REQUEST_CODE2 = 1000002;
	
    
	private BusStop destination;
	private double proximity;
	private String proximityUnit;
	private boolean vibration;
	private Ringtone ringtone;
	private BusRoute currentBusRoute;
	
	private static int time;      // time in seconds
	private Uri ringtone_uri;
	private Alarm alarmObject;
	
	private NotificationManager notificationManager;
	private AlarmManager alarmManager;
	
	// constructor
	public ConfirmationPage() {
		destination = null;
		proximity = 0;
		proximityUnit = "";
		vibration = false;
		ringtone = null;
		currentBusRoute = null;
		time = 10;  // 10 seconds for testing
		ringtone_uri = null;
			
	}
	
	/** Called when the activity is first created.
	 *  
	 *  
	 *  */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
			boolean ifLoadRecentSettings = false;
				try {
					ifLoadRecentSettings = loadRecentSettings();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	
		Log.v(TAG, "check if successfully loaded the recent settings:  " + ifLoadRecentSettings); 
		
		setContentView(R.layout.confirmation);
		
		String stop = getIntent().getStringExtra("name");
		TextView stopView = (TextView) findViewById(R.id.stopname);
		stopView.setText(stop);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

		

		// OK Button confirms the alarm setting
		// it creates Alarm object
		// then, it goes back to MainPage
		final Button OKButton = (Button)findViewById(R.id.OKButton);
		OKButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				
				Intent intent = new Intent(ConfirmationPage.this, OneTimeAlarmReceiver.class);
				
				  if (ringtone_uri == null) {               
					     ringtone_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
					//  ringtone_uri = Uri.parse("file://Z:/cse403/175379_KeSha_TiK_ToK.mp3");  
				  }
				
				intent.putExtra("Ringtone", ringtone_uri);
				intent.putExtra("Vibration", vibration);
				PendingIntent pendingIntent_alarm = PendingIntent.getBroadcast(getBaseContext(), PENDING_INTENT_REQUEST_CODE1,
						intent, PendingIntent.FLAG_CANCEL_CURRENT);
				
			//	AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent_alarm);

			//	NotificationManager manager = (NotificationManager) v.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
				Notification notification = new Notification(R.drawable.icon, "Bus Stop Alarm is set!", System.currentTimeMillis());
				PendingIntent contentIntent = PendingIntent.getActivity(v.getContext(), PENDING_INTENT_REQUEST_CODE2, 
						new Intent(v.getContext(), ConfirmationPage.class), PendingIntent.FLAG_CANCEL_CURRENT);
				
				notification.setLatestEventInfo(v.getContext(), "Bus Stop Alarm", timeConverter(), contentIntent);
				notification.flags = Notification.FLAG_INSISTENT;
				
				//Log.v(TAG, "vibrate " + intent.getBooleanExtra("Vibration", false));
				//Log.v(TAG, "ringtone " + intent.getParcelableExtra("Ringtone"));

				/*
				notification.sound = (Uri) intent.getParcelableExtra("Ringtone");
				if (vibration)
				  notification.defaults |= Notification.DEFAULT_VIBRATE;
				 */
				
				notificationManager.notify(NOTIFICATION_ID1, notification);

				Log.v(TAG, "Alarm is set ");
			
				Toast.makeText(ConfirmationPage.this, "Alarm is set", Toast.LENGTH_LONG).show();
				
				
			//Alarm alarmObject =	new Alarm (ConfirmationPage.this, proximityUnit, vibration, ringtone, ringtone_uri, time);
				//Alarm alarmObject =	new Alarm (proximityUnit, vibration, ringtone, ringtone_uri, time);
				//alarmObject =	new Alarm (ConfirmationPage.this, proximityUnit, vibration, ringtone, ringtone_uri, time);
				//alarmObject.setAlarm(v);
			
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
		         	* Note: The intent and PendingIntent have to be the same as the ones used to create the alarm.
		         	*/
				
				Intent intent = new Intent(ConfirmationPage.this, OneTimeAlarmReceiver.class);
				PendingIntent pendingIntent_alarm = PendingIntent.getBroadcast(getBaseContext(), PENDING_INTENT_REQUEST_CODE1,
						intent, PendingIntent.FLAG_CANCEL_CURRENT);
			//	AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.cancel(pendingIntent_alarm);
                notificationManager.cancel(NOTIFICATION_ID1);
				finish();
			}	
		});
		
	    getVibrate();
		getRingtones();
		getProximity();
		getProximityUnits();
	
	}  // ends onCreate method

	
	// this method loads from res/raw/favorite_settings_data to read the user's 
	// recent settings saved.
	private boolean loadRecentSettings() throws IOException{
		InputStream in = getResources().openRawResource(R.raw.favorite_settings_data);
		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
  		if (bin == null) 
  			return false;
  		String line;
  		String[] setting_result = new String[4];
  		
  		for (int k = 0; k < setting_result.length; k++){
    		line = bin.readLine();
    		setting_result[k] = line;
    	}
  		bin.close();
  		setSettings(setting_result[0], setting_result[1], setting_result[2], 
				   setting_result[3]);
  		return true;
	}

	// this method is for setting the confirmation page with these user pre-defined settings 
	private void setSettings(String data_vibrate, String data_ringtone, String data_proximity,
			String data_proximity_unit) {
		// TODO Auto-generated method stub
		Log.v(TAG, "check from settings_data - vibrate:  "+ data_vibrate);
		Log.v(TAG, "check from settings_data - ringtone:  "+ data_ringtone);
		Log.v(TAG, "check from settings_data - proximity:  "+ data_proximity);
		Log.v(TAG, "check from settings_data - proximity_unit:  "+ data_proximity_unit);
		// set	
	}

	// For distance between two points, we will use Euclidean distance. The Earth is not an Euclidean plane, but this will
	// give a good approximation. Assuming Euclidean plane, this algorithm sums a number of straight line distances.
	// This means the calculated distance will never underestimate the actual distance, which is good.	
	// To calculate the remaining distance once alarm has started, we need to get the current location with the GPS.
	// Then we need to find the closest busstop to the current location (with caveat), then do sum of straight lines again.
	// The return values will be in some unit that will need to be converted to either miles or km. 
	// not implemented yet!
	public static double calculateInitialDistance() {
		// get starting s busstop in busroute
			// will the starting busstop be specified by the user or does the app have to figure it out?
		// get ending d busstop in busroute
		// double dist = 0.0;
		// for (int i = s; i < d; i++) {
		//     dist += calculateDist(busroute[i],busroute[i+1];
		// return dist;
		return 0.0;
	}
	
	public static double calculateRemainingDistance() {
		return 0.0;
	}
	
	// Updates the average speed based on previous average speed and current speed.
	// If implemented like this, we need a average speed field?
	// We could start with an initial average speed (equivalent to 30 mph?) and do a something like
	// avg = k*avg + (1-k)current where 0 <= k <= 1.
	public static void updateAverageSpeed() {
		
	}

	public static String timeConverter() {
		if (time < 0)
			return ("timeConverter(): Error! time should not be negative.");
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
				//	Log.v(TAG, "prox.units - index: " + index_prox);
				//	Log.v(TAG, "prox.units - selectedUnit: " + selectedUnit);
				//	Log.v(TAG, "prox.units - Id: " + arg0.getId());
					
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
	//	Log.v(TAG, "ringtones column count: " + ringtoneCursor.getColumnCount());
		
		String[] ringtoneList = new String[ringtoneCursor.getCount()];
	//	Log.v(TAG, "ringtones row count: " + ringtoneCursor.getCount());
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
			break;
		}
		return super.onOptionsItemSelected(item);


	}
}