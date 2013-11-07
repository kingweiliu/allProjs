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
		
		Log.i(Common.LogTag, "hello world!");
		
		TimePicker tp = (TimePicker)findViewById(R.id.timePick);
		tp.setIs24HourView(true);
		
		init();
		
		readFromStorage();
			
	
		Intent intent = new Intent("com.ljw.quitphoneaddiction.bindService");
		 boolean bRet = false;
		 try{
	     bRet =bindService(intent, conn, Context.BIND_AUTO_CREATE);          // bindService
		 }
		 catch(Exception e){
			 Log.e("ljw.qpa", e.toString());
		 }
		
	}
	
	protected void onStop() {
		try{
		unbindService(conn);
		super.onStop();
		}
		catch(Exception ex){
			Log.d("ljw.qpa", ex.getMessage());
		}
	}
	
	private void init(){
		Button btnSave = (Button)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View view){
				saveToStorage();
			}
		});
	}
	
	private void readFromStorage(){
//		SharedPreferences setting = this.getSharedPreferences("SP", MODE_PRIVATE);
//		int nLong =0, nHour=0, nMinute=0;
//		nLong = setting.getInt("durationLong", 30);
//		nHour = setting.getInt("sleepTimeHours", 22);
//		nMinute = setting.getInt("sleepTimeMinutes", 30);
		if(qpaService != null){
			TimePicker tp = (TimePicker)findViewById(R.id.timePick);
			tp.setCurrentHour(qpaService.sleepSetting.Hour);
			tp.setCurrentMinute(qpaService.sleepSetting.Minute);
		}
		
	
		EditText edt = (EditText)findViewById(R.id.edtNumber);
		//edt.setText(""+nLong);
		
	}
	
	private void saveToStorage(){
//		SharedPreferences setting = this.getPreferences(Activity.MODE_PRIVATE);
//		
//		SharedPreferences.Editor edt =  setting.edit();
//		
//		int nLong;
//		EditText edtNumber = (EditText)findViewById(R.id.edtNumber);
//		String strText= edtNumber.getText().toString();
//		
//		edt.putInt("durationLong", Integer.parseInt(strText));	
//		
		TimePicker tp = (TimePicker)findViewById(R.id.timePick);
		
//		edt.putInt("sleepTimeHours", tp.getCurrentHour());
//		edt.putInt("sleepTimeMinutes", tp.getCurrentMinute());
//		boolean bRet = edt.commit();
		
		if(qpaService != null){
			qpaService.SetSleepTime(tp.getCurrentHour(),  tp.getCurrentMinute());
		}
		Log.e("ljw.qpa", "save to storage :");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void onClick(View src) {
		
	}
	
	private ServiceConnection conn = new ServiceConnection() {        
        @Override
        public void onServiceDisconnected(ComponentName name) {
        	Common.service = null;
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBinder binder = (MyBinder)service;
            qpaService = binder.getService();
            Common.service = qpaService;
        }
    };

}
