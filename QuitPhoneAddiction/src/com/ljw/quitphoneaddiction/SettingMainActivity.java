package com.ljw.quitphoneaddiction;


import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

public class SettingMainActivity extends ListActivity {

	private List<Node> layoutData = new ArrayList<Node>();
	SettingAdapter sa = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_main);
		sa = new SettingAdapter();
		setListAdapter(sa);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		getMenuInflater().inflate(R.menu.setting_main, menu);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item){
		if(item.getItemId() == R.id.action_accept){
			RulesManager.Instance().ResetRule();
		}
		finish();
		return false;
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id){
		if(position == 4){			
			CareEyeEntity cee = (CareEyeEntity)layoutData.get(position).tag;			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);		
			builder.setTitle("连续使用时长");	
			//View vw = LayoutInflater.from(getApplicationContext()).inflate(R.layout.temp_dialog_time_gap, null); //= 
			
			NumberPicker np =new NumberPicker(this);
			np.setMaxValue(100);
	        np.setMinValue(15);
	        np.setWrapSelectorWheel(false);
	        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
	        np.setValue(cee.Duration);	        
			builder.setView(np);
			builder.setPositiveButton(android.R.string.ok, new DurationClickListen(np, position));			
			builder.setNegativeButton(android.R.string.cancel, null);
			// 3. Get the AlertDialog from create()
			AlertDialog dialog = builder.create();			
			dialog.show();
		}
		else if(position ==2 || position ==1 ){
			Intent intent = new Intent (this, SettingSleepTimeActivity.class);
			Bundle bd = new Bundle();
			Node n =  layoutData.get(position);
			if(n.tag.getClass() == SleepSettingEntity.class){
				SleepSettingEntity sse = (SleepSettingEntity)n.tag;
				bd.putBoolean(Common.KeySleepEnable, sse.getEnable());
				bd.putInt(Common.KeySleepTimeHour, sse.Hour);
				bd.putInt(Common.KeySleepTimeMinute, sse.Minute);
				bd.putInt(Common.KeySleepDays, sse.Days);				
				intent.putExtra(Common.KeySleepSetting, bd);				
				startActivityForResult(intent, position);
			}			
		}
	}

	
	private class DurationClickListen implements OnClickListener{
		public NumberPicker npp = null;
		public int pos = -1;
		public DurationClickListen(NumberPicker np, int p){
			npp = np;			
			pos = p;
		}
		
		@Override
		public void onClick(DialogInterface arg0, int arg1) {				
			int duration = npp.getValue();
			CareEyeEntity cee = (CareEyeEntity)layoutData.get(pos).tag;
			cee.Duration = duration;
			layoutData.get(pos).setTag(cee);
			cee.SaveData();
			SettingMainActivity.this.sa.notifyDataSetChanged();			
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == RESULT_OK){			
			
			SleepSettingEntity sse = (SleepSettingEntity)layoutData.get(requestCode).tag;						
			Bundle bd = data.getBundleExtra(Common.KeySleepSetting);
			sse.setEnable(bd.getBoolean(Common.KeySleepEnable));
			sse.Hour = bd.getInt(Common.KeySleepTimeHour);
			sse.Minute = bd.getInt(Common.KeySleepTimeMinute);
			sse.Days = bd.getInt(Common.KeySleepDays);
			layoutData.get(requestCode).setTag(sse);
			sse.SaveData();
			sa.notifyDataSetChanged();
			Log.e(Common.LogTag, "result ok");
		}
	}
	
	public class Node{
		public int category = 0;		
		
		public Node(int cat, String st){
			category = cat;
			MainTitle = st;
		}
		
		public Node(int cat, IHeadUpSetting sse){
			MainTitle = sse.getMainTitle();
			SubTitle = sse.getSubTitle();
			
			category= cat;	
			tag = sse;
		}
		

		public void setTag(IHeadUpSetting hus){
			MainTitle = hus.getMainTitle();
			SubTitle = hus.getSubTitle();
			tag = hus;			
		}
		
		public String MainTitle;
		public String SubTitle;
		
		public IHeadUpSetting tag;
	}
	
	public class SettingAdapter extends BaseAdapter{		
		public SettingAdapter(){
			layoutData.add(new Node(1, "睡眠时间"));	
			layoutData.add(new Node(2, RulesManager.Instance().sse[0]));
			layoutData.add(new Node(2, RulesManager.Instance().sse[1]));
			layoutData.add(new Node(3, "护眼模式"));
			layoutData.add(new Node(6, RulesManager.Instance().cee));
		}		
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return layoutData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return layoutData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		

		@Override
		public View getView(int pos, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View v = null;
			Node cusNode= (Node)getItem(pos);
			if(cusNode.category == 1 || cusNode.category ==3){
				v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.temp_sleep_setting_header, null);
				TextView tvTitle = (TextView) v.findViewById(R.id.txtSecTitle);
				tvTitle.setText(layoutData.get(pos).MainTitle);
			}				
			else{
				v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.temp_sleep_setting_item, null);
				TextView tvTitle = (TextView) v.findViewById(R.id.txtTitle);
				tvTitle.setText(layoutData.get(pos).MainTitle);
				TextView tvDown = (TextView) v.findViewById(R.id.txtDown);
				tvDown.setText(layoutData.get(pos).SubTitle);		
				CheckBox cb = (CheckBox)v.findViewById(R.id.chkEnable);
				cb.setChecked(layoutData.get(pos).tag.getEnable());
				cb.setOnCheckedChangeListener(new CheckChangeListen(pos));
			}
			
			return v;
		}
		
		public class CheckChangeListen implements OnCheckedChangeListener{
			
			private int pos;
			
			public CheckChangeListen(int p) {
				pos = p;
			}

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				layoutData.get(pos).tag.setEnable(isChecked);				
				SettingMainActivity.this.sa.notifyDataSetChanged();				
			}
			
		}
	}

	
	
}
