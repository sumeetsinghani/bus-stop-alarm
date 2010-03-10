package com.busstopalarm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * @author Orkhan Muradov, David Nufer
 * 
 * This class creates a google map which shows current user location and draws 
 * bus route user requested.
 * 
 */

public class MapPage extends MapActivity {

	private MapController mapController;
	private MapView mapView;
	private LocationManager lm;
	private String routeID;
	private List<Overlay> mapOverlays;
	private ItemizedOverlayHelper currentLocOverlay;

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * Called when activity is first created in Map page
	 * (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
	 * @param @param Bundle which holds the current state (info)
	 */
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.map);
		//RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.mapview);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(13);

		//instantiates gps service
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000,
				10.0f, new GPSUpdateHandler());

		//array of overlay items
		mapOverlays = mapView.getOverlays();

		routeID = getIntent().getExtras().getString("routeID");
		DataFetcher df = new DataFetcher();
		BusRoute busRoute;
		try {
			// draw routes on map
			busRoute = df.getBusRouteById(routeID, true);
			for (Polyline p : busRoute.getPolylines()) {
				PolylineOverlay po = new PolylineOverlay(p);
				mapOverlays.add(po);
			}

			// place stops on map
			//List<BusStop> stops = df.getBusStopsForRoute(routeNumber);

			Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.busstopicon);
			ItemizedOverlayHelper itemizedoverlay = new ItemizedOverlayHelper(this, drawable);

			for (BusStop bs : busRoute.getBusStops()) {
				BusStopOverlayItem ov = new BusStopOverlayItem(bs);
				itemizedoverlay.addOverlay(ov);
			}
			mapOverlays.add(itemizedoverlay);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast t = Toast.makeText(this, "Error occured while trying to draw bus route", Toast.LENGTH_LONG);
			t.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast t = Toast.makeText(this, "Error occured while trying to draw bus route", Toast.LENGTH_LONG);
			t.show();
		}

		//currentLocOverlay = null;

	}

	public void fillData() {
		setContentView(mapView);
	}

	/**
	 * This method is needed in order to use GPS location of the user
	 * 
	 * @param 
	 * @return returns boolean false
	 */
	@Override
	protected boolean isLocationDisplayed() {
		return super.isLocationDisplayed();
	}

	/**
	 * Grabs user's location coordinates and displays user's location
	 */
	public class GPSUpdateHandler implements LocationListener {

		public void onLocationChanged(Location location) {
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			GeoPoint point = new GeoPoint((int) (lat*1E6), (int) (lon*1E6));
			mapView.getController().animateTo(point);
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}
	}

	/**
	 * 
	 * This method adds button to pop-up setting menu
	 *    	 
	 * @param menu that pops us when pressing menu button
	 * @return returns boolean true
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0,1, 1, "Go back");
		menu.add(0,2, 2, "Settings");
		return true;
	}

	/**
	 * This method returns to Main page when go back is pressed.
	 * And exits the program when exit button is pressed
	 * 
	 * @param takes menuitem as parameter
	 * @return returns boolean true
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 1:
			final Intent i = new Intent(this, MainPage.class);
			startActivity(i);
			finish();
			break;
		case 2:
			final Intent j = new Intent(this, SettingsPage.class);
			startActivity(j);
			break;
		}


		return true;
	}
}
