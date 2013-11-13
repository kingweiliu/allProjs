package com.ljw.quitphoneaddiction;

import android.app.Application;
import android.content.SharedPreferences;

public class SleepSettingEntity {
	public SleepSettingEntity(int g, Application a){
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
		edt.putBoolean(Common.KeySleepEnable+group, Enabled);
		edt.commit();		
		return true;
	}
	
	public boolean LoadData(){
		SharedPreferences sp = app.getSharedPreferences(Common.KeySleepSetting + group, 0);
		Hour = sp.getInt(Common.KeySleepTimeHour+group, 22);
		Minute = sp.getInt(Common.KeySleepTimeMinute+group, 30);
		Days = sp.getInt(Common.KeySleepDays+group, 0);
		Enabled = sp.getBoolean(Common.KeySleepEnable+group, false);
		return true;
	}
	
	private int group;
	private Application app;
	
	public int Hour;
	public int Minute;		
	public int Days;
	public boolean Enabled;
	
	public boolean getDayEnable(int dayOfWeek) {
		int i = 1 << dayOfWeek;
		return (Days & i) != 0;
	}
	
//	public String getDaysString(){
//		int idx = 1;
//		String strRet="";
//		for(int i =0; i<7 ; ++i){
//			idx = idx <<i;
//			if((Days & idx) != 0){
//				strRet += days[i]+" ";
//			}
//		}
//		return strRet;
//	}
//	
//	public static String[] days = new String[]{
//			"周一", "周二","周三","周四","周五","周六","周日"
//	};
//	
		
}
