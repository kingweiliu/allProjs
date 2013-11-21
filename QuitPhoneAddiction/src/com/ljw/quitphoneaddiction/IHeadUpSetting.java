package com.ljw.quitphoneaddiction;

public interface IHeadUpSetting {
	
	public String getMainTitle();
	public String getSubTitle();
	
	public boolean getEnable();
	public void setEnable(boolean b);
	
	public int getTagId();	
}
