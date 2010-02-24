package com.busstopalarm;


import java.io.FileNotFoundException;
import java.io.IOException;

import com.busstopalarm.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
		final Button RouteSearchButton = (Button)findViewById(R.id.RouteSearchButton);
		RouteSearchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					String s = DataFetcher.getRouteById("30");
					//String s = DataFetcher.OneBusAway(1, 30);
					Toast t = Toast.makeText(v.getContext(), s.subSequence(0, 500), Toast.LENGTH_LONG);
					t.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(new Intent(v.getContext(), MapPage.class));
				finish();
			}	
		});
		
		final Button FavButton = (Button)findViewById(R.id.FavButton);
		FavButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), LocationListPage.class);
				i.putExtra("listType", LocationListPage.FAVORITES);
				startActivity(i);
				finish();
			}	
		});
		
		final Button MajorButton = (Button)findViewById(R.id.MajorButton);
		MajorButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d("MAIN", "MAJOR BUTTON PUSHED");
				Intent i = new Intent(v.getContext(), LocationListPage.class);
				i.putExtra("listType", LocationListPage.MAJOR);
				startActivity(i);
				finish();
			}	
		});
		

		///////// test //////////
		BusDbAdapter ad = new BusDbAdapter(getApplicationContext());
		ad.open();
		try {
			Log.d("MAIN", "major db sample" + ad.readDbFile(1));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		///////// test //////////
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 1, "Confirmation");
		menu.add(0, 2, 2, "Help/About");
		menu.add(0, 3, 3, "Num3");
		menu.add(0,4,4, "Exit");
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
		case 3:
		case 4:
			finish();
			break;
		}
		return false;

	}
}