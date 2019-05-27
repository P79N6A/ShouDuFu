package com.futuretongfu.utils;

import android.os.Handler;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class TimerUtil {

    public static void startTimer(int time, final TimerCallBackListener callBack){
        if(callBack != null){
            callBack.onStart();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(callBack != null){
                    callBack.onEnd();
                }
            }
        }, time);
    }

    public static void startTimer(int time, final TimerCallBackListener2 callBack){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(callBack != null){
                    callBack.onEnd();
                }
            }
        }, time);
    }

    public interface TimerCallBackListener{
        public void onStart();
        public void onEnd();
    }

    public interface TimerCallBackListener2{
        public void onEnd();
    }
}
