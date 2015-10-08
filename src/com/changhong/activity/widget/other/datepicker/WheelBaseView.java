package com.changhong.activity.widget.other.datepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


/**
 * 自定义视图的基类
 * 
 * @since 2013-8-28
 */
abstract public class WheelBaseView extends View {
    public WheelBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public WheelBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public WheelBaseView(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        resize();
        onInit();
    }

    protected void onInit() {
    	
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void invisible() {
        setVisibility(View.INVISIBLE);
    }

    /**
     * 重新计算当前View的宽高、边距
     * 
     */
    public void resize() {
        DatePickerUtil.resize(this);
    }

    public String getLogTag() {
        return this.getClass().getSimpleName();
    }

    public void onDestroy() {
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
}
