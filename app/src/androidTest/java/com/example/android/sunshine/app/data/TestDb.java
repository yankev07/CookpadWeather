package com.example.android.sunshine.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {


    void deleteTheDatabase() {
        mContext.deleteDatabase(WeatherDatabase.DATABASE_NAME);
    }


    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(WeatherContract.LocationEntry.TABLE_NAME);
        tableNameHashSet.add(WeatherContract.WeatherEntry.TABLE_NAME);

        mContext.deleteDatabase(WeatherDatabase.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDatabase(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        }
        while( c.moveToNext() );


        assertTrue("Error: Your database was created without both the location entry and weather entry tables", tableNameHashSet.isEmpty());
        c = db.rawQuery("PRAGMA table_info(" + WeatherContract.LocationEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());


        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(WeatherContract.LocationEntry._ID);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        }
        while(c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns", locationColumnHashSet.isEmpty());
        db.close();
    }


    public void testLocationTable() {
        insertLocation();
    }



    public void testWeatherTable() {

        long locationRowId = insertLocation();
        WeatherDatabase dbHelper = new WeatherDatabase(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);
        long weatherRowId = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue(weatherRowId != -1);

        Cursor cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();

        TestUtilities.validateCurrentRecord("Error: Weather query validation failed", cursor, weatherValues);

        cursor.close();
        db.close();
    }




    public long insertLocation() {

        WeatherDatabase dbHelper = new WeatherDatabase(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();

        long locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, testValues);
        assertTrue(locationRowId != -1);

        Cursor cursor = db.query(WeatherContract.LocationEntry.TABLE_NAME,null,null,null,null,null,null);

        assertTrue("Error: no records returned from location query", cursor.moveToFirst());
        TestUtilities.validateCurrentRecord("Error: Location query validation failed", cursor, testValues);
        cursor.close();
        db.close();

        return locationRowId;
    }
}
