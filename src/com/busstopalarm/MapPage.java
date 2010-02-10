package com.busstopalarm;


import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.busstopalarm.DatabaseHelper;
import com.busstopalarm.ItemizedOverlayHelper;
import com.busstopalarm.R;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class MapPage extends MapActivity {


	LinearLayout linearLayout;
	MapView mapView;
	ItemizedOverlayHelper itemizedOverlay;
	MapController mapController;
	List<Overlay> mapOverlays;
	DatabaseHelper events = new DatabaseHelper(this);


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		linearLayout = (LinearLayout) findViewById(R.id.zoomview);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapOverlays = mapView.getOverlays();	
		LocationManager lm = (LocationManager)
		getSystemService(Context.LOCATION_SERVICE);
		// connect to the GPS location service
		itemizedOverlay = new ItemizedOverlayHelper(this.getResources().getDrawable(R.drawable.position));
		Location loc = lm.getLastKnownLocation("gps");
		Double Lat = loc.getLatitude()*1E6;
		Double Long = loc.getLongitude()*1E6;
		SQLiteDatabase db = events.getWritableDatabase();
		//events.onUpgrade(db, 1, 2);
		addEvent(db, Lat.intValue(), Long.intValue());
		GeoPoint point = new GeoPoint(Lat.intValue(), Long.intValue());
		mapController = mapView.getController();
		mapController.animateTo(point);
		OverlayItem overlayitem = new OverlayItem(point, "your position", "position");
		itemizedOverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedOverlay);
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
	
	private void addEvent(SQLiteDatabase db,int Lat, int Long) {
		//events.onUpgrade(db, 1, 2);
		db.execSQL("INSERT INTO gpsloc VALUES ('" + Lat + "', '" + Long + "', 'LOCATION');");
	}


}