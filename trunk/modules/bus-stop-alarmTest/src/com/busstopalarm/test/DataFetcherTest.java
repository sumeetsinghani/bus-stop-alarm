/**
 * Test driven test case for DataFetcher.java. The intended use for
 * DataFetcher is to allow for easy retrieval of data from queries to
 * the OneBusAway API, parse the data and produce objects to represent
 * the data.
 * 
 * @author Michael Eng
 */

package com.busstopalarm.test;

import java.io.IOException;
import java.util.List;

import android.test.ActivityInstrumentationTestCase2;

import com.busstopalarm.BusRoute;
import com.busstopalarm.BusStop;
import com.busstopalarm.DataFetcher;
import com.busstopalarm.MainPage;
import com.busstopalarm.Polyline;

public class DataFetcherTest extends ActivityInstrumentationTestCase2<MainPage> {

	private DataFetcher fetcher;
	
	// This test doesn't require an activity, so
	// tests in the main page.
	public DataFetcherTest() {
		super("com.busstopalarm", MainPage.class);
	}
	
	// Instantiates DataFetcher for testing.
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		fetcher = new DataFetcher();
	}
	

	/**
	 * Verify that the constructor creates a new DataFetcher.
	 */
	public void testDataFetcher() {
		assertNotNull(fetcher);
	}
	
	
	/* Start of Tests for getBusRouteById() */
	
	/**
	 * Test functionality of getBusRouteById by
	 * giving it a valid stop id. Each route query sends a list of polylines, so this 
	 * checks that the first of the list of polylines loaded
	 * into the returned BusRoute is correct when
	 * the method does try to get polylines or stops.
	 * @throws IOException
	 */
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckPolylinesLast()throws IOException {
		BusRoute busRoute = fetcher.getBusRouteById(30, true);
		Polyline actual = busRoute.getPolylines().get(busRoute.getPolylines().size()-1);
		
		
		assertEquals("g|_bHtpfiV?uE?mBAsBAmCiGFCBCJ@dFrGB",
				actual.getEncodedPolyline());
		assertEquals("BBBBBBBBBB", actual.getEncodedLevels());
	}
	
	/**
	 * Test functionality of getBusRouteById by
	 * giving it a valid stop id. Each route query sends a list of polylines, so this 
	 * checks that the first of the list of polylines loaded
	 * into the returned BusRoute is correct when
	 * the method does try to get polylines or stops.
	 * @throws IOException
	 */
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckPolylinesFirst()throws IOException {
		BusRoute busRoute = fetcher.getBusRouteById(30, true);
		List<Polyline> actual = busRoute.getPolylines();
		
		
		assertEquals("cetaHnpxiViFCcGC?_C?aC@eG?cG?iO?eCBcCA_G?uE?i@@EAk@@iE?O?K?kB?iA?Y?S?S?W?uC?wA?{C?C?cB?cCAaCeB?e@?e@@SFODEDMFOH_An@}AbAsBpA_CbBwBvASLu@b@g@Pm@Le@@cA?uJA_@?a@?A?e@AYCMAACOAQCuAa@y@U_@GYKg@KE?_AG_B@gIDyB?E?y@@iARq@Ps@\\uEtBk@VuAr@{DtBgB`As@^kDlBo@d@i@b@eA|@]ZsAbAa@\\W`@Sf@y@xBAB{BnHELELK\\mA~DQj@eBnESDgA?u@?G?a@?E?I?C?W?s@?_@?}@Cq@?aA?oB?s@?e@BsCLS?E?MBhAqNBI?MjAqNToCLkAXcDBS}EIqLU{GMkBEoCGASGe@UuBQuBG]?yDBuF?qF@kF?qFDsFDqF@kF?mFBqF?iF@sE?aC@}A?aFBqA@sA?mAAmC^?pCBT??I?M?Q@W?aA@aF?WCk@Ee@CgA?a@PgANq@DMJa@TaA^w@\\u@Ra@ZoABODk@@}@@I?Q?a@B{FK[?iCe@?]?s@Ca@?cEE_CC@uB@oBm@C{BAgHGgHEgHGkJEgJE?O?gB?}A?M?M@cB?qB@mB?qB?g@?sB?qB@qB?qB@eCM?iHC}H?iBCsABk@?GyAYuBGe@Eo@AaA@_BFi@bCQ~AGp@AJCSyAG]kAoHGm@GcA?iB?U?}D?sE?wF?qE@sB?qBAwB@wB?{B?W?_B?{B?sB?{B?W?eB?mB?q@?_A?kB?a@?mA?oB?O?aB?mB?K?aB?wD?_NGw@}AwOi@iGKgAEeACcAB}APeDPQTIRBt@H|@JR@HiB@aACaAE{@AICWGe@G]Gc@Ke@Ma@Qg@Si@KUu@qAAAeAeBKMw@mAg@u@oAqBqAmBsBeDGI]i@g@w@y@qAS[qCgEiByC??e@u@CEkCkEiCaEc@WEC]]YKOGc@Ek@?qCBeLFsB@iNJ}@???cA@{ABmB?YFe@Hi@VmBvAg@X}D`AoAZMoAGw@Ao@@iC?kI?gF?Y?c@D[Fy@Ny@DUh@eB`BoFPaAHqAJyDJ_ARq@SOs@EwADCMEKICI@IJARDRNJ?`@BdHuABGOGGGCC?IDEP?B?LDHJJHEJIBS",
				actual.get(0).getEncodedPolyline());
		assertEquals("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", actual.get(0).getEncodedLevels());
	}
	
	/**
	 * Test functionality of getBusRouteById by
	 * giving it a valid stop id. Each route query sends a list of polylines, so this 
	 * checks that the number of polylines loaded
	 * into the returned BusRoute is correct when
	 * the method does try to get polylines or stops.
	 * @throws IOException
	 */
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckPolylinesSize()throws IOException {
		BusRoute busRoute = fetcher.getBusRouteById(30, true);
		int actual = busRoute.getPolylines().size();
		int expected = 12;
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test functionality of getBusRouteById by
	 * giving it a valid stop id. Checks that the last bus stop loaded
	 * into the returned BusRoute is correct when
	 * the method does try to get polylines or stops.
	 * @throws IOException
	 */
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckBusStopsLast()throws IOException {
		BusRoute busRoute = fetcher.getBusRouteById(30, true);
		BusStop actual = busRoute.getBusStops().get(108);
		
		// Create expected
		BusStop expected = new BusStop();
		expected.setStopId("1_9990");
		expected.setDirection("W");
		expected.setLatitude(47.6690254);
		expected.setLongitude(-122.279846);
		expected.setLocationType("0");
		expected.setName("NE 55th St &amp; 45th Ave NE");
		expected.setCode("9990");
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test functionality of getBusRouteById by
	 * giving it a valid stop id. Checks that the first bus stop loaded
	 * into the returned BusRoute is correct when
	 * the method does try to get polylines or stops.
	 * @throws IOException
	 */
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckBusStopsFirst()throws IOException {
		BusRoute busRoute = fetcher.getBusRouteById(30, true);
		BusStop actual = busRoute.getBusStops().get(0);
		
		// Create expected
		BusStop expected = new BusStop();
		expected.setStopId("1_10000");
		expected.setDirection("W");
		expected.setLatitude(47.6685753);
		expected.setLongitude(-122.283653);
		expected.setLocationType("0");
		expected.setName("NE 55th St &amp; 43rd Ave NE");
		expected.setCode("10000");
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test functionality of getBusRouteById by
	 * giving it a valid stop id. Checks that the number of bus stops
	 * loaded into the returned BusRoute is correct when
	 * the method does try to get polylines or stops.
	 * @throws IOException
	 */
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckBusStopsSize()throws IOException {
		BusRoute actual = fetcher.getBusRouteById(30, true);
		int expected = 109;
		
		// Assert state of object.
		// Can't use equal because too many stops to manual check.
		assertNotNull(actual.getBusStops());
		assertEquals(expected, actual.getBusStops().size());
	}
	
	/**
	 * Test functionality of getBusRouteById by
	 * giving it a valid stop id. Checks that the route info
	 * that should be loaded into the returned BusRoute is correct when
	 * the method does try to get polylines or stops.
	 * @throws IOException
	 */
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckRouteInfo()throws IOException {
		BusRoute actual = fetcher.getBusRouteById(30, true);
		
		// Assert state of object.
		// Can't use equal because too many stops to manual check.
		assertEquals("1_30", actual.getRouteId());
		assertEquals("30", actual.getShortName());
		assertEquals("Sandpoint/U-Dist/Seattle Center", actual.getDescription());
		assertEquals("3", actual.getType());
	}
	
	/**
	 * Test functionality of getBusRouteById by
	 * giving it a valid stop id. Checks that no polylines or bus stops are 
	 * loaded into the returned BusRoute when
	 * the method doesn't try to get polylines or stops.
	 * @throws IOException
	 */
	public void testGetBusRouteById_KnownRoute_NoPolylinesAndStops_CheckPolylinesAndStopsNull()throws IOException {
		BusRoute actual = fetcher.getBusRouteById(30, false);
		
		// Test to make sure no polylines or stops were added.
		assertNull(actual.getPolylines());
		assertNull(actual.getBusStops());
	}
	
	/**
	 * Test functionality of getBusRouteById by
	 * giving it a valid stop id. Checks that the route info
	 * that should be loaded into the returned BusRoute is correct when
	 * the method doesn't try to get polylines or stops.
	 * @throws IOException
	 */
	public void testGetBusRouteById_KnownRoute_NoPolylinesAndStops_CheckRouteInfo()throws IOException {
		BusRoute actual = fetcher.getBusRouteById(30, false);
		
		// Assert state of the object.
		assertEquals("1_30", actual.getRouteId());
		assertEquals("30", actual.getShortName());
		assertEquals("Sandpoint/U-Dist/Seattle Center", actual.getDescription());
		assertEquals("3", actual.getType());
	}
	
	/**
	 * Test to see if method getBusRouteById properly
	 * detects that there really is no corresponding 
	 * route for a possible stop id but not real (0).
	 * Should throw an IllegalArgumentException.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void testGetBusRouteById_NonExistingRoute_WithPolylinesAndStops() throws IOException, IllegalArgumentException {
		try {
			fetcher.getBusRouteById(0, true);
			fail("getBusRouteById() should've thrown an IllegalArgumentException!");
		} catch(IllegalArgumentException e) {
			// Test Passes!
		} catch(Exception e) {
			fail("getBusRouteById() threw the wrong exception.");
		}
	}
	
	/**
	 * Test to see if method getBusRouteById properly
	 * detects that there really is no corresponding 
	 * route for a possible stop id but not real (0).
	 * Should throw an IllegalArgumentException.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void testGetBusRouteById_NonExistingRoute_NoPolylinesAndStops() throws IOException, IllegalArgumentException {
		try {
			fetcher.getBusRouteById(0, false);
			fail("getBusRouteById() should've thrown an IllegalArgumentException!");
		} catch(IllegalArgumentException e) {
			// Test Passes!
		} catch(Exception e) {
			fail("getBusRouteById() threw the wrong exception.");
		}
	}
	
	/**
	 * Test to see if method getBusRouteById properly
	 * detects an impossible negative route id. Also when
	 * includePolylinesAndStops is set to true.
	 * Should throw an IllegalArgumentException.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void testGetBusRouteById_NegativeRoute_WithPolylinesAndStops() throws IOException, IllegalArgumentException {
		try {
			fetcher.getBusRouteById(-1, true);
			fail("getBusRouteById() should've thrown an IllegalArgumentException!");
		} catch(IllegalArgumentException e) {
			// Test Passes!
		} catch(Exception e) {
			fail("getBusRouteById() threw the wrong exception.");
		}
	}
	
	/**
	 * Test to see if method getBusRouteById properly
	 * detects an impossible negative route id. Also when
	 * includePolylinesAndStops is set to false.
	 * Should throw an IllegalArgumentException.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void testGetBusRouteById_NegativeRoute_NoPolylinesAndStops() throws IOException, IllegalArgumentException {
		try {
			fetcher.getBusRouteById(-1, false);
			fail("getBusRouteById() should've thrown an IllegalArgumentException!");
		} catch(IllegalArgumentException e) {
			// Test Passes!
		} catch(Exception e) {
			fail("getBusRouteById() threw the wrong exception.");
		}
	}
	/* End of Tests for getBusRouteById() */
	
	
	
	/* Start of Tests for getStopById() */
	
	/**
	 * Test functionality of getStopById by
	 * giving it a valid stop id.
	 * @throws IOException
	 */
	public void testGetStopById_KnownRoute() throws IOException{
		BusStop actual = fetcher.getStopById(10020);
		
		// Create expected
		BusStop expected = new BusStop();
		expected.setStopId("1_10020");
		expected.setDirection("W");
		expected.setLatitude(47.6685791);
		expected.setLongitude(-122.2883);
		expected.setLocationType("0");
		expected.setName("NE 55th St &amp; 37th Ave N");
		expected.setCode("10020");
		
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
	public void testGetStopById_NonExistingRoute() throws IOException, IllegalArgumentException {
		try {
			fetcher.getStopById(0);
			fail("getStopById() should've thrown an IllegalArgumentException!");
		} catch(IllegalArgumentException e) {
			// Test Passes!
		} catch(Exception e) {
			fail("getStopById() threw the wrong exception.");
		}
	}
	
	/**
	 * Test to see if method getStopById properly
	 * detects an impossible negative stop id.
	 * Should throw an IllegalArgumentException.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void testGetStopById_NegativeRoute() throws IOException, IllegalArgumentException {
		try {
			fetcher.getStopById(-1);
			fail("getStopById() should've thrown an IllegalArgumentException!");
		} catch(IllegalArgumentException e) {
			// Test Passes!
		} catch (Exception e) {
			fail("getStopById() threw the wrong exception.");
		}
	}
	/* End of Tests for getStopById() */
	
	
	/* Tests for getBusStopsForRoute() */
	
	/**
	 * Test functionality of getStopById to return the list of stops by
	 * giving it a valid stop id. Check the boundary case of the last stop.
	 * @throws IOException
	 */
	public void testGetBusStopsForRoute_KnownRoute_CheckLastStop() throws IOException{
		List<BusStop> busStops = fetcher.getBusStopsForRoute(30);
		BusStop actual = busStops.get(0);
		
		// Create expected
		BusStop expected = new BusStop();
		expected.setStopId("1_9990");
		expected.setDirection("W");
		expected.setLatitude(47.6690254);
		expected.setLongitude(-122.279846);
		expected.setLocationType("0");
		expected.setName("NE 55th St &amp; 45th Ave NE");
		expected.setCode("9990");
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test functionality of getStopById to return the list of stops by
	 * giving it a valid stop id. Check the boundary case of the first stop.
	 * @throws IOException
	 */
	public void testGetBusStopsForRoute_KnownRoute_CheckFirstStop() throws IOException{
		List<BusStop> busStops = fetcher.getBusStopsForRoute(30);
		BusStop actual = busStops.get(0);
		
		// Create expected
		BusStop expected = new BusStop();
		expected.setStopId("1_10000");
		expected.setDirection("W");
		expected.setLatitude(47.6685753);
		expected.setLongitude(-122.283653);
		expected.setLocationType("0");
		expected.setName("NE 55th St &amp; 43rd Ave NE");
		expected.setCode("10000");
		
		assertEquals(expected, actual);
	}
	
	/**
	 * Test functionality of getStopById to return the correct number of stops by
	 * giving it a valid stop id.
	 * @throws IOException
	 */
	public void testGetBusStopsForRoute_KnownRoute_CheckSize() throws IOException{
		List<BusStop> busStops = fetcher.getBusStopsForRoute(30);
		int actual = busStops.size();
		int expected = 109;
		
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
	public void testGetBusStopsForRoute_NonExistingRoute() throws IOException, IllegalArgumentException {
		try {
			fetcher.getBusStopsForRoute(0);
			fail("getBusStopsForRoute() should've thrown an IllegalArgumentException!");
		} catch(IllegalArgumentException e) {
			// Test Passes!
		} catch(Exception e) {
			fail("getBusStopsForRoute() threw the wrong exception.");
		}
	}
	
	/**
	 * Test to see if method getStopsForRoute properly
	 * detects an impossible negative route id.
	 * Should throw an IllegalArgumentException.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void testGetBusStopsForRoute_NegativeRoute() throws IOException, IllegalArgumentException {
		try {
			fetcher.getBusStopsForRoute(-1);
			fail("getBusStopsForRoute() should've thrown an IllegalArgumentException!");
		} catch(IllegalArgumentException e ) {
			// Test Passes!
		} catch(Exception e) {
			fail("getBusStopsForRoute() threw the wrong exception.");
		}
	}
	/* End of Tests for getBusStopsForRoute() */
	
	
	/* Start of Tests for getPolylines() */
	
	/**
	 * Tests getPolylines() with a valid route id (30).
	 * Asserts that there should be 12 polylines for this route.
	 * @throws IOException
	 */
	public void testGetPolylines_FunctionalSize() throws IOException{
		List<Polyline> actual = fetcher.getPolylines(30);
		int expected = 12;
		assertNotNull(actual);
		assertEquals(expected, actual.size());
	}	
	
	/**
	 * Tests getPolylines() with a valid route id (30).
	 * Checks the last polyline for expected encodedPolyline and encodedLevels.
	 * @throws IOException
	 */
	public void testGetPolylines_CheckAPolylineLast() throws IOException {
		List<Polyline> polylines = fetcher.getPolylines(30);
		Polyline actual = polylines.get(polylines.size()-1);
		
		assertEquals("g|_bHtpfiV?uE?mBAsBAmCiGFCBCJ@dFrGB",
				actual.getEncodedPolyline());
		assertEquals("BBBBBBBBBB", actual.getEncodedLevels());
	}
	
	/**
	 * Tests getPolylines() with a valid route id (30).
	 * Checks the first polyline for expected encodedPolyline and encodedLevels.
	 * @throws IOException
	 */
	public void testGetPolylines_CheckAPolylineFirst() throws IOException {
		List<Polyline> polylines = fetcher.getPolylines(30);
		Polyline actual = polylines.get(0);
		
		assertEquals("cetaHnpxiViFCcGC?_C?aC@eG?cG?iO?eCBcCA_G?uE?i@@EAk@@iE?O?K?kB?iA?Y?S?S?W?uC?wA?{C?C?cB?cCAaCeB?e@?e@@SFODEDMFOH_An@}AbAsBpA_CbBwBvASLu@b@g@Pm@Le@@cA?uJA_@?a@?A?e@AYCMAACOAQCuAa@y@U_@GYKg@KE?_AG_B@gIDyB?E?y@@iARq@Ps@\\uEtBk@VuAr@{DtBgB`As@^kDlBo@d@i@b@eA|@]ZsAbAa@\\W`@Sf@y@xBAB{BnHELELK\\mA~DQj@eBnESDgA?u@?G?a@?E?I?C?W?s@?_@?}@Cq@?aA?oB?s@?e@BsCLS?E?MBhAqNBI?MjAqNToCLkAXcDBS}EIqLU{GMkBEoCGASGe@UuBQuBG]?yDBuF?qF@kF?qFDsFDqF@kF?mFBqF?iF@sE?aC@}A?aFBqA@sA?mAAmC^?pCBT??I?M?Q@W?aA@aF?WCk@Ee@CgA?a@PgANq@DMJa@TaA^w@\\u@Ra@ZoABODk@@}@@I?Q?a@B{FK[?iCe@?]?s@Ca@?cEE_CC@uB@oBm@C{BAgHGgHEgHGkJEgJE?O?gB?}A?M?M@cB?qB@mB?qB?g@?sB?qB@qB?qB@eCM?iHC}H?iBCsABk@?GyAYuBGe@Eo@AaA@_BFi@bCQ~AGp@AJCSyAG]kAoHGm@GcA?iB?U?}D?sE?wF?qE@sB?qBAwB@wB?{B?W?_B?{B?sB?{B?W?eB?mB?q@?_A?kB?a@?mA?oB?O?aB?mB?K?aB?wD?_NGw@}AwOi@iGKgAEeACcAB}APeDPQTIRBt@H|@JR@HiB@aACaAE{@AICWGe@G]Gc@Ke@Ma@Qg@Si@KUu@qAAAeAeBKMw@mAg@u@oAqBqAmBsBeDGI]i@g@w@y@qAS[qCgEiByC??e@u@CEkCkEiCaEc@WEC]]YKOGc@Ek@?qCBeLFsB@iNJ}@???cA@{ABmB?YFe@Hi@VmBvAg@X}D`AoAZMoAGw@Ao@@iC?kI?gF?Y?c@D[Fy@Ny@DUh@eB`BoFPaAHqAJyDJ_ARq@SOs@EwADCMEKICI@IJARDRNJ?`@BdHuABGOGGGCC?IDEP?B?LDHJJHEJIBS",
				actual.getEncodedPolyline());
		assertEquals("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", actual.getEncodedLevels());
	}
	
	/**
	 * Calls getPolylines(0), a non existing route id.
	 * It should detect that it is not a real route an throw
	 * and IllegalArgumentException.
	 * An empty ArrayList should be returned.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void testGetPolylines_NonExistingRoute() throws IOException, IllegalArgumentException {
		try {
			fetcher.getPolylines(0);
			fail("getPolylines() should've thrown an IllegalArgumentException!");
		} catch(IllegalArgumentException e ) {
			// Test Passes!
		} catch(Exception e) {
			fail("getPolylines() threw the wrong exception.");
		}
	}
	
	/**
	 * Tests getPolylines() with negative route.
	 * Method should catch checkable erroneous route and throw
	 * an IllegalArgumentException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void testGetPolylines_NegativeRoute() throws IOException, IllegalArgumentException {
		try {
			fetcher.getPolylines(-1);
			fail("getPolylines() should've thrown an IllegalArgumentException!");
		} catch(IllegalArgumentException e ) {
			// Test Passes!
		} catch(Exception e) {
			fail("getPolylines() threw the wrong exception.");
		}
	}
	
	/*End of Tests for getPolylines() */
}
