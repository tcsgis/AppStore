package com.aaa.receiver;

import com.aaa.util.MyTools;
import com.changhong.util.CHLogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver{
      
    @Override  
    public void onReceive(Context context, Intent intent){
        //接收安装广播 
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {   
            String packageName = intent.getDataString();   
            String s = packageName.substring(packageName.lastIndexOf(':') + 1);
            MyTools.refreshInstallState(s);
        }   
    }
}  