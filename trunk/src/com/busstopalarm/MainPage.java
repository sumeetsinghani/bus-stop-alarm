package com.busstopalarm;


import com.busstopalarm.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
				startActivity(new Intent(v.getContext(), MapPage.class));
				finish();
			}	
		});
		
		final Button FavButton = (Button)findViewById(R.id.FavButton);
		FavButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), LocationListPage.class));
				finish();
			}	
		});
		
		final Button MajorButton = (Button)findViewById(R.id.MajorButton);
		MajorButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), LocationListPage.class));
				finish();
			}	
		});
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
		case 4:
			finish();
		}
		return false;

	}
}