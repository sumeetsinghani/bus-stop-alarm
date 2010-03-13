/**
 * Overlay Item to display on the map for a bus stop
 * 
 * @author David Nufer
 */

package com.busstopalarm;

import com.google.android.maps.OverlayItem;

public class BusStopOverlayItem extends OverlayItem {
	
	/**
	 * The Bus stop this overlay item represents
	 */
	private BusStop stop;
	
	/**
	 * Constructs a new overlay item
	 * 
	 * @param b the Bus stop for this Overlay item
	 */
	public BusStopOverlayItem(BusStop b) {
		super(b.getGeoPoint(), "Stop #: " + b.getCode(), b.getName());
		stop = b;
	}
	
	/**
	 * Getter for the bus stop
	 * 
	 * @return the bus stop
	 */
	public BusStop getStop() {
		return stop;
	}
}
