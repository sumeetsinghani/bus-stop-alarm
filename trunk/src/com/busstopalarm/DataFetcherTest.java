/**
 * Test driven test case for DataFetcher.java. The intended use for
 * DataFetcher is to allow for easy retrieval of data from queries to
 * the OneBusAway API.
 * 
 * @author Michael Eng
 */

package com.busstopalarm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataFetcherTest {
	public static final int TIMEOUT = 10000;
	private DataFetcher fetcher;

	@Before
	public void setUp() throws Exception {
		fetcher = new DataFetcher();
	}

	/**
	 * Verify that the constructor creates a new DataFetcher.
	 */
	@Test(timeout = TIMEOUT)
	public void testDataFetcher() {
		assertNotNull(fetcher);
	}
	
	/**
	 * Test functionality of getStopById by
	 * giving it a valid stop id.
	 * @throws IOException
	 */
	@Test(timeout = TIMEOUT)
	public void testGetStopsForRoute_KnownRoute() throws IOException{
		JSONObject actual = fetcher.getStopsForRoute(30);
		
		JSONObject expected = null;
		try {
			expected = new JSONObject(R.string.testGetStopsForRoute_KnownRoute_Expected);
		} catch (Exception e) {
			System.out.println("testGetRouteById_KnownRoute: Error in creating expected value.");
		}
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test to see if method getStopsForRoute properly
	 * detects that there really is no corresponding 
	 * route for a possible route id but not real (0).
	 * Should throw an IllegalArgumentException.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	@Test(timeout = TIMEOUT, expected=IllegalArgumentException.class)
	public void testGetStopsForRoute_NonExistingRoute() throws IOException, IllegalArgumentException {
		JSONObject actual = fetcher.getStopsForRoute(0);
	}
	
	/**
	 * Test to see if method getStopsForRoute properly
	 * detects an impossible negative route id.
	 * Should throw an IllegalArgumentException.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	@Test(timeout = TIMEOUT, expected=IllegalArgumentException.class)
	public void testGetStopsForRoute_NegativeRoute() throws IOException, IllegalArgumentException {
		JSONObject actual = fetcher.getStopsForRoute(-1);
	}
	
	/**
	 * Test functionality of getStopById by
	 * giving it a valid stop id.
	 * @throws IOException
	 */
	@Test(timeout = TIMEOUT)
	public void testGetStopById_KnownRoute() throws IOException{
		JSONObject actual = fetcher.getStopById(10020);
		
		JSONObject expected = null;
		try {
			expected = new JSONObject(R.string.testGetStopById_KnownRoute_Expected);
		} catch (Exception e) {
			System.out.println("testGetRouteById_KnownRoute: Error in creating expected value.");
		}
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test to see if method getStopById properly
	 * detects that there really is no corresponding 
	 * route for a possible stop id but not real (0).
	 * Should throw an IllegalArgumentException.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	@Test(timeout = TIMEOUT, expected=IllegalArgumentException.class)
	public void testGetStopById_NonExistingRoute() throws IOException, IllegalArgumentException {
		JSONObject actual = fetcher.getStopById(0);
	}
	
	/**
	 * Test to see if method getStopById properly
	 * detects an impossible negative stop id.
	 * Should throw an IllegalArgumentException.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	@Test(timeout = TIMEOUT, expected=IllegalArgumentException.class)
	public void testGetStopById_NegativeRoute() throws IOException, IllegalArgumentException {
		JSONObject actual = fetcher.getStopById(-1);
	}
	
	/**
	 * Test functionality of getRouteById by
	 * giving it a valid routeID.
	 * @throws IOException
	 */
	@Test(timeout = TIMEOUT)
	public void testGetRouteById_KnownRoute() throws IOException{
		JSONObject actual = fetcher.getRouteById(30);
		
		JSONObject expected = null;
		try {
			expected = new JSONObject(R.string.testGetRouteById_KnownRoute_Expected);
		} catch (Exception e) {
			System.out.println("testGetRouteById_KnownRoute: Error in creating expected value.");
		}
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test to see if method getRouteById properly
	 * detects that there really is no corresponding 
	 * route for a possible route id but not real (0).
	 * Should throw an IllegalArgumentException.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	@Test(timeout = TIMEOUT, expected=IllegalArgumentException.class)
	public void testGetStopsByLocation_NonExistingRoute() throws IOException, IllegalArgumentException {
		JSONObject actual = fetcher.getRouteById(0);
	}
	
	/**
	 * Test to see if method getRouteById properly
	 * detects an impossible negative route id.
	 * Should throw an IllegalArgumentException.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	@Test(timeout = TIMEOUT, expected=IllegalArgumentException.class)
	public void testGetStopsByLocation_NegativeRoute() throws IOException, IllegalArgumentException {
		JSONObject actual = fetcher.getRouteById(-1);
	}

	
	/**
	 * Tests getPolylines() with a valid route id (5).
	 * Asserts that there should be 11 polylines for this route.
	 * @throws IOException
	 */
	@Test(timeout = TIMEOUT)
	public void testGetPolylines_FunctionalSize() throws IOException{
		List<Polyline> actual = fetcher.getPolylines(5);
		
		assertEquals(11, actual);
	}
	
	/**
	 * Tests getPolylines() with a valid route id (5).
	 * Checks the a polyline(second polyline for ease of test)
	 * for expected encodedPolyline and encodedLevels.
	 * @throws IOException
	 */
	public void testGetPolylines_CheckAPolyline() throws IOException {
		List<Polyline> actual = fetcher.getPolylines(5);
		
		assertEquals("u{yaHh}viVcB?oB@q@?U?i@?gEAe@?{F@sFAG?yE?wDDeCCq@B{A?gB?}@c@k@]]Ou@c@a@Su@WEBGDGJ?f@@bB?V?b@?xBAj@C`@E`@AVAd@AfB?|A@`H?vF?xF",
				actual.get(1).getEncodedPolyline());
		assertEquals("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", actual.get(1).getEncodedLevels());
	}
	
	/**
	 * Calls getPolylines(0), a non existing route id.
	 * An empty ArrayList should be returned.
	 * @throws IOException
	 */
	@Test(timeout = TIMEOUT)
	public void testGetPolylines_NonExistingRoute() throws IOException {
		List<Polyline> actual = fetcher.getPolylines(0);
		List<Polyline> expected = new ArrayList<Polyline>();
		assertEquals(expected, actual);
	}
	
	/**
	 * Tests getPolylines() with negative route.
	 * Method should catch checkable erroneous route and throw
	 * an IllegalArgumentException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	@Test(timeout = TIMEOUT, expected=IllegalArgumentException.class)
	public void testGetPolylines_NegativeRoute() throws IOException, IllegalArgumentException {
		List<Polyline> actual = fetcher.getPolylines(-1);
	}

}
