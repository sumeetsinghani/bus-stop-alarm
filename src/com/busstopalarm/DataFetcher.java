/**
 * Class DataFetcher allows for querying and interpretation of OneBusAway API for
 * bus stop and bus route data relavant to Bus Stop Alarm.
 * 
 * @author Michael Eng, Orkhan Muradov
 */

package com.busstopalarm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
public class DataFetcher {
	// Location of REST API.
	private static final String HOST_NAME = "http://api.onebusaway.org/api/where/";
	
	// Our current implementation only supports the agency Seattle (1).
	private static final String DEFAULT_AGENCY = "1";
	
	// API Key needed for use of OneBusAway. Generated by OneBusAway.
	private static final String API_KEY = "v1_2XAu/HZkwK6PBLM63rgeRp34j5g=cXVhbmdodXkuZGFuZ0BnbWFpbC5jb20=";
	
	// Actions for querying.
	private static final String ACTION_GET_STOP_BY_ID = "stop/";
	private static final String ACTION_GET_ROUTE_BY_ID = "route/";
	private static final String ACTION_GET_STOPS_FOR_A_ROUTE = "stops-for-route/";
	
	/**
	 * Retrieves a single stop element corresponding to the given stop id,
	 * and loads response data into returned BusStop
	 * @param stopId
	 * @return the BusStop corresponding to given stop id. Otherwise null if no corresponding stop.
	 */
	public BusStop getStopById(int stopId) throws IOException {
		if(stopId < 0) {
			throw new IllegalArgumentException();
		}
		
		// HTTP GET request for a bus stop in JSON format.
		JSONObject json = doQuery(ACTION_GET_STOP_BY_ID, stopId);	
		if(json == null) {
			throw new IllegalArgumentException("No stop for ID.");
		}
		
		return getStopByIdParser(json);
	}
	
	/**
	 * Retrieves bus route data from OneBusAway and loads
	 * data into new BusRoute object. If getPolylinesAndStops is true,
	 * then polylines and bus stops are added as well.
	 * @param routeId
	 * @param getPolylinesAndStops flags whether or not to add polylines and stops
	 * @return BusRoute object loaded with retrieved data
	 * @throws IOException
	 */
	public BusRoute getBusRouteById(int routeId, boolean includePolylinesAndStops) throws IOException, IllegalArgumentException, JSONException {
		if(routeId < 0) {
			throw new IllegalArgumentException();
		}
		BusRoute busRoute = new BusRoute();
		
		// Query for info on the bus route.
		
		// HTTP GET request for a bus route in JSON format.
		JSONObject jsonBusRoute = doQuery(ACTION_GET_ROUTE_BY_ID, routeId);
		
		if(jsonBusRoute == null) {
			throw new IllegalArgumentException("No route for ID.");
		}
		
		// Retrieve the list of polylines from the JSON response.
		try {
			// Check to see if response code.
			String text = jsonBusRoute.getString("text");
			if(!text.equals("OK")) {
				throw new IllegalArgumentException("No such Bus Stop Id.");
			}
			
			JSONObject data = jsonBusRoute.getJSONObject("data");
			
			busRoute.setRouteId(data.getString("id"));
			busRoute.setShortName(data.getString("shortName"));
			busRoute.setDescription(data.getString("description"));
			busRoute.setType(data.getString("type"));
		} catch (JSONException e) {
			Log.e("getBusRouteById", 
					"Error getting BusRoute from json response.", e);
			throw e;
		}
		
		
		// Good to get both polylines and stops at same time because 
		// data for them is in one big response from OneBusAway.
		if(includePolylinesAndStops) {
			JSONObject json = null;
			
			// HTTP GET request for the stops for a route in JSON format.
			json = doQuery(ACTION_GET_STOPS_FOR_A_ROUTE, routeId);
			
			busRoute.setPolylines(getPolylinesParser(json));
			busRoute.setBusStops(getBusStopsForRouteParser(json));
		}
		
		return busRoute;
	}
	
	
	
	/**
	 * Retrieves the list of polylines for the given route.
	 * @param routeId is the bus route id of the route in Seattle.
	 * @return List of polylines containing the encoded polyline and the encoded level.
	 *         List is empty if there are now line for given route id.
	 */
	public List<Polyline> getPolylines(int routeId) throws IOException, 
													IllegalArgumentException {
		if(routeId < 0) {
			throw new IllegalArgumentException();
		}
		
		// HTTP GET request for the stops for a route in JSON format.
		JSONObject json = doQuery(ACTION_GET_STOPS_FOR_A_ROUTE, routeId);
		
		return getPolylinesParser(json);
	}
	
	/** 
	 * Retrieves a list of stops corresponding to the given route id,
	 * represented as JSONObject rooted at key "stopGroupings".
	 * @param routeID
	 * @return list of stops as JSONObject, null if no such routeID.
	 */
	public List<BusStop> getBusStopsForRoute(int routeId) throws IOException, JSONException {
		if(routeId < 0) {
			throw new IllegalArgumentException();
		}
		
		// HTTP GET request for the stops for a route in JSON format.
		JSONObject json = doQuery(ACTION_GET_STOPS_FOR_A_ROUTE, routeId);
		if(json == null) {
			throw new IllegalArgumentException("No route for ID.");
		}
		
		
		return getBusStopsForRouteParser(json);
	}
	
	/**
	 * Parses json response from OneBusAway api for polylines.
	 * @param json is the response from OneBusAway by a "Stops for a route" 
	 * query.
	 * @return List of polylines containing the encoded polyline and the encoded 
	 * level. List is empty if there are no line for given route id.
	 */
	private List<Polyline> getPolylinesParser(JSONObject json) {
		if(json == null) {
			throw new IllegalArgumentException("getPolylinesParser: json " +
					"parameter is null");
		}
		
		ArrayList<Polyline> polylines = new ArrayList<Polyline>();
		
		// Retrieve the list of polylines from the JSON response.
		try {
			// Check to see if response code.
			String text = json.getString("text");
			if(!text.equals("OK")) {
				return polylines;
			}
			
			JSONObject data = json.getJSONObject("data");
			JSONArray polylineArray = data.getJSONArray("polylines");
			
			// Add all polylines to our collection.
			for(int i = 0; i < polylineArray.length(); i++) {
				if(!polylineArray.isNull(i)) {
					JSONObject polyline = polylineArray.getJSONObject(i);
					String line = polyline.getString("points");
					int length = polyline.getInt("length");
					polylines.add(new Polyline(line, length));
				}
			}
		} catch (Exception e) {
			Log.e("getPolylinesParser", "Error getting polylines from json " +
					"response.", e);
		}
		
		return polylines;
	}
	
	/**
	 * Parses json response from OneBusAway api for bus stops.
	 * @param json is the response from OneBusAway by a "Stops for a route" 
	 * query.
	 * @return List of bus stops on the route.
	 * @throws JSONException 
	 */
	private List<BusStop> getBusStopsForRouteParser(JSONObject json) {
		try {
			JSONObject data = json.getJSONObject("data");
			JSONArray stops = data.getJSONArray("stops");
			
			List<BusStop> busStopList = new ArrayList<BusStop>();
			
			for (int i = 0; i < stops.length(); i++) {
				if(!stops.isNull(i)) {
					JSONObject stop = stops.getJSONObject(i);
					BusStop newStop = new BusStop();
					newStop.setLatitude(stop.getDouble("lat"));
					newStop.setLongitude(stop.getDouble("lon"));
					newStop.setName(stop.getString("name"));
					newStop.setCode(stop.getString("code"));
					newStop.setStopId(stop.getString("id"));
					busStopList.add(newStop);
				}
			}
			
			return busStopList;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Parses json response from OneBusAway api for a single bus stop.
	 * JSON response should be in format returned by OneBusAway.
	 * @param json
	 * @return BusStop object with data from json loaded into it.
	 */
	private BusStop getStopByIdParser(JSONObject json) {
		if(json == null) {
			throw new IllegalArgumentException("getStopByIdParser: json " +
					"parameter is null");
		}
		
		BusStop busStop = new BusStop();
		
		// Retrieve the list of polylines from the JSON response.
		try {
			// Check to see if response code.
			String text = json.getString("text");
			if(!text.equals("OK")) {
				throw new IllegalArgumentException("No such Bus Stop Id.");
			}
			
			JSONObject data = json.getJSONObject("data");
			
			busStop.setStopId(data.getString("id"));
			busStop.setLatitude(data.getDouble("lat"));
			busStop.setLongitude(data.getDouble("lon"));
			// TODO: i think direction comes up null for some reason
			busStop.setDirection(data.getString("direction"));
			busStop.setName(data.getString("name"));
			busStop.setCode(data.getString("code"));
			busStop.setLocationType(data.getString("locationType"));
		} catch (Exception e) {
			Log.e("getStopByIdParser", "Error getting bus stop from json " +
					"response.");
		}
		
		return busStop;
	}
	
	/**
	 * Queries the OneBusAway API for a JSONObject.
	 * Runs the supplied action and searching for the given id.
	 * Use supplied Action constants of the class.
	 * @param action
	 * @param id
	 * @return JSONObject containing the RAW web response.
	 * @throws IOException
	 */
	private JSONObject doQuery(String action, int id) throws IOException {
		if (id < 1) 
			throw new IllegalArgumentException("id should not be less than 0, was " + id);
		
		JSONObject json = null;
		
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(HOST_NAME);
			sb.append(action);
			sb.append(DEFAULT_AGENCY); // The default agency, Seattle is 1.
			sb.append("_"); // Add divider between agency and id.
			sb.append(id);
			sb.append(".json?key="); // Request a json response.
			sb.append(API_KEY); // Needed API key.
			
			URL url = new URL(sb.toString());
			URLConnection connection = url.openConnection();
			InputStream inStream = connection.getInputStream();
			json = new JSONObject(convertStreamToString(inStream));
		} catch (IOException e) {
			Log.e("doQuery", "Error: Connecting to the OneBusAway API", e);
		} catch(JSONException e) {
			Log.e("doQuery", "Error: Converting response into JSONObject");
			return null;
		}
		
		return json;
	}
	
	/**
	 * Converts an InputStream into a String
	 * @param inStream
	 * @return the converted string
	 * @throws IOException
	 */
	private String convertStreamToString(InputStream inStream) throws 
																IOException {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		if (inStream != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inStream, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				inStream.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}
}
