package com.changhong.activity.widget.other.datepicker;

import android.graphics.Paint;

import com.changhong.activity.widget.other.datepicker.WheelView;

/**
 * 滚轮适配器
 * 
 * @author wangzengyang@gmail.com
 * @since 2013-12-26
 */
abstract public class WheelAdapter {
    private WheelView mWheelView;

    abstract public int getCount();

    abstract public String getItem(int index);

    public float getMaxWidth(Paint paint) {
        String first = getItem(0);
        String last = getItem(getCount() - 1);
        float firstWidth = paint.measureText(first);
        float lastWidth = paint.measureText(last);
        return Math.max(firstWidth, lastWidth);
    }

    public void setWheelView(WheelView wheelView) {
        mWheelView = wheelView;
    }

    public void notifyChanged() {
        mWheelView.computeTextBaseY();
        mWheelView.makeSureScrollVisible();
        mWheelView.refresh();
    }

    abstract public int getValue(int index);
    


    /**
     * 获取当前起始值
     * 
     * @return
     */
    abstract public int getStartValue();

    /**
     * 获取当前结束值
     * 
     * @return
     */
    abstract public int getEndValue();

    /**
     * 获取间隔值
     * 
     * @return
     */
    abstract public int getInterval();

    abstract public int getValueIndex(int value);

    abstract public int getStringValueIndex(String str);
    
    abstract public void setStartValue(int value);

	public int getValue() {
		// TODO Auto-generated method stub
		return 0;
	}
}
