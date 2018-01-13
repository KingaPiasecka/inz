package com.agh.wfiis.piase.inz.models.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.agh.wfiis.piase.inz.models.pojo.Dust;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by piase on 2017-12-25.
 */

public class DatabaseAdapter {

    private SQLiteDatabase sqLiteDatabase;
    private DatabaseHelper databaseHelper;

    public DatabaseAdapter(Context context) {
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    public void open() {
        try {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
            Log.i("DatabaseAdapter.class:", "open");
        } catch (SQLiteException e) {
            Log.e("DatabaseAdapter.class:", e.getMessage());
        }
    }

    public void close() {
        Log.i("DatabaseAdapter.class:", "closing");
        databaseHelper.close();
    }

    public void insert(List<Dust> dustList) {
        Log.d("DatabaseAdapter.class:", "insert:" + dustList.size());
        for (Dust dust : dustList) {
            if (!sqLiteDatabase.isOpen()) {
                sqLiteDatabase = databaseHelper.getWritableDatabase();
            }

            try {
                ContentValues contentValues = databaseHelper.parseToContentValues(dust);
                long insertId = sqLiteDatabase.insert(DatabaseHelper.TABLE_DUST, null, contentValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Dust> getAllElements() {

        ArrayList<Dust> list = new ArrayList<Dust>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_DUST;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Dust obj = new Dust();
                        obj.setId(cursor.getString(0));
                        obj.setDevId(cursor.getString(1));
                        obj.setDateTime(new Date(cursor.getString(2)));
                        obj.setLatitude(cursor.getDouble(3));
                        obj.setLongitude(cursor.getDouble(4));
                        obj.setAltitude(cursor.getDouble(5));
                        obj.setSpeed(cursor.getDouble(6));
                        obj.setPm1(cursor.getDouble(7));
                        obj.setPm25(cursor.getDouble(8));
                        obj.setPm10(cursor.getDouble(9));
                        obj.setTemperature(cursor.getDouble(10));
                        obj.setrHum(cursor.getDouble(11));
                        obj.setpAtm(cursor.getDouble(12));

                        list.add(obj);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        Log.i("DatabaseAdapter.class:", "size:" + list.size());
        return list;
    }

    public void clearDatabase() {
        Log.i("DatabaseAdapter.class: ", "deleting data");
        databaseHelper.getWritableDatabase().execSQL("delete from " + DatabaseHelper.TABLE_DUST);
    }

}
