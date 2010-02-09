package com.busstopalarm;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

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
}
