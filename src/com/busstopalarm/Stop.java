/**
 * TODO: Stub class for stop element.
 * Contains information pertaining to a stop such as coordinates,
 * bus direction and street location.
 * 
 * @author Michael Eng
 */

package com.busstopalarm;

public class Stop {
	private String id;			// The stop id
	private String code;		// Passenger-facing stop identifier
	private String name;		// Passenger-facing name for the stop
	private double latitude;	// The latitude location of the stop
	private double longitude;	// The longitude location of the stop
	private String direction;	// The direction of travel for routes serving this stop
	private String locationType;	/* Corresponds to the stop location_type field 
									   defined in the GTFS spec found at OneBusAway website*/
	
	public Stop(int stopId) {
		
	}
	
	public String getStopId() {
		return id;
	}
	
}
