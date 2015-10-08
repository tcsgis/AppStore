package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;

public class OfferBiddingView implements Serializable{

//	    Price:{decimal},                            //价格

	@CHTransient
	private static final long serialVersionUID = 1L;
	
	//所有用户
	@CHPrimaryKey()
    private int Id;//竞价参与信息 Id
	private int OfferId;//原竞价Offer Id
	private int BarberId;//参与竞价的自由发型师Id
	private float Price;//价格
	private String Name;
	private String Photo;
	
	public void setId(int i){
		Id = i;
	}
	
	public int getId(){
		return Id;
	}
	
	public void setOfferId(int i){
		OfferId = i;
	}
	
	public int getOfferId(){
		return OfferId;
	}
	
	public void setBarberId(int i){
		BarberId = i;
	}
	
	public int getBarberId(){
		return BarberId;
	}
	
	public void setPrice(float i){
		Price = i;
	}
	
	public float getPrice(){
		return Price;
	}
	
	public void setName(String i){
		Name = i;
	}
	
	public String getName(){
		return Name;
	}
	
	public void setPhoto(String i){
		Photo = i;
	}
	
	public String getPhoto(){
		return Photo;
	}
}
