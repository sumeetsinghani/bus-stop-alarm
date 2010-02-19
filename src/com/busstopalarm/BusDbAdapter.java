package com.busstopalarm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Calendar;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple destination database access helper class. Defines the basic CRUD 
 * operations for retrieving bus information, such as bus route id, bus route
 * description, stop id, stop description, number of time using the destination,
 * the time stamp to keep track the recent use. 
 * The class main purpose is for LocationListPage to view its entries
 *
 * TODO: Add method to initialize the database for major destinations at the 
 * 		 begin. Therefore, this helper class now is also used in MainPage
 *  	 because we initial database at the first page
 */
public class BusDbAdapter {
	
	private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
	
	/*
	 * DESTINATION TABLE INFO
	 */
    private static final String DATABASE_TABLE_DEST = "destination";
    private static final String TAG = "BusDbAdapter";
    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 2;
	
    /*
     * DESTINATION TABLE KEYS
     * _id: the id is generated by system to keep track of entry (Primary key)
     * route_id: the bus route id
     * route_desc: the bus route description
     * stop_id: the bus stop id
     * stop_desc: the bus stop description
     * count: number of time user uses this destination as his/her alarm object
     * time: time stamp to remember the last time user uses this entry. Useful
     * 		 to retrieve the list of recent destinations
     * major: the flag to keep track of which entries are major destination.
     */
    private static final String KEY_DEST_ROWID = "_id";
	private static final String KEY_DEST_ROUTE_ID = "route_id";
	private static final String KEY_DEST_ROUTE_DESC = "route_desc";
	private static final String KEY_DEST_STOP_ID = "stop_id";
	private static final String KEY_DEST_STOP_DESC = "stop_desc";
	private static final String KEY_DEST_COUNT = "count"; 
	private static final String KEY_DEST_TIME = "time"; 
	private static final String KEY_DEST_MAJOR = "major";
	
    /**
     * Database Query Statements
     */
    private static final String DATABASE_CREATE_DEST=
            "CREATE TABLE "
            + " destination (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " route_id TEXT NOT NULL, route_desc TEXT NOT NULL, " 
            + " stop_id TEXT NOT NULL, stop_desc TEXT NOT NULL, " 
            + " count INTEGER NOT NULL, time TEXT NOT NULL, "
            + " major INTEGER NOT NULL);";

    private static final String DATABASE_INSERT_DEST=
    		" INSERT INTO destination" +
    		" (route_id, route_desc, stop_id, stop_desc, count, time, major) " +
    		" VALUES (?,?,?,?,?,?,?);";	
    	
    private static final String DATABASE_UPDATE_DEST_DESC=
			" UPDATE destination " +
			" SET route_desc = ? , stop_desc = ? " +
			" WHERE route_id = ? AND stop_id = ? ;";

    private static final String DATABASE_UPDATE_DEST_TIME_COUNT=
			" UPDATE destination " +
			" SET count = ? , time = ? " +
			" WHERE route_id = ? AND stop_id = ? ;";

    private static final String DATABASE_RETRIEVE_BY_ROUTEID_STOPID=
    		" SELECT * FROM destination " +
    		" WHERE route_id = ? AND stop_id = ? ;";
    
    private static final String DATABASE_RETRIEVE_BY_ROWID=
    		" SELECT * FROM destination " +
    		" WHERE _id = ? ;";
    
    private static final String DATABASE_DELETE_ALL_DEST=
    		" DELETE FROM destination;";
    
    private static final String DATABASE_DELETE_ONE_DEST=
	    	" DELETE FROM destination " +
			" WHERE route_id = ? AND stop_id = ? ;";
    
    private static final String DATABASE_DELETE_ONE_DEST_BY_ROWID=
			" DELETE FROM destination " +
			" WHERE _id = ? ;";
    	
    private static final String DATABASE_FETCH_ALL_DEST=
    		" SELECT * FROM destination ;";
    
    private static final String DATABASE_FETCH_ONE_DEST=
    		" SELECT * FROM destination " +
    		" WHERE _id = ? ;";
    
    private static final String DATABASE_GET_FAVORITE_DEST=
    		" SELECT * FROM destination " +
    		" ORDER BY count DESC" +
    		" LIMIT ?  ;";
    
    private static final String DATABASE_GET_RECENT_DEST=
			" SELECT * FROM destination " +
			" ORDER BY time DESC" +
			" LIMIT ?  ;";
    
    private static final String DATABASE_GET_MAJOR_DEST=
			" SELECT * FROM destination " +
			" WHERE major = 1 " +
			" ORDER BY count DESC" +
			" LIMIT ?  ;";
    
    /**
     * Private class DatabaseHelper is used for create/initialize the table
     * at the begin and help to update to new version
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	db.execSQL(DATABASE_CREATE_DEST);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, 
        					  int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS destination");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public BusDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the destination database. If it cannot be opened, try to create a 
     * new instance of the database. If it cannot be created, throw an exception 
     * to signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public BusDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    /**
     * Close the destination database. If there's no current database running,
     * simply do nothing
     */
    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new destination using the route_id, description, longitude,
     * latitude, count, and time provided. If the destination is
     * successfully created return the new rowId for that destination, 
     * otherwise return a -1 to indicate failure.
     * 
     * @param route_id The Id of the bus route
     * @param stop_id The Id of the bus stop
     * @param destination The description of the destination, e.x: 45th & 15th
     * @param longitude The longitude of the destination
     * @param latitude The latitude of the destination
     * @param count The number of times that this route is used
     * @param time The time of this route being used
     * @return rowId or -1 if failed
     */
    public long createDest(String route_id, String route_desc,
    					   String stop_id, String stop_desc, int major){
    						
    	/*
    	 * Check if the entry is already in the table
    	 */
    	if (checkIfDestExist(route_id, stop_id))
    		return -1;
    	
    	/*
    	 * Get the new time stamp, which is used to retrieve recent used route
    	 */
    	String time = new Timestamp(Calendar.getInstance().getTimeInMillis()).
									toString();
    	
    	/*
    	 * The new destination which is put in Db will have initial "count" of 1
    	 */
    	String[] args = new String[]{route_id, route_desc, stop_id, stop_desc,
    							   "1", time, Integer.toString(major)};
    	
    	Cursor mCursor = mDb.rawQuery(DATABASE_INSERT_DEST, args);
    	
    	/*
    	 * mCursot.getCount() returns the number of row it changes, whic
    	 * is supposed to be 1 in this case.
    	 */
    	return mCursor.getCount();
    	
    }

    /*
     * The commented code should work too (untested). The difference between
     * two type of codes is the test runner are likely hate the transaction
     * set up. It actually messed up the result among tests.
     */
    /**
     * Delete the destination with the given rowId
     * TODO: Haven't tested any function which is relevant to the rowId since
     *       system generates the id itself. Need the LocationListPage to bind
     *       the rowId to its context in order to make a call to Db.
     * 
     * @param rowId id of the destination to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteDest(long rowId) {
    	
    	/*
    	String[] args = new String[]{Long.toString(rowId)};
    	mDb.rawQuery(DATABASE_DELETE_ONE_DEST_BY_ROWID, args);
    	*/
    	int nRows = 0;
        mDb.beginTransaction();
    	try {
	        nRows = mDb.delete(DATABASE_TABLE_DEST, 
	        				   KEY_DEST_ROWID + "=" + rowId, null);
	        mDb.setTransactionSuccessful();
    	} catch (SQLException anyDbError){
    		//error logging
    	}finally {
    		mDb.endTransaction();
    	}
    	return nRows >0;
    }
    
    /*
     * NOTE****: 
     * For unknown reason, I have to use execSQL here. The function doesn't work
     * when I use rawQuery() here.
     */
    /**
     * Delete one destination entry using route_id and stop_id
     * 
     * @param route_id id of the bus route
     * @param stop_id id of the bus stop
     */
    public void deleteDest(String route_id, String stop_id) {
    	String[] args = new String[]{route_id, stop_id};        
    	mDb.execSQL(DATABASE_DELETE_ONE_DEST, args);
    }
    
    
    /**
     * Delete all destination from the destination table
     * 
     * @return true if all destinations deleted, false otherwise
     */
    public boolean deleteAllDestinations() {
        return mDb.delete(DATABASE_TABLE_DEST, null, null) > 0;
    }

    /**
     * Return a Cursor over the list of all destinations in the table
	 * destination of the database
     * 
     * @return Cursor over all destinations
     */
    public Cursor fetchAllDestinations() {
    	return mDb.rawQuery(DATABASE_FETCH_ALL_DEST, new String[]{});
    }
    
    /**
     * Return the number of rows in the current destination table
     * 
     *  @return number of rows in the destination table
     */
    public int getDestTbSize(){
    	Cursor mCursor = mDb.rawQuery(DATABASE_FETCH_ALL_DEST, new String[]{});
    	return mCursor.getCount();
    }
    
    /**
     * Return a Cursor positioned at the destination that matches given rowId
     * 
     * @param rowId id of destination to retrieve
     * @return Cursor positioned to matching destination, if found
     * @throws SQLException if destination could not be found/retrieved
     */
    public Cursor fetchDestination(long rowId) throws SQLException {
    	
    	Cursor mCursor = mDb.rawQuery(DATABASE_FETCH_ONE_DEST, 
    								  new String[]{Long.toString(rowId)});
        
    	/*
    	 * because cursor points to the array of result. Need to move to the 
    	 * first result of the array. In this case, it is supposed to have only
    	 * 1 result.
    	 */
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    /**
     * Update the destination using the details provided. The destination to be 
     * updated is specified using the route_id and stop_id, and it is altered to
     * use the description values passed in
     * 
     * @param route_id Id of the bus route
     * @param stop_id Id of the bus stop
     * @return 1 if the destination is successfully updated. 0 otherwise.
     */
    public int updateDestDesc(String route_id, 
    					String route_desc, String stop_id, String stop_desc){
    
    	String[] args = new String[]{route_desc, stop_desc, route_id, stop_id};
    	Cursor mCursor = mDb.rawQuery(DATABASE_UPDATE_DEST_DESC, args);
    	return mCursor.getCount();
    	
    }
    
    /**
     * Update the time stamp and count value of the given destination entry.
     * This should only be call in Confirmation page when user finally chooses
     * this destination as his/her choice.
     * 
     * @param route_id Id of the bus route
     * @param stop_id Id of the bus stop
     * @return 1 if the destination is successfully updated. 0 otherwise.
     */public int updateDestDesc_TimeCount(String route_id, String stop_id){
    	String time = new Timestamp(Calendar.getInstance().getTimeInMillis()).
								   toString();
    	int count = Integer.parseInt(getDestCount(route_id, stop_id)) + 1;
    	String[] args = new String[]
    	                    { Integer.toString(count), time, route_id, stop_id};
    	Cursor mCursor = mDb.rawQuery(DATABASE_UPDATE_DEST_TIME_COUNT, args);
    	return mCursor.getCount();
    }
    
   /**
    * Checks if a destination is already in the database based on route_id and 
    * stop_id combination match.
    * 
    * @param route_id Id of the bus route
    * @param stop_id Id of the bus stop
    * @return true if the destination is already in the table. false otherwise.
    */
   	public boolean checkIfDestExist(String route_id, String stop_id){
   		Cursor mCursor = null;
   		mCursor = mDb.rawQuery(DATABASE_RETRIEVE_BY_ROUTEID_STOPID, 
   							   new String[]{route_id, stop_id});

   		if (mCursor.getCount() != 0)
               return true;
        return false;
   	}
   	
   	
    //==================================================================
   	/**
   	 * Return rowID for specific (route_id, stop_id)
   	 * 
   	 * @param route_id Id of the bus route
     * @param stop_id Id of the bus stop
     * @return the rowID for the current destination entry
     */
   	public String getDestRowID(String route_id, String stop_id){
   		return getDestColumn(KEY_DEST_ROWID, route_id, stop_id);
   	}
   	
   	/**
   	 * Return bus route description for specific (route_id, stop_id)
   	 * 
   	 * @param route_id Id of the bus route
     * @param stop_id Id of the bus stop
     * @return the description of current destination bus route
     */
   	public String getDestRouteDesc(String route_id, String stop_id){
   		return getDestColumn(KEY_DEST_ROUTE_DESC, route_id, stop_id);
   	}
   	
   	/**
   	 * Return bus stop description for specific (route_id, stop_id)
   	 * 
   	 * @param route_id Id of the bus route
     * @param stop_id Id of the bus stop
     * @return the description of current destination bus stop
     */
   	public String getDestStopDesc(String route_id, String stop_id){
   		return getDestColumn(KEY_DEST_STOP_DESC, route_id, stop_id);
   	}
   	
   	/**
   	 * Return count value for specific (route_id, stop_id)
   	 * 
   	 * @param route_id Id of the bus route
     * @param stop_id Id of the bus stop
     * @return the count value of current destination entry
     */
   	public String getDestCount(String route_id, String stop_id){
   		return getDestColumn(KEY_DEST_COUNT, route_id, stop_id);
   	}
   	
   	/**
   	 * Return time stamp value for specific (route_id, stop_id)
   	 * 
   	 * @param route_id Id of the bus route
     * @param stop_id Id of the bus stop
     * @return the time stamp value of current destination entry
     */
   	public String getDestTime(String route_id, String stop_id){
   		return getDestColumn(KEY_DEST_TIME, route_id, stop_id);
   	}
   	
   	/**
   	 * Return major flag value for specific (route_id, stop_id)
   	 * 
   	 * @param route_id Id of the bus route
     * @param stop_id Id of the bus stop
     * @return the major flag value of current destination entry
     */
   	public String getDestMajorVal(String route_id, String stop_id){
   		return getDestColumn(KEY_DEST_MAJOR, route_id, stop_id);
   	}
   	
   	/**
   	 * Private function to return specific column based on (route_id, stop_id)
   	 * Using those wrapper classes above instead.
   	 */
   	private String getDestColumn(String columnName, 
   								 String route_id, String stop_id){
   		Cursor mCursor = null;
   		mCursor = mDb.rawQuery(DATABASE_RETRIEVE_BY_ROUTEID_STOPID,
   							   new String[]{route_id, stop_id});
   		
   		int column_index = mCursor.getColumnIndex(columnName);
   		
   		if (mCursor.getCount() == 0)
   			return null;
   		
   		mCursor.moveToFirst();
   		return mCursor.getString(column_index); 
   	}
   	
   	
   	//=================================================================
   	/**
   	 * Return the list of favorite destinations based on number of times each
   	 * of them has been used
   	 * 
   	 * @param limit number of favorite destinations to return
     * @return Cursor The cursor points to the list of favorite destinations
     */
   	public Cursor getFavoriteDest(int limit){
   		return getListDest(DATABASE_GET_FAVORITE_DEST, limit);
   	}
   	
   	/**
   	 * Return the list of recent destinations based on number of times each
   	 * of them has been used
   	 * 
   	 * @param limit number of recent destinations to return
     * @return Cursor The cursor points to the list of recent destinations
     */
   	public Cursor getRecentDest(int limit){
        return getListDest(DATABASE_GET_RECENT_DEST, limit);
   	}
   	
   	/**
   	 * Return the list of major destinations based on number of times each
   	 * of them has been used
   	 * 
   	 * @param limit number of major destinations to return
     * @return Cursor The cursor points to the list of major destinations
     */
   	public Cursor getMajorDest(int limit){
   		return getListDest(DATABASE_GET_MAJOR_DEST, limit);
   	}
   	
   	/**
   	 * Private function to return specific type of destination.
   	 * Using those wrapper classes above instead.
   	 */
   	private Cursor getListDest(String query, int limit){
   		Cursor mCursor = null; 
   		mCursor = mDb.rawQuery(query, 
   							   new String[]{Integer.toString(limit)});
   		
   		if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
   	}
   	//==================================================================
   	
   	/**
   	 * Reading the text file in /res/raw/ folder to create the new database.
   	 * This one should be called in the MainPage when the application is first 
   	 * open.
   	 * 
   	 * In order to check if the table exists, use the helper function 
   	 * getDestTbSize(). If the return value is zero, then the table is empty,
   	 * and it's necessary to read file. Otherwise, stop!
   	 * 
   	 *  Reason don't include that check in this helper: for test convenience 
   	 * 
   	 * @param testFlag read main majorDb file if the flag value is 0
   	 * 				   read majorDb_sample file if the the flag value is 1
   	 * 				   read favoriteDb_sample file if the flag value is 2
   	 * @return True if reading the file and put new destinations to 
   	 * 		   database successfully. False if not reading the file at all.
   	 */
   	public boolean readDbFile(int testFlag) throws IOException, FileNotFoundException {
   		/*
   		 * Not sure if I should attach this condition into this function
   		 * or just let coder decide what to do based on helper function 
   		 * isTbExist(). 
   		 * Because whether reading file or not, the DB is not gonna change
   		 * just decrease the performance because each time open the app, this
   		 * function is called.
   		 */
   		
   		InputStream in=null;
   		if (testFlag == 0){
   			in = mCtx.getResources().openRawResource(R.raw.majordb);
   		} else if (testFlag == 1){
   			in = mCtx.getResources().openRawResource(R.raw.majordb_sample);
   		} else if (testFlag == 2){
   			in = mCtx.getResources().openRawResource(R.raw.favoritedb_sample);
   		}
   		
  		BufferedReader bin = new BufferedReader(new InputStreamReader(in) );
  		if (bin == null) return false;
  		String line;
  		String[] result;
  		while(true) {
    		line = bin.readLine();
  			if (line == null) break;
    		result = line.split("\t");
    		/*
    		Log.v("BusDbAdapter 1st", result[0]);
    		Log.v("BusDbAdapter 2nd", result[1]);
    		Log.v("BusDbAdapter 3rd", result[2]);
    		Log.v("BusDbAdapter 4th", result[3]);
    		Log.v("BusDbAdapter 5th", result[4]);
    		*/
    		createDest(result[0], result[1], result[2], 
    				   result[3], Integer.parseInt(result[4]));
    	}
  		bin.close();
  		return true;
   	}


   
}
