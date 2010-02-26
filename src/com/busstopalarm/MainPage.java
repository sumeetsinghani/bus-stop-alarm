package com.busstopalarm;

import java.io.FileNotFoundException;

import java.io.IOException;

import com.busstopalarm.R;

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

/**
 * Main page of the Bus Stop Alarm
 * 
 */
public class MainPage extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final Button RouteSearchButton = (Button) findViewById(R.id.RouteSearchButton);

		// search button behavior
		RouteSearchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String routeText = ((EditText) findViewById(R.id.RouteSearchBox))
						.getText().toString();
				if (routeText.matches("[0-9]{1,5}")) {
					int routeNumber = Integer.parseInt(routeText);
					// String s = DataFetcher.getRouteById(routeNumber);
					// //DataFetcher implementation changed.
					String s = "PlaceHolder for Route Info.";
					startActivity(new Intent(v.getContext(), MapPage.class));
					Toast t = Toast.makeText(v.getContext(), s.subSequence(0,
							Math.max(s.length(), 500)), Toast.LENGTH_LONG);
					t.show();
				} else {
					Toast t = Toast.makeText(v.getContext(),
							"Invalid Route Number", Toast.LENGTH_LONG);
					t.show();
				}
			}
		});

		// favorite button behavior
		final Button FavButton = (Button) findViewById(R.id.FavButton);
		FavButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), LocationListPage.class);
				i.putExtra("listType", LocationListPage.FAVORITES);
				startActivity(i);
				finish();
			}
		});

		// major button behavior
		final Button MajorButton = (Button) findViewById(R.id.MajorButton);
		MajorButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), LocationListPage.class);
				i.putExtra("listType", LocationListPage.MAJOR);
				startActivity(i);
				finish();
			}
		});

		// TODO: add recent routes to main page
		BusDbAdapter ad = new BusDbAdapter(getApplicationContext());
		ad.open();
		Cursor recent = ad.getRecentDest(5);
		LinearLayout recentList = (LinearLayout) findViewById(R.id.recent_routes);
		int routeIndex = recent.getColumnIndex("route_id");
		int routeDescIndex = recent.getColumnIndex("route_desc");
		while (!recent.isAfterLast()) {
			final TextView recentItem = new TextView(this);
			recentItem.setClickable(true);

			recentItem.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Toast t = Toast.makeText(getApplicationContext(),
							recentItem.getText(), Toast.LENGTH_SHORT);
					t.show();
				}
			});

			recentItem.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);
			recentItem.setText("Route " + recent.getString(routeIndex) + ", "
					+ recent.getString(routeDescIndex));
			recentList.addView(recentItem);
			recent.moveToNext();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 1, "Confirmation");
		menu.add(0, 2, 2, "Help/About");
		menu.add(0, 3, 3, "Num3");
		menu.add(0, 4, 4, "Exit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			final Intent q = new Intent(this, ConfirmationPage.class);
			startActivity(q);
			finish();
			break;
		case 2:
			final Intent helpPage = new Intent(this, HelpPage.class);
			startActivity(helpPage);
			finish();
			break;
		case 3:
			finish();
			break;
		}
		return false;

	}
}