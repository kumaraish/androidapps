package com.aishwarya.osctracker;

import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OscDbHelper {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "ideaosc.db";
	private static final String TABLE_NAME = "activity_records";
	
	public static final String TIMESHEET_COLUMN_ID = "_id";
	public static final String TIMESHEET_COLUMN_DATE = "date";
	public static final String TIMESHEET_COLUMN_NOTES = "notes";
	
	private OscDbOpenHelper openHelper;
	private SQLiteDatabase database;
	
	OscDbHelper(Context context) {
		openHelper = new OscDbOpenHelper(context);
		try {
			database = openHelper.getWritableDatabase();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void closedb() {
		database.close();
	}
	
	public void saveOscActivityRecord(String date, String notes) {
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(TIMESHEET_COLUMN_DATE, date);
			contentValues.put(TIMESHEET_COLUMN_NOTES, notes);
			database.insert(TABLE_NAME, null, contentValues);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public Cursor getAllTimeRecords() {
		return database.rawQuery("select * from " + TABLE_NAME, null);
	}
	
	public void deleteOscActivityRecord(List<Long> ids) {
		Long id;
		String whereClause = TIMESHEET_COLUMN_ID + " in (" ;
		
		Iterator<Long> myListIterator = ids.iterator(); 
		while (true) {
		    id = myListIterator.next();    
		    whereClause += id;
		    if (myListIterator.hasNext()) {
		    	whereClause += ",";
		    } else {
		    	break;
		    }
		}
		
		whereClause += ")";
		
		String whereArgs[] = null;
		database.delete(TABLE_NAME, whereClause, whereArgs);
	}
	
	public void updateOscActivityRecord(long id, String date, String notes) {
		String whereClause = TIMESHEET_COLUMN_ID + "=" + id;
		String whereArgs[] = null;
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put(TIMESHEET_COLUMN_DATE, date);
			contentValues.put(TIMESHEET_COLUMN_NOTES, notes);
			database.update(TABLE_NAME, contentValues, whereClause, whereArgs);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public class OscDbOpenHelper extends SQLiteOpenHelper {
		OscDbOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {			
			db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
					+ TIMESHEET_COLUMN_ID + " INTEGER PRIMARY KEY, "
					+ TIMESHEET_COLUMN_DATE + " TEXT, "
					+ TIMESHEET_COLUMN_NOTES + " TEXT )"
					);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);			
		}
	}
}

