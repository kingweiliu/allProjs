package com.ljw.quitphoneaddiction;

import com.ljw.quitphoneaddiction.QPAService.MyBinder;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.view.View;
import android.content.ServiceConnection;
import java.util.ArrayList;
import android.widget.ArrayAdapter;


public class MainActivity extends Activity implements android.view.View.OnClickListener {

	QPAService qpaService;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnStart = (Button)findViewById(R.id.btnStart);
		btnStart.setOnClickListener(this);
		Button btnStop = (Button)findViewById(R.id.btnStop);
		btnStop.setOnClickListener(this);
		
		Button btnRefresh = (Button)findViewById(R.id.btnRefresh);
		btnRefresh.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void onClick(View src) { 
		switch(src.getId()){
		case R.id.btnStart:
			 Intent intent = new Intent("com.ljw.quitphoneaddiction.bindService");
			 boolean bRet = false;
			 try{
		     bRet =bindService(intent, conn, Context.BIND_AUTO_CREATE);          // bindService
			 }
			 catch(Exception e){
				 Log.e("ljw", e.toString());
			 }
		     
		     
		     
			//startService(new Intent(this, QPAService.class));
			break;
		case R.id.btnStop:
			unbindService(conn);
			//stopService(new Intent(this, QPAService.class));
			break;
		case R.id.btnRefresh:
			{
				ArrayAdapter<String> adapt= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
						qpaService.vecMsg);
				ListView lv = (ListView)findViewById(R.id.lvMsg);
				lv.setAdapter(adapt);
			}
		
			break;
		}
	}
	
	private ServiceConnection conn = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            qpaService = null;
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            MyBinder binder = (MyBinder)service;
            qpaService = binder.getService();
             
            
        }
    };

}
