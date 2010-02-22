/**
 * Author: Orkhan Muradov
 * Date: 02/21/2010
 * 
 * This class creates google maps with the gps location of the user. 
 * It zooms window to the location of the user and has zooming features.
 * It also adds button for pop-up menu
 */

package com.busstopalarm;


import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.busstopalarm.ItemizedOverlayHelper;
import com.busstopalarm.R;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class MapPage extends MapActivity {

	private MapView mapView;
	private ItemizedOverlayHelper itemizedOverlay;
	private MapController mapController;
	private List<Overlay> mapOverlays;
	
  	/**
   	 * Creates Google map with zooming feature, locates the use
   	 * and displays his location.
   	 * 
   	 * @param 
     * @return void
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.zoomview);
		
		//zooming feature
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		//overlays for adding drawings on the map
		mapOverlays = mapView.getOverlays();	
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		itemizedOverlay = new ItemizedOverlayHelper(this.getResources().getDrawable(R.drawable.position));
		Location loc = lm.getLastKnownLocation("gps");

		//fetching users location and displaying it
		Double Latitude = loc.getLatitude()*1E6;
		Double Longitude = loc.getLongitude()*1E6;
		GeoPoint point = new GeoPoint(Latitude.intValue(), Longitude.intValue());
		mapController = mapView.getController();
		mapController.animateTo(point);
		OverlayItem overlayitem = new OverlayItem(point, "your position", "position");
		itemizedOverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedOverlay);
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

  	/**
   	 * This method is needed in order to use GPS location of the user
   	 * 
   	 * @param 
   	 * @return returns boolean false
     */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}