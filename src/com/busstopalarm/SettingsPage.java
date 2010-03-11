/**
 * Author: Orkhan Muradov, Derek Cheng
 * Date: 02/03/2010
 * 
 * Settings Page where the user interacts with this page to 
 * set the alarm settings: vibrate, ringtone, proximity, and proximityUnit
 * The user can save the settings as a favorite by pressing the "Save Settings"
 * button.
 * The user can also press Go back button to return to previous page.
 */

package com.busstopalarm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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

public class SettingsPage extends Activity {

	// this TAG is for debugging
	private static final String TAG = "inSettings";
	
	// These are the settings in the file.
	private boolean vibration;
	// This is the ringtone data in the settings file aka the current favorite.
	private String dataRingtone;
	private Uri ringtoneUri;
	private int proximity;
	private String proximityUnit;  

	// This is the ringtone title that the user selects and that will be saved
	// into the file.
	private String ringtoneTitleToSave;
	
	/**
	 * SettingsPage constructor. Sets settings to default values.
	 */
	public SettingsPage() {
		// The default values.
		vibration = false;
		dataRingtone = null;
		ringtoneUri = null;
		proximity = 0;
		proximityUnit = null;
		
		ringtoneTitleToSave = null;
	}


	/** 
	 * Called when the activity is first created on the confirmation page.
	 * @param Bundle which holds the current state (info)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadRecentSettings();
		
		setContentView(R.layout.settings);
		
		goBackButton();
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
	 *  Sets up the Go Back button.
	 */
	private void goBackButton() {
		final Button GobackButton = (Button)findViewById(R.id.Goback);
		GobackButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
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
	 */
	private void saveButton() {
		final Button SaveButton = (Button) findViewById(R.id.SaveSettings);
		SaveButton.setOnClickListener(new View.OnClickListener(){
			/**
			 * Generates settings to be written on the file, and
			 * returns it.
			 * @return The String content to be written to file.
			 */
			private String buildSettingsString() {

				StringBuilder settings = new StringBuilder();

				if (vibration)
					settings.append("vibrate");
				else
					settings.append("vibrate_false");

				settings.append("\t");

				if (ringtoneUri != null)
					settings.append(ringtoneTitleToSave);
				settings.append("\t");				
				settings.append(proximity);
				settings.append("\t");
				settings.append(proximityUnit);

				return new String(settings);
			}

			/**
			 * Writes String settings to the file SETTINGS_FILE_NAME,
			 * and reports success or failure.
			 */
			public void writeSettingsToFile(String settings) {
				OutputStreamWriter writer = null; 

				try {
					writer = new OutputStreamWriter(
							openFileOutput(SettingsObj.SETTINGS_FILE_NAME,
									MODE_PRIVATE)); 
					writer.write(new String(settings)); 
					writer.flush(); 
					Toast.makeText(SettingsPage.this, "Settings saved",
							Toast.LENGTH_SHORT).show(); 
				} catch (IOException e) {       
					e.printStackTrace();
					Toast.makeText(SettingsPage.this, 
							"Settings not saved. Make sure you have " +
							"permissions to write to the settings file.",
							Toast.LENGTH_SHORT).show(); 
				} finally { 
					try {
						if (writer != null)
							writer.close(); 
					} catch (IOException e) { 
						// Do nothing
						Log.v(TAG, "Failed to close settings writer");
					} 
				}
			}

			public void onClick(View v) {			
				String settings = buildSettingsString();
				writeSettingsToFile(settings);
			}

		}); // ends "Save as favorite" button
	} // ends saveButton method


	/**
	 * Sets the settings values to default. This is invoked when either the
	 * settings file does not exist, or is corrupted, or cannot be read form.
	 */
	private void setDefaultSettingsValues() {
		vibration = false;
		ringtoneUri = null;
		proximity = 0;
		proximityUnit = null;
	}
	
	/**
	 * This method loads from 
	 * data/data/com.busstopalarm/files/favorite_settings_data
	 * to read the user's recent settings saved.
	 * The file contains the values with tabs to separate them.
	 * After reading from the file, it sets the data values
	 * vibration, ringtone, proximity and units into the settings object.
	 */
	private void loadRecentSettings() {
		BufferedReader bin = null;
		String line = null;
		try {
			bin = new BufferedReader(new InputStreamReader(
					openFileInput(SettingsObj.SETTINGS_FILE_NAME)));
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

		if (line == null) {
			setDefaultSettingsValues();
			return;
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
		if (proximity > SettingsObj.MAX_PROXIMITY)
			proximity = SettingsObj.MAX_PROXIMITY;
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
	 * this method is invoked when proximity bar is used
	 * for both Yards and Meters, the range is 0 to SettingsObj.MAX_PROXIMITY.
	 */
	public void getProximity() {
		final SeekBar proximitySeekBar = 
			(SeekBar)findViewById(R.id.ProximityBar);
		final TextView progressText = 
			(TextView)findViewById(R.id.ProximityNumber);
		progressText.setText(Integer.toString(proximity));

		proximitySeekBar.setMax(SettingsObj.MAX_PROXIMITY);  
		proximitySeekBar.setProgress(proximity);

		proximitySeekBar.setOnSeekBarChangeListener(
				new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBarOnProgress, 
					int progress, boolean fromTouch) {
				Log.v(TAG, "progress:  " + progress);
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

		if (proximityUnit != null && 
				proximityUnit.equalsIgnoreCase(SettingsObj.METERS)) {
			proximityUnitsSpinner.setSelection(1);
		}
		
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
		ringtoneManager.setType(RingtoneManager.TYPE_ALL);

		Cursor ringtoneCursor = ringtoneManager.getCursor();
		// Have we found the ringtone?
		boolean ringtoneFound = false;
		int defaultRingtoneIndex = 0;
		String[] ringtoneList = new String[ringtoneCursor.getCount()];
		//	Log.v(TAG, "ringtones row count: " + ringtoneCursor.getCount());
		ringtoneCursor.moveToFirst();
		for (int i = 0; i < ringtoneCursor.getCount(); i++) {
			String titleOfRingtone = ringtoneCursor.getString(
					RingtoneManager.TITLE_COLUMN_INDEX);
			Log.v(TAG, "ringtone list:  " + titleOfRingtone);
			ringtoneList[i] = titleOfRingtone;
			
			if (!ringtoneFound) {
				if (dataRingtone != null && dataRingtone.equals(titleOfRingtone)) {
					ringtoneFound = true;
					defaultRingtoneIndex = i;
				}
			}
			Log.d("CONFPAGE", ringtoneManager.getRingtoneUri(
					ringtoneCursor.getPosition()).toString());
			ringtoneCursor.moveToNext();
		}

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
			}
		});
	}  // ends getRingtones method
	
} // class ends