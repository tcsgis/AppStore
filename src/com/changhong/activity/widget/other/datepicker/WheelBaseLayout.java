package com.changhong.activity.widget.other.datepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;


/**
 * 自定义控件的基类
 * 
 */
abstract public class WheelBaseLayout extends RelativeLayout {
    protected View mContentView;
    public WheelBaseLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public WheelBaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public WheelBaseLayout(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        setContentView(onInitLayoutResId());
        
    }

    private void setContentView(int layoutResId) {
    	if(layoutResId!=0){
    		View inflater = LayoutInflater.from(getContext()).inflate(layoutResId, this, true);
    		onInit(inflater);
    		mContentView = getChildAt(0);
    	}else{
    		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
    		LinearLayout inflater= new LinearLayout(getContext());
    		inflater.setLayoutParams(params);
    		inflater.setOrientation(LinearLayout.HORIZONTAL);
    		onInit(inflater);
    		mContentView=getChildAt(0);
    	}
        
    }

    abstract protected int onInitLayoutResId();

    protected void onInit(View inflater) {

    }

    public void show() {
        show(this);
    }

    public void hide() {
        hide(this);
    }

    public void show(View v) {
        v.setVisibility(View.VISIBLE);
    }

    public void hide(View v) {
        v.setVisibility(View.GONE);
    }

    public void invisible(View v) {
        v.setVisibility(View.INVISIBLE);
    }


    @Override
    public void setBackgroundResource(int resid) {
        mContentView.setBackgroundResource(resid);
    }

    /**
     * 重新计算View及子View的宽高、边距
     * 
     * @param view
     */
    public void resizeView(View view) {
    	DatePickerUtil.resizeRecursively(view);
    }

    public String getLogTag() {
        return this.getClass().getSimpleName();
    }

    public void onDestroy() {
        this.removeAllViews();
        mContentView = null;
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void clear() {
    }

    public void reset() {
    }

    public void onShow() {

    }
    public  int getWindowWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        if (dm == null)
            return 0;
        return dm.widthPixels;
    }
    public  int getWindowHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        if (dm == null)
            return 0;
        return dm.heightPixels;
    }
}
