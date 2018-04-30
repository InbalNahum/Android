package com.example.inahum.theshawshankredemption;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by inahum on 4/25/2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "Kotel";
    private static final String COL_ID = "ID";
    private static final String COL_TITLE = "Title";
    private static final String COL_DESCRIPTION = "Description";
    private static final String COL_STATUS = "Status";
    private static final String COL_ADD_DATE = "Add_Date";
    private static final String TAG = "DatabaseHelper";
    private static final String SENT = "Sent";
    private static final String ACCEPT = "Accepted";
    private static int numOfNotes = 0;

    public DataBaseHelper(Context contex){
        super(contex, TABLE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
     /*String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                           COL1 + " TEXT)";
        sqLiteDatabase.execSQL(createTable);*/

        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY, " + COL_TITLE +
                " TEXT, " + COL_DESCRIPTION + " TEXT, " + COL_STATUS + " TEXT, "
                + COL_ADD_DATE + " TEXT" + ")";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String str = "DROP IF TABLE EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(str);
        onCreate(sqLiteDatabase);
    }

    public boolean addData(String title, String description){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, numOfNotes++);
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_DESCRIPTION, description);
        contentValues.put(COL_STATUS, SENT);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = format.format(date);
        contentValues.put(COL_ADD_DATE, dateStr);

        Log.d(TAG, "addData: Adding " + description + " to "+ TABLE_NAME);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getData(){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        return cursor;
    }

}
