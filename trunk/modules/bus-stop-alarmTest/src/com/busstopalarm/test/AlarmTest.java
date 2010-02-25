/**
 * Author: Pyong Byon
 * Date: 02/23/2010
 * 
 * White box testing for Alarm class and related things in ConfirmationPage class
 * 
 */



package com.busstopalarm.test;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;

import com.busstopalarm.Alarm;
import com.busstopalarm.ConfirmationPage;
import com.jayway.android.robotium.solo.Solo;

public class AlarmTest extends ActivityInstrumentationTestCase2<ConfirmationPage> {

	private static final String TAG = "inAlarmTest";
	private ConfirmationPage cp;
	private Solo solo;
	
	public AlarmTest() {
		super("com.busstopalarm", ConfirmationPage.class);
		
	}
	
	
	/**
	 * Initializes (if any) the set up for the test
	 */
	protected void setUp() throws Exception {
	 cp = (ConfirmationPage) getActivity();
	 solo = new Solo(getInstrumentation(), getActivity());
	}
	
	


	/**
	 * Tests alarm time set and get
	 * @throws Throwable
	 */
	public void testAlarmTimeSetAndGet() throws Throwable{
		cp.setTime(567);
		assertEquals(567, cp.getTime());
	}
	
	
	/**
	 * Tests alarm timeConverter with negative number
	 * @throws Throwable
	 */
	public void testAlarmTimeConverterNegative() throws Throwable{
		cp.setTime(-3);
		assertEquals("timeConverter(): Error! time should not be negative.", cp.timeConverter());
	}
	
	/**
	 *  Tests alarm timeConverter with 0 time
	 * @throws Throwable
	 */
	public void testAlarmTimeConverterZero() throws Throwable{
		cp.setTime(0);
		assertEquals("0 seconds left until alarm goes off", cp.timeConverter());
	}

	
	/**
	 *  Tests alarm timeConverter with positive number (less than one minute)
	 * @throws Throwable
	 */
	public void testAlarmTimeConverterSmall() throws Throwable{
		cp.setTime(35);
		assertEquals("35 seconds left until alarm goes off", cp.timeConverter());
	}
	
	
	/**
	 *  Tests alarm timeConverter with positive number (more than one minute, but less than 2 minutes)
	 * @throws Throwable
	 */
	public void testAlarmTimeConverterLessThanTwoMinutes() throws Throwable{
		cp.setTime(103);
		assertEquals("1 minute  43 seconds left until alarm goes off", cp.timeConverter());
	}
	
	
	/**
	 *  Tests alarm timeConverter with positive number (more than two minutes, but less than 1 hour)
	 * @throws Throwable
	 */
	public void testAlarmTimeConverterLessThanOneHour() throws Throwable{
		cp.setTime(1932);
		assertEquals("32 minutes  12 seconds left until alarm goes off", cp.timeConverter());
	}
	
	
	/**
	 *  Tests alarm timeConverter with positive number (more than one hour)
	 * @throws Throwable
	 */
	public void testAlarmTimeConverterMoreThanOneHour() throws Throwable{
		cp.setTime(4043);
		Log.v(TAG, cp.timeConverter());
		assertEquals("1 hour(s)  7 minutes left until alarm goes off", cp.timeConverter());
	}
	
	/**
	 *  Tests vibrate setter and getter
	 * @throws Throwable
	 */
	public void testAlarmVibrateSetAndGet() throws Throwable {
		cp.setVibration(true);
		assertEquals(true, cp.getVibration());
		cp.setVibration(false);
		assertEquals(false, cp.getVibration());
	}
	
	
	/**
	 *  Tests ringtone setter and getter
	 * @throws Throwable
	 */
	public void testAlarmRingtoneSetAndGet() throws Throwable {
		Uri ringtoneUri = RingtoneManager.getDefaultUri(0);
		Log.v(TAG, "ringtoneUri:  "+ringtoneUri);
		cp.setRingtoneUri(ringtoneUri);
		assertEquals(ringtoneUri, cp.getRingtoneUri());
		cp.setRingtoneUri(null);
		assertEquals(null, cp.getRingtoneUri());
	}
	
	
	
	
	/**
	 *  Tests alarm that works basic.
	 * @throws Throwable
	 */
	public void testAlarmBasicWorks() throws Throwable {
		assertEquals(true, cp.setAlarm(new View(cp.getBaseContext())));
	}
	
	
	/**
	 *  Tests alarm that works with ringtone and vibrate specified
	 * @throws Throwable
	 */
	public void testAlarmWorks1() throws Throwable {
		Uri ringtoneUri = RingtoneManager.getDefaultUri(0);
		cp.setRingtoneUri(ringtoneUri);
		cp.setVibration(true);
		assertEquals(true, cp.setAlarm(new View(cp.getBaseContext())));
		
	}
	
	
	/**
	 *  Tests alarm class without calling the onReceive method
	 *  
	 * @throws Throwable
	 */
	public void testAlarmClassNotWorks() throws Throwable {
		Alarm alarm = new Alarm();
		assertEquals(false, alarm.getIfSuccessful());
	}
	
	
	/**
	 *  Tests alarm class if it successfully goes through onReceive method
	 *  and notifies the user
	 * @throws Throwable
	 */
	public void testAlarmClass() throws Throwable {
		Uri ringtoneUri = RingtoneManager.getDefaultUri(0);
		Intent intent = new Intent();
		intent.putExtra("Ringtone", ringtoneUri);
		intent.putExtra("Vibration", true);
		Alarm alarm = new Alarm();
		alarm.onReceive(cp.getBaseContext(), intent);
		assertEquals(true, alarm.getIfSuccessful());
	}
	
	
	
	
	
} // AlarmTest class ends
