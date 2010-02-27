/**
 * STUB CLASS for BusRoute
 * 
 * @author Michael Eng
 */

package com.busstopalarm;

import java.util.List;

public class BusRoute {
	private List<Polyline> polylines;
	private List<BusStop> busStops;
	private String routeId;
	private String shortName;
	private String longName;
	private String description;
	private String type;
	//private double agency; // Available but needed.
	
	public BusRoute() {
		
	}

	public List<Polyline> getPolylines() {
		return polylines;
	}

	public void setPolylines(List<Polyline> polylines) {
		this.polylines = polylines;
	}

	public List<BusStop> getBusStops() {
		return busStops;
	}

	public void setBusStops(List<BusStop> busStops) {
		this.busStops = busStops;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean equals(BusRoute other) {
		//TODO: Must implement to test.
		return this == other;
	}
}
