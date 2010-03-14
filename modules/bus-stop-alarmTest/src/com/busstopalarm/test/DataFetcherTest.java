package com.busstopalarm.test;
/**
 * Test driven test case for DataFetcher.java. The intended use for
 * DataFetcher is to allow for easy retrieval of data from queries to
 * the OneBusAway API, parse the data and produce objects to represent
 * the data.
 * 
 * @author Michael Eng
 */


import java.io.IOException;
import java.util.List;

import org.json.JSONException;

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
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckPolylinesLast()throws IOException, JSONException {
		BusRoute busRoute = fetcher.getBusRouteById(30, true);
		Polyline actual = busRoute.getPolylines().get(busRoute.getPolylines().size()-1);
		
		assertEquals("i|_bHrpfiV?uE?mB?oBAoCiGDCDCJ@bFpGB",
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
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckPolylinesFirst()throws IOException, JSONException {
		BusRoute busRoute = fetcher.getBusRouteById(30, true);
		List<Polyline> actual = busRoute.getPolylines();
		
		assertEquals("cetaHlpxiViFCeG??aC?aC@eG?cG@iO@eC?cC?_G?uE?k@?C?k@?iE?O?K?mB@gA?YAU@Q?W?wC?uA?yC?E?cB?cC?aCiB?c@?g@BSDMDEBMJMFaAl@}AdAsBpA_CbBwBvASJu@b@g@Tm@Je@@aA?yJA_@?]?C?e@CYAMAACOAQEuA_@y@U_@G[Kg@IEC}@E_B?gIDyB?E?y@DgANq@Ru@\\uEtBm@VsAr@{DtBgB`As@^kDnBo@b@i@`@gA|@[ZsAdAa@\\W`@Sd@{@zB?@}BpHCLELK\\mA~DQl@cBlEUBgA?u@?I?_@?E?G?E?W?q@?a@?}@Cq@?aA?oB?s@?e@BsCLU@C?MBjAqN?I@MjAqNRoCLkAZeDBQ}EIsLWyGMkBCoCIAQGe@UwBSuBE]?wDBwF?oF@mF?oFDuFDoF@mF@mF@oF?gF@uE?aC@}A@aF?qABuA?mAAmC^?pCDT??I?M?S?U@aA@aF?YCi@Eg@CeA?a@PgAPs@BMH_@VaA`@y@Zs@Ra@XmABQFm@@}@@I?Q?_@B}FMW@mCc@?_@?s@Ca@?cEC_CC@sB@qBm@?{BGgHCgHGgHGkJEiJE@O?gB?_B?I?Q@cB?oB@oB?oB?g@?sB?qB@sB?oB@gCM?iHA}H?iBCsABk@?GyAYuBIe@Cq@AaA@}AFi@`CS`BEp@CJASyAG]kAoHGm@GcA?iB?W?{D?qE?{F?qE@oB?uB?wB?wB?yB?W?_B?yB?uB?{B?W?eB?mB?q@?_A?mB?]?oA?qB?M?}A?oB?M?aB?wD?_NIy@yAwOk@gGKiAGcAAcAByANiDRSRGRBv@Hz@JT@HiB?aAAaAG{@?KCWGe@E]Ic@Me@K_@Qg@Ui@IUu@qAAAeAeBKOw@kAi@w@mAkBqAsBsBeDGG]k@g@w@y@oAS[oCiEkBwC??e@w@CCkCgEkCgEc@QCI_@]WIOGc@Ek@?qC@eLHsB@iNH}@?eA@yABkB@]Dc@Jk@VkBvAi@V{DfAoAVMoAIy@?m@?iC@kIAgF?YBc@@[H{@Nw@DSf@gBbBqFPaAHoAJyDJaAPo@SQo@CyABCIGMIAE?KHATDPNJAb@DdHuABGMGIGCC?IBEP?D?LDHJHJEHIBQ",
				actual.get(0).getEncodedPolyline());
		assertEquals("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", actual.get(0).getEncodedLevels());
	}
	
	/**
	 * Test functionality of getBusRouteById by
	 * giving it a valid stop id. Each route query sends a list of polylines, so this 
	 * checks that the number of polylines loaded
	 * into the returned BusRoute is correct when
	 * the method does try to get polylines or stops.
	 * @throws IOException
	 */
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckPolylinesSize()throws IOException, JSONException {
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
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckBusStopsLast()throws IOException, JSONException {
		BusRoute busRoute = fetcher.getBusRouteById(30, true);
		BusStop actual = busRoute.getBusStops().get(busRoute.getBusStops().size() - 1);
		
		// Create expected
		BusStop expected = new BusStop();
		expected.setStopId("1_9990");
		expected.setDirection("W");
		expected.setLatitude(47.6690254);
		expected.setLongitude(-122.279839);
		expected.setLocationType("0");
		expected.setName("NE 55th St & 45th Ave NE");
		expected.setCode("9990");
		
		/* Forced to used assertTrue because assertEquals was
		 * calling the two parameter's toString method an comparing those strings, 
		 * instead of calling their .equals() methods.
		 */
		//assertEquals(expected, actual);
		assertTrue(expected.equals(actual));
	}
	
	/**
	 * Test functionality of getBusRouteById by
	 * giving it a valid stop id. Checks that the first bus stop loaded
	 * into the returned BusRoute is correct when
	 * the method does try to get polylines or stops.
	 * @throws IOException
	 */
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckBusStopsFirst()throws IOException, JSONException {
		BusRoute busRoute = fetcher.getBusRouteById(30, true);
		BusStop actual = busRoute.getBusStops().get(0);
		
		// Create expected
		BusStop expected = new BusStop();
		expected.setStopId("1_10000");
		expected.setDirection("W");
		expected.setLatitude(47.6685753);
		expected.setLongitude(-122.283653);
		expected.setLocationType("0");
		expected.setName("NE 55th St & 43rd Ave NE");
		expected.setCode("10000");
		
		/* Forced to used assertTrue because assertEquals was
		 * calling the two parameter's toString method an comparing those strings, 
		 * instead of calling their .equals() methods.
		 */
		//assertEquals(expected, actual);
		assertTrue(expected.equals(actual));
	}
	
	/**
	 * Test functionality of getBusRouteById by
	 * giving it a valid stop id. Checks that the number of bus stops
	 * loaded into the returned BusRoute is correct when
	 * the method does try to get polylines or stops.
	 * @throws IOException
	 */
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckBusStopsSize()throws IOException, JSONException {
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
	public void testGetBusRouteById_KnownRoute_IncludePolylinesAndStops_CheckRouteInfo()throws IOException, JSONException {
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
	public void testGetBusRouteById_KnownRoute_NoPolylinesAndStops_CheckPolylinesAndStopsNull()throws IOException, JSONException {
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
	public void testGetBusRouteById_KnownRoute_NoPolylinesAndStops_CheckRouteInfo()throws IOException, JSONException {
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
			fail("getBusRouteById() threw the wrong exception." + e.toString());
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
		expected.setName("NE 55th St & 37th Ave N");
		expected.setCode("10020");
		
		/* Forced to used assertTrue because assertEquals was
		 * calling the two parameter's toString method an comparing those strings, 
		 * instead of calling their .equals() methods.
		 */
		//assertEquals(expected, actual);
		assertTrue(expected.equals(actual));
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
	public void testGetBusStopsForRoute_KnownRoute_CheckLastStop() throws IOException, JSONException{
		List<BusStop> busStops = fetcher.getBusStopsForRoute(30);
		BusStop actual = busStops.get(busStops.size() - 1);
		
		// Create expected
		BusStop expected = new BusStop();
		expected.setStopId("1_9990");
		expected.setDirection("W");
		expected.setLatitude(47.6690254);
		expected.setLongitude(-122.279839);
		expected.setLocationType("0");
		expected.setName("NE 55th St & 45th Ave NE");
		expected.setCode("9990");
		
		/* Forced to used assertTrue because assertEquals was
		 * calling the two parameter's toString method an comparing those strings, 
		 * instead of calling their .equals() methods.
		 */
		//assertEquals(expected, actual);
		assertTrue(expected.equals(actual));
	}
	
	/**
	 * Test functionality of getStopById to return the list of stops by
	 * giving it a valid stop id. Check the boundary case of the first stop.
	 * @throws IOException
	 */
	public void testGetBusStopsForRoute_KnownRoute_CheckFirstStop() throws IOException, JSONException{
		List<BusStop> busStops = fetcher.getBusStopsForRoute(30);
		BusStop actual = busStops.get(0);
		
		// Create expected
		BusStop expected = new BusStop();
		expected.setStopId("1_10000");
		expected.setDirection("W");
		expected.setLatitude(47.6685753);
		expected.setLongitude(-122.283653);
		expected.setLocationType("0");
		expected.setName("NE 55th St & 43rd Ave NE");
		expected.setCode("10000");
		
		/* Forced to used assertTrue because assertEquals was
		 * calling the two parameter's toString method an comparing those strings, 
		 * instead of calling their .equals() methods.
		 */
		//assertEquals(expected, actual);
		assertTrue(expected.equals(actual));
	}
	
	/**
	 * Test functionality of getStopById to return the correct number of stops by
	 * giving it a valid stop id.
	 * @throws IOException
	 */
	public void testGetBusStopsForRoute_KnownRoute_CheckSize() throws IOException, JSONException{
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
		
		assertEquals("i|_bHrpfiV?uE?mB?oBAoCiGDCDCJ@bFpGB",
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
		
		assertEquals("cetaHlpxiViFCeG??aC?aC@eG?cG@iO@eC?cC?_G?uE?k@?C?k@?iE?O?K?mB@gA?YAU@Q?W?wC?uA?yC?E?cB?cC?aCiB?c@?g@BSDMDEBMJMFaAl@}AdAsBpA_CbBwBvASJu@b@g@Tm@Je@@aA?yJA_@?]?C?e@CYAMAACOAQEuA_@y@U_@G[Kg@IEC}@E_B?gIDyB?E?y@DgANq@Ru@\\uEtBm@VsAr@{DtBgB`As@^kDnBo@b@i@`@gA|@[ZsAdAa@\\W`@Sd@{@zB?@}BpHCLELK\\mA~DQl@cBlEUBgA?u@?I?_@?E?G?E?W?q@?a@?}@Cq@?aA?oB?s@?e@BsCLU@C?MBjAqN?I@MjAqNRoCLkAZeDBQ}EIsLWyGMkBCoCIAQGe@UwBSuBE]?wDBwF?oF@mF?oFDuFDoF@mF@mF@oF?gF@uE?aC@}A@aF?qABuA?mAAmC^?pCDT??I?M?S?U@aA@aF?YCi@Eg@CeA?a@PgAPs@BMH_@VaA`@y@Zs@Ra@XmABQFm@@}@@I?Q?_@B}FMW@mCc@?_@?s@Ca@?cEC_CC@sB@qBm@?{BGgHCgHGgHGkJEiJE@O?gB?_B?I?Q@cB?oB@oB?oB?g@?sB?qB@sB?oB@gCM?iHA}H?iBCsABk@?GyAYuBIe@Cq@AaA@}AFi@`CS`BEp@CJASyAG]kAoHGm@GcA?iB?W?{D?qE?{F?qE@oB?uB?wB?wB?yB?W?_B?yB?uB?{B?W?eB?mB?q@?_A?mB?]?oA?qB?M?}A?oB?M?aB?wD?_NIy@yAwOk@gGKiAGcAAcAByANiDRSRGRBv@Hz@JT@HiB?aAAaAG{@?KCWGe@E]Ic@Me@K_@Qg@Ui@IUu@qAAAeAeBKOw@kAi@w@mAkBqAsBsBeDGG]k@g@w@y@oAS[oCiEkBwC??e@w@CCkCgEkCgEc@QCI_@]WIOGc@Ek@?qC@eLHsB@iNH}@?eA@yABkB@]Dc@Jk@VkBvAi@V{DfAoAVMoAIy@?m@?iC@kIAgF?YBc@@[H{@Nw@DSf@gBbBqFPaAHoAJyDJaAPo@SQo@CyABCIGMIAE?KHATDPNJAb@DdHuABGMGIGCC?IBEP?D?LDHJHJEHIBQ",
				actual.getEncodedPolyline());
		assertEquals("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", actual.getEncodedLevels());
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
