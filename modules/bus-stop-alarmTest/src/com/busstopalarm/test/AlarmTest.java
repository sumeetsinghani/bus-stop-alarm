/**
 * Author: Pyong Byon
 * Date: 02/23/2010
 * 
 * The White box testing for Alarm
 * 
 */



package com.busstopalarm.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.busstopalarm.ConfirmationPage;

public class AlarmTest extends ActivityInstrumentationTestCase2<ConfirmationPage> {

	private static final String TAG = "inAlarmTest";
	private ConfirmationPage cp;
	
	public AlarmTest() {
		super("com.busstopalarm", ConfirmationPage.class);
		
	}
	
	
	/**
	 * Initializes (if any) the set up for the test
	 */
	protected void setUp() throws Exception {
	 cp = (ConfirmationPage) getActivity();
		
	}
	
	
	
	/**
	 * Tests alarm time set and get
	 * @throws Throwable
	 */
	public void testAlarmTimeSetAndGet() throws Throwable{
	//	ConfirmationPage cp = (ConfirmationPage) getActivity();
	//	final Button OKButton = (Button) 
	//	cp.findViewById(com.busstopalarm.R.id.OKButton);
		
		/*
		runTestOnUiThread(new Runnable() {
			public void run() {
				OKButton.performClick();
			}
		});*/
		
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
	
	
	
	
	
} // AlarmTest class ends
