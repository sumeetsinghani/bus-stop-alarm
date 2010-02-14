package com.busstopalarm;

public class Polyline {
	private String polyline;
	private String encodedLevels;
	
	public Polyline(String polyline, int length) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++) {
			sb.append("B");
		}
		
		this.polyline = polyline;
		this.encodedLevels = sb.toString();
	}
	
	public Polyline(String polyline, String encodedLevels) {
		this.polyline = polyline;
		this.encodedLevels = encodedLevels;
	}
	
	public String getPolyline() {
		return polyline;
	}
	
	public String encodedLevels() {
		return encodedLevels;
	}
}
