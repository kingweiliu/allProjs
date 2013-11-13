package com.ljw.quitphoneaddiction;

import android.app.Application;
import android.content.SharedPreferences;

public class CareEyeEntity {
	public CareEyeEntity(Application a){
		app = a;
	}
	
	public boolean LoadData(){
		SharedPreferences sp = app.getSharedPreferences(Common.KeyCareEyeSetting, 0);
		Duration = sp.getInt(Common.KeyCareEyeDuration, 22);
		Enable = sp.getBoolean(Common.KeyCareEyeEnable, true);
		return true;
	}
	
	public boolean SaveData(){
		SharedPreferences sp = app.getSharedPreferences(Common.KeyCareEyeSetting, 0);
		SharedPreferences.Editor edt = sp.edit();
		edt.putInt(Common.KeyCareEyeDuration, Duration);
		edt.putBoolean(Common.KeyCareEyeEnable, Enable);
		edt.commit();		
		return true;
	}
	
	public String Description = "每个一段时间进行提醒，防止长时间使用手机引起的视力问题";
		
	public int Duration;
	public boolean Enable;
	private Application app;
}
