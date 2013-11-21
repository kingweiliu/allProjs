package com.ljw.quitphoneaddiction;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class QpaApplication extends Application {
	private static Context context;

    public void onCreate(){
        super.onCreate();
        Log.i(Common.LogTag, "QpaApplication onCreate");
        QpaApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return QpaApplication.context;
    }
}
