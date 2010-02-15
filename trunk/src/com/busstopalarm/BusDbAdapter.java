package com.busstopalarm;

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
    public static final String KEY_DEST_ROUTE = "route_id";
    public static final String KEY_DEST_DESC = "description";
    public static final String KEY_DEST_LONG = "longtitude";
    public static final String KEY_DEST_LAT = "latitude";
    public static final String KEY_DEST_COUNT = "count";  //if the bus route is used, increase count. Fav return the top 20 routes
    public static final String KEY_DEST_TIME = "time"; //get current time from phone, used to get recent routes.
    public static final String KEY_DEST_ROWID = "_id";

    /*
     * MAJOR DESTINATION TABLE
     */
    public static final String KEY_MAJOR_DEST_ROUTE = "route_id";
    public static final String KEY_MAJOR_DEST_DESC = "description";
    public static final String KEY_MAJOR_DEST_LONG = "longitude";
    public static final String KEY_MAJOR_DEST_LAT = "latitude";
    public static final String KEY_MAJOR_DEST_ROWID = "_id";
    
    private static final String TAG = "BusDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE_DEST=
            "create table destination (_id integer primary key autoincrement, "
                    + "route_id text not null, description text not null, "
                    + "longtitude text not null, latitude text not null, "
                    + "count text not null, time text not null);";
    
    private static final String DATABASE_CREATE_MAJOR_DEST =
        	"create table major_destination (_id integer primary key autoincrement, "
                + "route text not null, description text not null, "
                + "longtitude text not null, latitude text not null);";

    private static final String DATABASE_TABLE_DEST = "destination";
    private static final String DATABASE_TABLE_MAJOR_DEST = "major_destination";
    
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_DEST);
            db.execSQL(DATABASE_CREATE_MAJOR_DEST);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS destination");
            db.execSQL("DROP TABLE IF EXISTS major_destination");
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
     * @param destination The description of the destination, e.x: 45th & 15th
     * @param longitude The longitude of the destination
     * @param latitude The latitude of the destination
     * @param count The number of times that this route is used
     * @param time The time of this route being used
     * @return rowId or -1 if failed
     */
    public long createDest(String route_id, String destination, String longitude, String latitude,
    					   String count, String time) {
    	
    	//TODO: any time select the destination from favorite list, increate the count by 1
    	//TODO: any time new destination is added, check if it exists in table, If yes, increments count by 1
    	
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DEST_ROUTE, route_id);
        initialValues.put(KEY_DEST_DESC, destination);
        initialValues.put(KEY_DEST_LONG, longitude);
        initialValues.put(KEY_DEST_LAT, latitude);
        initialValues.put(KEY_DEST_COUNT, count);
        initialValues.put(KEY_DEST_TIME, time);
        
        return mDb.insert(DATABASE_TABLE_DEST, null, initialValues);
    }

    /**
     * Create a new major destination using the route_id, description, longitude,
     * latitude provided. If the destination is successfully created return the 
     * new rowId for that destination, otherwise return a -1 to indicate failure.
     * 
     * @param route_id The Id of the bus route
     * @param destination The description of the destination, e.x: 45th & 15th
     * @param longitude The longitude of the destination
     * @param latitude The latitude of the destination
     * @return rowId or -1 if failed
     */
    public long createMajorDest(String route_id, String destination, String longitude, String latitude) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MAJOR_DEST_ROUTE, route_id);
        initialValues.put(KEY_MAJOR_DEST_DESC, destination);
        initialValues.put(KEY_MAJOR_DEST_LONG, longitude);
        initialValues.put(KEY_MAJOR_DEST_LAT, latitude);
                
        return mDb.insert(DATABASE_TABLE_MAJOR_DEST, null, initialValues);
    }

    /**
     * Delete the destination with the given rowId
     * 
     * @param rowId id of the destination to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteDest(long rowId) {

        return mDb.delete(DATABASE_TABLE_DEST, KEY_DEST_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Delete the major destination with the given rowId
     * 
     * @param rowId id of the major destination to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteMajorDest(long rowId) {

        return mDb.delete(DATABASE_TABLE_MAJOR_DEST, KEY_MAJOR_DEST_ROWID + "=" + rowId, null) > 0;
    }
    
    /**
     * Return a Cursor over the list of all destinations in the table destination of the database
     * 
     * @return Cursor over all destinations
     */
    public Cursor fetchAllDestinations() {

        return mDb.query(DATABASE_TABLE_DEST, new String[] {
        		KEY_DEST_ROUTE, KEY_DEST_DESC, 
        		KEY_DEST_LONG, KEY_DEST_LAT,
        		KEY_DEST_COUNT, KEY_DEST_TIME}, null, null, null, null, null);
    }
    
    /**
     * Return a Cursor over the list of all destinations in the table destination of the database
     * 
     * @return Cursor over all destinations
     */
    public Cursor fetchAllMajorDestinations() {

        return mDb.query(DATABASE_TABLE_MAJOR_DEST, new String[] {
        		KEY_DEST_ROUTE, KEY_DEST_DESC, 
        		KEY_DEST_LONG, KEY_DEST_LAT}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the destination that matches the given rowId
     * 
     * @param rowId id of destination to retrieve
     * @return Cursor positioned to matching destination, if found
     * @throws SQLException if destination could not be found/retrieved
     */
    public Cursor fetchDestination(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_DEST, new String[] {
                		KEY_DEST_ROUTE, KEY_DEST_DESC, 
                		KEY_DEST_LONG, KEY_DEST_LAT,
                		KEY_DEST_COUNT, KEY_DEST_TIME}, KEY_DEST_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    /**
     * Return a Cursor positioned at the major destination that matches the given rowId
     * 
     * @param rowId id of major destination to retrieve
     * @return Cursor positioned to matching major destination, if found
     * @throws SQLException if major destination could not be found/retrieved
     */
    public Cursor fetchMajorDestination(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_MAJOR_DEST, new String[] {
                		KEY_DEST_ROUTE, KEY_DEST_DESC, 
                		KEY_DEST_LONG, KEY_DEST_LAT}, KEY_MAJOR_DEST_ROWID + "=" + rowId, null,
                        null, null, null, null);
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
    public boolean updateDestDesc(long rowId, String description) {
        ContentValues args = new ContentValues();
        args.put(KEY_DEST_DESC, description);

        return mDb.update(DATABASE_TABLE_DEST, args, KEY_DEST_ROWID + "=" + rowId, null) > 0;
    }
}
