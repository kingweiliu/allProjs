package com.ljw.quitphoneaddiction;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Iterator;

import ljw.control.TimePickerPereference;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;

public class PreferenceSettingActivity extends PreferenceActivity {
	
	
	

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.setting);
        
        Preference pref = findPreference("sleep_alarm_days_1");
        Preference pref2 = findPreference("sleep_alarm_days_2");
        
        OnPreferenceChangeListener opcl = new OnPreferenceChangeListener(){
			@Override
			public boolean onPreferenceChange(Preference pref, Object arg1) {
				MultiSelectListPreference mslp = (MultiSelectListPreference)pref;
				HashSet si = (HashSet)arg1;
				String ret = "";
				for(Iterator iter = si.iterator(); iter.hasNext(); ){
					Integer i = Integer.parseInt(iter.next().toString());
					if(i<6 )
						ret += (String) mslp.getEntries()[i];
										
				}
				mslp.setSummary(ret);
				return true;
			}			
		};
		pref.setOnPreferenceChangeListener(opcl);
		pref2.setOnPreferenceChangeListener(opcl);
		
		opcl.onPreferenceChange(pref, PreferenceManager.getDefaultSharedPreferences(
				pref.getContext()).getStringSet(pref.getKey(), new HashSet<String>()));
		opcl.onPreferenceChange(pref2, PreferenceManager.getDefaultSharedPreferences(
				pref.getContext()).getStringSet(pref.getKey(), new HashSet<String>()));
		
		pref = findPreference("sleep_alarm_time_1");
		pref2 = findPreference("sleep_alarm_time_2");
        opcl = new OnPreferenceChangeListener(){
			@Override
			public boolean onPreferenceChange(Preference pref, Object arg1) {
				TimePickerPereference mslp = (TimePickerPereference)pref;				
				String ret = arg1.toString();				
				mslp.setTitle(ret);
				return true;
			}			
		};
		pref.setOnPreferenceChangeListener(opcl);
		pref2.setOnPreferenceChangeListener(opcl);
		
		pref = findPreference("sleep_alarm_enable_1");
		pref2 = findPreference("sleep_alarm_enable_2");
        opcl = new OnPreferenceChangeListener(){
			@Override
			public boolean onPreferenceChange(Preference pref, Object arg1) {
				CheckBoxPreference cbp = (CheckBoxPreference)pref;				
				return true;
			}			
		};
		pref.setOnPreferenceChangeListener(opcl);
		pref2.setOnPreferenceChangeListener(opcl);	
    }
	
	private void OnPreferenceChanged(Preference pref){
		String[] strNames = pref.getKey().split("_");
		int nGroup = Integer.parseInt(strNames[strNames.length-1]);
		Preference prefDays = findPreference("sleep_alarm_days_"+nGroup);
		Preference prefTime = findPreference("sleep_alarm_time_"+nGroup);
		Preference prefEnable = findPreference("sleep_alarm_enable_"+nGroup);
		
	}
	
	
}
