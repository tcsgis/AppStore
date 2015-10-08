package com.changhong.activity.widget.other.datepicker;

import com.changhong.util.CollectionUtils;


/**
 * 数字滚轮适配器
 * 
 * @since 2013-12-26
 */
public class WheelNumberWheelAdapter extends WheelAdapter {
    private int startNumber;
    private int endNumber;
    private int interval;
    private String label;

    public WheelNumberWheelAdapter(int startNumber, int endNumber, int interval, String label) {
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.interval = interval;
        this.label = label;
    }

    @Override
    public int getCount() {
        return (endNumber - startNumber) / interval;
    }

    @Override
    public String getItem(int index) {
        int number = startNumber + index * interval;
        if (CollectionUtils.isEmpty(label))
            return String.valueOf(number);
        return number + label;
    }

    /**
     * 设置起始数值
     * 
     * @param startNumber
     */
    public void setStartItem(int startItem) {
        this.startNumber = startItem * this.interval;
        this.notifyChanged();
    }

    /**
     * 获取当前起始数字
     * 
     * @return
     */
    @Override
    public int getStartValue() {
        return this.startNumber;
    }

    /**
     * 获取当前结束数字
     * 
     * @return
     */
    @Override
    public int getEndValue() {
        return this.endNumber;
    }

    /**
     * 获取数字间隔
     * 
     * @return
     */
    @Override
    public int getInterval() {
        return this.interval;
    }

    @Override
    public int getValueIndex(int value) {
        return (value - this.startNumber) / this.interval;
    }

    @Override
    public void setStartValue(int value) {
        this.startNumber = value;
    }

	@Override
	public int getValue(int index) {
		// TODO Auto-generated method stub
		return index * this.interval + this.startNumber;
	}

	@Override
	public int getStringValueIndex(String str) {
		// TODO Auto-generated method stub
		return 0;
	}

}
