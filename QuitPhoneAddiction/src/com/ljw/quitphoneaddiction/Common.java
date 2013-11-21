package com.ljw.quitphoneaddiction;

import java.util.HashMap;

public class Common {
	static String LogTag = "QuitPhone";
	
	//static QPAService service = null;
	
	static String 	KeySleepSetting = "kss";
	static String 	KeySleepEnable = "kse";
	static String 	KeySleepDays = "ksd";// 0; // 0x1 & 0x2 & 0x4
	static String 	KeySleepTimeHour = "ksth"; //22;
	static String 	KeySleepTimeMinute ="kstm";// 30;
	
	static String 	KeyCareEyeSetting = "kces";
	static String 	KeyCareEyeEnable = "kcee";
	static String 	KeyCareEyeDuration = "kced";
	
	
	static String   LockTypeSleep="lts";
	static String 	LockTypeCareEye = "ltce";
	static String 	LockTypeLock = "ltl";
	
	
	public static String  ConvertToDays(int n){		
		String str = "";
		
		for(int i=0;i<7;++i){
			int d = 1<<i;
			if((d & n) != 0){
				str += days[i]+" ";
			}
		}
		return str;
	}
	
	public static int ConvertDaysToInt(String s){
		int ret = 0;
		for(int i = 0; i<7; ++i){
			if(s.indexOf(days[i]) >=0){
				ret += (1<<i);
			}
		}		
		return ret;		
	}
	
	public static boolean[] ConvertToArray(int n){
		boolean[] bret = new boolean[7];
		for(int i=0;i<7;++i){
			bret[i] = (n & 1<<i) != 0;			
		}
		return bret;
	}
	
	public static int ConvertArrayToInt(boolean[] ba){
		int n = 0;
		for(int i =0;i<7;++i){
			if(ba[i]){
				n += 1<<i;
			}
		}
		return n;
	}
	
	private static String[] days = new String[]{
		"周一", "周二","周三","周四","周五","周六","周日"
};
}
