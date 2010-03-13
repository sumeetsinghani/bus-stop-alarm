package com.busstopalarm.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import com.busstopalarm.BusRoute;
import com.busstopalarm.BusStop;
import com.busstopalarm.DataFetcher;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.busstopalarm.AlarmService;
import com.busstopalarm.BusRoute;
import com.busstopalarm.BusStop;
import com.busstopalarm.ConfirmationPage;
import com.busstopalarm.HelpPage;
import com.busstopalarm.ItemizedOverlayHelper;
import com.busstopalarm.LocationListPage;
import com.busstopalarm.MainPage;
import com.busstopalarm.MapPage;
import com.busstopalarm.SettingsObj;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.jayway.android.robotium.solo.Solo;

public class AutoTest extends ActivityInstrumentationTestCase2<MainPage> {

	DataFetcher df = new DataFetcher();


	public AutoTest() {
		super("com.busstopalarm", MainPage.class);
	}

	protected void setUp() throws Exception {
	}

	//goes to help page then to main page then settings page
	public void testHelpPageAndRouteSetAlarm() throws Throwable{
		Solo solo = new Solo(getInstrumentation(), getActivity());

		//go to help page
		solo.pressMenuItem(2);
		solo.clickOnButton("Ok");

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
				proxBar.incrementProgressBy(4);
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

	//this test goes to settings page and tests if settings were saved
//	public void testSavingSettingConfirmationPage() throws Throwable{
//		Solo solo = new Solo(getInstrumentation(), getActivity());
//
//		//click on Favorites
//		solo.clickOnButton("Favorites");
//		solo.assertCurrentActivity("Supposed to be Favorites page", LocationListPage.class);
//
//		ArrayList<View> views = solo.getViews();
//		if (views.size() > 0) {
//			solo.clickOnScreen(views.get(1));
//		}
//		ConfirmationPage cp = (ConfirmationPage) solo.getCurrentActivity();
//		solo.assertCurrentActivity("Supposed to be confirmation page", ConfirmationPage.class);
//
//		//check vibrate	box
//		final CheckBox checkBox= (CheckBox)
//		cp.findViewById(com.busstopalarm.R.id.VibrateCheckbox);
//		runTestOnUiThread(new Runnable() {
//			public void run() {
//				checkBox.performClick();
//			}
//		});
//		boolean VibCheck = checkBox.isChecked();
//
//		//get spinners
//		ArrayList<Spinner> spinners = solo.getCurrentSpinners();
//
//		//change ringtone
//		solo.pressSpinnerItem(0, (int)Math.random() * spinners.get(0).getCount());
//		String oldRingtone = spinners.get(0).getSelectedItem().toString();
//
//		//change proximity
//		solo.pressSpinnerItem(1, (int)(Math.random() * 100));
//		int oldProximity = Integer.parseInt(spinners.get(1).getSelectedItem().toString());
//
//		//change proximity units
//		final Spinner rSelect = (Spinner) 
//		cp.findViewById(com.busstopalarm.R.id.ProximityUnits);
//		runTestOnUiThread(new Runnable() {
//			public void run() {
//				rSelect.setSelection(1);
//			}
//		});
//		String oldProxUnits = spinners.get(2).getSelectedItem().toString();
//
//		//save settings as exit
//		solo.clickOnButton("Save as favorite");
//		cp.finish();
//
//		//load settings
//		cp = (ConfirmationPage) solo.getCurrentActivity();
//		solo.assertCurrentActivity("Supposed to be confirmation page", ConfirmationPage.class);
//
//		SettingsObj settings = cp.getSettings();
//
//		boolean vibEquals =  (VibCheck == settings.getVibration());
//		boolean ringEquals = (oldRingtone.equals(settings.getRingtoneName()));  
//		boolean proxiEquals = (oldProximity == settings.getProximity());
//		boolean proxUnits = (oldProxUnits.equals(settings.getProximityUnit()));
//
//		//check if settings were loaded correctly
//		assertTrue(vibEquals);
//		assertTrue(ringEquals);
//		assertTrue(proxiEquals);
//		assertTrue(proxUnits);
//	}


	//test for most recent routes
	public void testRecentRouteSetAlarm() throws Throwable{
		Solo solo = new Solo(getInstrumentation(), getActivity());
		ArrayList<View> views = solo.getViews();
		if (views.size() > 0) {
			solo.clickOnScreen(views.get(1));
		}
		//solo.assertCurrentActivity("must be MapPage", MapPage.class);
		Thread.sleep(5000);

		solo = new Solo(getInstrumentation(), getActivity());
		//select busstop
		ArrayList<View> bViews = solo.getViews();
		if (bViews.size() > 0) {
			//MapView map = (MapView) solo.getCurrentActivity().findViewById(com.busstopalarm.R.id.mapview);
			//ArrayList<Overlay> overlays = (ArrayList<Overlay>) map.getOverlays();
			//((ItemizedOverlayHelper)overlays.get(0)).onTap(0);
			solo.clickOnScreen(bViews.get(2));
		}
		
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
				proxBar.incrementProgressBy(4);
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
		solo.clickOnText("OK");

		solo.assertCurrentActivity("must be MainPage", MainPage.class);
		boolean service = cp.stopService(new Intent(cp, AlarmService.class));
		assertTrue("alarm service should be running", service);
	}


	//test for major locations
	public void testMajorLocationSetAlarm() throws Throwable{
		MainPage activity = getActivity();
		Solo solo = new Solo(getInstrumentation(), activity);
		solo.assertCurrentActivity("must be MainPage", MainPage.class);

		solo.clickOnButton("Major Locations");

		solo.assertCurrentActivity("must be location list page", LocationListPage.class);
		ArrayList<View> views = solo.getViews();
		if (views.size() > 0) {
			solo.clickOnScreen(views.get((int)(Math.random() * views.size())));
		}

		solo.assertCurrentActivity("must be ConfirmationPage", ConfirmationPage.class);
		int items = solo.getCurrentSpinners().get(0).getCount();
		solo.pressSpinnerItem(0, (int)(Math.random() * items));
		solo.clickOnButton("OK");

		solo.assertCurrentActivity("must be MainPage", MainPage.class);
		boolean service = activity.stopService(new Intent(activity, AlarmService.class));
		assertTrue("alarm service should be running", service);
	}

	//test for favorite locations
	public void testFavLocationSetAlarm() throws Throwable{
		MainPage activity = getActivity();
		Solo solo = new Solo(getInstrumentation(), activity);
		solo.assertCurrentActivity("must be MainPage", MainPage.class);

		solo.clickOnButton("Favorites");

		solo.assertCurrentActivity("must be location list page", LocationListPage.class);
		ArrayList<View> views = solo.getViews();
		if (views.size() > 0) {
			solo.clickOnScreen(views.get((int)(Math.random() * views.size())));
		}

		solo.assertCurrentActivity("must be ConfirmationPage", ConfirmationPage.class);
		int items = solo.getCurrentSpinners().get(0).getCount();
		solo.pressSpinnerItem(0, (int)(Math.random() * items));
		solo.clickOnButton("OK");

		solo.assertCurrentActivity("must be MainPage", MainPage.class);
		boolean service = activity.stopService(new Intent(activity, AlarmService.class));
		assertTrue("alarm service should be running", service);
	}



}