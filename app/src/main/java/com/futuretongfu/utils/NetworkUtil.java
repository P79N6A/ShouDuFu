package com.futuretongfu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.futuretongfu.WeiLaiFuApplication;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class NetworkUtil {

    public static boolean isHasNetwork(){
        ConnectivityManager mConnectivityManager = (ConnectivityManager) WeiLaiFuApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null && mNetworkInfo.isAvailable()){   //判断网络连接是否打开
            return  mNetworkInfo.isConnected();
        }

        return false;
    }

    //检查wifi是否连接
    public static boolean isWifiConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm != null){
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().toUpperCase().equals("WIFI") && info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    //获取当前连接wifi的SSID
    public static String getCurConnectWifiSSID(Context context) {
        WifiManager wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifiMgr.getConnectionInfo();

        return info != null ? info.getSSID() : "";
    }

}
