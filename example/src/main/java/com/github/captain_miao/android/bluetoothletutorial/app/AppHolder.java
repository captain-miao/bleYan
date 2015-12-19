package com.github.captain_miao.android.bluetoothletutorial.app;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.github.captain_miao.android.ble.utils.BleLog;

public class AppHolder extends Application {
    private static final String TAG = "AppHolder";

    private static AppHolder _instance;


    public static AppHolder getInstance() {
   		return _instance;
   	}

    public static Context getContext() {
        return _instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        BleLog.setPrintLog(true);
        AppLog.setPrintLog(true);

		_instance = this;
	}


}

