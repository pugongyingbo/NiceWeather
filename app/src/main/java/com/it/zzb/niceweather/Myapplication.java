package com.it.zzb.niceweather;

import android.app.Application;
import android.content.Context;

/**
 * Created by zzb on 2017/3/5.
 */

public class Myapplication extends Application {

    private  static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
