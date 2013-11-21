package ljw.control;


import com.ljw.quitphoneaddiction.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TimePicker;

public class TimePickerPereference extends DialogPreference{

	
	TimePicker tp = null;
	int hour = 22;
	int minute = 30;
	private String mText;
	
	public TimePickerPereference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDialogLayoutResource(R.layout.layout_timp_pick_preference);
	}
	
	@Override
    protected void onBindDialogView(View view) {
		super.onBindDialogView(view);		
		tp = (TimePicker) view.findViewById(R.id.time_picker);	
		tp.setIs24HourView(true);		
        tp.setCurrentHour(hour);
        tp.setCurrentMinute(minute);
	}
	
	@Override
    protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if(positiveResult){
			hour = tp.getCurrentHour();
			minute = tp.getCurrentMinute();	
			String strHM = hour +":" + minute;
			if(!callChangeListener(strHM))
				setText(strHM);
		}
	}
	
	public void setText(String text) {
        final boolean wasBlocking = shouldDisableDependents();	  
        mText = text;
        persistString(text);	    
        String[] strHMs = text.split(":");
        if(strHMs.length != 2)
        	return;
        hour = Integer.parseInt(strHMs[0]);
        minute = Integer.parseInt(strHMs[1]);
        notifyDependencyChange(true);
        this.notifyChanged();
    }
	
	@Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setText(restoreValue ? getPersistedString(mText) : (String) defaultValue);
    }
	
	@Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }
	
	

}
