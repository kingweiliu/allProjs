package com.ljw.quitphoneaddiction;

import android.app.Service;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Binder;
import android.widget.Toast;
import android.util.Log;
import java.util.Vector;
import java.util.ArrayList;

public class QPAService extends Service{

	private ScreenEventReceiver screenReceiver;
	public ArrayList<String> vecMsg;
	
	private final IBinder binder = new MyBinder();  
	  
    public class MyBinder extends Binder {  
    	QPAService getService() {  
            return QPAService.this;  
        }  
    }  
	
	
	public void onDestroy(){
		Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();
		unregisterReceiver(screenReceiver);
	}
	
	 public void onStart(android.content.Intent intent, int startId){
		 try{
		 Toast.makeText(this, "onStart", Toast.LENGTH_LONG).show();
		 /*
		 screenReceiver = new ScreenEventReceiver();
		 IntentFilter  intentFilter = new IntentFilter();
		 intentFilter.addAction("android.intent.action.SCREEN_OFF");
		 intentFilter.addAction("android.intent.action.SCREEN_ON");
		 registerReceiver(screenReceiver, intentFilter);
		 */
		 screenReceiver = new ScreenEventReceiver();
		 IntentFilter  intentFilter = new IntentFilter();
		 intentFilter.addAction("android.intent.action.SCREEN_OFF");
		 intentFilter.addAction("android.intent.action.SCREEN_ON");
		 registerReceiver(screenReceiver, intentFilter);
		 screenReceiver.myService = this;
		 
		 }
		 catch(Exception e){
			 Log.e("ljw", e.toString());
		 }
		 
	 }
	 
	 public void onCreate(){
		 try{
			 vecMsg = new ArrayList<String>();
			 Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
		 }
		 catch(Exception e){
			 Log.e("ljw", e.getMessage());
		 }
	 }
	 
	 public IBinder onBind(android.content.Intent arg0){
		 return binder;
	 }
	 public void AddMessage(String strMsg){
		 vecMsg.add(strMsg);
	 }
}
