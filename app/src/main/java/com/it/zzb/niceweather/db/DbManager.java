package com.it.zzb.niceweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzb on 2017/3/5.
 */

public class DbManager {
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase dbWriter;
    private SQLiteDatabase dbReader;
    private static DbManager instance;

    public DbManager(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        dbReader = dbHelper.getReadableDatabase();
        dbWriter = dbHelper.getWritableDatabase();
    }



    public  static synchronized DbManager getInstance(Context context){
        if (instance == null){
            instance = new DbManager(context);
        }
        return instance;
    }

    //添加数据
    public void addProvince(Province province){
        ContentValues cv = new ContentValues();
        cv.put("province",province.getProvinceName());
        cv.put("provincecode",province.getProvinceCode());
        dbWriter.insert("province",null,cv);
    }
    //添加数据
    public void addCity(City city){
        ContentValues cv = new ContentValues();
        cv.put("city",city.getCityName());
        cv.put("citycode",city.getCityCode());
        cv.put("provinceid",city.getProvinceId());
        dbWriter.insert("city",null,cv);
    }
    //添加数据
    public void addCounty(County county){
        ContentValues cv = new ContentValues();
        cv.put("county",county.getCountyName());
        cv.put("cityid",county.getCityId());
        cv.put("weatherid",county.getWeatherId());
        dbWriter.insert("county",null,cv);
    }


    //查询市数据
    public City searchCity(String id){
        Cursor cursor = dbReader.rawQuery("SELECT * FROM city WHERE _id = ?", new String[]{id + ""});
        cursor.moveToFirst();
        City city = new City();
        city.setId(cursor.getInt(cursor.getColumnIndex("id")));
        city.setCityName(cursor.getString(cursor.getColumnIndex("city")));
        city.setCityCode(cursor.getInt(cursor.getColumnIndex("citycode")));
        city.setProvinceId(cursor.getInt(cursor.getColumnIndex("provinceid")));
        return city;
    }
    //查询县数据
    public County searchCounty(int id){
        Cursor cursor = dbReader.rawQuery("SELECT * FROM county WHERE _id = ?", new String[]{id + ""});
        cursor.moveToFirst();
        County county = new County();
        county.setId(cursor.getInt(cursor.getColumnIndex("id")));
        county.setCountyName(cursor.getString(cursor.getColumnIndex("county")));
        county.setCityId(cursor.getInt(cursor.getColumnIndex("cityid")));
        county.setWeatherId(cursor.getString(cursor.getColumnIndex("weatherid")));
        return county;
    }

    //查询省
    public List<Province> selectProvince() {
        List<Province> provinceList = new ArrayList<Province>();
        SQLiteDatabase db = null;
        // 获取一个光标对象
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query("province", null, null, null, null, null, null);
            Province province = null;
            while (cursor.moveToNext()) {
                province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province")));
                province.setProvinceCode(cursor.getInt(cursor.getColumnIndex("provincecode")));
                provinceList.add(province);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
            db.close();
        }

        return provinceList;
    }
    //查询市
    public List<City> selectCity(int provinceid) {
        List<City> cityList = new ArrayList<City>();
        SQLiteDatabase db = null;
        // 获取一个光标对象
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = dbReader.rawQuery("SELECT * FROM city WHERE provinceid = ?", new String[]{provinceid + ""});
            //cursor = db.query("city", null, null, null, null, null, null);
             City city = null;
            while (cursor.moveToNext()) {
                 city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city")));
                city.setCityCode(cursor.getInt(cursor.getColumnIndex("citycode")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("provinceid")));
                cityList.add(city);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
            db.close();
        }

        return cityList;
    }
    //查询县
    public List<County> selectCounty(int cityid) {
        List<County> countyList = new ArrayList<County>();
        SQLiteDatabase db = null;
        // 获取一个光标对象
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = dbReader.rawQuery("SELECT * FROM county WHERE cityid = ?", new String[]{cityid + ""});
            //cursor = db.query("province", null, null, null, null, null, null);
            County county = null;
            while (cursor.moveToNext()) {
                county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("cityid")));
                county.setWeatherId(cursor.getString(cursor.getColumnIndex("weatherid")));
               countyList.add(county);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
            db.close();
        }

        return countyList;
    }
}
