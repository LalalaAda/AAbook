package com.atally.thisgood;

import android.content.Context;

import org.litepal.LitePalApplication;

public class MyApplication extends LitePalApplication {
     private static Context sContext;

    @Override
    public void onCreate() {

        super.onCreate();
        sContext=getApplicationContext();
    }

    public static Context getsContext() {
        return sContext;
    }
}
