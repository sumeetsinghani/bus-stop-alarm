/**
 * Author: Huy Dang
 * Date: 03/09/2010
 * 
 * The White box testing for BusDbNumAdapter which will be used to validate
 * bus number input
 */
package com.busstopalarm.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.busstopalarm.BusNumDbAdapter;
import com.busstopalarm.MainPage;
import com.busstopalarm.R;

public class BusNumDbAdapterTest extends ActivityInstrumentationTestCase2
										<MainPage>{

	private MainPage activity;
	private static int KingCounty_flag = 0;
	private static int KingCounty_lineNumber = 0;
	private static String[] KingCounty_busArr;
	
	private static int SoundTransit_flag = 1;
	private static int SoundTransit_lineNumber = 0;
	private static String[] SoundTransit_busArr;
	
	
	/**
	 * Specifies the location of the test at MainPage
	 */
	public BusNumDbAdapterTest(){
		super("com.busstopalarm", MainPage.class);
	}
	
	/**
	 * Initializes the set up for the test
	 */
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		activity = getActivity();
		activity.mBusNumDbHelper = new BusNumDbAdapter(activity);
		
		KingCounty_lineNumber = countLine(KingCounty_flag);
		KingCounty_busArr = new String[KingCounty_lineNumber];
		readFile(KingCounty_flag, KingCounty_busArr);
		
		SoundTransit_lineNumber = countLine(SoundTransit_flag);
		SoundTransit_busArr = new String[SoundTransit_lineNumber];
		readFile(SoundTransit_flag, SoundTransit_busArr);
	}
	
	/**
	 * Counting how many bus numbers in each file, using testFlag
	 * 
	 * @param testFlag The flag to indicate which file to read
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private int countLine(int testFlag) throws IOException, 
   												   FileNotFoundException {
   		
   		InputStream in=null;
   		if (testFlag == 0) {
   			in = activity.getBaseContext().getResources().
   				 openRawResource(R.raw.kingcounty);
   		} else if (testFlag == 1) {
   			in = activity.getBaseContext().getResources().
   				 openRawResource(R.raw.soundtransit);
   		} 
   		
  		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
  		if (bin == null) {
  			return 0;
  		}
  		
  		String line;
  		int count = 0;
  		while (true) {
    		line = bin.readLine();
  			if (line == null) {
  				break;
  			}
  			
    		count++;
    	}
  		bin.close();
  		return count;
   	}
	
	/**
	 * Put all data from file (given flag) into string array for testing purpose
	 * 
	 * @param testFlag The flag to indicate which file to read
	 * @param stringArr The array to store all bus numbers.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void readFile(int testFlag, String[] stringArr) 
						throws IOException, FileNotFoundException {
   		
   		InputStream in=null;
   		if (testFlag == KingCounty_flag) {
   			in = activity.getBaseContext().getResources().
   				 openRawResource(R.raw.kingcounty);
   		} else if (testFlag == SoundTransit_flag) {
   			in = activity.getBaseContext().getResources().
   				 openRawResource(R.raw.soundtransit);
   		} 
   		
  		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
  		if (bin == null) {
  			return;
  		}
  		
  		String line;
  		int count = 0;
  		while (true) {
    		line = bin.readLine();
  			if (line == null) {
  				break;
  			}
  			
    		stringArr[count] = line.toString();
    		count++;
    	}
  		bin.close();
  		return;
   	}
	
	/**
	 * Get list of entries given limit value from busnum Db, with SoundTransit 
	 * file
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SmallTest
	public void testGetListBusNum_1() 
									throws FileNotFoundException, IOException {
		int limit = 10;
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		activity.mBusNumDbHelper.readDbFile(SoundTransit_flag);
		Cursor mCursor = activity.mBusNumDbHelper.getListBusNum(limit);
		int result = mCursor.getCount();
		activity.mBusNumDbHelper.close();
		assertEquals("Result should contain"+limit+"bus entries", limit,result);
		
	}
	
	/**
	 * Get list of entries given limit value from busnum Db, with SoundTransit 
	 * file
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SmallTest
	public void testGetListBusNum_2() 
									throws FileNotFoundException, IOException {
		int limit = SoundTransit_busArr.length + 100;
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		activity.mBusNumDbHelper.readDbFile(SoundTransit_flag);
		Cursor mCursor = activity.mBusNumDbHelper.getListBusNum(limit);
		int result = mCursor.getCount();
		activity.mBusNumDbHelper.close();
		assertEquals("Result should contain" + SoundTransit_busArr.length 
					 + "bus entry", SoundTransit_busArr.length ,result);
		
	}
	
	/**
	 * Get list of entries given limit value from busnum Db, with SoundTransit 
	 * file
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SmallTest
	public void testGetListBusNum_3() 
									throws FileNotFoundException, IOException {
		int limit = 0;
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		activity.mBusNumDbHelper.readDbFile(SoundTransit_flag);
		Cursor mCursor = activity.mBusNumDbHelper.getListBusNum(limit);
		int result = mCursor.getCount();
		activity.mBusNumDbHelper.close();
		assertEquals("Result should contain"+limit+"bus entry", limit,result);
		
	}
	
	/**
	 * The database size after adding all entries from kingcounty file should
	 * be 260
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SmallTest
	public void testDBSize_KingCounty() 
								throws FileNotFoundException, IOException {
		
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		activity.mBusNumDbHelper.readDbFile(KingCounty_flag);
		int result = activity.mBusNumDbHelper.getBusNumTbSize();
		activity.mBusNumDbHelper.close();
		assertEquals("Db should contains" + KingCounty_busArr.length+ 
					 " bus numbers", KingCounty_busArr.length,result);
	}
	
	/**
	 * The database size after adding all entries from kingcounty file should
	 * be 24
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SmallTest
	public void testDBSize_SoundTransit() 
								throws FileNotFoundException, IOException {
		
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		activity.mBusNumDbHelper.readDbFile(SoundTransit_flag);
		int result = activity.mBusNumDbHelper.getBusNumTbSize();
		activity.mBusNumDbHelper.close();
		assertEquals("Db should contains " + SoundTransit_busArr.length+ 
					 " bus numbers", SoundTransit_busArr.length,result);
	}
	
	/**
	 * The database size after adding all entries from kingcounty and 
	 * soundtransit files should be 260+24-10. (due to there're some duplicates
	 * between two files)
	 * 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SmallTest
	public void testDBSize_KingCounty_SoundTransit() 
								throws FileNotFoundException, IOException {
		
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		activity.mBusNumDbHelper.readDbFile(KingCounty_flag);
		activity.mBusNumDbHelper.readDbFile(SoundTransit_flag);
		int total = KingCounty_busArr.length + SoundTransit_busArr.length - 10;
		int result = activity.mBusNumDbHelper.getBusNumTbSize();
		activity.mBusNumDbHelper.close();
		assertEquals("Db should contains" + KingCounty_busArr.length+ 
					 " bus numbers", total,result);
	}

	/**
	 * Delete all entries from busnum Db
	 */
	@SmallTest
	public void testDeleteAllBusEntries_0() {
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		int result = activity.mBusNumDbHelper.getBusNumTbSize();
		activity.mBusNumDbHelper.close();
		assertEquals("Db should contains 0 bus entry", 0,result);
		
	}
	
	/**
	 * Delete all entries from busnum Db twice
	 */
	@SmallTest
	public void testDeleteAllBusEntries_1() {
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		int result = activity.mBusNumDbHelper.getBusNumTbSize();
		activity.mBusNumDbHelper.close();
		assertEquals("Db should contains 0 bus entry", 0,result);
		
	}
	
	/**
	 * Check if the bus entry exist with null value input
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SmallTest
	public void testIfBusEntryExists_0() 
								throws FileNotFoundException, IOException {
		int num = (int) Math.random()*KingCounty_busArr.length;
		
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		boolean result = activity.mBusNumDbHelper.
						 checkIfBusEntryExist(null);
		activity.mBusNumDbHelper.close();
		assertFalse("Result should be false for null value of route number" 
				   , result);	
	}
	
	/**
	 * Check if the bus entry exist in KingCounty file
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SmallTest
	public void testIfBusEntryExists_1() 
								throws FileNotFoundException, IOException {
		int num = (int) Math.random()*KingCounty_busArr.length;
		
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		activity.mBusNumDbHelper.readDbFile(KingCounty_flag);
		boolean result = activity.mBusNumDbHelper.
						 checkIfBusEntryExist(KingCounty_busArr[num]);
		activity.mBusNumDbHelper.close();
		assertTrue("Db should contains bus" + KingCounty_busArr[num], result);	
	}
	
	/**
	 * Check if the bus entry exist in Soundtransit file
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SmallTest
	public void testIfBusEntryExists_2() 
								throws FileNotFoundException, IOException {
		int num = (int) Math.random()*SoundTransit_busArr.length;
		
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		activity.mBusNumDbHelper.readDbFile(SoundTransit_flag);
		boolean result = activity.mBusNumDbHelper.
						 checkIfBusEntryExist(SoundTransit_busArr[num]);
		activity.mBusNumDbHelper.close();
		assertTrue("Db should contains bus" + SoundTransit_busArr[num], result);	
	}

	/**
	 * The initial database for bus number entries is zero.
	 */
	@SmallTest
	public void testDBSize_0() {
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		int result = activity.mBusNumDbHelper.getBusNumTbSize();
		activity.mBusNumDbHelper.close();
		assertEquals("Db should contains 4 destinations", 0,result);
	}
	
	/**
	 * Test the database size after adding entries from file.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SmallTest
	public void testDBSize_1() throws FileNotFoundException, IOException {
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		activity.mBusNumDbHelper.readDbFile(SoundTransit_flag);
		int result = activity.mBusNumDbHelper.getBusNumTbSize();
		activity.mBusNumDbHelper.close();
		assertEquals("Db should contains 4 destinations", 
				     SoundTransit_busArr.length,result);
	}
	/**
	 * Fetch all entries from empty busnum Db
	 */
	@SmallTest
	public void testFetchAllBusEntries_0() {
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		Cursor mCursor = activity.mBusNumDbHelper.fetchAllBusEntries();
		int result = mCursor.getCount();
		activity.mBusNumDbHelper.close();
		assertEquals("Db should contains 0 bus entry", 0,result);
		
	}
	
	/**
	 * Fetch all entries from busnum Db, with SoundTransit file
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SmallTest
	public void testFetchAllBusEntries_1() 
									throws FileNotFoundException, IOException {
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		activity.mBusNumDbHelper.readDbFile(SoundTransit_flag);
		Cursor mCursor = activity.mBusNumDbHelper.fetchAllBusEntries();
		int result = mCursor.getCount();
		activity.mBusNumDbHelper.close();
		assertEquals("Db should contains" + SoundTransit_busArr.length 
					 + "bus entry", SoundTransit_busArr.length,result);	
	}
	
	/**
	 * Fetch all entries from busnum Db, with KingCounty file
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@SmallTest
	public void testFetchAllBusEntries_2() 
									throws FileNotFoundException, IOException {
		activity.mBusNumDbHelper.open();
		activity.mBusNumDbHelper.deleteAllBusEntries();
		activity.mBusNumDbHelper.readDbFile(KingCounty_flag);
		Cursor mCursor = activity.mBusNumDbHelper.fetchAllBusEntries();
		int result = mCursor.getCount();
		activity.mBusNumDbHelper.close();
		assertEquals("Db should contains" + KingCounty_busArr.length 
					 + "bus entry", KingCounty_busArr.length,result);
		
	}
}
