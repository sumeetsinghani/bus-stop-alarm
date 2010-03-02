/**
 * TODO: STUB CLASS for BusRoute not fully tested.
 * TODO: Add error checking.
 * Stores data pertaining to a bus route such as name,
 * description, list of polylines, and list of bus stops.
 * Currently getters, setters and an equals method.
 * @author Michael Eng
 */

package com.busstopalarm;

import java.util.List;

public class BusRoute {
	private List<Polyline> polylines; // List of polylines that are used to draw the route.
	private List<BusStop> busStops; // List of bus stops along this route.
	private String routeId; // Route id of this route.
	private String shortName; // shortName is the route id without the agency.
	private String longName; // name of the route.
	private String description; // Description of the bus route. The cross street.
	private String type; // The type of bus route this is.
	//private double agency; // Available but not needed.
	
	public BusRoute() {
		
	}

	/**
	 * 
	 * @return polylines of this bus route.
	 */
	public List<Polyline> getPolylines() {
		return polylines;
	}

	/**
	 * Set the polylines that are used to draw a route.
	 * @param polylines
	 */
	public void setPolylines(List<Polyline> polylines) {
		this.polylines = polylines;
	}

	/**
	 * 
	 * @return List of BusStops along this route.
	 */
	public List<BusStop> getBusStops() {
		return busStops;
	}

	/**
	 * Set the list of BusStops along this route.
	 * @param busStops
	 */
	public void setBusStops(List<BusStop> busStops) {
		this.busStops = busStops;
	}

	/**
	 * 
	 * @return routeId for this route.
	 */
	public String getRouteId() {
		return routeId;
	}

	/**
	 * Set routeId for this route.
	 * @param routeId
	 */
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
	/**
	 * 
	 * @return shortName of this bus route.
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Set shortName of this bus route.
	 * @param shortName
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * 
	 * @return longName of this bus route.
	 */
	public String getLongName() {
		return longName;
	}

	/**
	 * Set longName of this bus route.
	 * @param longName
	 */
	public void setLongName(String longName) {
		this.longName = longName;
	}

	/**
	 * 
	 * @return description of this bus route.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set description of this bus route.
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return type of bus route this is.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the type of bus route this is.
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Returns true if both BusRoutes' ids are equal. False otherwise.
	 * @param other
	 * @return
	 */
	public boolean equals(BusRoute other) {
		return this.routeId.equals(other.routeId);
	}
}
