package com.vinsol.spree;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;

/**
 * Created by vaibhav on 10/20/15.
 */
public class SpreeApplication extends Application{
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        FacebookSdk.sdkInitialize(applicationContext);
    }


    public static Context getContext() {
        return applicationContext;
    }
}


