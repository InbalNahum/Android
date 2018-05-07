package com.example.inahum.theshawshankredemption;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    private static final String RECEIVED = "Received";
    private static int numOfNotes = 0;

    public DataBaseHelper(Context contex) {
        super(contex, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY, " + COL_TITLE +
                " TEXT, " + COL_DESCRIPTION + " TEXT, " + COL_STATUS + " TEXT, "
                + COL_ADD_DATE + " INTEGER" + ")";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String str = "DROP IF TABLE EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(str);
        onCreate(sqLiteDatabase);
    }

    public boolean addData(String title, String description) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, numOfNotes++);
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_DESCRIPTION, description);
        contentValues.put(COL_STATUS, SENT);

        Date date = Calendar.getInstance().getTime();
        long currentTime = date.getTime();
        contentValues.put(COL_ADD_DATE, currentTime);
        Log.d(TAG, "add currentTime " + currentTime);
        Log.d(TAG, "addDate: Adding " + date.toString() + " to " + TABLE_NAME);
        Log.d(TAG, "addDate: String " + currentTime + " to " + TABLE_NAME);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData() {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        return cursor;
    }

    public Cursor getItemId(String title) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT " + COL_ID + " FROM " + TABLE_NAME +
                " WHERE " + COL_TITLE + " = '" + title + "'";

        Cursor data = sqLiteDatabase.rawQuery(query, null);
        return data;
    }

    public void updateNote(int id, String newTitle, String oldTitle,
                           String newDescription, String oldDescription) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String queryTitle = "UPDATE " + TABLE_NAME + " SET " + COL_TITLE +
                " = '" + newTitle + "' WHERE " + COL_ID + " = '" + id +
                "' AND " + COL_TITLE + " = '" + oldTitle + "'";

        String queryDecs = "UPDATE " + TABLE_NAME + " SET " + COL_DESCRIPTION +
                " = '" + newDescription + "' WHERE " + COL_ID + " = '" + id +
                "' AND " + COL_DESCRIPTION + " = '" + oldDescription + "'";

        Log.d(TAG, "Update note: " + queryTitle + " " + queryDecs);
        Log.d(TAG, "Set note: " + newTitle + " " + newDescription);

        sqLiteDatabase.execSQL(queryTitle);
        sqLiteDatabase.execSQL(queryDecs);
    }

    public void deleteNote(int id, String title) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID +
                " = '" + id + "' AND " + COL_TITLE + " = '" + title + "'";

        Log.d(TAG, "Delete note: " + query + " " + title);
        sqLiteDatabase.execSQL(query);
    }

    // return true when over two days
    public boolean setStatus(int id) {

        Cursor data = getData();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        while (data.moveToNext()) {
            int currId = data.getInt(0);
            if (id == currId) {
                long addDate = data.getInt(4);
                Date currentTime = Calendar.getInstance().getTime();
                long timeTwo = currentTime.getTime();
                long twoDays = 1000 * 60 * 60 * 24 * 2;
                long diffDate = (timeTwo - addDate) / twoDays;
                Log.d(TAG, "old " + addDate);
                Log.d(TAG, "new " + timeTwo);
                Log.d(TAG, "DiffDay " + diffDate);
                if (diffDate >= 2) {

                    String query = "UPDATE " + TABLE_NAME + " SET " + COL_STATUS +
                            " = '" + RECEIVED + "' WHERE " + COL_ID + " = '" + id + "'";

                    Log.d(TAG, "setStatus: " + query + "with id " + id);
                    sqLiteDatabase.execSQL(query);
                    return true;

                }
            }
        }
        return false;
    }
}
