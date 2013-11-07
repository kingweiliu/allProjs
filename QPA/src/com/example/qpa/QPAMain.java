package com.example.qpa;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;
import android.util.Log;
import android.content.ComponentName;

public class QPAMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qpamain);
		Log.i(PublicDef.LogTag, " QPAMain onCreate");
		Intent intentService = new Intent(QPAMain.this, QPAService.class);
		ComponentName cn = startService(intentService);
		Log.i(PublicDef.LogTag, " QPAMain onCreate  end");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qpamain, menu);
		return true;
	}
	
	@Override
	protected void onStop(){
		Log.i(PublicDef.LogTag, " QPAMain onStop");
		Intent intentService = new Intent(QPAMain.this, QPAService.class);
		stopService(intentService);
		super.onStop();
	}

}
