/**
 * Author: Orkhan Muradov, Pyong Byon
 * Date: 02/03/2010
 * 
 * Settings Page where the user interacts with this page to 
 * set the alarm settings: vibrate, ringtone, proximity, and proximityUnit
 * The user can save the settings as a favorite by pressing the "Save Settings"
 * button.
 * The user can also press Go back button to return to previous page.
 */

package com.busstopalarm;

import android.app.Activity;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
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
	private static final String TAG = "inSettingsPage";
	
	// The settings displayed on the page.
	private SettingsObj currentSettings;
	
	/**
	 * SettingsPage constructor. 
	 */
	public SettingsPage() {		
		currentSettings = null;
	}


	/** 
	 * Called when the activity is first created on the confirmation page.
	 * @param Bundle which holds the current state (info)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		currentSettings = SettingsObj.getSettingsFromFile();			
		setContentView(R.layout.settings);
		
		goBackButton();
		saveButton();
		getVibrate();
		getRingtones();
		getProximity();
		getProximityUnits();
		
		Log.v(TAG, "vibrate:  " + currentSettings.getVibration());
		Log.v(TAG, "ringtone:  " + currentSettings.getRingtoneName());
		Log.v(TAG, "proximity:  " + currentSettings.getProximity());
		Log.v(TAG, "proximityUnit:  " + currentSettings.getProximityUnit());
		
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

			public void onClick(View v) {			
				if (SettingsObj.writeSettingsToFile(currentSettings)) {
					Toast.makeText(SettingsPage.this, "Settings saved",
							Toast.LENGTH_SHORT).show(); 
				} else {
					Toast.makeText(SettingsPage.this, 
							"Settings not saved. Make sure you have " +
							"permissions to write to the settings file.",
							Toast.LENGTH_SHORT).show(); 
				}
			}
		}); // ends "Save as favorite" button
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

		vib.setChecked(currentSettings.getVibration());

		vib.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {	
				currentSettings.setVibration(isChecked);
				Log.v(TAG, "under onCheckedChanged: " + isChecked);
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
		progressText.setText(Integer.toString(currentSettings.getProximity()));
		
		proximitySeekBar.setMax(SettingsObj.MAX_PROXIMITY);  
		proximitySeekBar.setProgress(currentSettings.getProximity());

		proximitySeekBar.setOnSeekBarChangeListener(
				new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBarOnProgress, 
					int progress, boolean fromTouch) {
				Log.v(TAG, "progress:  " + progress);
				currentSettings.setProximity(progress);
				progressText.setText(Integer.toString(progress));
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

		String settingsUnit = currentSettings.getProximityUnit();
		if (settingsUnit != null && 
				settingsUnit.equalsIgnoreCase(SettingsObj.METERS)) {
			proximityUnitsSpinner.setSelection(1);
		}
		
		proximityUnitsSpinner.setOnItemSelectedListener( 
				new OnItemSelectedListener() { 
					public void onItemSelected(AdapterView<?> adapterView, 
							View arg1, int arg2, long arg3) {

						int indexProx = adapterView.getSelectedItemPosition();
						CharSequence selectedUnit =
							(CharSequence) adapterView.getSelectedItem();

						Log.v(TAG, "under onItemSelected(proximity unit): " + 
								indexProx);
						Log.v(TAG, "under onItemSelected(proximity unit): " +
								selectedUnit);
						
						currentSettings.setProximityUnit(
								selectedUnit.toString());						
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
		String settingsRtName = currentSettings.getRingtoneName();
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
				if (settingsRtName != null && 
						settingsRtName.equals(titleOfRingtone)) {
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
				Ringtone rt = ringtoneManager.getRingtone(indexRingtone);
				String ringtoneTitleToSave = rt.getTitle(getBaseContext());
				currentSettings.setRingtoneName(ringtoneTitleToSave);
				
				Log.v(TAG, "under onItemSelected(index ringtone): " +
						indexRingtone);
				Log.v(TAG, "under onItemSelected(ringtoneTitleToSave): " + 
						ringtoneTitleToSave);

			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}  // ends getRingtones method


	public SettingsObj getSettings() {
		return currentSettings;
	}
	
} // class ends
