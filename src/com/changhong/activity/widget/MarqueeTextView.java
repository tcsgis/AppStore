package com.changhong.activity.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView {
 
	public MarqueeTextView(Context context){
		this(context,null);
	}
	public MarqueeTextView(Context context,AttributeSet attrs){
		super(context,attrs);
	}	
	@Override
	public boolean isFocused(){
	
		return true;
	}
}  

