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

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
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

	// This TAG is for debugging
	private static final String TAG = "inConfirmationPage";
	// This keeps the state of whether a notification has been set. This way 
	// of keeping track state is better than checking for the existence of 
	// the relevant service in the ActivityManager.
	private static boolean isAlarmSet = false;
	
	private Uri ringtoneUri;

	// The settings object to be pre-loaded into the page.
	private SettingsObj settings;
	
	/**
	 * ConfirmationPage constructor
	 */
	public ConfirmationPage() {
		ringtoneUri = null;	
		settings = null;
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
		
		// performance testing, starting from the confirmation page (onCreate)
		//Debug.startMethodTracing("performance_testing_on_confirmation");
		
		// load saved settings
		settings = SettingsObj.getSettingsFromFile();
		
		setContentView(R.layout.confirmation);
		BusStop stop = getIntent().getParcelableExtra("busstop");

	
		String routeID = getIntent().getStringExtra("busroute");
		
		TextView routeView = (TextView) findViewById(R.id.RouteNumberSelected);
		routeView.setText(routeID);
		TextView stopView = (TextView) findViewById(R.id.BusStopSelected);
		stopView.setText(stop.getName());

		okButton();
		cancelButton();
		saveButton();
		getVibrate();
		getRingtones();
		getProximity();
		getProximityUnits();

		Log.v(TAG, "ringtone:  " + ringtoneUri);

	}  // ends onCreate method
	/**
	 *  OK Button confirms the alarm setting
	 *  it calls alarm service to set alarm
	 *  after creating alarm set, it goes back to MainPage
	 *  
	 */
	public void okButton() {
		final Button OKButton = (Button)findViewById(R.id.OKButton);
		OKButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				BusStop b = getIntent().getParcelableExtra("busstop");
				Intent intentAlarmService = 
					new Intent(v.getContext(), AlarmService.class);
				
				intentAlarmService.putExtra("busstop", b);
				intentAlarmService.putExtra("proximity", settings.getProximity());
				intentAlarmService.putExtra("proximityUnit", 
						settings.getProximityUnit());
				intentAlarmService.putExtra("vibration", settings.getVibration());
				intentAlarmService.putExtra("ringtoneUri", ringtoneUri);
				startService(intentAlarmService);

				Intent intentToMainPage = new Intent(ConfirmationPage.this,
						MainPage.class);
				intentToMainPage.putExtra("busStopSaved", b);				
				intentToMainPage.putExtra("busroute", 
						getIntent().getIntExtra("busroute", 0));
				
				if (!isAlarmSet) {
					Toast.makeText(ConfirmationPage.this, "Alarm is set", 
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ConfirmationPage.this, "Alarm updated", 
							Toast.LENGTH_LONG).show();
				}
				isAlarmSet = true;
				
				// performance testing, starting from the onCreate 
				// up to this point (author: Pyong Byon)
				//Debug.stopMethodTracing();
				
				// We give signal that we can finish Map Page here.
				setResult(RESULT_OK);
				finishActivity(MapPage.MAP_CONFIRM_TRANSITION);
				finishActivity(MainPage.MAIN_CONFIRM_TRANSITION);

				startActivity(intentToMainPage);
				// We do not finish here because we want to be able to go back
				// to change settings.
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
	public void cancelButton() {
		final Button CancelButton = (Button)findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intentToMainPage = new Intent(ConfirmationPage.this,
						MainPage.class);

				Intent intentAlarmService = 
					new Intent(v.getContext(), AlarmService.class);
				stopService(intentAlarmService);

				if (isAlarmSet) {
					Toast.makeText(ConfirmationPage.this, "Alarm canceled", 
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ConfirmationPage.this, "There is no alarm set!", 
							Toast.LENGTH_LONG).show();
				}
				isAlarmSet = false;
				
				setResult(RESULT_OK);
				finishActivity(MapPage.MAP_CONFIRM_TRANSITION);
				finishActivity(MainPage.MAIN_CONFIRM_TRANSITION);

				startActivity(intentToMainPage);
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
	 * 
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
				String busRouteIDString = getIntent().getStringExtra("busroute");
				Log.v(TAG, "busRouteIDString:  "+ busRouteIDString);
				
				BusDbAdapter busDbAdapter = new BusDbAdapter(v.getContext());
				busDbAdapter.open();
				Log.v(TAG, "try to save. args: " + busRouteIDString + " " + busStopID);
				busDbAdapter.updateDestDesc_TimeCount(busRouteIDString, busStopID);
				busDbAdapter.close();
				Toast.makeText(ConfirmationPage.this, "Destination Saved", 
						Toast.LENGTH_LONG).show();
		
				//Debug.stopMethodTracing();
			} // ends onClick

		}); // ends "Save Destination" button
	} // ends saveButton method

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
		Log.v(TAG, "under getVibrate method, vibration:  " + 
				settings.getVibration());
		vib.setChecked(settings.getVibration());

		vib.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {	
				settings.setVibration(isChecked);
				Log.v(TAG, "under onCheckedChanged: " + isChecked);
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
		final SeekBar proximitySeekBar = (SeekBar) findViewById(R.id.ProximityBar);
		final TextView progressText = (TextView) findViewById(R.id.ProximityNumber);
		progressText.setText(Integer.toString(settings.getProximity()));

		// range from 0 to 1000 with step size 1.
		proximitySeekBar.setMax(1000);
		proximitySeekBar.setProgress(settings.getProximity());
		
		proximitySeekBar.setOnSeekBarChangeListener(
				new OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar seekBarOnProgress, 
							int progress, boolean fromTouch) {
						settings.setProximity(progress);
						progressText.setText(Integer.toString(progress));
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

		String settingsUnit = settings.getProximityUnit();
		if (settingsUnit != null && settingsUnit.equalsIgnoreCase("Meters"))
			proximityUnitsSpinner.setSelection(1);

		proximityUnitsSpinner.setOnItemSelectedListener(
				new OnItemSelectedListener() { 
					public void onItemSelected(AdapterView<?> adapterView, 
							View arg1, int arg2, long arg3) {
						CharSequence selectedUnit =
							(CharSequence) adapterView.getSelectedItem();
						settings.setProximityUnit(selectedUnit.toString());
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
		String settingsRingtoneName = settings.getRingtoneName();
		
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
				if (settingsRingtoneName != null && 
						settingsRingtoneName.equals(titleOfRingtone)) {
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
		
		if (defaultRingtoneIndex != 0) {
			ringtoneSpinner.setSelection(defaultRingtoneIndex);
			ringtoneUri = ringtoneManager.getRingtoneUri(defaultRingtoneIndex);
		}

		ringtoneSpinner.setOnItemSelectedListener(new OnItemSelectedListener() { 

			public void onItemSelected(AdapterView<?> adapterView, View arg1,
					int arg2, long arg3) {
				int indexRingtone = adapterView.getSelectedItemPosition();
				ringtoneUri = ringtoneManager.getRingtoneUri(indexRingtone);
				Ringtone rt = ringtoneManager.getRingtone(indexRingtone);
				settings.setRingtoneName(rt.getTitle(getBaseContext()));
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
