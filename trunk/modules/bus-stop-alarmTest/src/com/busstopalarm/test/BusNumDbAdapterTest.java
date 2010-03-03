/**
 * Author: Huy Dang
 * Date: 02/23/2010
 * 
 * The White box testing for BusNumDbAdapter which tests the basic functionality of 
 * the database
 */
package com.busstopalarm.test;

import java.io.IOException;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import com.busstopalarm.*;

public class BusNumDbAdapterTest extends ActivityInstrumentationTestCase2<MainPage>{
	
	private MainPage activity;
	
	public BusNumDbAdapterTest() {
		super("com.busstopalarm", MainPage.class);
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
	 * The initial database text file has 24 entries bus route numbers.
	 * Tests if number of entries in the DB is correct
	 */
	/*
	@SmallTest
	public void testAddBusDbFromFile_1() throws IOException{
		int testFile = 1;
		activity.mBusNumDb.deleteAllBusEntries();
		activity.mBusNumDb.readDbFile(testFile);
		int result = activity.mBusNumDb.getBusNumTbSize();
		assertEquals("Db should contains 24 destinations", 23,result);
	}
	*/

}
