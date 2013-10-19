package com.ljw.quitphoneaddiction;

import android.content.BroadcastReceiver;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;

public class ScreenEventReceiver extends BroadcastReceiver {
	
	public QPAService myService;
	public void onReceive(android.content.Context ctx, android.content.Intent intent){
		//Intent sayHelloIntent=new Intent(ctx, MainActivity.class);
		  /* sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   ctx.startActivity(sayHelloIntent);
		   */
		
		//ctx.startService(new Intent(ctx, QPAService.class));
		Log.i("ScreenEventReceiver", intent.getAction());	
		myService.AddMessage(intent.getAction());
		
	}
}
