package com.it.zzb.niceweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zzb on 2017/3/4.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_PROVINCE ="create table province ("
             + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
             + "province TEXT NOT NULL, "
             + "provincecode integer NOT NULL)";

    public static final String CREATE_CITY ="create table city ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "city TEXT NOT NULL, "
            + "citycode integer NOT NULL, "
            + "provinceid integer NOT NULL)";
    public static final String CREATE_COUNTY ="create table county ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "county TEXT NOT NULL, "
            + "cityid integer NOT NULL, "
            + "weatherid TEXT NOT NULL)";


    public DatabaseHelper(Context context) {
        super(context,"weather",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
