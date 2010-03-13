/**
 * Main page of the Bus Stop Alarm
 * 
 * @author David Nufer, Orkhan Muradov, Pyong Byon
 */

package com.busstopalarm;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainPage extends Activity {
	
	private static final String TAG = "inMainPage";
	// The number of most recent routes to display.
	private static final int numRecentRoutes = 5;
	// This number is arbitrary - this is used in startActivityForResult() and
	// finishActivity() calls to determine whether the user went to confirmaton
	// from main page or map page.
	public static final int MAIN_CONFIRM_TRANSITION = 456;
	
	// Add this DB for validating bus route
	public BusNumDbAdapter mBusNumDbHelper;
	private static boolean dbLoaded = false;
	private static List<Integer> validBusRoutes = null;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mBusNumDbHelper = new BusNumDbAdapter(this);
		mBusNumDbHelper.open();

		setupRouteSearchButton();
		setupFavoriteButton();
		setupMajorLocsButton();
		displayRecentRoutes();
		// We only need read in this file once.
		if (validBusRoutes == null) {			
			Toast.makeText(getApplicationContext(), 
					"Loading bus route data, please wait...", 
					Toast.LENGTH_LONG).show();
			initValidBusRoutes();
		}
		
		mBusNumDbHelper.close();
		
	}
	
	/**
	 * Initializes validBusRoutes, the list of valid bus routes.
	 */
	private void initValidBusRoutes() {
	
		try {
			// Reading both King County and Sound Transit tables.
			mBusNumDbHelper.readDbFile(BusNumDbAdapter.KINGCOUNTY_DB);
			mBusNumDbHelper.readDbFile(BusNumDbAdapter.SOUNDTRANSIT_DB);
			Log.v(TAG, "Successful read Num DB for valid bus routes!");
		} catch (IOException e) {
			// Do nothing, getBusRoutesList will just return an empty list.
			Log.v(TAG, "Failed to read Num DB for valid bus routes!");
		}
		
		// Read from BusNumDb to get the list of valid bus routes.
		validBusRoutes = mBusNumDbHelper.getBusRoutesList();
		Log.v(TAG, validBusRoutes.toString());
	}
	
	/**
	 * Sets up the route search button. Called by onCreate() method.
	 */
	private void setupRouteSearchButton() {
		final Button RouteSearchButton = (Button) findViewById(R.id.RouteSearchButton);
		// search button behavior
		RouteSearchButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				String routeText = 
					((EditText)findViewById(R.id.RouteSearchBox)).getText().toString();
				int routeNumInput;
				
				try {
					// If the route number input is in any way invalid, we
					// throw an exception.
					routeNumInput = Integer.parseInt(routeText);
					if (Collections.binarySearch(validBusRoutes, routeNumInput) < 0) {
						throw new IllegalArgumentException();
					}
				} catch (IllegalArgumentException e) {
					Toast t = Toast.makeText(v.getContext(),
							"Invalid Route Number", Toast.LENGTH_LONG);
					t.show();
					return;
				}
							
				showTransitionToastMsg(routeNumInput);
				
				Intent i = new Intent(v.getContext(), MapPage.class);
				i.putExtra("routeID", "1_" + routeText);
				startActivityForResult(i,MAIN_CONFIRM_TRANSITION);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG, "onActivityResult" + requestCode + " " + resultCode);
		if (requestCode == MAIN_CONFIRM_TRANSITION) {
			if (resultCode == RESULT_OK) {
				finish();
			}
		}
	}
	
	/**
	 * Sets up the favorite button. Called by onCreate() method.
	 */
	private void setupFavoriteButton() {
		// favorite button behavior
		final Button favButton = (Button) findViewById(R.id.FavButton);
		favButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), LocationListPage.class);
				i.putExtra("listType", LocationListPage.FAVORITES);
				startActivityForResult(i,MAIN_CONFIRM_TRANSITION);
			}
		});
	}
	
	/**
	 * Sets up the major locations button. Called by onCreate() method.
	 */
	private void setupMajorLocsButton() {
		// major button behavior
		final Button majorButton = (Button) findViewById(R.id.MajorButton);
		majorButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), LocationListPage.class);
				i.putExtra("listType", LocationListPage.MAJOR);
				startActivityForResult(i,MAIN_CONFIRM_TRANSITION);
			}
		});
	}
	
	/**
	 * Retrieves from the local database recent locations, and displays them
	 * on the page.
	 */
	private void displayRecentRoutes() {
		
		BusDbAdapter ad = new BusDbAdapter(getApplicationContext());
		ad.open();
		

		if (!dbLoaded) {
			try { 
				// These load entries from file into the database every time...
				ad.readDbFile(BusDbAdapter.MAJORDB);
				dbLoaded = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Gets the five most recent locations.
		ad.initRecentTable();
		Cursor recent = ad.getRecentDest(numRecentRoutes);
		LinearLayout recentList = 
			(LinearLayout) findViewById(R.id.recent_routes);
		int routeIndex = recent.getColumnIndex("route_id");
		int routeDescIndex = recent.getColumnIndex("route_desc");
		
		while (!recent.isAfterLast()) {
			final int routeNumber = 
				Integer.parseInt(recent.getString(routeIndex));
			Log.v(TAG, "main page got recent route number: " + routeNumber);
			final TextView recentItem = new TextView(this);
			recentItem.setClickable(true);
			recentItem.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {					
					showTransitionToastMsg(routeNumber);
					Intent i = new Intent(v.getContext(), MapPage.class);
					i.putExtra("routeID", "1_" + routeNumber);
					startActivity(i);
				}
			});
			recentItem.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
			recentItem.setText("Route " + recent.getString(routeIndex) + ", "
					+ recent.getString(routeDescIndex));
			recentList.addView(recentItem);
			
			recent.moveToNext();
		}
		
		ad.close();
	}
	
	private void showTransitionToastMsg(int routeNumber) {
		String s = "Drawing route " + routeNumber + " on map. " +
		"Please wait...";
		Toast t = Toast.makeText(this, s, Toast.LENGTH_LONG);
		t.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 1, "Confirmation");
		menu.add(0, 2, 2, "Help/About");
		menu.add(0, 3, 3, "Settings");
		menu.add(0, 4, 4, "Exit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			BusStop stop = getIntent().getParcelableExtra("busStopSaved");
			
			Log.v(TAG, "bus stop:  " + stop);
			
			if (stop != null) {
			  finish();
			  break;
			}
			 // stop == null
			Toast.makeText(this, "You have to choose Destination first!", 
					Toast.LENGTH_LONG).show();
			break;
		case 2:
			final Intent helpPage = new Intent(this, HelpPage.class);
			startActivity(helpPage);
			break;
		case 3:
			final Intent Settings = new Intent(this, SettingsPage.class);
			startActivity(Settings);
			break;
		case 4:
			System.exit(0);
			break;
		}
		return false;

	}
}