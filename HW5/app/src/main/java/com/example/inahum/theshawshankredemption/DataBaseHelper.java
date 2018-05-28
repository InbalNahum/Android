package com.example.inahum.theshawshankredemption;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.ParseException;
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
    private static final String RECEIVED = "Received";

    public DataBaseHelper(Context contex) {
        super(contex, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TITLE +
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

    public boolean addData(String title, String description) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
      /*  contentValues.put(COL_ID, numOfNotes++);*/
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_DESCRIPTION, description);
        contentValues.put(COL_STATUS, SENT);

        /*Date date = Calendar.getInstance().getTime();*/
        /*long currentTime = date.getTime()/1000;*/
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = sdf.format(c.getTime());
        contentValues.put(COL_ADD_DATE, currentTime);
        Log.d(TAG, "addDate: Adding " + currentTime + " to " + TABLE_NAME);
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
        String query = "SELECT " + "*" + " FROM " + TABLE_NAME +
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

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Cursor c = getData();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        while (c.moveToNext()) {
            int currId = c.getInt(0);
            if (id == currId) {
                Date oldDate  = new Date();
                String addDate = c.getString(4);
                try {
                    oldDate = dateFormat.parse(addDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date currentTime = Calendar.getInstance().getTime();
                int diffDate=((int)(currentTime.getTime())-(int)(oldDate.getTime()))/(24*60*60*1000);
                Log.d(TAG, "new " + (int)(currentTime.getTime()));
                Log.d(TAG, "old " + (int)(oldDate.getTime()));
                Log.d(TAG, "DiffDay " + diffDate);
                if (diffDate >= 2) {

                    String query = "UPDATE " + TABLE_NAME + " SET " + COL_STATUS +
                            " = '" + RECEIVED + "' WHERE " + COL_ID + " = '" + id + "'";

                    Log.d(TAG, "setStatus: " + query + "with id " + id);
                    sqLiteDatabase.execSQL(query);
                    c.close();
                    return true;

                }
            }
        }
        return false;
    }

   public void setStatusByLocation(){

       SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

       String query = "UPDATE " + TABLE_NAME + " SET " + COL_STATUS +
               " = '" + RECEIVED;

       Log.d(TAG, "setStatus received for all table");
       sqLiteDatabase.execSQL(query);
   }
}
