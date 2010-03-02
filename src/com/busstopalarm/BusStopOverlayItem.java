package com.busstopalarm;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class BusStopOverlayItem extends OverlayItem {
	
	private BusStop stop;
	
	public BusStopOverlayItem(BusStop b) {
		super(b.getGeoPoint(), "Stop #: " + b.getCode(), b.getName());
		stop = b;
	}
	
	public BusStop getStop() {
		return stop;
	}
}
