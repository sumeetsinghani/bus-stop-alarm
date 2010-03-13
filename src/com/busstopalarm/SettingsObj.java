/**
 * SettingsObj.java
 * Date: March 10, 2010
 * 
 * This class encapsulates user settings of an alarm: vibration, ringtone,
 * proxmity and units. Settings objects can be constructed from reading a 
 * text file with a specific format. A settings object can also be written
 * into a text file for reading later.
 * 
 * A valid settings file have the following format:
 * vibration[tab]ringtoneName[tab]proximity[tab]proximityUnit
 * where:
 * vibration - is either "vibrate" or "vibrate_false"
 * ringtoneName - is a name of a ringtone
 * proximity - is an integer from 0 to 1000
 * proximityUnit - is either "Meters" or "Yards"
 * 
 * @author Derek Cheng
 */

package com.busstopalarm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.util.Log;

public class SettingsObj {

	// Tag for logging purposes.
	private static final String TAG = "SettingsObj";
	
	// The filename of settings file.
	static final String SETTINGS_FILE_NAME = "favorite_settings_data";
	// The dir of settings file
	static final String SETTINGS_FILE_DIR = 
		"/data/data/com.busstopalarm/files/";
	// The full path of the settings file.
	static final String SETTINGS_FILE_PATH = 
		SETTINGS_FILE_DIR + SETTINGS_FILE_NAME;
	

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
	 * Sets the vibration to true or false.
	 * @param vibration True if alarm vibrates, false otherwise.
	 */
	public void setVibration(boolean vibration) {
		this.vibration = vibration;
	}

	/**
	 * Sets the name of ringtone to be played.
	 * @param ringtoneName Name of ringtone
	 */
	public void setRingtoneName(String ringtoneName) {
		this.ringtoneName = ringtoneName;
	}

	/**
	 * Sets proximity. Argument must be between 0 and MAX_PROXIMITY.
	 * @param proximity Proximity quantity.
	 * @throws IllegalArgumentException if argument is out of range.
	 */
	public void setProximity(int proximity) {
		if (proximity < 0 || proximity > MAX_PROXIMITY) {
			throw new IllegalArgumentException();
		}
		this.proximity = proximity;
	}

	/**
	 * Sets the proximity unit. Must be either "Meters" or "Yards".
	 * @param proximityUnit "Meters" or "Yards"
	 * @throws IllegalArgumentException if argument is not "Meters" or "Yards"
	 */
	public void setProximityUnit(String proximityUnit) {
		if (proximityUnit != null) {
			if (!proximityUnit.equalsIgnoreCase(METERS) && 
					!proximityUnit.equalsIgnoreCase(YARDS)) {
				throw new IllegalArgumentException();
			}
		}
		this.proximityUnit = proximityUnit;
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
			bin = new BufferedReader(new FileReader(SETTINGS_FILE_PATH));
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

		// If the file is empty...
		if (line == null) {
			return new SettingsObj();
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
		
		if (settingResult[0] != null && 
				settingResult[0].equalsIgnoreCase("vibrate")) {
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
		if (!proximityUnit.equalsIgnoreCase(METERS) && 
				!proximityUnit.equalsIgnoreCase(YARDS))
			proximityUnit = null;
		
		return new SettingsObj(vibration, 
				ringtoneName, 
				proximity, 
				proximityUnit);
	}
	
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
		settings.append('\t');

		settings.append(ringtoneName);
		settings.append('\t');	
		
		settings.append(proximity);
		settings.append('\t');
		
		settings.append(proximityUnit);

		return new String(settings);
	}
	
	/**
	 * Writes the SettingsObj to the settings file in said format above.
	 * This methods overwrites the old settings. If the file did not exist
	 * before, this method will attempt to create it.
	 * @param so The SettingsObj to write into the file
	 * @return true if the file was written successfully 
	 */
	public static boolean writeSettingsToFile(SettingsObj so) {
	
		// Construct the directory if it does not exist.
		File dir = new File(SETTINGS_FILE_DIR);
		if (dir.exists()) {
			if (!dir.isDirectory()) {
				// if the name happens to be the name of a non-dir for some
				// reason...
				Log.v(TAG, "There is a non-dir file with the same name!");
				return false;
			}
			// else the directory already exists, so it's okay.
		}
		else {
			if (!dir.mkdirs()) {
				Log.v(TAG, "Cannot create directories!");
				return false;
			}
		}
		
		OutputStreamWriter writer = null; 
		try {

			writer = new OutputStreamWriter(
					new FileOutputStream(SETTINGS_FILE_PATH)); 
			writer.write(so.buildSettingsString()); 
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.v(TAG, "Settings file cannot be opened!");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			Log.v(TAG, "I/O error occurred while writing to file!");
			return false;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				} 
			} catch (IOException e) {
				// do nothing, but log it anyways
				Log.v(TAG, "Failed to close writer!");
			}
		}
		return true;
	}

}
