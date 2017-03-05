package com.it.zzb.niceweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zzb on 2017/2/28.
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecasts;
}
