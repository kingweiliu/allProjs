package com.ljw.quitphoneaddiction;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Binder;
import android.util.Log;
import android.content.Intent;
import java.util.Calendar;

public class QPAService extends Service{
	
	public class MyBinder extends Binder {  
    	QPAService getService() {  
            return QPAService.this;  
        }  
    }
	
	private ScreenEventReceiver screenReceiver;	
	private final IBinder binder = new MyBinder();  
	

	private MediaPlayer player = null;
	
		
	public void onDestroy(){		
		Log.i(Common.LogTag, "Service  onDestroy");
		if(screenReceiver != null)
			unregisterReceiver(screenReceiver);
		screenReceiver = null;
		
		player.stop();
		player = null;
	}
	
	@Override
	public int onStartCommand(android.content.Intent intent, int flags, int startId){		
		 try{			 			 
			 Log.i(Common.LogTag, "Service  onStartCommand in");
		 }
		 catch(Exception e){
			 Log.e("ljw.qpa", e.toString());			 
		 }		 
		 return START_STICKY;		 
	 }
	 
	@Override
	public void onCreate(){
		super.onCreate();		 
		player = MediaPlayer.create(this, R.raw.deep);
		player.setLooping(true);
		player.start();		
		setupReceiver();				 
		Log.i(Common.LogTag, "Service  onCreate");	
	 }
	 


	public IBinder onBind(android.content.Intent arg0){
		 return binder;
	 }	 
	
	private void setupReceiver(){
		 //install screen event handler.
		 screenReceiver = new ScreenEventReceiver();
		 IntentFilter  intentFilter = new IntentFilter();
		 intentFilter.addAction("android.intent.action.SCREEN_OFF");
		 intentFilter.addAction("android.intent.action.SCREEN_ON");
		 registerReceiver(screenReceiver, intentFilter);
		 screenReceiver.myService = this;		
		 
	 }		
}
