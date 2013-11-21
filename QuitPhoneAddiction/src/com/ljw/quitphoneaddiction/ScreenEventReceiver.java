package com.ljw.quitphoneaddiction;

import java.util.List;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;


public class ScreenEventReceiver extends BroadcastReceiver {
	
	public QPAService myService;
	public void onReceive(android.content.Context ctx, android.content.Intent intent){
		Log.i(Common.LogTag, intent.getAction());	
		if("android.intent.action.SCREEN_ON" == intent.getAction()){
			Log.e("process", ""+android.os.Process.myPid());
			RulesManager.Instance().ReceiveEvent(ctx, intent);
		}
		else if("android.intent.action.SCREEN_OFF" == intent.getAction()){
			RulesManager.Instance().ReceiveEvent(ctx, intent);
		}
		else if(Common.LockTypeSleep == intent.getAction()){
			try{
				Intent intentLocker = new Intent(ctx, RemindLocker.class);	
				intentLocker.putExtra("category", Common.LockTypeSleep);
				intentLocker.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ctx.startActivity(intentLocker);
			}
			catch(Exception ex){
				Log.e(Common.LogTag, ex.getMessage());
			}
		}
		else if(Common.LockTypeLock==intent.getAction()){ // ËøÆÁ¼ì²â			
			
			String strTop = getTopActivityClass(ctx);	        
	        if(!strTop.equals("com.ljw.quitphoneaddiction.RemindLocker")){
	        	Intent intentLocker = new Intent(ctx, RemindLocker.class);
	        	intentLocker.putExtra("category", Common.LockTypeLock);
				intentLocker.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ctx.startActivity(intentLocker);
	        }
		}
		else if(Common.LockTypeCareEye == intent.getAction()){
			String strTop = getTopActivityClass(ctx);	        
	        if(!strTop.equals("com.ljw.quitphoneaddiction.RemindLocker")){
	        	Intent intentLocker = new Intent(ctx, RemindLocker.class);
	        	intentLocker.putExtra("category", Common.LockTypeCareEye);
	        	intentLocker.putExtra("duration", intent.getIntExtra("duration", 45));
				intentLocker.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ctx.startActivity(intentLocker);
	        }
		}
	}
	
	

	private String getTopActivityClass(Context ctx){
		ActivityManager mActivityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);         
        String strTop =ar.topActivity.getClassName();
        Log.e("CareEyeMoniter", ar.topActivity.getClassName().toString());
        return strTop;
	}
	
	
}
