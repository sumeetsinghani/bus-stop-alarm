/**
 * SettingsObj.java
 * Date: March 10, 2010
 * 
 * This class encapsulates user settings of an alarm: vibration, ringtone,
 * proxmity and units. Settings objects can be constructed from reading a 
 * text file with a specific format. A settings object can also be written
 * into a text file for reading later.
 * 
 * @author Derek Cheng
 */

package com.busstopalarm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.net.Uri;
import android.util.Log;
import android.content.ContextWrapper;

public class SettingsObj {

	// Tag for logging purposes.
	private static final String TAG = "SettingsObj";
	
	// The filename in which these settings will be stored.
	static final String SETTINGS_FILE_NAME = "favorite_settings_data";
	// Units. There are currently two, so we don't really need to use a enum.
	static final String YARDS = "Yards";
	static final String METERS = "Meters";
	
	// The maximum proximity quantity permitted. (both Yards and Meters)
	static final int MAX_PROXIMITY = 1000;
	
	// These are the settings in the file.
	private boolean vibration;
	private String ringtoneName;
	private Uri ringtoneUri;
	private int proximity;
	private String proximityUnit;  

	/**
	 * Constructs a SettingsObj with default values.
	 */
	public SettingsObj() {
		vibration = false;
		ringtoneName = null;
		ringtoneUri = null;
		proximity = 0;
		proximityUnit = null;		
	}
	/**
	 * Constructs a SettingsObj with given parameters.
	 * @param vibration Vibration option
	 * @param ringtoneName Name of ringtone
	 * @param ringtoneUri URI of ringtone
	 * @param proximity Proximity quantity
	 * @param proximityUnit Proximity unit
	 */
	public SettingsObj(boolean vibration, String ringtoneName, Uri ringtoneUri,
			int proximity, String proximityUnit) {
		this.vibration = vibration;
		this.ringtoneName = ringtoneName;
		this.ringtoneUri = ringtoneUri;
		this.proximity = proximity;
		this.proximityUnit = proximityUnit;
	}
	/*
	public SettingsObj getSettingsFromFile() {
	
		BufferedReader bin = null;
		String line = null;
		try {
			bin = new BufferedReader(new InputStreamReader(
					openFileInput(SETTINGS_FILE_NAME)));
		} catch (FileNotFoundException e) {
			return null;
		}
		try {
			line = bin.readLine();
		} catch (IOException e) {
			return null;
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
			return null;
		}

		if (settingResult[0] != null && settingResult[0].equals("vibrate"))
			vibration = true;

		ringtoneName = settingResult[1];

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
		
		return null;
	}
	*/
}
