package com.changhong.activity.widget.other.datepicker;

import java.util.ArrayList;

/**
 * 数组滚轮适配器
 * 
 */
public class WheelArrayAdapter extends WheelAdapter {
    private String[] data;
    
    private ArrayList<String> dataList;
    
    private boolean isArray=false;

    public WheelArrayAdapter(String[] data) {
        this.data = data;
        this.isArray=true;
    }
    public WheelArrayAdapter(ArrayList<String> data){
    	this.dataList=data;
    	this.isArray=false;
    }

    @Override
    public int getCount() {
    	if(isArray)
    		return this.data.length;
    	else
    		return this.dataList.size();
    }

    @Override
    public String getItem(int index) {
    	if(isArray)
    		return data[index];
    	else
    		return dataList.get(index);
    }

    @Override
    public int getValue(int index) {
        return index;
    }

    /**
     * 获取当前起始数字
     * 
     * @return
     */
    @Override
    public int getStartValue() {
        return 0;
    }

    /**
     * 获取当前结束数字
     * 
     * @return
     */
    @Override
    public int getEndValue() {
        return this.getCount() - 1;
    }

    /**
     * 获取数字间隔
     * 
     * @return
     */
    @Override
    public int getInterval() {
        return 1;
    }

    @Override
    public int getValueIndex(int value) {
        return 0;
    }

    @Override
    public void setStartValue(int value) {
    }

	@Override
	public int getStringValueIndex(String str) {
		int tempNum=0;
		if(isArray){
			for(int i=0;i<this.data.length;i++){
				if (str.equals(this.data[i])){
					tempNum=i;
					break;
				}
			}
		}else{
			for(int j=0;j<this.dataList.size();j++){
				if(str.equals(this.dataList.get(j))){
					tempNum=j;
					break;
				}
			}
		}
		return tempNum;
	}

}
