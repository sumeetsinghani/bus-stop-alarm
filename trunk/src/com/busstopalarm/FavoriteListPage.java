package com.busstopalarm;

import java.util.ArrayList;
import java.util.HashMap;

import com.busstopalarm.R;
import com.busstopalarm.DatabaseHelper;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class FavoriteListPage extends ListActivity {

	ArrayList<HashMap<String, String>> FavoriteList = new ArrayList<HashMap<String,String>>();
	SimpleAdapter listAdapter;
	DatabaseHelper events;
	Cursor cursor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		events = new DatabaseHelper(this);  
		try {
			cursor = getEvents();

			listAdapter = new SimpleAdapter(this, FavoriteList, R.layout.fav_item, new String[] {"name"}, new int[] {R.id.listItemName});
			setListAdapter(listAdapter);

			ListView lv = getListView();
			lv.setTextFilterEnabled(true);

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// When clicked, show a toast with the TextView text
					Intent i = new Intent(view.getContext(), ConfirmationPage.class);
					i.putExtra("name", FavoriteList.get(position).get("name"));
					startActivity(i);
					finish();
				}
			});
			fillList(cursor);
		} finally {
			events.close();
		}

	}

	/** 
	 * temporary method for ZFR.  Should be removed when real data can be put into list
	 */
	private void fillList(Cursor cursor) {
		while (cursor.moveToNext()) { 
			String latitude = cursor.getString(0);
			String longitude = cursor.getString(1);
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("name", "coordinates: " + latitude + " " + longitude);
			FavoriteList.add(item);
		}
		listAdapter.notifyDataSetChanged();
	}

	private Cursor getEvents() {
		SQLiteDatabase db = events.getReadableDatabase();
		String[] foo = new String[1];
		foo[0]="LOCATION";
		Cursor cursor = db.rawQuery("SELECT LAT, LON FROM gpsloc WHERE LOC=?", foo);
		startManagingCursor(cursor);
		return cursor;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0,1,1, "Go Back");
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			final Intent q = new Intent(this, MainPage.class);
			startActivity(q);
			finish();
		}
		return super.onOptionsItemSelected(item);


	}

}