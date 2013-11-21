package com.ljw.quitphoneaddiction;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;



//�������е� ���� ����ϵͳ�¼��ķַ�
public class RulesManager {
	
	public static RulesManager  Instance(){
		if(instance == null)
			instance = new RulesManager();
		return instance;
	}
	private static RulesManager instance = null;	
	
	//˯��ʱ�� ��ع���
	public SleepSettingEntity[] sse = new SleepSettingEntity[2];	
	public CareEyeEntity cee = null;
	
	
	private RulesManager(){
		sse[0] = new SleepSettingEntity(0, QpaApplication.getAppContext());
		sse[1] = new SleepSettingEntity(1, QpaApplication.getAppContext());
		cee = new CareEyeEntity(QpaApplication.getAppContext());
	}

	public void OnBootCompleted(){
				
	 	for(int i=0;i<2;++i){
	 		if(!sse[i].getEnable())
	 			continue;
 			sse[i].SetupMoniter(); 			 		
	 	}
	 	QpaApplication.getAppContext().startService(new Intent(QpaApplication.getAppContext(), QPAService.class));
	 	
	 	cee.OnScreenOn();
	}
	
	public void ResetRule(){
		sse[0] = new SleepSettingEntity(0, QpaApplication.getAppContext());
		sse[1] = new SleepSettingEntity(1, QpaApplication.getAppContext());
		cee = new CareEyeEntity(QpaApplication.getAppContext());
		
		OnBootCompleted();
	}
	
	public void ReceiveEvent(Context ctx, Intent intent) {
		if(intent.getAction() == "android.intent.action.SCREEN_ON"){
			cee.OnScreenOn();
		}
		else if(intent.getAction() == "android.intent.action.SCREEN_OFF"){
			cee.OnScreenOff();
		}
	}
		
}
