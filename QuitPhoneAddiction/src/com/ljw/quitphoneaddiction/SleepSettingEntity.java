package com.ljw.quitphoneaddiction;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SleepSettingEntity implements IHeadUpSetting {
	public SleepSettingEntity(int g, Context a){
		group = g;
		app = a;
		LoadData();
	}	
	
	public boolean SaveData(){
		SharedPreferences sp = app.getSharedPreferences(Common.KeySleepSetting + group, 0);
		SharedPreferences.Editor edt = sp.edit();
		edt.putInt(Common.KeySleepTimeHour+group, Hour);
		edt.putInt(Common.KeySleepTimeMinute+group, Minute);
		edt.putInt(Common.KeySleepDays+group, Days);
		edt.putBoolean(Common.KeySleepEnable+group, enabled);
		edt.commit();		
		return true;
	}
	
	public boolean LoadData(){
		SharedPreferences sp = app.getSharedPreferences(Common.KeySleepSetting + group, 0);
		Hour = sp.getInt(Common.KeySleepTimeHour+group, 22);
		Minute = sp.getInt(Common.KeySleepTimeMinute+group, 30);
		Days = sp.getInt(Common.KeySleepDays+group, 0x1111111);
		enabled = sp.getBoolean(Common.KeySleepEnable+group, true);
		return true;
	}
	
	//建立监控
	public void SetupMoniter(){
		Calendar cNow =Calendar.getInstance();
		if((1<<cNow.get(Calendar.DAY_OF_WEEK) & Days) == 0)
			return ;
		AlarmManager am = (AlarmManager)QpaApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
		Intent amIntent = new Intent(QpaApplication.getAppContext(), ScreenEventReceiver.class);
		amIntent.setAction(Common.LockTypeSleep);		 
		PendingIntent pending = PendingIntent.getBroadcast(QpaApplication.getAppContext(), 0, amIntent, 0);		
		
		Calendar cSleep = Calendar.getInstance();
		
		cSleep.set(Calendar.HOUR_OF_DAY, Hour);
		cSleep.set(Calendar.MINUTE, Minute);		
		cSleep.set(Calendar.SECOND, 0);

		long msSpan = cSleep.getTimeInMillis() - cNow.getTimeInMillis();
		long ms24hour = 24*60*60*1000;
		if(msSpan <0){ // 还没有到时间 , 还需要在判断以下
			msSpan = msSpan + ms24hour;
		}		
		am.set(AlarmManager.RTC, cSleep.getTimeInMillis(), pending); 
	}
	
	private int group;
	private Context app;
	
	public int Hour;
	public int Minute;		
	public int Days;
	private boolean enabled;
	
	public boolean getDayEnable(int dayOfWeek) {
		int i = 1 << dayOfWeek;
		return (Days & i) != 0;
	}

	@Override
	public String getMainTitle() {
		return Hour + ":" + Minute;
	}

	@Override
	public String getSubTitle() {
		return Common.ConvertToDays(Days);
	}

	@Override
	public boolean getEnable() {		
		return enabled;				
	}

	@Override
	public void setEnable(boolean b) {
		enabled = b;		
	}
	
	public int getTagId(){
		return 1;
	}

}
