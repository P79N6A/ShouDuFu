package com.futuretongfu.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.futuretongfu.utils.Logger.Logger;

/**
 * @author DoneYang 2017/6/30
 */

public class AMapLocation implements AMapLocationListener {
    public double mLongitude = 0.0;//经 度
    public double mLatitude = 0.0;//纬 度
    public String mCity = null;//城市
    public MyLocationListener locationListener;
    public AMapLocationClientOption mOption;
    public AMapLocationClient mLocationClient;
    private Context mContext;
    public interface MyLocationListener {
        void myLocationListener(double longitude, double latitude, String city);
    }

    /**
     * 默认的定位参数
     */
    public void getDefaultOption(Context context) {
        mContext = context;
        if (mOption == null) {
            mOption = new AMapLocationClientOption();
        }
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(context);
        }
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(180000);//可选，设置定位间隔。180秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mLocationClient.setLocationOption(mOption);
        mLocationClient.setLocationListener(this);
        mLocationClient.startLocation();
    }

    public void setLocationListener(MyLocationListener locationListener) {
        this.locationListener = locationListener;
    }

    @Override
    public void onLocationChanged(com.amap.api.location.AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {
            mLongitude = aMapLocation.getLongitude();
            mLatitude = aMapLocation.getLatitude();
            mCity = aMapLocation.getCity();
            SharedPreferencesUtils.saveString(mContext,"mLongitude",mLongitude+"");
            SharedPreferencesUtils.saveString(mContext,"mLatitude",mLatitude+"");
            SharedPreferencesUtils.saveString(mContext,"mCity",mCity+"");
            Logger.i("高德定位", "城市=" + mCity + ",经度=" + mLongitude + ",纬度=" + mLatitude);
            if (mLatitude != 0 && mLongitude != 0) {
                if (locationListener != null) {
                    locationListener.myLocationListener(mLongitude, mLatitude, mCity);
                }
                mLocationClient.stopLocation();
            }
        } else {
            //To.s("定位失败");
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            Logger.e("AmapError", "location Error, ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());
        }
//        mLocationClient.startLocation();
    }

    /**
     * 消除定位
     */
    public void unRegisterMap() {
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(this);
            mLocationClient = null;
        }
        if (mOption != null) {
            mOption = null;
        }
    }
}
