package com.ljw.quitphoneaddiction;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
	public void onReceive(android.content.Context ctx, android.content.Intent arg1){				
		Log.d(Common.LogTag, "BootCompleteReceiver");
		RulesManager.Instance().OnBootCompleted();
	}
}