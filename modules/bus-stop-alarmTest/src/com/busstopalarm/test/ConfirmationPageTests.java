/**
 * Author: Huy Dang
 * Date: 02/23/2010
 * 
 * The Black box testing for the ConfirmationPage
 */
package com.busstopalarm.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import com.busstopalarm.ConfirmationPage;

public class ConfirmationPageTests extends 
					ActivityInstrumentationTestCase2<ConfirmationPage> {

	/**
	 * Specifies the location of the test at MainPage
	 */
	public ConfirmationPageTests() {
		super("com.busstopalarm", ConfirmationPage.class);
	}

	/**
	 * Initializes (if any) the set up for the test
	 */
	protected void setUp() throws Exception {
	}
	
	/**
	 * Tests if a checkbox for vibration setting is initial empty
	 * @throws Throwable
	 */
	public void test_Vibrate_beforeChecked() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final CheckBox checkBox= (CheckBox)
			cp.findViewById(com.busstopalarm.R.id.VibrateCheckbox);
		
		boolean isChecked = checkBox.isChecked();
		assertEquals("Vibrate box is not initially empty", false, isChecked);
	}
	
	/**
	 * Tests if a checkbox for vibration setting is filled after user selects.
	 * 
	 * @throws Throwable
	 */
	public void test_Vibrate_afterChecked() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final CheckBox checkBox= (CheckBox)
			cp.findViewById(com.busstopalarm.R.id.VibrateCheckbox);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				checkBox.performClick();
			}
		});
		
		boolean isChecked = checkBox.isChecked();
		assertEquals("can't check this vibrate box", true, isChecked);
	}
	
	/**
	 * TODO: Can't run any test about ringtones in emulator since emulator does
	 * not have any ringtones.
	 */
	
	/**
	 * Tests if the proximity bar is initially in the zero mode
	 */
	public void test_Proximity_isSelectable(){
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final SeekBar proxBar = (SeekBar) 
			cp.findViewById(com.busstopalarm.R.id.ProximityBar);
		
		boolean result = proxBar.isIndeterminate();
		assertEquals("the bar should be in zero mode", false, result);
	}
	
	/**
	 * Tests if the proximity bar can be moved to left from the zero mode
	 * 
	 * @throws Throwable
	 */
	public void test_Proximity_Scrolledleft() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final SeekBar proxBar = (SeekBar) 
			cp.findViewById(com.busstopalarm.R.id.ProximityBar);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				proxBar.incrementProgressBy(-5);
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}			
			}
		});
		
		int result = proxBar.getProgress();
		assertEquals("the bar should be in zero mode", 0, result);
	}
	
	/**
	 * Tests if the proximity bar can be moved to right from the zero mode
	 * 
	 * @throws Throwable
	 */
	public void test_Proximity_Scrolled5() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final SeekBar proxBar = (SeekBar) 
			cp.findViewById(com.busstopalarm.R.id.ProximityBar);
		
		runTestOnUiThread(new Runnable() {
			public void run() {
				proxBar.incrementProgressBy(5);
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		int result = proxBar.getProgress();
		assertEquals("the bar should be in 5 mode", 5, result);
	}
	
	/**
	 * Tests if the proximity bar can be moved to right the maximum amount 
	 * from the zero mode
	 * 
	 * @throws Throwable
	 */
	public void test_Proximity_ScrolledMax() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final SeekBar proxBar = (SeekBar) 
			cp.findViewById(com.busstopalarm.R.id.ProximityBar);
		final int max = proxBar.getMax();
		runTestOnUiThread(new Runnable() {
			public void run() {
				proxBar.incrementProgressBy(max);	
			}
		});
		
		int result = proxBar.getProgress();
		assertEquals("the bar should be in full mode", max, result);
	}
	
	/**
	 * Tests if the proximity bar can be moved to right more than the maximum 
	 * amount from the zero mode
	 * 
	 * @throws Throwable
	 */
	public void test_Proximity_ScrolledMax2() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final SeekBar proxBar = (SeekBar) 
			cp.findViewById(com.busstopalarm.R.id.ProximityBar);
		final int max = proxBar.getMax() + 20;
		runTestOnUiThread(new Runnable() {
			public void run() {
				proxBar.incrementProgressBy(max);
			}
		});
		
		int result = proxBar.getProgress();
		assertEquals("the bar should be in full mode", max-20, result);
	}
	
	/**
	 * Tests the proximity unit selection
	 * 
	 * @throws Throwable
	 */
	public void test_ProximityUnitMiles() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final Spinner proxUnit = (Spinner) 
			cp.findViewById(com.busstopalarm.R.id.ProximityUnits);
		final int pos = 0; final int a = proxUnit.pointToPosition(pos, 0);
		
		
		String result = proxUnit.getItemAtPosition(pos).toString();
		assertEquals("the proximity unit should be mile", "Miles", result);
	}
	
	/**
	 * Tests the proximity unit selection
	 * 
	 * @throws Throwable
	 */	
	public void test_ProximityUnitKms() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final Spinner proxUnit = (Spinner) 
			cp.findViewById(com.busstopalarm.R.id.ProximityUnits);
		final int pos = 1; 		
		
		String result = proxUnit.getItemAtPosition(pos).toString();
		assertEquals("the proximity unit should be Kilometers", 
					 "Kilometers", result);
	}
	
	/**
	 * Tests the proximity unit selection
	 * 
	 * @throws Throwable
	 */
	public void test_ProximityUnitMinutes() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final Spinner proxUnit = (Spinner) 
			cp.findViewById(com.busstopalarm.R.id.ProximityUnits);
		final int pos = 2; 		
		
		String result = proxUnit.getItemAtPosition(pos).toString();
		assertEquals("the proximity unit should be Minutes", "Minutes", result);
	}
	
	/**
	 * Tests if the cancel button is clickable
	 * 
	 * @throws Throwable
	 */
	public void test_CancelButton_Clickable() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final Button cancelButton = (Button) 
			cp.findViewById(com.busstopalarm.R.id.CancelButton);
		
		boolean isClicked = cancelButton.isClickable();
		assertEquals("can't click cancel button", true, isClicked);	
	}
	
	/**
	 * Tests if the SetAsFav button is clickable
	 * 
	 * @throws Throwable
	 */
	public void test_SaveButton_Clickable() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final Button saveButton = (Button) 
			cp.findViewById(com.busstopalarm.R.id.SetAsFavButton);
		
		boolean isClicked = saveButton.isClickable();
		assertEquals("can't click save button", true, isClicked);	
	}
	
	/**
	 * Tests if the Ok button is clickable
	 * @throws Throwable
	 */
	public void test_OKButton_Clickable() throws Throwable{
		ConfirmationPage cp = (ConfirmationPage) getActivity();
		final Button OKButton = (Button) 
			cp.findViewById(com.busstopalarm.R.id.OKButton);
		
		boolean isClicked = OKButton.isClickable();
		assertEquals("can't click OK button", true, isClicked);	
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}