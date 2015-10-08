package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;

public class CouponView implements Serializable{

	@CHTransient
	private static final long serialVersionUID = 1L;
	
	//所有用户
	@CHPrimaryKey()
    private int Id;//优惠券Id
	private int CustomerId;//顾客Id
	private float Value;//优惠券面值
	private boolean Used;//是否已使用
	private int SalesId; //商家(发廊/自由发型师)Id
	private String SalesName; //商家(发廊/自由发型师)name
	private byte SalesRole; //商家(发廊/自由发型师)name
	
	public void setId(int i){
		Id = i;
	}
	
	public int getId(){
		return Id;
	} 
	
	public void setCustomerId(int i){
		CustomerId = i;
	}
	
	public int getCustomerId(){
		return CustomerId;
	} 
	
	public void setSalesName(String i){
		SalesName = i;
	}
	
	public String getSalesName(){
		return SalesName;
	} 
	
	public void setSalesId(int i){
		SalesId = i;
	}
	
	public int getSalesId(){
		return SalesId;
	} 
	
	public void setValue(float i){
		Value = i;
	}
	
	public float getValue(){
		return Value;
	} 
	
	public void setUsed(boolean i){
		Used = i;
	}
	
	public boolean getUsed(){
		return Used;
	} 
	
	public void setSalesRole(byte i){
		SalesRole = i;
	}
	
	public byte getSalesRole(){
		return SalesRole;
	} 
}
