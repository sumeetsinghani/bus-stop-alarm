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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
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
	private static final String SETTINGS_FILE_NAME = "favorite_settings_data";

	private boolean vibration;
	private Uri ringtoneUri;
	private int proximity;
	private String proximityUnit;

	// these below are the data saved in the "favorite_settings_data" file in
	// sdcard to be retrieved from the file to load the recent settings
	private String dataRingtone;
	private String ringtoneTitleToSave;

	private SeekBar proximitySeekBar;
	private TextView progressText; 

	/**
	 * ConfirmationPage constructor
	 */
	public ConfirmationPage() {

		dataRingtone = null;

		vibration = false;
		ringtoneUri = null;
		proximity = 0;
		proximityUnit = null;

		ringtoneTitleToSave = null; 
	}



	/**
	 * vibrate setter
	 * @param boolean vibrate input
	 */
	public void setVibration(boolean vibrateInput){
		vibration = vibrateInput;
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
	public void setRingtoneUri(Uri ringtoneInput){
		ringtoneUri = ringtoneInput;
	}


	/**
	 * getter for ringtone uri
	 * @return Uri ringtone uri
	 */
	public Uri getRingtoneUri(){
		return ringtoneUri;
	}


	/** Called when the activity is first created on the confirmation page.
	 *  @param Bundle which holds the current state (info)
	 *  
	 *  */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// performance testing, starting from the map page 
		// up to this point (author: Pyong Byon)
		//Debug.stopMethodTracing();
		
		// performance testing, starting from the confirmation page
		//Debug.startMethodTracing("performance_testing_on_confirmation");
		
		
		
		// load saved settings
		loadRecentSettings();

		setContentView(R.layout.confirmation);
		BusStop stop = getIntent().getParcelableExtra("busstop");
		int routeID = getIntent().getIntExtra("busroute", 0);
		TextView routeView = (TextView) findViewById(R.id.RouteNumberSelected);
		routeView.setText(Integer.toString(routeID));
		TextView stopView = (TextView) findViewById(R.id.BusStopSelected);
		stopView.setText(stop.getName());

		okButton();
		cancelButton();
		saveButton();
		getVibrate();
		getRingtones();
		getProximity();
		getProximityUnits();

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

				BusStop b = getIntent().getParcelableExtra("busstop");
				Intent intentAlarmService = 
					new Intent(v.getContext(), AlarmService.class);
				intentAlarmService.putExtra("proximity", proximity);
				intentAlarmService.putExtra("proximityUnit", proximityUnit);
				intentAlarmService.putExtra("busstop", b);
				intentAlarmService.putExtra("vibration", vibration);
				intentAlarmService.putExtra("ringtoneUri", ringtoneUri);
				startService(intentAlarmService);

				Intent intentToMainPage = new Intent(ConfirmationPage.this,
						MainPage.class);
				intentToMainPage.putExtra("busStopSaved", b);
				
				Toast.makeText(ConfirmationPage.this, "Alarm is set", 
						Toast.LENGTH_LONG).show();
				
				// performance testing, starting from the onCreate 
				// up to this point (author: Pyong Byon)
				//Debug.stopMethodTracing();
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

				Intent intentAlarmService = 
					new Intent(v.getContext(), AlarmService.class);
				stopService(intentAlarmService);
				startActivity(intentToMainPage);
				finish();
			}
		});
	}


	/** 
	 *  This is invoked when the user presses "Save Destination" button
	 *  It gets all the current settings (vibrate, ringtone, proximity, and 
	 *  proximity unit) from this page
	 *  and writes them on the file "favorite_settings_data", which is located 
	 *  on data/data/com.busstopalarm/files/
	 *  
	 *  If the file does not exist (if "Save Destination" button has never been 
	 *  pushed before),
	 *  It will create the file in the designated location
	 *  If it exists, it will overwrite the old settings when the button is 
	 *  pushed It stays in the current page
	 *   TODO: this should also save the stop in the database
	 */
	public void saveButton() {
		final Button SaveButton = (Button) findViewById(R.id.SaveDestination);
		SaveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {	
				BusStop busStop = getIntent().getParcelableExtra("busstop");
				String busStopID = busStop.getStopId();
				Bundle bundle = getIntent().getExtras();
				Log.v(TAG, "busstop is :  " + busStop);
				Log.v(TAG, "bus id:  " +  busStopID);
				Log.v(TAG, "Bundle :  " + bundle);
				
				// get busRouteID
				int busRouteID = getIntent().getIntExtra("busroute", 0);
				Log.v(TAG, "busRoute id:  " + busRouteID);
				String busRouteIDString = Integer.toString(busRouteID);
				Log.v(TAG, "busRouteIDString:  "+ busRouteIDString);
				
				 //Try with DB
				//mBusDbHelper = new BusDbAdapter(v.getContext());
				//mBusDbHelper.open();
				
				// not working !! it crashes
			
				BusDbAdapter busDbAdapter = new BusDbAdapter (v.getContext());
				busDbAdapter.open();	
			    busDbAdapter.updateDestDesc_TimeCount(busRouteIDString, busStopID);
			
				//mBusDbHelper.close();
				busDbAdapter.close();
				Toast.makeText(ConfirmationPage.this, "Destination Saved", 
						Toast.LENGTH_LONG).show();
		
			} // ends onClick

		}); // ends "Save Destination" button
	} // ends saveButton method


	private void setDefaultSettingsValues() {

		vibration = false;
		ringtoneUri = null;
		proximity = 0;
		proximityUnit = null;

	}

	/**
	 * this method loads from 
	 * data/data/com.busstopalarm/files/favorite_settings_data
	 * to read the user's recent settings saved.
	 * the file contains the values with tabs to separate them.
	 * After reading from the file, it sets the data values
	 * dataVibrate, dataRingtone, dataProximity, dataProximityUnit appropriately
	 */
	public void loadRecentSettings() {
		BufferedReader bin = null;
		String line = null;
		try {
			bin = new BufferedReader(new InputStreamReader(
					openFileInput(SETTINGS_FILE_NAME)));
		} catch (FileNotFoundException e) {
			setDefaultSettingsValues();
			return;
		}
		try {
			line = bin.readLine();
		} catch (IOException e) {
			setDefaultSettingsValues();
			return;
		} finally {
			try {
				bin.close();
			} catch (IOException e) {
				// do nothing
			}
		}

		String[] settingResult = line.split("\t");

		Log.v(TAG, "settingResult length:  " + settingResult.length);
		if (settingResult.length < 4) {
			Log.v(TAG, "settingResult length less than 4 - corrupted file");
			setDefaultSettingsValues();
			return;
		}

		if (settingResult[0] != null && settingResult[0].equals("vibrate"))
			vibration = true;

		dataRingtone = settingResult[1];

		try {
			proximity = Integer.parseInt(settingResult[2]);
		} catch (NumberFormatException e) {
			// default value is 0
			proximity = 0;
		}
		if (proximity > 1000)
			proximity = 1000;
		else if (proximity < 0)
			proximity = 0;

		proximityUnit = settingResult[3];
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
		Log.v(TAG, "under getVibrate method, vibration:  " + vibration);
		vib.setChecked(vibration);

		vib.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {	
				vibration = isChecked;
				Log.v(TAG, "under onCheckedChanged: " + vibration);
			}
		});
	}  // ends getVibrate method


	/**
	 * This method is invoked when proximity bar is used.
	 * There are current three units:
	 * for Yards, the range is from 0 to 1000
	 * for Meters, the range is from 0 to 1000
	 */
	public void getProximity() {
		proximitySeekBar = (SeekBar) findViewById(R.id.ProximityBar);
		progressText = (TextView) findViewById(R.id.ProximityNumber);
		progressText.setText(Integer.toString(proximity));

		// range from 0 to 1000 with step size 1.
		proximitySeekBar.setMax(1000);
		proximitySeekBar.setProgress(proximity);
		proximitySeekBar.setOnSeekBarChangeListener(
				new OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar seekBarOnProgress, 
							int progress, boolean fromTouch) {
						proximity = progress;
						progressText.setText(Integer.toString(proximity));

					}

					public void onStartTrackingTouch(SeekBar seekBarOnStart) {
					}
					
					public void onStopTrackingTouch(SeekBar seekBarOnStop) {
					}
				});

	} // ends getProximity method


	/**
	 * Loads all units (Yards, Meters) on the spinner and sets it to 
	 * the one that the user previously has saved as a favorite.
	 * Invoked when the user selects the proximity unit on the spinner.
	 */
	public void getProximityUnits() {
		Spinner proximityUnitsSpinner = 
			(Spinner) findViewById(R.id.ProximityUnits);
		ArrayAdapter<CharSequence> proxSpinnerValues = 
			ArrayAdapter.createFromResource(this, R.array.ProximityUnitList,
					android.R.layout.simple_spinner_item);
		
		proxSpinnerValues.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		proximityUnitsSpinner.setAdapter(proxSpinnerValues);

		if (proximityUnit != null && proximityUnit.equalsIgnoreCase("Meters"))
			proximityUnitsSpinner.setSelection(1);

		proximityUnitsSpinner.setOnItemSelectedListener(
				new OnItemSelectedListener() { 
					public void onItemSelected(AdapterView<?> adapterView, 
							View arg1, int arg2, long arg3) {
						CharSequence selectedUnit =
							(CharSequence) adapterView.getSelectedItem();
						proximityUnit = selectedUnit.toString();	
						Log.v(TAG, "under onItemSelected(proximity unit): " +
								selectedUnit);
					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
	}  // ends getProximityUnits method


	/**
	 * Loads all types of sounds from the local storage (database).
	 * If the user has set a ringtone as a favorite, it will preselect
	 * that ringtone.
	 * If not, it will preselect the first ringtone on the list.
	 */
	public void getRingtones() {

		final RingtoneManager ringtoneManager = new RingtoneManager(this);

		// There are three types of sounds (ringtones, notifications, alarms)
		// In here, we only get the ringtones.
		ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);

		Cursor ringtoneCursor = ringtoneManager.getCursor();
		int defaultRingtoneIndex = 0;
		// Did we find the ringtone specified in the settings file?
		boolean ringtoneFound = false;
		String[] ringtoneList = new String[ringtoneCursor.getCount()];
		Log.v(TAG, "ringtones row count: " + ringtoneCursor.getCount());
		ringtoneCursor.moveToFirst();
	
		for (int i = 0; i < ringtoneCursor.getCount(); i++) {
			String titleOfRingtone = ringtoneCursor.getString(
					RingtoneManager.TITLE_COLUMN_INDEX);
			Log.v(TAG, "ringtone list:  " + titleOfRingtone);
			ringtoneList[i] = titleOfRingtone;
			
			// This is just a small optimization so we don't have to compare
			// strings every time.
			if (!ringtoneFound) {
				if (dataRingtone != null && dataRingtone.equals(titleOfRingtone)) {
					defaultRingtoneIndex = i;
					ringtoneFound = true;
				}
			}
			ringtoneCursor.moveToNext();
		} // for loop ends

		Spinner ringtoneSpinner = (Spinner) findViewById(R.id.RingtoneSelector);
		ArrayAdapter<String> ringtoneAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item,
				ringtoneList);
		ringtoneAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		ringtoneSpinner.setAdapter(ringtoneAdapter);
		ringtoneSpinner.setSelection(defaultRingtoneIndex);

		ringtoneSpinner.setOnItemSelectedListener(new OnItemSelectedListener() { 

			public void onItemSelected(AdapterView<?> adapterView, View arg1,
					int arg2, long arg3) {

				int indexRingtone = adapterView.getSelectedItemPosition();
				ringtoneUri = ringtoneManager.getRingtoneUri(indexRingtone);
				Ringtone rt = ringtoneManager.getRingtone(indexRingtone);
				ringtoneTitleToSave = rt.getTitle(getBaseContext());
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			
			}

		});

	}  // ends getRingtones method


	/** 
	 * It is invoked when option menu is pushed
	 * @param Menu
	 * @return boolean
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0,1,1, "Go Back");
		return true;
	}


	/** 
	 * It is invoked when option item is selected
	 * @param MenuItem
	 * @return boolean
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