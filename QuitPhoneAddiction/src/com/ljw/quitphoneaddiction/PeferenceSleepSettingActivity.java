package com.ljw.quitphoneaddiction;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class PeferenceSleepSettingActivity extends PreferenceActivity {
	@Override 
	protected void onCreate(Bundle b){
		super.onCreate(b);	
		addPreferencesFromResource(R.xml.setting_sleep);
		
		
	}
}
