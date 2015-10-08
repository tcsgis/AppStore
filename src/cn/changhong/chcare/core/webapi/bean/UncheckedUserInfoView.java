package cn.changhong.chcare.core.webapi.bean;

import java.io.Serializable;

import com.changhong.util.db.annotation.CHPrimaryKey;
import com.changhong.util.db.annotation.CHTransient;

public class UncheckedUserInfoView implements Serializable{
	@CHTransient
	private static final long serialVersionUID = 1L;
	@CHPrimaryKey
	private int Id;						//用户Id
	private String Nick;                  //用户昵称
	private String Photo;                   //用户照片文件Id
    private byte Role;                    //用户身份

    public void setId(int i){
    	Id = i;
    }
    
    public void setNick(String s){
    	Nick = s;
    }
    
    public void setPhoto(String s){
    	Photo = s;
    }
    
    public void setRole(byte s){
    	Role = s;
    }
    
    public int getId(){
    	return Id;
    }
    
    public String getNick(){
    	return Nick;
    }
    
    public String getPhoto(){
    	return Photo;
    }
    
    public byte getRole(){
    	return Role;
    }
}
