/**
 * Author: Pyong Byon, Orkhan Muradov, David Nufer
 * Date: 03/01/2010
 * 
 * Confirmation Page where the user interacts with this page to 
 * set the alarm settings: vibrate, ringtone, proximity, and proximityUnit
 * and to set the alarm ("OK Button") with those settings defined in this page.
 * When OK Button is pressed, it will set alarm service running in the 
 * background to alert the user on time.
 * The user can save the settings as a favorite by pressing the "Save as Fav"
 * button.
 * The user can also cancel the current alarm and go back to the main page by
 * pressing the "Cancel" button.
 */

package com.busstopalarm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.NotificationManager;
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
	//private static final int PENDING_INTENT_REQUEST_CODE2 = 1000002;

	private boolean vibration;
	private Uri ringtoneUri;
	private int proximity;
	private String proximityUnit;

	/* these are not used yet
	private BusStop destination;
	private BusRoute currentBusRoute;
	 */

	// these below are the data saved in the "favorite_settings_data" file in
	// sdcard to be retrieved from the file to load the recent settings
	private String dataVibrate;
	private String dataRingtone;
	private String dataProximity;
	private String dataProximityUnit;

	private String ringtoneTitleToSave;

	// time (in seconds) is used for Alarm, alarm goes off after time seconds
	private int time;      

	private NotificationManager notificationManager;

	/**
	 * ConfirmationPage constructor
	 */
	public ConfirmationPage() {
		dataVibrate = null;
		dataRingtone = null;
		dataProximity = null;
		dataProximityUnit = null;

		vibration = false;
		ringtoneUri = null;
		proximity = 0;
		proximityUnit = null;

		ringtoneTitleToSave = null;

		/* these are not used yet
		currentBusRoute = null;
		destination = null;
		 */

		time = 10;  // 10 seconds for testing
	}


	/**
	 * this is for the purpose of updating time
	 *
	 */
	public void setTime(int time_input){
		time = time_input;
	}


	/**
	 * getter for time
	 * @return int time
	 */
	public int getTime(){
		return time;
	}


	/**
	 * vibrate setter
	 * @param boolean vibrate input
	 */
	public void setVibration(boolean vibrate_input){
		vibration = vibrate_input;
	}


	/**
	 * getter for vibration
	 * @return boolean vibration
	 */
	public boolean getVibration(){
		return vibration;
	}


	/**
	 * setter for ringtone uri
	 * @param Uri ringtone uri
	 */
	public void setRingtoneUri(Uri ringtone_input){
		ringtoneUri = ringtone_input;
	}


	/**
	 * getter for ringtone uri
	 * @return Uri ringtone uri
	 */
	public Uri getRingtoneUri(){
		return ringtoneUri;
	}


	/**
	 * it gets the default proximity from the file
	 * "favorite_settings_data" that holds the data saved when the user
	 * saved settings as a favorite
	 * 
	 * @return proximity value (int)
	 */
	private int default_proximity() {
		if (dataProximity == null)
			return 0;
		return Integer.parseInt(dataProximity);
	}


	/** 
	 * it gets the default proximityUnit from the file
	 * "favorite_settings_data" that holds the data saved when the user
	 * saved settings as a favorite
	 * @return String proximity unit
	 */
	private String default_proximity_unit() {
		return dataProximityUnit;
	}


	/** 
	 * it gets the default vibrate from the file "favorite_settings_data" which
	 * holds the data saved when the user saved the settings as a favorite
	 * @return false if dataVibrate is null (settings data haven't been created)
	 * @return false if dataVibrate is non-null and is "vibrate_false" 
	 * @return true if dataVibrate is non-null and is "vibrate"
	 */
	private boolean default_vibrate(){
		if (dataVibrate != null && dataVibrate.equals("vibrate"))
			return true;
		return false;	
	}


	/** Called when the activity is first created on the confirmation page.
	 *  @param Bundle which holds the current state (info)
	 *  
	 *  */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		notificationManager = (NotificationManager) 
		getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

		try {  // load saved settings
			loadRecentSettings();
		} catch (IOException e1) { // if the file "favorite_settings_data" 
			e1.printStackTrace();  // is not found
		} 

		vibration = default_vibrate();
		//ringtoneUri = default_rington_uri();
		proximity = default_proximity();
		proximityUnit = default_proximity_unit();

		setContentView(R.layout.confirmation);
		BusStop stop = getIntent().getParcelableExtra("busstop");
		TextView stopView = (TextView) findViewById(R.id.stopname);
		stopView.setText(stop.getName());

		okButton();
		cancelButton();
		saveButton();
		getVibrate();
		getRingtones();
		getProximity();
		getProximityUnits();

		// Logs for debugging purpose.
		Log.v(TAG, "dataVibrate:  " + dataVibrate);
		Log.v(TAG, "dataRingtone:  " + dataRingtone);
		Log.v(TAG, "dataProximity:  " + dataProximity);
		Log.v(TAG, "dataProximityUnit:  " + dataProximityUnit);

		Log.v(TAG, "vibrate:  " + vibration);
		Log.v(TAG, "ringtone:  " + ringtoneUri);
		Log.v(TAG, "proximity:  " + proximity);
		Log.v(TAG, "proximityUnit:  " + proximityUnit);
	}  // ends onCreate method


	/**
	 *  OK Button confirms the alarm setting
	 *  it calls alarm service to set alarm
	 *  after creating alarm set, it goes back to MainPage
	 *  
	 */
	private void okButton() {
		final Button OKButton = (Button)findViewById(R.id.OKButton);
		OKButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				//Alarm alarmObject = new Alarm(time, vibration, ringtoneUri,
				//proximity, proximityUnit, ConfirmationPage.this);
				//alarmObject.setAlarm();
				
				BusStop b = getIntent().getParcelableExtra("busstop");
				Intent intentAlarmService = new Intent(v.getContext(), AlarmService.class);
				intentAlarmService.putExtra("proximity", proximity);
				intentAlarmService.putExtra("proximityUnit", proximityUnit);
				intentAlarmService.putExtra("busstop", b);
				intentAlarmService.putExtra("vibration", vibration);
				intentAlarmService.putExtra("ringtoneUri", ringtoneUri);
				startService(intentAlarmService);

				Toast.makeText(ConfirmationPage.this, "Alarm is set", 
						Toast.LENGTH_LONG).show();
				Intent intentToMainPage = new Intent(ConfirmationPage.this,
						MainPage.class);
				intentToMainPage.putExtra("busStopSaved", b);
				startActivity(intentToMainPage);
				finish();
			}
		});
	} // ends okButton method


	/** 
	 *  Cancel Button cancels the current alarm set
	 *  it erases the notification
	 *  then, it goes back to MainPage
	 *  
	 *  To show how alarms are canceled we will create a new Intent and a new 
	 *  PendingIntent with the same requestCode as the PendingIntent alarm we
	 *  want to cancel. In this case, it is PENDING_INTENT_REQUEST_CODE1.
	 *  Note: The intent and PendingIntent have to be the same as the ones used
	 *  to create the alarm.
	 */
	private void cancelButton() {
		final Button CancelButton = (Button)findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intentToMainPage = new Intent(ConfirmationPage.this,
						MainPage.class);
				
				Intent intentAlarmService = new Intent(v.getContext(), AlarmService.class);
				stopService(intentAlarmService);
				startActivity(intentToMainPage);
				finish();
			}
		});
	}


	/** 
	 *  This is invoked when the user pushes "Save as favorite" button
	 *  It gets all the current settings (vibrate, ringtone, proximity, and 
	 *  proximity unit) from this page
	 *  and writes them on the file "favorite_settings_data", which is located 
	 *  on data/data/com.busstopalarm/files/
	 *  
	 *  If the file does not exist (if "Save as favorite" button has never been 
	 *  pushed before),
	 *  It will create the file in the designated location
	 *  If it exists, it will overwrite the old settings when the button is 
	 *  pushed It stays in the current page
	 *   TODO: this should also save the stop in the database
	 */
	private void saveButton() {
		final Button SaveButton = (Button) findViewById(R.id.SetAsFavButton);
		SaveButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){

				// data settings to be written on the file
				String settings = "";
				if (vibration)
					settings += "vibrate";
				else
					settings += "vibrate_false";

				settings += "\t";

				if (ringtoneUri != null)
					settings += ringtoneTitleToSave;
				settings += "\t";
				settings += Integer.toString(proximity);
				settings += "\t";
				settings += proximityUnit;

				FileOutputStream fileOut = null; 
				OutputStreamWriter writer = null; 

				try {
					fileOut = openFileOutput("favorite_settings_data",
							MODE_PRIVATE);  
					writer = new OutputStreamWriter(fileOut); 
					writer.write(settings); 
					writer.flush(); 
					Toast.makeText(ConfirmationPage.this, "Settings saved",
							Toast.LENGTH_SHORT).show(); 
				} 
				catch (Exception e) {       
					e.printStackTrace(); 
					Toast.makeText(ConfirmationPage.this, "Settings not saved",
							Toast.LENGTH_SHORT).show(); 
				} 
				finally { 
					try { 
						writer.close(); 
						fileOut.close(); 
					} catch (IOException e) { 
						e.printStackTrace(); 
					} 
				}
			} // ends onClick
		}); // ends "Save as favorite" button
	} // ends saveButton method


	/**
	 * this method loads from 
	 * data/data/com.busstopalarm/files/favorite_settings_data
	 * to read the user's recent settings saved.
	 * the file contains the values with tabs to separate them.
	 * After reading from the file, it sets the data values
	 * dataVibrate, dataRingtone, dataProximity, dataProximityUnit appropriately
	 */
	private void loadRecentSettings() throws IOException {
		FileInputStream fIn = openFileInput("favorite_settings_data"); 
		InputStreamReader isr = new InputStreamReader(fIn);
		BufferedReader bin = new BufferedReader(isr);

		if (bin == null)
			return;
		String line = bin.readLine();
		if (line == null) {
			bin.close();
			return;
		}

		String[] settingResult = line.split("\t");
		bin.close();
		Log.v(TAG, "settingResult length:  " + settingResult.length);

		dataVibrate = settingResult[0];
		dataRingtone = settingResult[1];
		dataProximity = settingResult[2];
		dataProximityUnit = settingResult[3];
	}


	/**
	 *  For distance between two points, we will use Euclidean distance.
	 *  The Earth is not an Euclidean plane,
	 *  but this will give a good approximation. Assuming Euclidean plane, 
	 *  this algorithm sums a number of straight line distances.  This means the
	 *  calculated distance will never underestimate the actual distance, which
	 *  is good.
	 *  To calculate the remaining distance once alarm has started, we need to 
	 *  get the current location with the GPS.
	 *  Then we need to find the closest busstop to the current location 
	 *  (with caveat), then do sum of straight lines again.
	 *  The return values will be in some unit that will need to be converted 
	 *  to either miles or km. 
	 *  not implemented yet!
	 *   
	 * @return double initial distance
	 */ 
	public static double calculateInitialDistance() {
		// get starting s busstop in busroute
		// will the starting busstop be specified by the user or does the app
		// have to figure it out?
		// get ending d busstop in busroute
		// double dist = 0.0;
		// for (int i = s; i < d; i++) {
		//     dist += calculateDist(busroute[i],busroute[i+1];
		// return dist;
		return 0.0;
	}


	/**
	 * not implemented yet!
	 * 
	 * @return double remaining distance
	 */
	public static double calculateRemainingDistance() {
		return 0.0;
	}


	/**
	 *  Updates the average speed based on previous average speed and current
	 *  speed. If implemented like this, we need a average speed field?
	 *  We could start with an initial average speed (equivalent to 30 mph?) 
	 *  and do a something like
	 *  avg = k*avg + (1-k)current where 0 <= k <= 1.
	 *  
	 */
	public static void updateAverageSpeed() {

	}


	/**
	 * It is invoked when vibrate is clicked. 
	 * when loading vibrate is not checked if dataVibrate is null or
	 * "vibrate_false"
	 * when loading vibrate is checked if dataVibrate is non-null and is
	 * "vibrate"
	 * vibrate is on / off when the check box is checked / unchecked 
	 * respectively.
	 */
	public void getVibrate(){

		final CheckBox vib = (CheckBox) findViewById(R.id.VibrateCheckbox);

		if (dataVibrate != null && dataVibrate.equalsIgnoreCase("vibrate"))
			vib.setChecked(true);

		vib.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {	
				vibration = isChecked;
				Log.v(TAG, "under onCheckedChanged: " + vibration);
			}
		});
	}  // ends getVibrate method


	/**
	 * this method is invoked when proximity bar is used
	 * for Yards, the range is from 0 to 1000
	 * for Meters, the range is from 0 to 1000
	 * for Minutes, the range is from 0 to 1000 (temporary)
	 * 
	 * 
	 */
	public void getProximity() {
		final SeekBar proximitySeekBar = (SeekBar) 
		findViewById(R.id.ProximityBar);
		final TextView progressText = (TextView) 
		findViewById(R.id.ProximityNumber);
		progressText.setText(Integer.toString(proximity));
		// range from 0 to 1000  with step size 1
		proximitySeekBar.setMax(1000);  

		if (dataProximity != null && !dataProximity.equalsIgnoreCase("0"))
			proximitySeekBar.setProgress(Integer.parseInt(dataProximity));

		proximitySeekBar.setOnSeekBarChangeListener(
				new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBarOnProgress, 
					int progress, boolean fromTouch) {
				// TODO Auto-generated method stub
				Log.v(TAG, "progress:  " + progress);
				proximity = progress;
				progressText.setText(Integer.toString(proximity));

				//	Log.v(TAG, "fromTouch:  " + fromTouch);
			}

			public void onStartTrackingTouch(SeekBar seekBarOnStart) {
				// TODO Auto-generated method stub

			}

			public void onStopTrackingTouch(SeekBar seekBarOnStop) {
				// TODO Auto-generated method stub


			}
		});


	} // ends getProximity method


	/**
	 * It is invoked when the user selects the proximity unit on the spinner
	 * It first loads all units (Yards, Meters, Minutes) on the spinner
	 * then, sets it to the one that the user previously has saved as a favorite 
	 * 
	 */
	public void getProximityUnits() {
		Spinner proximityUnitsSpinner = (Spinner) 
		findViewById(R.id.ProximityUnits);
		ArrayAdapter<CharSequence> proxSpinnerValues = 
			ArrayAdapter.createFromResource(this, R.array.ProximityUnitList,
				android.R.layout.simple_spinner_item);
		proxSpinnerValues.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		proximityUnitsSpinner.setAdapter(proxSpinnerValues);

		if (dataProximityUnit != null && 
				dataProximityUnit.equalsIgnoreCase("Meters"))
			proximityUnitsSpinner.setSelection(1);
		if (dataProximityUnit != null &&
				dataProximityUnit.equalsIgnoreCase("Minutes"))
			proximityUnitsSpinner.setSelection(2);

		proximityUnitsSpinner.setOnItemSelectedListener(
				new OnItemSelectedListener() { 
			public void onItemSelected(AdapterView<?> adapterView, View arg1,
					int arg2, long arg3) {

				int indexProx = adapterView.getSelectedItemPosition();
				CharSequence selectedUnit =
					(CharSequence) adapterView.getSelectedItem();
				selectedUnit.toString();
				proximityUnit = (String) selectedUnit;
				Log.v(TAG, "under onItemSelected(proximity unit): " + 
						indexProx);
				Log.v(TAG, "under onItemSelected(proximity unit): " +
						selectedUnit);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}  // ends getProximityUnits method


	/**
	 * It loads all types of sounds (ringtones, notifications, alarms) from the
	 * local storage. 
	 * ringtone cursor goes through all the sounds and get the titles and show
	 * them on the spinner.
	 * if the user has set the ringtone as a favorite, it will set it to that
	 * ringtone in the first place
	 * if not, it will set the ringtone to the first one on the list
	 */
	public void getRingtones() {

		final RingtoneManager ringtoneManager = new RingtoneManager(this);

		// get all types of sounds (ringtones, notifications, alarms)
		// or just get ringtones type
		ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);

		Cursor ringtoneCursor = ringtoneManager.getCursor();
		int defaultRingtoneIndex = 0;
		String[] ringtoneList = new String[ringtoneCursor.getCount()];
		//	Log.v(TAG, "ringtones row count: " + ringtoneCursor.getCount());
		ringtoneCursor.moveToFirst();
		for (int i = 0; i < ringtoneCursor.getCount(); i++) {
			String titleOfRingtone = ringtoneCursor.getString(
					RingtoneManager.TITLE_COLUMN_INDEX);
			Log.v(TAG, "ringtone list:  " + titleOfRingtone);
			ringtoneList[i] = titleOfRingtone;
			if (dataRingtone != null && dataRingtone.equals(titleOfRingtone))
				defaultRingtoneIndex = i;
			Log.d("CONFPAGE", ringtoneManager.getRingtoneUri(ringtoneCursor.getPosition()).toString());
			ringtoneCursor.moveToNext();
		}

		//String ringtoneList = ringtone.getTitle(this);	
		// = ringtoneCursor.getColumnNames();

		Spinner ringtoneSpinner = (Spinner) findViewById(R.id.RingtoneSelector);
		ArrayAdapter<String> ringtoneAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item,
				ringtoneList);
		ringtoneAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		ringtoneSpinner.setAdapter(ringtoneAdapter);

		if (defaultRingtoneIndex != 0)
			ringtoneSpinner.setSelection(defaultRingtoneIndex);

		ringtoneSpinner.setOnItemSelectedListener(new OnItemSelectedListener() { 

			public void onItemSelected(AdapterView<?> adapterView, View arg1,
					int arg2, long arg3) {

				int indexRingtone = adapterView.getSelectedItemPosition();
				ringtoneUri = ringtoneManager.getRingtoneUri(indexRingtone);
				Ringtone rt = ringtoneManager.getRingtone(indexRingtone);
				ringtoneTitleToSave = rt.getTitle(getBaseContext());

				Log.v(TAG, "under onItemSelected(index ringtone): " +
						indexRingtone);
				Log.v(TAG, "under onItemSelected(ringtoneTitleToSave): " + 
						ringtoneTitleToSave);
				Log.v(TAG, "under onItemSelected(ringtoneUri): " + ringtoneUri);

			}
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}  // ends getRingtones method


	/** 
	 * It is invoked when option menu is pushed
	 * @param Menu
	 * @return boolean
	 * 
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0,1,1, "Go back");
		return true;
	}


	/** 
	 * It is invoked when option item is selected
	 * @param MenuItem
	 * @return boolean
	 * 
	 * 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);


	}

} // class ends