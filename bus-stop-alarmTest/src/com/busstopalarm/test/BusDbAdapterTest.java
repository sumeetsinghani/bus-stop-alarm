package com.busstopalarm.test;

//import static org.junit.Assert.*;
import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

/*
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
*/
import com.busstopalarm.LocationListPage;

public class BusDbAdapterTest extends ActivityInstrumentationTestCase2<LocationListPage>{

	private LocationListPage activity;
	
	public BusDbAdapterTest(){
		super("com.busstopalarm", LocationListPage.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		activity = getActivity();
	}
	
	@SmallTest
	public void testCreateDest() {
		
		long result = activity.mBusDbHelper.createDest("70", "45th&brooklyn", "12.2", "10.2", "1", "5");
		
		
		//make sure the print out says it was successfully put in
		assertTrue("Should have been added the entry from the database", -1< result);
		
		//make sure it's in the database
		//assertTrue("the destination shoule be in the database now", activity.mFavDbHelper.checkIfFav(ROUTE_ID_1, STOP_ID_1));
		//ail("Not yet implemented");
	}

	@SmallTest
	public void testCreateMajorDest() {
		fail("Not yet implemented");
	}

	@SmallTest
	public void testDeleteDest() {
		fail("Not yet implemented");
	}

	@SmallTest
	public void testDeleteMajorDest() {
		fail("Not yet implemented");
	}

	@SmallTest
	public void testFetchAllDestinations() {
		fail("Not yet implemented");
	}

	@SmallTest
	public void testFetchAllMajorDestinations() {
		fail("Not yet implemented");
	}

	@SmallTest
	public void testFetchDestination() {
		fail("Not yet implemented");
	}

	@SmallTest
	public void testFetchMajorDestination() {
		fail("Not yet implemented");
	}

	@SmallTest
	public void testUpdateDestDesc() {
		fail("Not yet implemented");
	}

}
