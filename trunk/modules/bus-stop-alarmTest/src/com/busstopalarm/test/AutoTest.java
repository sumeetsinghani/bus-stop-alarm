/**
 * This class consists of 6 automated tests. 
 * 1) This test searches for route and then by selecting busstop check if the alarm is set
 * 2) This test goes to settings page and tests if settings were saved
 * 3) This test checks most recent routes and the alarm
 * 4) This test goes to helppage and checks if it returns to mainpage
 * 5) This test goes to major locations and sets the alarm
 * 6) This test goes to favorite locations and sets the alarm
 * 
 * @author Orkhan Muradov
 */


package com.busstopalarm.test;


import java.util.ArrayList;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import com.busstopalarm.AlarmService;
import com.busstopalarm.ConfirmationPage;
import com.busstopalarm.HelpPage;
import com.busstopalarm.LocationListPage;
import com.busstopalarm.MainPage;
import com.busstopalarm.MapPage;
import com.busstopalarm.SettingsObj;
import com.busstopalarm.SettingsPage;
import com.jayway.android.robotium.solo.Solo;

public class AutoTest extends ActivityInstrumentationTestCase2<MainPage> {

	public AutoTest() {
		super("com.busstopalarm", MainPage.class);
	}

	protected void setUp() throws Exception {
	}
	
	/**
	 * This test enters route in the search box then selects a busstop for that route.
	 * After that it changes settings randomly and checks if the alarm was set
	 * @throws Throwable
	 */
	public void testRouteSetAlarm() throws Throwable{
		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.enterText(1, "3 0");
		//go to #30 route
		solo.clickOnButton("Go");
		Thread.sleep(5000);
		solo.assertCurrentActivity("Supposed to be map page", MapPage.class);

		//select busstop
		ArrayList<View> views = solo.getViews();
		if (views.size() > 0)
			solo.clickOnScreen(views.get(0));

		//set alarm, go to confirmation page
		solo.clickOnButton("Set Alarm");
		ConfirmationPage cp = (ConfirmationPage) solo.getCurrentActivity();
		solo.assertCurrentActivity("Supposed to be confirmation page", ConfirmationPage.class);

		//check vibrate
		final CheckBox checkBox= (CheckBox)
		cp.findViewById(com.busstopalarm.R.id.VibrateCheckbox);
		runTestOnUiThread(new Runnable() {
			public void run() {
				checkBox.performClick();
			}
		});

		//change ringtone
		final Spinner rSelect = (Spinner) 
		cp.findViewById(com.busstopalarm.R.id.RingtoneSelector);
		runTestOnUiThread(new Runnable() {
			public void run() {
				rSelect.setSelection(2);
			}
		});

		//change proximity
		final SeekBar proxBar = (SeekBar) 
		cp.findViewById(com.busstopalarm.R.id.ProximityBar);
		runTestOnUiThread(new Runnable() {
			public void run() {
				proxBar.incrementProgressBy(450);
			}
		});

		//change proximity units
		final Spinner rSelect2 = (Spinner) 
		cp.findViewById(com.busstopalarm.R.id.ProximityUnits);
		runTestOnUiThread(new Runnable() {
			public void run() {
				rSelect2.setSelection(1);
			}
		});

		solo.clickOnButton("OK");
		solo.assertCurrentActivity("must be MainPage", MainPage.class);
		boolean service = cp.stopService(new Intent(cp, AlarmService.class));
		assertTrue("alarm service should be running", service);
	}

	/**
	 * This test enters setting page changes ringtone randomly, checks vibration, changes proximity 
	 * and selects proximity units randomly. After that test presses on save settings button and returns
	 * to main menu. To check if the settings were saved test goes to settings page again and verifies
	 * if all the settings were saved correctly.
	 * @throws Throwable
	 **/
	public void testSavingSettingsPage() throws Throwable{
		MainPage activity = getActivity();
		Solo solo = new Solo(getInstrumentation(), activity);
		solo.assertCurrentActivity("must be MainPage", MainPage.class);
		
		//go to settings page
		solo.pressMenuItem(3);
		solo.assertCurrentActivity("Supposed to be settings page", SettingsPage.class);
		SettingsPage cp = (SettingsPage) solo.getCurrentActivity();
		
		//check vibrate	box
		final CheckBox checkBox= (CheckBox)
		cp.findViewById(com.busstopalarm.R.id.VibrateCheckbox);
		runTestOnUiThread(new Runnable() {
			public void run() {
				checkBox.performClick();
			}
		});
		boolean VibCheck = checkBox.isChecked();

		//get spinners
		ArrayList<Spinner> spinners = solo.getCurrentSpinners();

		//change ringtone
		int ringItems = solo.getCurrentSpinners().get(0).getCount();
		solo.pressSpinnerItem(0, (int)(Math.random() * ringItems));
		String oldRingtone = spinners.get(0).getSelectedItem().toString();

		//change proximity units
		int proxItems = solo.getCurrentSpinners().get(1).getCount();
		solo.pressSpinnerItem(1, (int)(Math.random() * proxItems));
		String oldProxUnits = spinners.get(1).getSelectedItem().toString();
		
		//change proximity
		final SeekBar proxBar = (SeekBar) 
		cp.findViewById(com.busstopalarm.R.id.ProximityBar);
		runTestOnUiThread(new Runnable() {
			public void run() {
				proxBar.incrementProgressBy(230);
			}
		});
		int oldProximity = proxBar.getProgress();

		//save settings, go back, and go to settings again
		solo.clickOnButton("Save Settings");
		solo.clickOnButton("   Go Back    ");
		solo.pressMenuItem(3);
		solo.assertCurrentActivity("Supposed to be settings page", SettingsPage.class);

		SettingsObj settings = cp.getSettings();

		boolean vibEquals =  (VibCheck == settings.getVibration());
		boolean ringEquals = (oldRingtone.equals(settings.getRingtoneName()));  
		boolean proxUnits = (oldProxUnits.equals(settings.getProximityUnit()));
		boolean proximity = (oldProximity == settings.getProximity());
		
		//check if settings were loaded correctly
		assertTrue(vibEquals);
		assertTrue(ringEquals);
		assertTrue(proxUnits);
		assertTrue(proximity);
	}

	/**
	 * pre: Recent route list is not empty
	 * This test selects most recently selected route and then selects a busstop for that route. Then test 
	 * checks if alarm is set after randomly changing settings and pressing OK button to start the alarm
	 * @throws Throwable
	 */
	public void testRecentRouteSetAlarm() throws Throwable{
		Solo solo = new Solo(getInstrumentation(), getActivity());
		ArrayList<View> views = solo.getViews();
		if (views.size() > 0) {
			solo.clickOnScreen(views.get(0));
		} 
		Solo solo2 = new Solo(getInstrumentation(), getActivity());
		Thread.sleep(2000);
		//select busstop
		ArrayList<View> bViews = solo2.getViews();
		if (bViews.size() > 0) {
			solo2.clickOnScreen(bViews.get(0));
		}
		Thread.sleep(2000);
		
		//set alarm, go to confirmation page
		solo.clickOnButton("Set Alarm");
		ConfirmationPage cp = (ConfirmationPage) solo.getCurrentActivity();
		solo.assertCurrentActivity("must be Confirmation Page", ConfirmationPage.class);

		//check vibrate
		final CheckBox checkBox= (CheckBox)
		cp.findViewById(com.busstopalarm.R.id.VibrateCheckbox);
		runTestOnUiThread(new Runnable() {
			public void run() {
				checkBox.performClick();
			}
		});

		//change ringtone
		final Spinner rSelect = (Spinner) 
		cp.findViewById(com.busstopalarm.R.id.RingtoneSelector);
		runTestOnUiThread(new Runnable() {
			public void run() {
				rSelect.setSelection(2);
			}
		});

		//change proximity
		final SeekBar proxBar = (SeekBar) 
		cp.findViewById(com.busstopalarm.R.id.ProximityBar);
		runTestOnUiThread(new Runnable() {
			public void run() {
				proxBar.incrementProgressBy(400);
			}
		});

		//change proximity units
		final Spinner rSelect2 = (Spinner) 
		cp.findViewById(com.busstopalarm.R.id.ProximityUnits);
		runTestOnUiThread(new Runnable() {
			public void run() {
				rSelect2.setSelection(1);
			}
		});
		//set the alarm
		solo.clickOnButton("OK");

		solo.assertCurrentActivity("must be MainPage", MainPage.class);
		boolean service = cp.stopService(new Intent(cp, AlarmService.class));
		assertTrue("alarm service should be running", service);
	}

	/**
	 * This test verifies if Major location works by 
	 * selecting major location and setting the alarm.
	 * @throws Throwable
	 */
	public void testMajorLocationSetAlarm() throws Throwable{
		MainPage activity = getActivity();
		Solo solo = new Solo(getInstrumentation(), activity);
		solo.assertCurrentActivity("must be MainPage", MainPage.class);

		//go to Major Locations
		solo.clickOnButton("Major Locations");
		Thread.sleep(2000);
		solo.assertCurrentActivity("must be location list page", LocationListPage.class);
		ArrayList<View> views = solo.getViews();
		if (views.size() > 0) {
			solo.clickOnScreen(views.get((int)(Math.random() * views.size())));
		}
		Thread.sleep(2000);
		//Goes to confirmation page by selecting route
		solo.assertCurrentActivity("must be ConfirmationPage", ConfirmationPage.class);

		//change ringtone
		int items = solo.getCurrentSpinners().get(0).getCount();
		solo.pressSpinnerItem(0, (int)(Math.random() * items));

		solo.clickOnButton("OK");
		solo.assertCurrentActivity("must be MainPage", MainPage.class);

		//testing if alarm is working
		boolean service = activity.stopService(new Intent(activity, AlarmService.class));
		assertTrue("alarm service should be running", service);
	}

	
	/**
	 * This test verifies if Favorites page works by selecting favorite 
	 * location and setting the alarm.
	 * @throws Throwable
	 */
	public void testFavLocationSetAlarm() throws Throwable{
		MainPage activity = getActivity();
		Solo solo = new Solo(getInstrumentation(), activity);
		solo.assertCurrentActivity("must be MainPage", MainPage.class);

		solo.clickOnButton("Favorites");

		Thread.sleep(2000);
		solo.assertCurrentActivity("must be location list page", LocationListPage.class);
		ArrayList<View> views = solo.getViews();
		if (views.size() > 0) {
			solo.clickOnScreen(views.get((int)(Math.random() * views.size())));
		}
		Thread.sleep(2000);
		solo.assertCurrentActivity("must be ConfirmationPage", ConfirmationPage.class);

		//change ringtone
		int ringtoneItems = solo.getCurrentSpinners().get(0).getCount();
		solo.pressSpinnerItem(0, (int)(Math.random() * ringtoneItems));

		//change proximity units
		int proxItems = solo.getCurrentSpinners().get(1).getCount();
		solo.pressSpinnerItem(1, (int)(Math.random() * proxItems));

		//set the alarm
		solo.clickOnButton("OK");
		solo.assertCurrentActivity("must be MainPage", MainPage.class);

		//testing if alarm is working
		boolean service = activity.stopService(new Intent(activity, AlarmService.class));
		assertTrue("alarm service should be running", service);
	}

	/**
	 * This test goes to help page verifies if it is on a write page and then returns to main menu by 
     * pressing OK button
	 * @throws Throwable
	 */
	public void testHelpMainPages() throws Throwable{	
		Solo solo = new Solo(getInstrumentation(), getActivity());

		//go to help page
		solo.pressMenuItem(2);
		solo.assertCurrentActivity("Not in help page", HelpPage.class);
		solo.clickOnButton("Ok");
		
		//test for main page
		Solo solo2 = new Solo(getInstrumentation(), getActivity());
		solo2.assertCurrentActivity("Not in main page", MainPage.class);
	}
	
}  //end of class

