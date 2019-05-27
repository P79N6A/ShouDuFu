package com.futuretongfu.listener;

/**
 * 权限结果回调
 *
 * @author DoneYang 2017/6/21
 */

public interface IPermissionListener {

    /**
     * 权限授予
     */
    void onPermissionGranted(String name);

    /**
     * 权限拒绝
     */
    void onPermissionDenied(String name);
}
