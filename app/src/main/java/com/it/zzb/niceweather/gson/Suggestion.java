package com.it.zzb.niceweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zzb on 2017/2/28.
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carwash;

    public Sport sport;
    public class Comfort{
        @SerializedName("txt")
        public String info;

    }
    public class CarWash{
        @SerializedName("txt")
        public String info;
    }
    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
