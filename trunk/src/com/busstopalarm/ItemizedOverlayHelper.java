/**
 * @author Orkhan Muradov, David Nufer, Michael Eng
 * This class is used to draw overlay items on the map.
 * It uses an array of overlay items.
 */

package com.busstopalarm;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ItemizedOverlayHelper extends ItemizedOverlay {

	private static final String TAG = "inOverlayHelper";
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private BusStop lastSelectedStop;
	private Activity mCtx;
	private String routeNum;
	private String busRouteDesc;
	
	/**
	 * It shows current bus route and a button to set alarm.
	 * @param mCtx gets activity
	 * @param defaultMarker grabs marker
	 */
	public ItemizedOverlayHelper(final Activity mCtx, Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		this.mCtx = mCtx;
		lastSelectedStop = null;
		try {
			routeNum = mCtx.getIntent().getStringExtra("routeID").split("_")[1];
			busRouteDesc = mCtx.getIntent().getStringExtra("busroutedesc");
		} catch (Exception e) {
			// NullPointerException, IndexOutOfBoundsException, and
			// NumberFormatException are caught here.
			Log.v(TAG, "Error trying to get routeID sent from main page");
			e.printStackTrace();
		}
		TextView tv = (TextView)mCtx.findViewById(R.id.stopinfo);
		String text = "Route " + routeNum + ": " + "<No Stop Selected>";
		tv.setText(text);
		
		Button b = (Button)mCtx.findViewById(R.id.stopinfoarea_button);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (lastSelectedStop != null) {
					Intent i = 
						new Intent(v.getContext(), ConfirmationPage.class);
					i.putExtra("busstop", lastSelectedStop);
					i.putExtra("busroute", routeNum);
					i.putExtra("busroutedesc", busRouteDesc);
					Log.v(TAG, "busRouteDesc = " + busRouteDesc);
					mCtx.startActivityForResult(i,
							MapPage.MAP_CONFIRM_TRANSITION);
				} else {
					Toast.makeText(v.getContext(), "Please Select a Route", 
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	/**
	 * Adds overlay item to the overlay array
	 */
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	/**
	 * Given index returns OverLayItem in array(non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#createItem(int)
	 * @param array index
	 * @return Overlay item in the array
	 */
	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}
	
	/**
	 * Returns size of the overlay array (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#size()
	 * @return size of the overlay array
	 */
	@Override
	public int size() {
		return mOverlays.size();
	}
	
	/**
	 * Returns the bus stop information when the bus stop icon is tapped.
	 * @return Bus Stop Text information
	 */
	public boolean onTap(int index) {
		BusStopOverlayItem stop = (BusStopOverlayItem)mOverlays.get(index);
		lastSelectedStop = stop.getStop();
		
		TextView tv = (TextView)mCtx.findViewById(R.id.stopinfo);
		String text = 
			"Route " + 
			mCtx.getIntent().getStringExtra("routeID").split("_")[1] + 
			": " + lastSelectedStop.getName();
		tv.setText(text);
		return super.onTap(index);
	}

}
