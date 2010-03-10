package com.busstopalarm.test;

import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;
import com.busstopalarm.BusRoute;
import com.busstopalarm.BusStop;
import com.busstopalarm.DataFetcher;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.busstopalarm.BusRoute;
import com.busstopalarm.BusStop;
import com.busstopalarm.ConfirmationPage;
import com.busstopalarm.MainPage;
import com.busstopalarm.MapPage;
import com.jayway.android.robotium.solo.Solo;

public class AutoTest extends ActivityInstrumentationTestCase2<MainPage> {

	DataFetcher fetcher = new DataFetcher();


	public AutoTest() {
		super("com.busstopalarm", MainPage.class);
	}

	protected void setUp() throws Exception {
	}

	//goes to help page then to main page then settings page
	public void test1() throws Throwable{
		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.pressMenuItem(2);
		solo.clickOnButton("Ok");
		solo.enterText(1, "3 0");
		//go to #72 route
		solo.clickOnButton("Go");
		MapPage mp = (MapPage) solo.getCurrentActivity();
		solo.clickOnImage(3);
		
		//press on a busstop
		mp.getCurrentFocus().focusSearch(1);
		mp.getCurrentFocus().performClick();
		
		BusStop expected = new BusStop();
		expected.setStopId("1_9990");
		expected.setDirection("W");
		expected.setLatitude(47.6690254);
		expected.setLongitude(-122.279846);
		expected.setLocationType("0");
		expected.setName("NE 55th St &amp; 45th Ave NE");
		expected.setCode("9990");
		//assertEquals(expected, actual);
		//go to settings page
		solo.pressMenuItem(2);

	}

	//this test goes to settings page and tests if settings were saved
	public void test2() throws Throwable{
		Solo solo = new Solo(getInstrumentation(), getActivity());
		MainPage mp = (MainPage) getActivity();
		
		//click on Settings in menu
		solo.pressMenuItem(3);
		ConfirmationPage cp = (ConfirmationPage) solo.getCurrentActivity();
		
		//check vibrate	box
		final CheckBox checkBox= (CheckBox)
		cp.findViewById(com.busstopalarm.R.id.VibrateCheckbox);
		runTestOnUiThread(new Runnable() {
			public void run() {
				checkBox.performClick();
			}
		});
		boolean VibCheck = checkBox.isChecked();
		
		//change ringtone
		cp.loadRecentSettings();
		final Spinner rSelect = (Spinner) 
		cp.findViewById(com.busstopalarm.R.id.RingtoneSelector);
		runTestOnUiThread(new Runnable() {
			public void run() {
				rSelect.setSelection(2);
			}
		});
		String oldRingtone = rSelect.getSelectedItem().toString();
		
		//change proximity
		final Spinner PSelect = (Spinner) 
		cp.findViewById(com.busstopalarm.R.id.ProximityUnits);
		runTestOnUiThread(new Runnable() {
			public void run() {
				PSelect.setSelection(1);
			}
		});
		int oldProximity = Integer.parseInt(rSelect.getSelectedItem().toString());
		
		//change units
		final SeekBar proxBar = (SeekBar) 
		cp.findViewById(com.busstopalarm.R.id.ProximityBar);
		runTestOnUiThread(new Runnable() {
			public void run() {
				proxBar.incrementProgressBy(4);
			}
		});
		int OldProxUnits = proxBar.getProgress();
		
		//save settings as exit
		solo.clickOnButton("Save as favorite");
		cp.finish();
		
		//load settings
		cp = (ConfirmationPage) solo.getCurrentActivity();
		cp.loadRecentSettings();
		boolean vibEquals =  (VibCheck == cp.vibration);
		boolean ringEquals = (oldRingtone.equals(cp.dataRingtone));  
		boolean proxiEquals = (oldProximity == cp.proximity);
		boolean proxUnits = (OldProxUnits == Integer.parseInt(cp.proximityUnit));
	
		//check if settings were loaded correctly
		assertTrue(vibEquals && ringEquals && proxiEquals && proxUnits);
	}
	
	//test for most recent routes
	public void test3() throws Throwable{
		Solo solo = new Solo(getInstrumentation(), getActivity());
		MainPage mp = (MainPage) solo.getCurrentActivity();
		mp.getCurrentFocus().focusSearch(0).performClick();
		//final TextView recentItem = new TextView(mp);
		//recentItem.performClick();
	}
	
	//test for major locations
	public void test4() throws Throwable{
		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.clickOnButton("Major Locations");
	}
	
	//test for favorite locations
	public void test5() throws Throwable{
		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.clickOnButton("Favorites");
		
	}
	
	
}

