package com.agh.wfiis.piase.inz.models.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.agh.wfiis.piase.inz.models.pojo.Dust;

/**
 * Created by piase on 2017-12-20.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_DUST = "dust";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DEV_ID = "dev_id";
    private static final String COLUMN_DATETIME = "datetime";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LON = "lon";
    private static final String COLUMN_ALT = "alt";
    private static final String COLUMN_SPEED = "speed";
    private static final String COLUMN_PM1 = "pm1";
    private static final String COLUMN_PM25 = "pm25";
    private static final String COLUMN_PM10 = "pm10";
    private static final String COLUMN_TEMP = "temp";
    private static final String COLUMN_R_HUM = "r_hum";
    private static final String COLUMN_P_ATM = "p_atm";
    private static final String COLUMN_SAT = "sat";
    private static final String COLUMN_HDOP = "hdop";

    private static final String DATABASE_NAME = "dust.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_DUST + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DEV_ID
            + " integer, " + COLUMN_DATETIME
            + " text, " + COLUMN_LAT
            + " real, " + COLUMN_LON
            + " real, " + COLUMN_ALT
            + " real, " + COLUMN_SPEED
            + " real, " + COLUMN_PM1
            + " real, " + COLUMN_PM25
            + " real, " + COLUMN_PM10
            + " real, " + COLUMN_TEMP
            + " real, " + COLUMN_R_HUM
            + " real, " + COLUMN_P_ATM
            + " real, " + COLUMN_SAT
            + " real, " + COLUMN_HDOP
            + " real);";

    private static final String DATABASE_DROP = "drop table if exists " + TABLE_DUST;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        Log.d("DatabaseHelper: ", "Database creating...");
        Log.d("DatabaseHelper: ", "Table " + TABLE_DUST + " ver." + DATABASE_VERSION + " created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DUST);
        onCreate(db);
    }

    public ContentValues parseToContentValues(Dust dust) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_DEV_ID, dust.getDevId());
        contentValues.put(COLUMN_DATETIME, dust.getDateTime().toString());
        contentValues.put(COLUMN_LAT, dust.getLatitude());
        contentValues.put(COLUMN_LON, dust.getLongitude());
        contentValues.put(COLUMN_ALT, dust.getAltitude());
        contentValues.put(COLUMN_SPEED, dust.getSpeed());
        contentValues.put(COLUMN_PM1, dust.getPm1());
        contentValues.put(COLUMN_PM25, dust.getPm25());
        contentValues.put(COLUMN_PM10, dust.getPm10());
        contentValues.put(COLUMN_TEMP, dust.getTemperature());
        contentValues.put(COLUMN_R_HUM, dust.getrHum());
        contentValues.put(COLUMN_P_ATM, dust.getpAtm());
        contentValues.put(COLUMN_SAT, dust.getSat());
        contentValues.put(COLUMN_HDOP, dust.getHDOP());

        return  contentValues;

    }
}
