package com.busstopalarm;


import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.busstopalarm.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class MapPage extends MapActivity {


	LinearLayout linearLayout;
	MapView mapView;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		linearLayout = (LinearLayout) findViewById(R.id.zoomview);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		//LocationManager lm = (LocationManager)
		getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0,1, 1, "Go back");
		menu.add(0,2, 2, "Exit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 1:
			final Intent i = new Intent(this, MainPage.class);
			startActivity(i);
			finish();
			break;
		case 2:
			finish();
			break;
		}
		return false;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}


}