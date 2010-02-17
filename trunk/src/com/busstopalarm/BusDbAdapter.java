package com.busstopalarm;


import java.sql.Timestamp;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the bus route, favorite/major location list example, and gives the 
 * ability to list all these table retrieve or modify a specific note.
 *
 */
public class BusDbAdapter {
	/*
	 * DESTINATION TABLE
	 */
	public static final String KEY_DEST_ROWID = "_id";
    public static final String KEY_DEST_ROUTE_ID = "route_id";
    public static final String KEY_DEST_ROUTE_DESC = "route_desc";
    public static final String KEY_DEST_STOP_ID = "stop_id";
    public static final String KEY_DEST_STOP_DESC = "stop_desc";
    public static final String KEY_DEST_COUNT = "count";  //if the bus route is used, increase count. Fav return the top 20 routes
    public static final String KEY_DEST_TIME = "time"; //get current time from phone, used to get recent routes.
    public static final String KEY_DEST_MAJOR = "major"; //flag: 1 indicates it's major. no otherwise
   
    
    private static final String TAG = "BusDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * Database creation sqlite statement
     */
    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE_DEST=
            "CREATE TABLE destination (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
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
    
    
    
    private static final String DATABASE_TABLE_DEST = "destination";
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	db.execSQL(DATABASE_CREATE_DEST);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
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
    						
    	
    	if (checkIfDestExist(route_id, stop_id))
    		return -1;
    	
    	String time = new Timestamp(Calendar.getInstance().getTimeInMillis()).
									toString();
    	String[] args = new String[]{route_id, route_desc, stop_id, stop_desc,
    							   "1", time, Integer.toString(major)};
    	
    	Cursor mCursor = mDb.rawQuery(DATABASE_INSERT_DEST, args);
    	return mCursor.getCount();
    	
    }

    /**
     * Delete the destination with the given rowId
     * 
     * @param rowId id of the destination to delete
     * @return true if deleted, false otherwise
     */
    //TODO: haven't test this one yet because everytime, machine generates
    //its own integer id for creating row. So it will be possible for use/test
    //when binding with id of row in the page. But the same syntax query for 
    //deleteDest(stop_id, route_id) works properly(tested), therefore this function
    //should work too
    public boolean deleteDest(long rowId) {
    	
    	//String[] args = new String[]{Long.toString(rowId)};
    	//mDb.rawQuery(DATABASE_DELETE_ONE_DEST_BY_ROWID, args);
    	int nRows = 0;
        mDb.beginTransaction();
    	try {
	        nRows = mDb.delete(DATABASE_TABLE_DEST, KEY_DEST_ROWID + "=" + rowId, null);
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
     * For unknow reason, I have to use execSQL here while it's supposed
     * that I can use either it or rawQuery() (!?!?!)
     */
    public void deleteDest(String route_id, String stop_id) {
    	String[] args = new String[]{route_id, stop_id};        
    	mDb.execSQL(DATABASE_DELETE_ONE_DEST, args);
    }
    
    
    /**
     * Delete all destination from the destination table
     * @return true if deleted, false otherwise
     */
    public boolean deleteAllDestinations() {
        return mDb.delete(DATABASE_TABLE_DEST, null, null) > 0;
    }
    
    

    /**
     * Return a Cursor over the list of all destinations in the table destination of the database
     * 
     * @return Cursor over all destinations
     */
    public Cursor fetchAllDestinations() {
    	return mDb.rawQuery(DATABASE_FETCH_ALL_DEST, new String[]{});
    }
    
    public int getDestTbSize(){
    	Cursor mCursor = mDb.rawQuery(DATABASE_FETCH_ALL_DEST, new String[]{});
    	return mCursor.getCount();
    }
    
    /**
     * Return a Cursor positioned at the destination that matches the given rowId
     * 
     * @param rowId id of destination to retrieve
     * @return Cursor positioned to matching destination, if found
     * @throws SQLException if destination could not be found/retrieved
     */
    public Cursor fetchDestination(long rowId) throws SQLException {
    	
    	Cursor mCursor = mDb.rawQuery(DATABASE_FETCH_ONE_DEST, 
    								  new String[]{Long.toString(rowId)});
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    /**
     * Update the destination using the details provided. The destination to be updated is
     * specified using the rowId, and it is altered to use the description values passed in
     * 
     * @param rowId id of note to update
     * @param description value to set destination description to
     * @return true if the destination was successfully updated, false otherwise
     */
    public int updateDestDesc(String route_id, 
    					String route_desc, String stop_id, String stop_desc){
    
    	String[] args = new String[]{route_desc, stop_desc, route_id, stop_id};
    	Cursor mCursor = mDb.rawQuery(DATABASE_UPDATE_DEST_DESC, args);
    	return mCursor.getCount();
    	
    }
    
    /*
     * Update time and count
     */
    public int updateDestDesc_TimeCount(String route_id, String stop_id){
    	String time = new Timestamp(Calendar.getInstance().getTimeInMillis()).
								   toString();
    	int count = Integer.parseInt(getDestCount(route_id, stop_id)) + 1;
    	String[] args = new String[]{ Integer.toString(count), time, route_id, stop_id};
    	Cursor mCursor = mDb.rawQuery(DATABASE_UPDATE_DEST_TIME_COUNT, args);
    	return mCursor.getCount();
    }
    
   /**
    * Checks if a favorite is already in the database based off routeId and stopId combination match.
    * @param routeId the id of the route of the potential favorite
    * @param stopId the id of the stop of the potential favorite
    * @return boolean value, True == the combination already represents a favorite in the database;  False == the combination doesn't match any combination in the database
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
   	/*
   	 * get row_id for specific (route_id, stop_id)
   	 */
   	public String getDestRowID(String route_id, String stop_id){
   		return getDestColumn(KEY_DEST_ROWID, route_id, stop_id);
   	}
   	
   	/*
   	 * get route description for specific (route_id, stop_id)
   	 */
   	public String getDestRouteDesc(String route_id, String stop_id){
   		return getDestColumn(KEY_DEST_ROUTE_DESC, route_id, stop_id);
   	}
   	
   	/*
   	 * get stop description for specific (route_id, stop_id)
   	 */
   	public String getDestStopDesc(String route_id, String stop_id){
   		return getDestColumn(KEY_DEST_STOP_DESC, route_id, stop_id);
   	}
   	
   	/*
   	 * For increase the count value when do updating the destination
   	 */
   	public String getDestCount(String route_id, String stop_id){
   		return getDestColumn(KEY_DEST_COUNT, route_id, stop_id);
   	}
   	
   	/*
   	 * For increase the count value when do updating the destination
   	 */
   	public String getDestTime(String route_id, String stop_id){
   		return getDestColumn(KEY_DEST_TIME, route_id, stop_id);
   	}
   	
   	/*
   	 * get stop description for specific (route_id, stop_id)
   	 */
   	public String getDestMajorVal(String route_id, String stop_id){
   		return getDestColumn(KEY_DEST_MAJOR, route_id, stop_id);
   	}
   	
   	/*
   	 * Private function to return specific column based on (route_id, stop_id)
   	 * Using wrapper class instead.
   	 */
   	private String getDestColumn(String columnName, String route_id, String stop_id){
   		Cursor mCursor = null;
   		mCursor = mDb.rawQuery(DATABASE_RETRIEVE_BY_ROUTEID_STOPID,//DATABASE_GET_COUNT, 
   							   new String[]{route_id, stop_id});
   		
   		int column_index = mCursor.getColumnIndex(columnName);
   		
   		if (mCursor.getCount() == 0)
   			return null;
   		
   		mCursor.moveToFirst();
   		return mCursor.getString(column_index); 
   	}
   	
   	
   	//=================================================================
   	/*
   	 * Get the list of favorite destination with limit
   	 */
   	public Cursor getFavoriteDest(int limit){
   		return getListDest(DATABASE_GET_FAVORITE_DEST, limit);
   	}
   	
   	/*
   	 * Get the list of Recent destination with limit
   	 */
   	public Cursor getRecentDest(int limit){
        return getListDest(DATABASE_GET_RECENT_DEST, limit);
   	}
   	
   	/*
   	 * Get the list of Major destination with limit
   	 */
   	public Cursor getMajorDest(int limit){
   		return getListDest(DATABASE_GET_MAJOR_DEST, limit);
   	}
   	
   	/*
   	 * This one is supposed to be private. use the wrapper class instead.
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
   	
   	

}
