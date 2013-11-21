package com.ljw.quitphoneaddiction;



import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;

import android.widget.TimePicker;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.view.View;
import android.content.ServiceConnection;

import android.content.SharedPreferences;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.ljw.quitphoneaddiction.QPAService.MyBinder;

import android.widget.EditText;
import android.widget.TimePicker;


public class MainActivity extends Activity implements android.view.View.OnClickListener {

	QPAService qpaService = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		Intent intent = new Intent("com.ljw.quitphoneaddiction.bindService");
//		 boolean bRet = false;
//		 try{
//	     bRet =bindService(intent, conn, Context.BIND_AUTO_CREATE);          // bindService
//		 }
//		 catch(Exception e){
//			 Log.e("ljw.qpa", e.toString());
//		 }
//		
		 Button btn = (Button)findViewById(R.id.button1);
		 btn.setOnClickListener(this);
		 startService(new Intent(this, QPAService.class));
	}
	
	protected void onStop() {		
		super.onStop();
		
		try{
		unbindService(conn);
		
		}
		catch(Exception ex){
			Log.d("ljw.qpa", ex.getMessage());
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void onClick(View src) {
//		Intent intent = new Intent(this, PreferenceSettingActivity.class);
//		startActivity(intent);
		
		Intent intent = new Intent (this, SettingMainActivity.class);
		startActivity(intent);
	}
	
	private ServiceConnection conn = new ServiceConnection() {        
        @Override
        public void onServiceDisconnected(ComponentName name) {
        	//Common.service = null;
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBinder binder = (MyBinder)service;
            qpaService = binder.getService();
            //Common.service = qpaService;
        }
    };

}
