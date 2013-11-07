package com.example.qpa;

import android.content.*;
import android.util.Log;


public class BootCompleteReceiver extends BroadcastReceiver {

	public void onReceive(Context ctx, Intent arg1){
		Log.i(PublicDef.LogTag, "onreceive" + arg1.getAction() );
	}
}
