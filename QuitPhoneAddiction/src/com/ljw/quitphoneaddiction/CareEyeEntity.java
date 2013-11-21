package com.ljw.quitphoneaddiction;



import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class CareEyeEntity implements IHeadUpSetting {
	public CareEyeEntity(Context a){
		app = a;
		
		dateScreenOn = Calendar.getInstance();
		dateScreenOff = Calendar.getInstance();
		LoadData();
	}
	
	public boolean LoadData(){
		SharedPreferences sp = app.getSharedPreferences(Common.KeyCareEyeSetting, 0);
		Duration = sp.getInt(Common.KeyCareEyeDuration, 22);
		enable = sp.getBoolean(Common.KeyCareEyeEnable, true);
		return true;
	}
	
	public boolean SaveData(){
		SharedPreferences sp = app.getSharedPreferences(Common.KeyCareEyeSetting, 0);
		SharedPreferences.Editor edt = sp.edit();
		edt.putInt(Common.KeyCareEyeDuration, Duration);
		edt.putBoolean(Common.KeyCareEyeEnable, enable);
		edt.commit();		
		return true;
	}
	
	public void OnScreenOn(){
		Calendar now = Calendar.getInstance();
		if((now.getTimeInMillis() - dateScreenOff.getTimeInMillis()) > 30*1000)  // 关屏30s
			dateScreenOn = Calendar.getInstance();
		SetUpMoniter();
	}
	
	//按照起始时间， 设置
	private void SetUpMoniter() {		
		AlarmManager am = (AlarmManager)QpaApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
		Intent amIntent = new Intent(QpaApplication.getAppContext(), ScreenEventReceiver.class);
		amIntent.setAction(Common.LockTypeCareEye);		
		amIntent.putExtra("duration", 30);
		PendingIntent pending = PendingIntent.getBroadcast(QpaApplication.getAppContext(), 0, amIntent, 0);
		am.cancel(pending);		
		Calendar cRemind = dateScreenOn;
		cRemind.add(Calendar.MINUTE, 1);
		am.set(AlarmManager.RTC, cRemind.getTimeInMillis(), pending); 		
	}
	
	private void CancelMoniter(){
		AlarmManager am = (AlarmManager)QpaApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
		Intent amIntent = new Intent(QpaApplication.getAppContext(), ScreenEventReceiver.class);
		amIntent.setAction("CareEyeMoniter");
		
		PendingIntent pending = PendingIntent.getBroadcast(QpaApplication.getAppContext(), 0, amIntent, 0);
		am.cancel(pending);//先取消掉上一次的。
	}

	public void OnScreenOff(){
		dateScreenOff = Calendar.getInstance();
		CancelMoniter();
	}
	
	public String Description ="每个一段时间进行提醒，防止长时间使用手机引起的视力问题" ;
		
	public int Duration;
	private boolean enable;
	private Context app;
	
	
	private Calendar dateScreenOn = null;
	private Calendar dateScreenOff = null;
	
	@Override
	public String getMainTitle() {		
		return ""+Duration;
	}

	@Override
	public String getSubTitle() {		
		return "每个一段时间进行提醒，防止长时间使用手机引起的视力问题";
	}

	@Override
	public boolean getEnable() {
		return enable;
		
	}

	@Override
	public void setEnable(boolean b) {
		enable= b;		
	}
	
	public int getTagId(){
		return 3;
	}
}
