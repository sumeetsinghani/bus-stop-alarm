package com.busstopalarm;
/**
 * Location List Page list a bunch of stops that can be selected used to
 * directly set an alarm without going through the map.
 * 
 * @author David Nufer
 */


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import android.widget.AdapterView.*;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

public class LocationListPage extends ListActivity {

	private static final String TAG = "inLocationListPage";
	/**
	 * static constants for determining if the list is for favorites
	 * or major locations.
	 */
	public static final int FAVORITES = 1;
	public static final int MAJOR = 2;

	private static final int NUM_ENTRIES_TO_FETCH = 20;

	public BusDbAdapter mBusDbHelper;
	public Cursor mCursor;


	/**
	 * a list is either LocatonListPage.FAVORTIES or LocatonListPage.MAJOR 
	 */
	private int listType;

	private ArrayList<HashMap<String, String>> locationList = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter listAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Try with DB
		mBusDbHelper = new BusDbAdapter(this);
		mBusDbHelper.open();


		listType = getIntent().getIntExtra("listType", 0);
		if (listType == 0) {
			Toast.makeText(this, "Internal error", Toast.LENGTH_LONG);
			Log.v(TAG, "Unable to get listType");
			finish();
		}

		listAdapter = new SimpleAdapter(this, locationList, R.layout.list_item, new String[] {"routeID", "stopName"}, new int[] {R.id.listItemRouteID, R.id.listItemName});
		setListAdapter(listAdapter);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(view.getContext(), ConfirmationPage.class);
				HashMap<String, String> item = locationList.get(position);
				DataFetcher df = new DataFetcher();
				try {
					BusStop b = df.getStopById(Integer.parseInt(item.get("stopID").split("_")[1]));
					i.putExtra("busstop", b);
					i.putExtra("busroute", item.get("routeID"));
					startActivity(i);
					finish();
					// if an exception occurs, nothing happens (for now).
				} catch (NumberFormatException e) {
					Log.v(TAG, "Error parsing stop id!");
					e.printStackTrace();
				} catch (IOException e) {
					Log.v(TAG, "Error fetching info!");
					e.printStackTrace();
				}
			}
		});

		registerForContextMenu(getListView());

		// populate list items
		fillList(mBusDbHelper);
		mBusDbHelper.close();
	}

	/**
	 * fills the list with stops from the local database
	 * 
	 * @param db the database adapter to use
	 */
	private void fillList(BusDbAdapter db) {
		Cursor c;
		if (listType == FAVORITES) {
			c = db.getFavoriteDest(NUM_ENTRIES_TO_FETCH); 
		} else { // listType == MAJOR
			c = db.getMajorDest(NUM_ENTRIES_TO_FETCH);
		}	
		int stopIDIndex = c.getColumnIndex("stop_id");
		int stopDescIndex = c.getColumnIndex("stop_desc");
		int routeIDIndex = c.getColumnIndex("route_id");
		if (c != null) {
			for (int i = 0; i < c.getCount(); i++) {
				HashMap<String, String> item = new HashMap<String, String>();

				String stopID = c.getString(stopIDIndex);
				String stopName = c.getString(stopDescIndex);
				String route = c.getString(routeIDIndex);

				item.put("stopID", stopID);
				item.put("stopName", stopName);
				item.put("routeID", route);
				c.moveToNext();
				locationList.add(item);
			}
			listAdapter.notifyDataSetChanged();
		}
	}

	private static final int SET_STOP_OPTION = 10;
	private static final int REMOVE_STOP_OPTION = 11;
	private static final int CANCEL = 12;

	/**
	 * creates the context menu for items when they get a long click
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, 
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		menu.add(0, SET_STOP_OPTION, SET_STOP_OPTION, "Set alarm for this stop");
		menu.add(0, REMOVE_STOP_OPTION, REMOVE_STOP_OPTION, "Remove this stop");
		menu.add(0, CANCEL, CANCEL, "Cancel");
	}

	/**
	 * actions for the context menu
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int id = (int)getListAdapter().getItemId(info.position);

		// sets an alarm for the selected stop
		switch(item.getItemId()) {
		case SET_STOP_OPTION:
			Intent i = new Intent(getApplicationContext(), ConfirmationPage.class);
			HashMap<String, String> busItem = locationList.get(id);
			DataFetcher df = new DataFetcher();
			try {
				BusStop b = df.getStopById(Integer.parseInt(busItem.get("stopID").split("_")[1]));
				i.putExtra("busstop", b);
				i.putExtra("busroute", busItem.get("routeID"));
				startActivity(i);
				finish();
				// if an exception occurs, nothing happens (for now).
			} catch (NumberFormatException e) {
				Log.v(TAG, "Error parsing stop id!");
				e.printStackTrace();
			} catch (IOException e) {
				Log.v(TAG, "Error fetching info!");
				e.printStackTrace();
			}
			break;	
			// removes the selected stop from the list
		case REMOVE_STOP_OPTION:
			locationList.remove(id);
			listAdapter.notifyDataSetChanged();
			break;
		case CANCEL:
			break;
		default:
			break;
		}
		return true;
	}
}