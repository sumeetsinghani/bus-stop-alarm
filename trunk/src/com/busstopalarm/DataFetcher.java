package com.busstopalarm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class DataFetcher {
	
	private static final String HOST_NAME = "http://api.onebusaway.org/api/where/";
	private static final String API_KEY = "v1_2XAu/HZkwK6PBLM63rgeRp34j5g=cXVhbmdodXkuZGFuZ0BnbWFpbC5jb20=";
	
	/**
	 * THIS METHOD IS UNFINSIHED.  IT CURRENTLY ONLY FETCHES DATA FOR THE ZFR.
	 * 
	 * @param RouteID
	 * @throws IOException
	 */
	public static String getRouteById(String RouteID) throws IOException {
		URL u = new URL(HOST_NAME +  "route/1_" + RouteID + ".xml?key=" + API_KEY);
		Scanner in = new Scanner(new InputStreamReader(u.openStream()));
		
		String xmlString = "";
		while (in.hasNextLine()) {
			xmlString += in.nextLine();
		}
		Log.i("Client: ", xmlString);
		return xmlString;
	}
	
	/**
	 * Retrieves the list of polylines for the given route.
	 * @param RouteID is the bus route id of the route in Seattle.
	 * @return ArrayList of polylines containing the encoded polyline and the endcoded level.
	 */
	public ArrayList<Polyline> getPolylines(String RouteID) {
		ArrayList<Polyline> polylines = new ArrayList<Polyline>();
		JSONObject json = null;
		
		
		// HTTP GET request for the polylines.
		try {
			URL url = new URL(HOST_NAME + "stops-for-route/1_" + RouteID + ".json?key=" + API_KEY);
			URLConnection connection = url.openConnection();
			InputStream inStream = connection.getInputStream();
			json = new JSONObject(convertStreamToString(inStream));
		} catch (Exception e) {
			System.out.println("Error: Connecting to the One Bus Away API.");
		}
		
		// Retrieve the list of polylines from the json response.
		try {
			JSONObject data = json.getJSONObject("data");
			JSONArray polylineArray = data.getJSONArray("polylines");
			for(int i = 0; i < polylineArray.length(); i++) {
				if(!polylineArray.isNull(i)) {
					JSONObject polyline = polylineArray.getJSONObject(i);
					String line = polyline.getString("points");
					int length = polyline.getInt("length");
					polylines.add(new Polyline(line, length));
				}
			}
		} catch (Exception e) {
			System.out.println("Error getting polylines from json response.");
		}
		
		return polylines;
	}
	
	
	/**
	 * Converts an InputStream into a String
	 * @param inStream
	 * @return the converted string
	 * @throws IOException
	 */
	private String convertStreamToString(InputStream inStream) throws IOException {
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
