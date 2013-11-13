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
	public SleepSettingEntity[] sleepSettings = new SleepSettingEntity[2];
	public CareEyeEntity careEyeSetting= null;
	private MediaPlayer player = null;
	private int remindLockerShow = 0;
		
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
		
		
		startSleepMoniter();		
		startCareEyeMoniter();
		setupReceiver();				 
		Log.i(Common.LogTag, "Service  onCreate");	
	 }
	 
	private void startCareEyeMoniter() {
		careEyeSetting = new CareEyeEntity(getApplication());
		//TODO: 完善护眼模式的 逻辑
		
	}

	private void startSleepMoniter() {
		for(int i=0; i<2; ++i){
			 sleepSettings[i] = new SleepSettingEntity(i, getApplication());
		 }
		setupSleepTrigger();		
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
	 
	 private void setupSleepTrigger(){
		 Calendar ca = Calendar.getInstance();
		 int dayOfWeek = ca.get(Calendar.DAY_OF_WEEK);
		 for(int i =0 ; i<2; ++i){
			 if(sleepSettings[i].getDayEnable(dayOfWeek)){
				 SetSleepAlarm(sleepSettings[i]);
			 }
		 }
		 
	 }
	 
	 
	 
	 public void SaveData(){
		 for(int i=0; i<2; ++i){
			 sleepSettings[i].SaveData();
		 }
	 }
	 
	 public void SetSleepAlarm(SleepSettingEntity sse){
		 if(!sse.Enabled)
			 return ; // should cancel clock that be setted before.
		 AlarmManager am = (AlarmManager)this.getSystemService(ALARM_SERVICE);
		 Intent amIntent = new Intent(this, ScreenEventReceiver.class);
		 amIntent.setAction("SleepTimer");		 
		 PendingIntent pending = PendingIntent.getBroadcast(this, 0, amIntent, 0);		
		 Calendar cNow =Calendar.getInstance();
		 Calendar cSleep = Calendar.getInstance();
		
		cSleep.set(Calendar.HOUR_OF_DAY, sse.Hour);
		cSleep.set(Calendar.MINUTE, sse.Minute);		
		cSleep.set(Calendar.SECOND, 0);

		long msSpan = cSleep.getTimeInMillis() - cNow.getTimeInMillis();
		long ms24hour = 24*60*60*1000;
		if(msSpan <0){ // 还没有到时间 , 还需要在判断以下
			msSpan = msSpan + ms24hour;
		}		
		am.set(AlarmManager.RTC, cSleep.getTimeInMillis(), pending); 
	 }	  
	 
	 public int getRemindLockerShow(){
		 return remindLockerShow;
	 }
	 
	 public void setRemindLockerShow(int i){
		 remindLockerShow  = i;
	 }
	
}
