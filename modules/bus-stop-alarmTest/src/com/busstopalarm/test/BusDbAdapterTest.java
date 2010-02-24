/**
 * Author: Huy Dang
 * Date: 02/23/2010
 * 
 * The White box testing for BusDbAdapter which tests the basic functionality of 
 * the database
 */
package com.busstopalarm.test;

import java.io.IOException;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import com.busstopalarm.*;

public class BusDbAdapterTest extends ActivityInstrumentationTestCase2<LocationListPage>{

	private LocationListPage activity;
	
	/**
	 * Specifies the location of the test at LocationListPage
	 */
	public BusDbAdapterTest(){
		super("com.busstopalarm", LocationListPage.class);
	}
	
	/**
	 * Initializes the set up for the test
	 */
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		activity = getActivity();
	}
		
	/**
	 * The initial database text file has 4 entries bus routes.
	 * Tests if number of entries in the DB is correct
	 */
	@SmallTest
	public void testAddMajorDbFromFile_1() throws IOException{
		int testFile = 1;
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.readDbFile(testFile);
		int result = activity.mBusDbHelper.getDestTbSize();
		assertEquals("Db should contains 4 destinations", 4,result);
	}
	
	/**
	 * The initial database text file has 4 entries bus routes.
	 * Checks the information in the first entry bus route
	 *
	 * @throws IOException Database file can't be read
	 */
	@SmallTest
	public void testAddMajorDbFromFile_2() throws IOException{
		int testFile = 1;
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.readDbFile(testFile);
		String result = activity.mBusDbHelper.getDestRouteDesc("70", "1_20");
		assertEquals("Destination description of route 70, stop 1_20", 
					 "UW Seattle",result);
	}
	
	/**
	 * The initial database text file has 4 entries bus routes.
	 * Checks the information in the second entry bus route
	 *
	 * @throws IOException Database file can't be read
	 */
	@SmallTest
	public void testAddMajorDbFromFile_3() throws IOException{
		int testFile = 1;
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.readDbFile(testFile);
		String result = activity.mBusDbHelper.getDestRouteDesc("71", "1_21");
		assertEquals("Destination description of route 71, stop 1_21", 
					 "UW and Ave",result);
	}
	
	/**
	 * The initial database text file has 4 entries bus routes.
	 * Checks the information in the third entry bus route
	 *
	 * @throws IOException Database file can't be read
	 */
	@SmallTest
	public void testAddMajorDbFromFile_4() throws IOException{
		int testFile = 1;
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.readDbFile(testFile);
		String result = activity.mBusDbHelper.getDestRouteDesc("72", "1_22");
		assertEquals("Destination description of route 72, stop 1_22", 
					 "Pike Market",result);
	}
	
	/**
	 * The initial database text file has 4 entries bus routes.
	 * Checks the information in the fourth entry bus route
	 *
	 * @throws IOException Database file can't be read
	 */
	@SmallTest
	public void testAddMajorDbFromFile_5() throws IOException{
		int testFile = 1;
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.readDbFile(testFile);
		String result = activity.mBusDbHelper.getDestRouteDesc("73", "1_23");
		assertEquals("Destination description of route 73, stop 1_23", 
					 "Downtown",result);
	}
	
	/**
	 * The initial database is empty. 
	 * Checks if the database generates the correct count for the added bus
	 * route entry
	 */	
	@SmallTest
	public void testCreateDestCheckInitialCount() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		
		String result = activity.mBusDbHelper.getDestCount("70", "1_12345");
		assertEquals("failed to assign count to entry","1", result);
	}
	
	/**
	 * The initial database is empty. 
	 * Checks if the database generates the correct route description for the 
	 * added bus route entry
	 */
	@SmallTest
	public void testCreateDestCheckInitialRouteDesc() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		
		String result = activity.mBusDbHelper.getDestRouteDesc("70", "1_12345");
		assertEquals("failed to assign description to bus entry","UW", result);
	}
	
	/**
	 * The initial database is empty. 
	 * Checks if the database generates the correct stop description for the 
	 * added bus route entry
	 */
	@SmallTest
	public void testCreateDestCheckInitialStopDesc() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		
		String result = activity.mBusDbHelper.getDestStopDesc("70", "1_12345");
		assertEquals("failed to assign description to bus stop",
					 "Paul Allen", result);
	}
	
	/**
	 * The initial database is empty. 
	 * Checks if the database generates the correct major flag value for the 
	 * added bus route entry
	 */
	@SmallTest
	public void testCreateDestCheckInitialMajorValue0() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		
		String result = activity.mBusDbHelper.getDestMajorVal("70", "1_12345");
		assertEquals("failed to assign major flag value to bus entry",
					 "0", result);
	}
	
	/**
	 * The initial database is empty. 
	 * Checks if the database generates the correct major flag value for the 
	 * added bus route entry
	 */
	@SmallTest
	public void testCreateDestCheckInitialMajorValue1() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 1);
		
		String result = activity.mBusDbHelper.getDestMajorVal("70", "1_12345");
		assertEquals("failed to assign major flag value to bus entry",
					 "1", result);
	}
	
	/**
	 * Tests to insert duplicate bus entry to the database
	 */
	@SmallTest
	public void testCreateExistedDest() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		
		long result = activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		assertEquals("failed to avoid insert existed entry", -1, result);
	}

	/**
	 * Tests if database can change bus route description
	 */
	@SmallTest
	public void testUpdateDestDesc_newRouteDesc() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		
		activity.mBusDbHelper.updateDestDesc
						("70", "UW-Seattle", "1_12345", "Paul Allen CSE");
		String result = activity.mBusDbHelper.getDestRouteDesc("70", "1_12345");
		assertEquals("failed to change bus route description", 
					 "UW-Seattle", result);
	}
	
	/**
	 * Tests if database can change stop description
	 */
	@SmallTest
	public void testUpdateDestDesc_newStopDesc() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		
		activity.mBusDbHelper.updateDestDesc
						("70", "UW-Seattle", "1_12345", "Paul Allen CSE");
		String result = activity.mBusDbHelper.getDestStopDesc("70", "1_12345");
		assertEquals("failed to change stop description", 
					 "Paul Allen CSE", result);
	}
	
	/**
	 * Tests if database increases count for the bus entry correctly
	 */
	@SmallTest
	public void testUpdateDestDesc_TimeCount_newCount1(){
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest("70", "UW", "1_12345", "Paul Allen", 0);
		activity.mBusDbHelper.updateDestDesc_TimeCount("70", "1_12345");
		String result = activity.mBusDbHelper.getDestCount("70", "1_12345");
		assertEquals("failed to update new count", "2", result);
	}
	
	/**
	 * Tests if database increases count for the bus entry correctly (2)
	 */
	@SmallTest
	public void testUpdateDestDesc_TimeCount_newCount2(){
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest("70", "UW", "1_12345", "Paul Allen", 0);
		activity.mBusDbHelper.updateDestDesc_TimeCount("70", "1_12345");
		activity.mBusDbHelper.updateDestDesc_TimeCount("70", "1_12345");
		activity.mBusDbHelper.updateDestDesc_TimeCount("70", "1_12345");
		
		String result = activity.mBusDbHelper.getDestCount("70", "1_12345");
		assertEquals("failed to update new count", "4", result);
	}
	
	/**
	 * Tests if database updates the timestamp for the bus entry correctly
	 */
	@SmallTest
	public void testUpdateDestDesc_TimeCount_newTime1(){
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest("70", "UW", "1_12345", "Paul Allen", 0);
		String result1 = activity.mBusDbHelper.getDestTime("70", "1_12345");
		
		activity.mBusDbHelper.updateDestDesc_TimeCount("70", "1_12345");
		String result2 = activity.mBusDbHelper.getDestTime("70", "1_12345");
		assertTrue("failed to update new timestamp", 
					result2.compareTo(result1) > 0);
	}
	
	/**
	 * Tests if database updates the timestamp for the bus entry correctly (2)
	 */
	@SmallTest
	public void testUpdateDestDesc_TimeCount_newTime2(){
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest("70", "UW", "1_12345", "Paul Allen", 0);
		String result1 = activity.mBusDbHelper.getDestTime("70", "1_12345");
		
		activity.mBusDbHelper.updateDestDesc_TimeCount("70", "1_12345");
		activity.mBusDbHelper.updateDestDesc_TimeCount("70", "1_12345");
		activity.mBusDbHelper.updateDestDesc_TimeCount("70", "1_12345");
		String result2 = activity.mBusDbHelper.getDestTime("70", "1_12345");
		assertTrue("failed to update new timestamp", 
					result2.compareTo(result1) > 0);
	}
	
	/**
	 * Tests if DB prevents deleting invalid bus entry properly
	 */
	@SmallTest
	public void testDeleteDest0() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.deleteDest("70", "1_12345");
		int result = activity.mBusDbHelper.getDestTbSize();
		assertEquals("failed to avoid delete destination from empty DB", 
					  0, result);
	}
	
	/**
	 * Tests if DB deletes the valid bus entry properly
	 * where there's zero entry left
	 */
	@SmallTest
	public void testDeleteDest0Left() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		
		activity.mBusDbHelper.deleteDest("70", "1_12345");
		int result = activity.mBusDbHelper.getDestTbSize();
		assertEquals("failed to delete destination", 0, result);
	}
	
	/**
	 * Tests if DB deletes the valid bus entry properly
	 * where there's one entry left
	 */
	@SmallTest
	public void testDeleteDest1Left() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		activity.mBusDbHelper.createDest
						("71", "UW", "2_12345", "Paul Allen", 0);
		
		activity.mBusDbHelper.deleteDest("70", "1_12345");
		int result = activity.mBusDbHelper.getDestTbSize();
		assertEquals("failed to delete destination", 1, result);
	}
	
	/**
	 * Tests if DB deletes the valid bus entry properly
	 * where there's two entries left
	 */
	@SmallTest
	public void testDeleteDest2Left() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		activity.mBusDbHelper.createDest
						("71", "UW", "2_12345", "Paul Allen", 0);
		activity.mBusDbHelper.createDest
						("72", "UW", "3_12345", "Paul Allen", 0);
		
		activity.mBusDbHelper.deleteDest("72", "3_12345");
		int result = activity.mBusDbHelper.getDestTbSize();
		assertEquals("failed to delete destination", 2, result);
	}
	
	/**
	 * Tests if table size is zero after deleting all entries
	 */
	@SmallTest
	public void testDeleteAllDestinations_0() {
		activity.mBusDbHelper.deleteAllDestinations();
		int result = activity.mBusDbHelper.getDestTbSize();
		assertEquals("failed to delete destination", 0, result);
	}
	
	/**
	 * Tests if table size is updated correctly after adding a new entry
	 */
	@SmallTest
	public void testDeleteAllDestinations_1() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		int result = activity.mBusDbHelper.getDestTbSize();
		assertEquals("failed to delete destination", 1, result);
	}
	
	/**
	 * Tests if table size is updated correctly after adding a new entry (2)
	 */	
	@SmallTest
	public void testDeleteAllDestinations_2() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		activity.mBusDbHelper.createDest
						("72", "UW", "3_12345", "Paul Allen", 0);
		int result = activity.mBusDbHelper.getDestTbSize();
		assertEquals("failed to delete destination", 2, result);
	}
	
	/**
	 * Tests if table size is updated correctly after adding a new entry (3)
	 */
	@SmallTest
	public void testDeleteAllDestinations_3() {
		activity.mBusDbHelper.deleteAllDestinations();
		activity.mBusDbHelper.createDest
						("70", "UW", "1_12345", "Paul Allen", 0);
		activity.mBusDbHelper.createDest
						("71", "UW", "2_12345", "Paul Allen", 0);
		activity.mBusDbHelper.createDest
						("72", "UW", "3_12345", "Paul Allen", 0);
		int result = activity.mBusDbHelper.getDestTbSize();
		assertEquals("failed to delete destination", 3, result);
	}
	
	

}
