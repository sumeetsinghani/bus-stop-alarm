/**
 * TODO: Stub class for stop element.
 * Contains information pertaining to a bus stop such as coordinates,
 * bus direction and street location.
 * 
 * @author Michael Eng
 */

package com.busstopalarm;

public class BusStop {
	private String stopId;			// The stop id
	private String code;		// Passenger-facing stop identifier
	private String name;		// Passenger-facing name for the stop
	private double latitude;	// The latitude location of the stop
	private double longitude;	// The longitude location of the stop
	private String direction;	// The direction of travel for routes serving this stop
	private String locationType;	/* Corresponds to the stop location_type field 
									   defined in the GTFS spec found at OneBusAway website*/
	
	public BusStop() {
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getStopId() {
		return stopId;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}
	
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	
	
	public boolean equals(BusStop other) {
		// TODO: Must implement to test.
		return this == other;
	}
}
