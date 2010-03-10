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

import android.net.Uri;

public class SettingsObj {

	// The filename in which these settings will be stored.
	private static final String SETTINGS_FILE_NAME = "favorite_settings_data";
	// Units. There are currently two, so we don't really need to use a enum.
	private static final String YARDS = "Yards";
	private static final String METERS = "Meters";
	
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
	
	
	
}
