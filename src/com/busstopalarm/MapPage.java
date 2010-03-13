package com.busstopalarm;

import java.io.IOException;
import java.util.List;

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
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * @author Orkhan Muradov, David Nufer
 * 
 * This class creates a google map which shows current user location and draws 
 * bus route user requested.
 * 
 */

public class MapPage extends MapActivity {

	private static final String TAG = "inMapPage";
	
	// This is the GeoPoint we use to set our map view focus on if we were
	// somehow unable to obtain information about bus stops. This location
	// is approximately in Downtown Seattle.
	private static final GeoPoint DEFAULT_MAPVIEW_CENTER =
		new GeoPoint(47639049,-122345299);
	// This number is arbitrary - this is used in startActivityForResult() and
	// finishActivity() calls to determine whether the user backed out of 
	// confirmation page to the map page, or confirmed the alarm.
	// If it is the latter, then we can finish the map page.
	public static final int MAP_CONFIRM_TRANSITION = 123;
	
	private MapController mapController;
	private MapView mapView;
	private LocationManager lm;
	private String routeID;
	private List<Overlay> mapOverlays;

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
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
	
		//instantiates gps service
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000,
				10.0f, new GPSUpdateHandler());

		//array of overlay items
		mapOverlays = mapView.getOverlays();

		routeID = getIntent().getStringExtra("routeID");
		DataFetcher df = new DataFetcher();
		BusRoute busRoute;
		try {
			// draw routes on map
			busRoute = df.getBusRouteById(
					Integer.parseInt(routeID.split("_")[1]), true);
			for (Polyline p : busRoute.getPolylines()) {
				PolylineOverlay po = new PolylineOverlay(p);
				mapOverlays.add(po);
			}

			Drawable drawable;
			drawable = getApplicationContext().getResources().getDrawable(
					R.drawable.busstopicon);
			getIntent().putExtra("busroutedesc", busRoute.getDescription());
			ItemizedOverlayHelper itemizedoverlay = 
				new ItemizedOverlayHelper(this, drawable);

			for (BusStop bs : busRoute.getBusStops()) {
				BusStopOverlayItem ov = new BusStopOverlayItem(bs);
				itemizedoverlay.addOverlay(ov);
			}
			mapOverlays.add(itemizedoverlay);
			
			// Set the zooming and centering here.
			setFocus(busRoute.getBusStops());
				
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Toast t = Toast.makeText(this, "Error occured while trying to " + 
					"draw bus route. There might have been an error " + 
					"obtaining route from the server. Please try again later.", 
					Toast.LENGTH_LONG);
			t.show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast t = Toast.makeText(this, "Error occured while trying to " + 
					"draw bus route. There might have been an error " + 
					"obtaining route from the server. Please try again later.", 
					Toast.LENGTH_LONG);
			t.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG, "onActivityResult" + requestCode + " " + resultCode);
		if (requestCode == MAP_CONFIRM_TRANSITION) {
			if (resultCode == RESULT_OK) {
				// Finish the old main page if we have already set the alarm.
				setResult(RESULT_OK);
				finishActivity(MainPage.MAIN_CONFIRM_TRANSITION);
				finish();
			}
		}
		// else, we got here from backing out of confirmation page, we do not
		// finish the map page.	
	}
	
	/**
	 * Sets the focus of the map view, as determined by the list of bus stops.
	 * @param stopList The list of bus stops.
	 * 
	 * @author Derek Cheng
	 */
	private void setFocus(List<BusStop> stopList) {
		// Just in case this happens, we set the center to the 
		// geolocation of Downtown Seattle.		
		GeoPoint centroid;
		if (stopList == null || stopList.size() == 0) {
			centroid = DEFAULT_MAPVIEW_CENTER;
		} else {
			// We compute the centroid to be set as center of map view.
			centroid = getCentroid(stopList);
		}

		mapController = mapView.getController();		
		mapController.setCenter(centroid);
		mapController.setZoom(12);	
	}
	
	/**
	 * Computes the location of the centroid of a list of bus stops.
	 * @param stopList A list of BusStops
	 * @return The "centroid" of stopList, given as a GeoLocation
	 * Precondition: stopList has at least 1 element.
	 * @author - Derek Cheng
	 */
	private GeoPoint getCentroid(List<BusStop> stopList) {
		// We get the "centroid" of all the bus stops' points, by taking the
		// average between the min and max for each latitude and longitude for
		// all bus stops on the route.
		GeoPoint gp, gp2;
		gp = stopList.get(0).getGeoPoint();		
		int minLat = gp.getLatitudeE6();
		int maxLat = minLat;
		int minLong = gp.getLongitudeE6();
		int maxLong = minLong;
		
		// Finding both the min and max in a list.
		// This algorithm does 3n/2 comparisons as opposed to the
		// naive algorithm that does 2n comparisons in worst case.
		int i;
		for (i = 1; i < stopList.size()-1; i += 2) {
			gp = stopList.get(i).getGeoPoint();
			gp2 = stopList.get(i+1).getGeoPoint();			
			int gpLat = gp.getLatitudeE6();
			int gp2Lat = gp2.getLatitudeE6();
			int gpLong = gp.getLongitudeE6();
			int gp2Long = gp2.getLongitudeE6();
			
			if (gpLat < gp2Lat) {
				minLat = Math.min(minLat,gpLat);
				maxLat = Math.max(maxLat,gp2Lat);
			} else {
				minLat = Math.min(minLat,gp2Lat);
				maxLat = Math.max(maxLat,gpLat);
			}
			if (gpLong < gp2Long) {
				minLong = Math.min(minLong,gpLong);
				maxLong = Math.max(maxLong,gp2Long);
			} else {
				minLong = Math.min(minLong,gp2Long);
				maxLong = Math.max(maxLong,gpLong);
			}
		}
		if (i == stopList.size()-1) {
			gp = stopList.get(stopList.size()-1).getGeoPoint();
			int gpLat = gp.getLatitudeE6();
			int gpLong = gp.getLongitudeE6();
			// don't even bother optimizating for only 1 point here
			minLat = Math.min(minLat,gpLat);
			maxLat = Math.max(maxLat,gpLat);
			minLong = Math.min(minLong,gpLong);
			maxLong = Math.max(maxLong,gpLong);
		}

		return new GeoPoint((minLat + maxLat)/2, (minLong + maxLong)/2);
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
