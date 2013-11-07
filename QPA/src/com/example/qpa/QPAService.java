package com.example.qpa;

import android.app.Service;
import android.util.Log;
import android.content.Intent;
import android.os.IBinder;

public class QPAService extends Service {
	
	@Override
	public void onCreate(){
		Log.i(PublicDef.LogTag, "service onCreate");
	}
	
	@Override
	public void onDestroy(){
		Log.i(PublicDef.LogTag, "service onDestroy");
	}
	
	@Override
	public IBinder onBind(Intent intent){
		Log.i(PublicDef.LogTag, "service onBind");
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.i(PublicDef.LogTag, "service onStartCommand");
		return START_STICKY;
	}
}
