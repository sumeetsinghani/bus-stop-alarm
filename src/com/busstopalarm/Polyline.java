/**
 * Class Polyline represents a single polyline. Stores the encoded polyline,
 * encoded levels, and coordinate points of the points on the polyline.
 * 
 * CAUTION: NOT YET FULLY TESTED.
 * 
 * @author Michael Eng
 */

package com.busstopalarm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.android.maps.GeoPoint;

public class Polyline implements Iterable<GeoPoint> {
	private String encodedPolyline;
	private String encodedLevels;
	private ArrayList<GeoPoint> coordinates;

	/**
	 * Polyline constructor that stores the given encodedPolyline and number of
	 * levels that follows Google's encoding standards. Decodes given polyline.
	 * 
	 * @param encodedPolyline
	 * @param encodedLevels
	 */
	public Polyline(String encodedPolyline, int length) {
		//this.coordinates = new ArrayList<GeoPoint>();
		
		StringBuilder sb = new StringBuilder();
		// The encodedLevels is a string of "B" of size length.
		for (int i = 0; i < length; i++) {
			sb.append('B');
		}

		this.encodedLevels = sb.toString();
		this.encodedPolyline = encodedPolyline;
		this.decodeLine();		
	}

	/**
	 * Polyline constructor that stores the given encodedPolyline and
	 * encodedLevels that follows Google's encoding standards. Decodes given
	 * polyline.
	 * 
	 * @param encodedPolyline
	 * @param encodedLevels
	 */
	public Polyline(String encodedPolyline, String encodedLevels) {
		this.coordinates = new ArrayList<GeoPoint>();
		this.encodedPolyline = encodedPolyline;
		this.encodedLevels = encodedLevels;
		this.decodeLine();
	}

	/**
	 * 
	 * @return the encodedPolyline
	 */
	public String getEncodedPolyline() {
		return encodedPolyline;
	}

	/**
	 * 
	 * @return the encodedLevels
	 */
	public String getEncodedLevels() {
		return encodedLevels;
	}

	/**
	 * @return an iterator for the list of GeoPoints.
	 */
	public Iterator<GeoPoint> iterator() {
		PolylineIterator p = new PolylineIterator(coordinates);
		return p;
	}

	/**
	 * Iterator class that iterates through the list of GeoPoints.
	 * 
	 * @author Michael Eng
	 * 
	 */
	public class PolylineIterator implements Iterator<GeoPoint> {
		private int index;
		private ArrayList<GeoPoint> coordinates;

		/**
		 * Constructor for PolylineIterator. Takes list of GeoPoints for future
		 * use for iteration.
		 * 
		 * @param coordinates
		 */
		public PolylineIterator(ArrayList<GeoPoint> coordinates) {
			index = 0;
			this.coordinates = coordinates;
		}

		/**
		 * @return true if there is an available GeoPoint.
		 */
		public boolean hasNext() {
			return index < coordinates.size();
		}

		/**
		 * @return the next GeoPoint in the list.
		 */
		public GeoPoint next() {
			if (hasNext()) {
				return coordinates.get(index++);
			} else {
				throw new NoSuchElementException();
			}
		}

		/**
		 * This method is not supported in the implementation.
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * This method is rewritten from Google's polyline utility which is written
	 * in Javascript. Decodes the class' encodedPolyline and stores the
	 * GeoPoints in the list of coordinates.
	 * See: http://code.google.com/apis/maps/documentation/include/polyline.js
	 */
	private void decodeLine() {
		// Clear all stored coordinates.
		coordinates.clear();

		int len = encodedPolyline.length();
		int index = 0;
		int lat = 0;
		int lng = 0;

		// Decode polyline according to Google's polyline decoder utility.
		while (index < len) {
			int b;
			int shift = 0;
			int result = 0;
			do {
				b = encodedPolyline.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = (((result & 1) != 0) ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encodedPolyline.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = (((result & 1) != 0) ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			
			// Convert the lat and lng to microdegrees.
			GeoPoint nextPoint = new GeoPoint(lat * 10, lng * 10); 
			
			coordinates.add(nextPoint);
		}
	}
}
