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

public class MapPage extends MapActivity {
	
	//private LinearLayout linearLayout;
	private MapController mapController;
	private MapView mapView;
	private LocationManager lm;
	private int routeNumber;
	private List<Overlay> mapOverlays;
	private ItemizedOverlayHelper currentLocOverlay;
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.map);
		//RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.mapview);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		lm= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
								  0, new GPSUpdateHandler());
		
		routeNumber = getIntent().getExtras().getInt("routeNumber");
		
		mapOverlays = mapView.getOverlays();
		currentLocOverlay = null;
		
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
	
	
	public class GPSUpdateHandler implements LocationListener {
	
		public void onLocationChanged(Location location) {
			Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.icon);
			ItemizedOverlayHelper itemizedoverlay = new ItemizedOverlayHelper(drawable);

			double lat = location.getLatitude();
			double lon = location.getLongitude();
			GeoPoint point = new GeoPoint((int) (lat*1E6), (int) (lon*1E6));
			mapView.getController().animateTo(point);
			OverlayItem overlayitem = new OverlayItem(point, "Current Location", 
					"You are currently at " + (point.getLatitudeE6()/1e6) + " lat, " + (point.getLongitudeE6()/1e6) + " long");
			
			itemizedoverlay.addOverlay(overlayitem);
			if (currentLocOverlay != null) {
				mapOverlays.remove(currentLocOverlay);
			}
			mapOverlays.add(itemizedoverlay);
			currentLocOverlay = itemizedoverlay;
			
			/*
			findRouteAndToast(lat, lon);
			*/
		}
		/*
		private void findRouteAndToast(double lat, double lon) {
		
			try {
				String s = DataFetcher.getRouteById(routeNumber); //getRouteByLocation(lat, lon);
				Toast.makeText(getBaseContext(), s.subSequence(0, 500), Toast.LENGTH_SHORT).show();
				//Log.v("ROUTES RESULT", result.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
*/
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
		menu.add(0,2, 2, "Exit");
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
			finish();
			break;
		}
		return true;
	}
}
