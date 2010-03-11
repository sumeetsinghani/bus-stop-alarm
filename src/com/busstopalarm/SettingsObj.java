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
import java.io.FileReader;
import java.io.IOException;

import android.util.Log;

public class SettingsObj {

	// Tag for logging purposes.
	private static final String TAG = "SettingsObj";
	
	// The filename in which these settings will be stored.
	static final String SETTINGS_FILE_NAME = "/data/data/com.busstopalarm/files/favorite_settings_data";
	// Units. There are currently two, so we don't really need to use a enum.
	static final String YARDS = "Yards";
	static final String METERS = "Meters";
	
	// The maximum proximity quantity permitted. (both Yards and Meters)
	static final int MAX_PROXIMITY = 1000;
	
	// These are the settings in the file.
	private boolean vibration;
	private String ringtoneName;
	private int proximity;
	private String proximityUnit;  

	/**
	 * Constructs a SettingsObj with default values.
	 */
	public SettingsObj() {
		vibration = false;
		ringtoneName = null;
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
	public SettingsObj(boolean vibration, String ringtoneName, int proximity, 
			String proximityUnit) {
		this.vibration = vibration;
		this.ringtoneName = ringtoneName;
		this.proximity = proximity;
		this.proximityUnit = proximityUnit;
	}
	
	/**
	 * Gets the ringtone name.
	 * @return Ringtone name
	 */
	public String getRingtoneName() {
		return ringtoneName;
	}
	
	/**
	 * Gets the proximity.
	 * @return Proximity
	 */
	public int getProximity() {
		return proximity;
	}
	
	/**
	 * Gets the proximity unit (Meters or Yards)
	 * @return Proximity unit
	 */
	public String getProximityUnit() {
		return proximityUnit;
	}
	
	/**
	 * Gets the vibration (true or false)
	 * @param vibration
	 */
	public boolean getVibration() {
		return vibration;
	}
	
	/**
	 * Constructs a SettingsObj from reading the settings file. 
	 * On failure to read the file, returns a SettingsObj with default values.
	 * If one of the settings is not properly formatted, that setting will be 
	 * set to default.
	 * @return A SettingsObj with settings given in file.
	 */
	public static SettingsObj getSettingsFromFile() {
	
		BufferedReader bin = null;
		String line = null;
		try {
			bin = new BufferedReader(new FileReader(SETTINGS_FILE_NAME));
		} catch (FileNotFoundException e) {
			return new SettingsObj();
		}
		try {
			line = bin.readLine();
		} catch (IOException e) {
			return new SettingsObj();
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
			return new SettingsObj();
		}

		boolean vibration;
		int proximity;
		String ringtoneName;
		String proximityUnit;
		
		if (settingResult[0] != null && settingResult[0].equals("vibrate")) {
			vibration = true;
		} else {
			vibration = false;
		}
		
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
		if (!proximityUnit.equals(METERS) || !proximityUnit.equals(YARDS))
			proximityUnit = null;
		
		return new SettingsObj(vibration, 
				ringtoneName, 
				proximity, 
				proximityUnit);
	}

	
}
