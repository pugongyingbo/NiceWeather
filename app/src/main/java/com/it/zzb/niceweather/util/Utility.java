package com.it.zzb.niceweather.util;

import android.text.TextUtils;


import com.google.gson.Gson;
import com.it.zzb.niceweather.Myapplication;
import com.it.zzb.niceweather.db.City;
import com.it.zzb.niceweather.db.County;
import com.it.zzb.niceweather.db.DbManager;
import com.it.zzb.niceweather.db.Province;
import com.it.zzb.niceweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by zzb on 2017/2/27.
 */

public class Utility {

public static DbManager dbManager;


    /**
     *    省级
     **/
    public static boolean handleProvinceResponse(String response){

            if(!TextUtils.isEmpty(response)){
               try{

                    JSONArray allProvinces = new JSONArray(response);
                    for (int i=0;i<allProvinces.length();i++){
                        JSONObject provinceObject = allProvinces.getJSONObject(i);
                        Province province = new Province();
                        province.setProvinceName(provinceObject.getString("name"));
                        province.setProvinceCode(provinceObject.getInt("id"));
                        dbManager = new DbManager(Myapplication.getContext());
                        dbManager.addProvince(province);

                        //Litepal用法
                        //province.save();
                        //GreenDao用法
                        //provinceDao.insert(province);

                    }
                   return true;
                }catch (JSONException e){
                   e.printStackTrace();
               }
            }
        return false;
    }
    //市级
    public static boolean handleCityResponse(String response, int provinceId){
       // CityDao cityDao = GreenDaoManager.getInstance().getSession().getCityDao();
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for (int i=0;i<allCities.length();i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city  = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    dbManager = new DbManager(Myapplication.getContext());
                    dbManager.addCity(city);
                    //Litepal用法

                    //city.save();//
                    //GreenDao用法
                   // cityDao.insert(city);

                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    //县级

    public static boolean handleCountyResponse(String response, int cityId){
        //CountyDao countyDao = GreenDaoManager.getInstance().getSession().getCountyDao();
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties = new JSONArray(response);
                for (int i=0;i<allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county =new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);

                    dbManager = new DbManager(Myapplication.getContext());
                    dbManager.addCounty(county);
                    //Litepal用法
                    //county.save();

                    //GreenDao用法
                    //countyDao.insert(county);


                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
