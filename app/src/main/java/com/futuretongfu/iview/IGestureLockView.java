package com.futuretongfu.iview;

/**
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public interface IGestureLockView {

    public void onGestureLockGestureSuccess();
    public void onGestureLockGestureFaile(String msg);

    public void onGestureLockSetSuccess();
    public void onGestureLockSetFaile(String msg);

    public void onGestureLockChangeGestureSuccess();
    public void onGestureLockChangeGestureFaile(String msg);

}
