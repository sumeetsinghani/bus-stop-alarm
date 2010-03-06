///**
// * Author: Pyong Byon
// * Date: 02/26/2010
// * 
// * White box testing for Alarm class
// * 
// * Test Boundary cases
// * - time with negative number is passed in alarm object. -> it is set to 0.
// * - proximity(distance) with negative number is passed. Again, it is set to 0.
// * - timeConverter method with negative time as a parameter -> Returns the
// * String that says, "timeConverter(): Error! time should not be negative."
// * 
// * - to check setAlarm method, it toggles a boolean value (from false to true)
// *   to test whether it successfully goes through the method and sets an alarm.
// * 
// * 
// * 
// */
//
//
//
//package com.busstopalarm.test;
//
//import android.test.ActivityInstrumentationTestCase2;
//
//import com.busstopalarm.Alarm;
//import com.busstopalarm.ConfirmationPage;
//
//public class AlarmTest extends ActivityInstrumentationTestCase2<ConfirmationPage>{
//
//	private static final String TAG = "inAlarmTest";
//	private Alarm alarmObject;
//	private ConfirmationPage cp;
//
//
//	// I'm using ConfirmationPage activity to simulate alarm set
//	// since alarm is first initiated in the confirmation page (when OK button is pushed)
//	public AlarmTest() {
//		super("com.busstopalarm", ConfirmationPage.class);
//	}
//
//	/**
//	 * Initializes the set up for the test
//	 */
//	@Override
//	protected void setUp() throws Exception{
//	  cp = (ConfirmationPage) getActivity();
//	}
//	
	
//
//	// this below are what Alarm object takes as parameters
//	// public Alarm(int time, boolean vibration, Uri ringtoneUri, 
//	//		int proximity, String proximityUnit, Context ctx) {
//
//
//	/**
//	 * Tests alarm constructor with time, and getTime
//	 * @throws Throwable
//	 */
//	public void testAlarmConstructorTime() throws Throwable{
//		alarmObject = new Alarm(3, true, null, 1, "Yards", cp.getBaseContext());
//		assertEquals(3, alarmObject.getTime());
//	}
//
//
//	/**
//	 * Tests alarm constructor with time (negative number), and getTime
//	 * @throws Throwable
//	 */
//	public void testAlarmConstructorTimeNegative() throws Throwable{
//		alarmObject = new Alarm(-3, true, null, 1, "Yards", cp.getBaseContext());
//		assertEquals("expected value is 0", 0, alarmObject.getTime());
//	}
//
//
//
//	/**
//	 * Tests alarm time set and get
//	 * @throws Throwable
//	 */
//	public void testAlarmTimeSetAndGet() throws Throwable{
//		alarmObject = new Alarm(3, true, null, 1, "Yards", cp.getBaseContext());
//		assertEquals(3, alarmObject.getTime());
//
//		alarmObject.setTime(547);
//		assertEquals(547, alarmObject.getTime());
//	}
//
//
//
//	/**
//	 * Tests alarm time set and get with negative number
//	 * @throws Throwable
//	 */
//	public void testAlarmTimeSetAndGetNegative() throws Throwable{
//		alarmObject = new Alarm(3, true, null, 1, "Yards", cp.getBaseContext());
//		alarmObject.setTime(-4356);
//		assertEquals(0, alarmObject.getTime());
//		alarmObject.setTime(-0);
//		assertEquals(0, alarmObject.getTime());
//	}
//
//
//	/**
//	 * Tests alarm constructor with vibration (false) 
//	 * @throws Throwable
//	 */
//	public void testAlarmConstructorVibrationFalse() throws Throwable{
//		alarmObject = new Alarm(3, false, null, 1, "Yards", cp.getBaseContext());
//		assertEquals(false,alarmObject.getVibration());
//	}
//
//
//
//	/**
//	 * Tests alarm constructor with vibration (true) 
//	 * @throws Throwable
//	 */
//	public void testAlarmConstructorVibrationTrue() throws Throwable{
//		alarmObject = new Alarm(3, true, null, 1, "Yards", cp.getBaseContext());
//		assertEquals(true, alarmObject.getVibration());
//	}
//
//
//	/**
//	 * Tests alarm constructor with ringtoneUri (null) 
//	 * @throws Throwable
//	 */
//	public void testAlarmConstructorRingtoneNull() throws Throwable{
//		alarmObject = new Alarm(3, false, null, 1, "Yards", cp.getBaseContext());
//		assertEquals(null ,alarmObject.getRingtoneUri());
//	}
//
//
//
//	/**
//	 * Tests alarm constructor with proximity, and getProximity
//	 * @throws Throwable
//	 */
//	public void testAlarmConstructorProximity() throws Throwable{
//		alarmObject = new Alarm(3, true, null, 1, "Yards", cp.getBaseContext());
//		assertEquals(1, alarmObject.getProximity());
//	}
//
//
//	/**
//	 * Tests alarm constructor with proximity, and getProximity
//	 * @throws Throwable
//	 */
//	public void testAlarmConstructorProximityNegative() throws Throwable{
//		alarmObject = new Alarm(3, true, null, -342, "Yards", cp.getBaseContext());
//		assertEquals(0, alarmObject.getProximity());
//	}
//
//
//
//
//	/**
//	 * Tests alarm proximity set and get
//	 * @throws Throwable
//	 */
//	public void testAlarmProximitySetAndGet() throws Throwable{
//		alarmObject = new Alarm(3, true, null, 1, "Yards", cp.getBaseContext());
//
//		alarmObject.setProximity(547);
//		assertEquals(547, alarmObject.getProximity());
//		alarmObject.setProximity(64);
//		assertEquals(64, alarmObject.getProximity());
//	}
//
//
//
//	/**
//	 * Tests alarm proximity set and get with negative number
//	 * @throws Throwable
//	 */
//	public void testAlarmProximitySetAndGetNegative() throws Throwable{
//		alarmObject = new Alarm(3, true, null, 1, "Yards", cp.getBaseContext());
//		alarmObject.setProximity(-435);
//		assertEquals(0, alarmObject.getProximity());
//		alarmObject.setTime(-0);
//		assertEquals(0, alarmObject.getProximity());
//	}
//
//
//	/**
//	 * Tests alarm proximityUnit Constructor
//	 * @throws Throwable
//	 */
//	public void testAlarmConstructorProximityUnit() throws Throwable{
//		alarmObject = new Alarm(3, true, null, 1, "Meters", cp.getBaseContext());
//		assertEquals("Meters",alarmObject.getProximityUnit());
//	}
//
//
//	/**
//	 * 
//	 * Tests alarm Constructor with null Context
//	 * @throws Throwable
//	 */
//	public void testAlarmConstructorContext() throws Throwable{
//		alarmObject = new Alarm(3, true, null, 1, "", cp.getBaseContext());
//		assertEquals(cp.getBaseContext(),alarmObject.getContext());
//	}
//
//
//
//	/**
//	 * Tests alarm timeConverter with negative number
//	 * @throws Throwable
//	 */
//	public void testAlarmTimeConverterNegative() throws Throwable{
//		alarmObject = new Alarm(0, false, null, 1, "", cp.getBaseContext());
//		String result = alarmObject.timeConverter(-45);
//		assertEquals("timeConverter(): Error! time should not be negative.", result);
//	}
//
//	/**
//	 *  Tests alarm timeConverter with 0 time
//	 * @throws Throwable
//	 */
//	public void testAlarmTimeConverterZero() throws Throwable{
//		alarmObject = new Alarm(0, false, null, 1, "", cp.getBaseContext());
//		String result = alarmObject.timeConverter(0);
//		assertEquals("0 seconds left until alarm goes off", result);
//	}
//
//
//	/**
//	 *  Tests alarm timeConverter with positive number (less than one minute)
//	 * @throws Throwable
//	 */
//	public void testAlarmTimeConverterSmall() throws Throwable{
//		alarmObject = new Alarm(0, false, null, 1, "", cp.getBaseContext());
//		String result = alarmObject.timeConverter(35);
//		assertEquals("35 seconds left until alarm goes off", result);
//	}
//
//
//	/**
//	 *  Tests alarm timeConverter with positive number (more than one minute, but less than 2 minutes)
//	 * @throws Throwable
//	 */
//	public void testAlarmTimeConverterLessThanTwoMinutes() throws Throwable{
//		alarmObject = new Alarm(0, false, null, 1, "", cp.getBaseContext());
//		String result = alarmObject.timeConverter(103);
//		assertEquals("1 minute  43 seconds left until alarm goes off", result);
//	}
//
//
//	/**
//	 *  Tests alarm timeConverter with positive number (more than two minutes, but less than 1 hour)
//	 * @throws Throwable
//	 */
//	public void testAlarmTimeConverterLessThanOneHour() throws Throwable{
//		alarmObject = new Alarm(0, false, null, 1, "", cp.getBaseContext());
//		String result = alarmObject.timeConverter(1932);
//		assertEquals("32 minutes  12 seconds left until alarm goes off", result);
//	}
//
//
//	/**
//	 *  Tests alarm timeConverter with positive number (more than one hour)
//	 * @throws Throwable
//	 */
//	public void testAlarmTimeConverterMoreThanOneHour() throws Throwable{
//		alarmObject = new Alarm(0, false, null, 1, "", cp.getBaseContext());
//		String result = alarmObject.timeConverter(4043);
//		assertEquals("1 hour(s)  7 minutes left until alarm goes off", result);
//	}
//
//
//	/**
//	 *  Tests alarm that works
//	 * @throws Throwable
//	 */
//	public void testAlarmWorks() throws Throwable {
//		alarmObject = new Alarm(3, false, null, 3, "", cp.getBaseContext());
//		assertEquals(false, alarmObject.getAlarmSuccessful());
//		alarmObject.setAlarm();
//		assertEquals(true, alarmObject.getAlarmSuccessful());
//	}
//
//
//
//
//
//} // AlarmTest class ends
