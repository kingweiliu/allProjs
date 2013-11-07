package com.ljw.quitphoneaddiction;

import android.content.BroadcastReceiver;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;


public class ScreenEventReceiver extends BroadcastReceiver {
	
	public QPAService myService;
	public void onReceive(android.content.Context ctx, android.content.Intent intent){
		Log.i(Common.LogTag, intent.getAction());	
		if("android.intent.action.SCREEN_ON" == intent.getAction()){
			
		}
		else if("android.intent.action.SCREEN_OFF" == intent.getAction()){
			
		}
		else if("SleepTimer" == intent.getAction()){
			try{
			Intent intentLocker = new Intent(ctx, RemindLocker.class);
			intentLocker.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentLocker.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ctx.startActivity(intentLocker);
			}
			catch(Exception ex){
				Log.e(Common.LogTag, ex.getMessage());
			}
		}
	}
	
	
	
}
