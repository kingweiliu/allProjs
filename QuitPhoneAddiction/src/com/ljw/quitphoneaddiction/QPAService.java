package com.ljw.quitphoneaddiction;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Binder;
import android.os.SystemClock;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.util.Log;
import java.util.ArrayList;
import android.content.Intent;

import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.GregorianCalendar;
import java.util.Date;
import android.os.SystemClock;






public class QPAService extends Service{
	
	public class MyBinder extends Binder {  
    	QPAService getService() {  
            return QPAService.this;  
        }  
    }  	
	public class SleepSetting{
		public SleepSetting(int h, int m, boolean e){
			Hour = h;
			Minute = m;
			Enabled = e;
		}		
		
		public void SetSleepTime(int h, int m){
			Hour = h;
			Minute = m;
		}
		
		public int Hour;
		public int Minute;		
		public boolean Enabled;
	}

	private ScreenEventReceiver screenReceiver;	
	private final IBinder binder = new MyBinder();  
	public SleepSetting sleepSetting = null;
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
			
		 try{
			 //install screen event handler.
			 screenReceiver = new ScreenEventReceiver();
			 IntentFilter  intentFilter = new IntentFilter();
			 intentFilter.addAction("android.intent.action.SCREEN_OFF");
			 intentFilter.addAction("android.intent.action.SCREEN_ON");
			 registerReceiver(screenReceiver, intentFilter);
			 screenReceiver.myService = this;		 
			 LoadData();
			 // install sleep timer
			 if(this.sleepSetting.Enabled){
			 	SetSleepAlarm();
			 }
			 
			 Log.i(Common.LogTag, "Service  onCreate");			 
			 
		 }
		 catch(Exception e){
			 Log.e(Common.LogTag, e.getMessage());
		 }
	 }
	 
	 public IBinder onBind(android.content.Intent arg0){
		 return binder;
	 }	 
	
	 private void LoadData(){
		 SharedPreferences setting = this.getSharedPreferences("SP", MODE_PRIVATE);
		 int nLong =0, nHour=0, nMinute=0;
		 nLong = setting.getInt("durationLong", 30);
		 nHour = setting.getInt("sleepTimeHours", 22);
		 nMinute = setting.getInt("sleepTimeMinutes", 30);
		 this.sleepSetting = new SleepSetting(nHour, nMinute, true);
	 }
	 private void SaveData(){
		SharedPreferences setting =  this.getSharedPreferences("SP", MODE_PRIVATE);			
		SharedPreferences.Editor edt =  setting.edit();
		edt.putInt("sleepTimeHours", sleepSetting.Hour);
		edt.putInt("sleepTimeMinutes", sleepSetting.Minute);
		boolean bRet = edt.commit();
	 }
	 
	 private void SetSleepAlarm(){
		 AlarmManager am = (AlarmManager)this.getSystemService(ALARM_SERVICE);
		 Intent amIntent = new Intent(this, ScreenEventReceiver.class);
		 amIntent.setAction("SleepTimer");
		 
		 PendingIntent pending = PendingIntent.getBroadcast(this, 0, amIntent, 0);
		 
		
		Calendar cNow =Calendar.getInstance();
		Calendar cSleep = Calendar.getInstance();
		
		cSleep.set(Calendar.HOUR_OF_DAY, this.sleepSetting.Hour);
		cSleep.set(Calendar.MINUTE, this.sleepSetting.Minute);		

		long msSpan = cSleep.getTimeInMillis() - cNow.getTimeInMillis();
		long ms24hour = 24*60*60*1000;
		if(msSpan <0){ // 还没有到时间 , 还需要在判断以下
			msSpan = msSpan + ms24hour;
		}
			
		
		am.set(AlarmManager.RTC, cSleep.getTimeInMillis(), pending); 
	 }
	 
	 public void SetSleepTime(int h, int m){
		 this.sleepSetting.SetSleepTime(h, m);
		 this.SetSleepAlarm();
		 this.SaveData();
	 }
	 
	
}
