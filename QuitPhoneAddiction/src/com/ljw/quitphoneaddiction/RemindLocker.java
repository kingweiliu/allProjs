package com.ljw.quitphoneaddiction;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

public class RemindLocker extends Activity {

	boolean canClose = false;
	
	private PendingIntent pi = null;
	String strCategory = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind_locker);
		Log.d(Common.LogTag, "Remind oncreat");
		Intent it =  this.getIntent();
		strCategory = it.getStringExtra("category");
		
		TextView tv = (TextView)findViewById(R.id.textView1);
		tv.setText(strCategory);
		if(strCategory.equals(Common.LockTypeCareEye)){
			//¶¨Ê±Ïû³ý
			Timer t = new Timer();
			TimerTask tt = new TimerTask() {
				
				@Override
				public void run() {
					canClose = true;
					RemindLocker.this.finish();					
				}
			};
			
			t.schedule(tt, 30*1000);
		}
		
		Button btn= (Button)findViewById(R.id.button1);
		btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				canClose = true;
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.remind_locker, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event){		
		switch(keyCode){
		case android.view.KeyEvent.KEYCODE_BACK:
		case android.view.KeyEvent.KEYCODE_MENU:
		case android.view.KeyEvent.KEYCODE_HOME:
			return true;
		}
	    return super.onKeyDown(keyCode, event);
	}	
	
	@Override
	protected void onStop(){
		Log.d(Common.LogTag, "Remind onStop");
		super.onStop();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		Intent amIntent = new Intent(QpaApplication.getAppContext(), ScreenEventReceiver.class);
		amIntent.setAction(Common.LockTypeLock);		 
		pi = PendingIntent.getBroadcast(QpaApplication.getAppContext(), 0, amIntent, 0);	
		AlarmManager am = (AlarmManager)QpaApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
		am.cancel(pi); 
	}
	
	@Override
	protected void onPause(){		
		if(!canClose){
			Intent amIntent = new Intent(QpaApplication.getAppContext(), ScreenEventReceiver.class);
			amIntent.setAction(Common.LockTypeLock);		 
			pi = PendingIntent.getBroadcast(QpaApplication.getAppContext(), 0, amIntent, 0);	
			AlarmManager am = (AlarmManager)QpaApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.ELAPSED_REALTIME, 0, 500, pi); 
		}
        super.onPause();
	}
	

}
