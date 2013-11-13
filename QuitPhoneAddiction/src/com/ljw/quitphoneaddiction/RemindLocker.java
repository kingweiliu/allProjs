package com.ljw.quitphoneaddiction;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

public class RemindLocker extends Activity {

	boolean canClose = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind_locker);
		Log.d(Common.LogTag, "Remind oncreat");
		
		Button btn= (Button)findViewById(R.id.button1);
		btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				canClose = true;
				finish();
			}
		});
		
		if(Common.service != null){
			Common.service.setRemindLockerShow(Common.service.getRemindLockerShow()+1);
			TextView tv = (TextView )findViewById(R.id.textView1);
			if(tv != null )
				tv.setText(""+ Common.service.getRemindLockerShow());
			
			
		}
		
		
		
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
		if(Common.service != null){
			Common.service.setRemindLockerShow(Common.service.getRemindLockerShow()-1);
		}
		super.onStop();
	}
	
	@Override
	protected void onPause(){
		
		if(!canClose){		
			Timer t = new Timer();
	        t.schedule(new TimerTask() {
	
	            @Override //dashboard.class is your main class
	            public void run() {
	                Intent dialogIntent = new Intent(getBaseContext(), RemindLocker.class);
				    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    getApplication().startActivity(dialogIntent);
	            }
	        }, 10);
		}
        super.onPause();
	}
	

}
