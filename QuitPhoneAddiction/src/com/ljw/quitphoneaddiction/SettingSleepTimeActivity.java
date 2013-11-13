package com.ljw.quitphoneaddiction;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

public class SettingSleepTimeActivity extends Activity {
	
	ListView lv = null;
	
	private boolean enable = false;
	private int hour = 0;
	private int minute = 0;
	private int days = 0;
	boolean[] bDays = null;
	
	SleepSettingAdapter ssa = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_sleep_time);
		lv = (ListView)findViewById(R.id.listView1);
		
		Bundle bd = getIntent().getBundleExtra(Common.KeySleepSetting);
		enable = bd.getBoolean(Common.KeySleepEnable);
		hour = bd.getInt(Common.KeySleepTimeHour);
		minute = bd.getInt(Common.KeySleepTimeMinute);
		days = bd.getInt(Common.KeySleepDays);
		
		
		ssa = new SleepSettingAdapter();
		lv.setAdapter(ssa);		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0,  View view, int position, long id) {
				Log.d("abc","abc" + position);
				switch(position){
				case 0:	{
					SettingSleepTimeActivity.this.enable = !SettingSleepTimeActivity.this.enable;
					SettingSleepTimeActivity.this.ssa.notifyDataSetChanged();
				}
					break;
				case 1:
					SettingSleepTimeActivity.this.setTime();
					break;
				case 2:
					SettingSleepTimeActivity.this.setDays();
					break;
				}				
			}
		});
	}
	
	private void setTime(){
		new TimePickerDialog(this, new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int m) {
				hour = hourOfDay;
				minute = m;
				SettingSleepTimeActivity.this.ssa.notifyDataSetChanged();
			}
		} , hour, minute, true).show();
		
	}
	
	private void setDays(){
		AlertDialog.Builder build = new AlertDialog.Builder(this);	
			
		bDays = Common.ConvertToArray(days);
	
		build.setTitle("选择生效日期").setMultiChoiceItems(R.array.days, bDays, new OnMultiChoiceClickListener() {			
			@Override
			public void onClick(DialogInterface arg0, int which, boolean isChecked) {
				SettingSleepTimeActivity.this.bDays[which] = isChecked;
			}
		}).setPositiveButton(android.R.string.ok, new OnClickListener() {			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {				
				SettingSleepTimeActivity.this.days = Common.ConvertArrayToInt(SettingSleepTimeActivity.this.bDays);
				SettingSleepTimeActivity.this.ssa.notifyDataSetChanged();
			}
		}).setNegativeButton(android.R.string.cancel, null);
		build.create().show();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getMenuInflater().inflate(R.menu.setting_sleep_time, menu);
		return true;
		
	}
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item){
		if(item.getItemId() == R.id.action_accept){
			Intent intent = new Intent();
			Bundle bd = new Bundle();
			bd.putBoolean(Common.KeySleepEnable, enable);
			bd.putInt(Common.KeySleepTimeHour, hour);
			bd.putInt(Common.KeySleepTimeMinute, minute);
			bd.putInt(Common.KeySleepDays, days);				
			intent.putExtra(Common.KeySleepSetting, bd);
			setResult(RESULT_OK, intent);			
		}
		else{
			setResult(RESULT_CANCELED, null);
		}
		finish();		
		return false;
	}
	
	public class SleepSettingAdapter extends BaseAdapter{

		@Override
		public int getCount() {

			return 3;
		}

		@Override
		public Object getItem(int arg0) {

			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int pos, View arg1, ViewGroup arg2) {
		
			View v= null;
			if(pos == 0){
				v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.temp_sleep_setting_checkbox, null);
//				CheckBox cb =  (CheckBox)v.findViewById(R.id.checkBox1);
//				cb.setChecked(enable);
				//v = LayoutInflater.from(getApplicationContext()).inflate(android.R.layout.simple_list_item_multiple_choice, null);
				CheckedTextView ctv = (CheckedTextView)v.findViewById(android.R.id.text1);
				if(ctv != null){
					ctv.setText("启用睡眠控制");
					ctv.setChecked(enable);
						
				
				}
				
			}
			else{
				//v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.temp_sleep_setting_two_line, null);
				v = LayoutInflater.from(getApplicationContext()).inflate(android.R.layout.simple_list_item_2, null);
				TextView tvUp = (TextView)v.findViewById(android.R.id.text1);
				TextView tvDown = (TextView)v.findViewById(android.R.id.text2);
				if(pos ==1 ){
					tvUp.setText("睡眠时间");
					tvDown.setText(hour +":"+minute);
				}
				else if(pos==2)
				{
					tvUp.setText("重复");
					tvDown.setText(Common.ConvertToDays(days));
				}
			}
			
			return v;
		}
		
	}

}
