package com.busstopalarm.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.busstopalarm.LocationListPage;

public class LocationListPageTests extends 
					ActivityInstrumentationTestCase2<LocationListPage> {

	public LocationListPageTests() {
		super("com.busstopalarm", LocationListPage.class);
		//setName(name);
	}

	protected void setUp() throws Exception {
		//super.setUp();	
	}
	
	/*
	 * TODO: I don't know how to do any GUI test here as black box testing
	 */

	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
}