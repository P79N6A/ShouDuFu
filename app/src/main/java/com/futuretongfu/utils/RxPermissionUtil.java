package com.futuretongfu.utils;

import android.Manifest;

import com.futuretongfu.listener.IPermissionListener;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Observable;
import rx.Subscriber;

/**
 * Rx方式获取权限
 *
 * @author DoneYang 2017/6/21
 */

public class RxPermissionUtil {

    public static void requestPermissionParent(Observable<Boolean> observable,
                                               final IPermissionListener listener) {

        observable.subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean granted) {
                if (granted) {
                    // 所有权限请求被同意
                    listener.onPermissionGranted(null);
                } else {
                    // 至少有一个权限没同意
                    listener.onPermissionDenied(null);
                }
            }
        });
    }

    /**
     * 请求权限  多个结果单独处理
     *
     * @param observable
     * @param listener
     */
    public static void requestEachPermissionParent(Observable<Permission> observable, final IPermissionListener listener) {
        observable.subscribe(new Subscriber<Permission>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Permission permission) {
                if (permission.granted) {
                    // 'permission.name' is granted !
                    listener.onPermissionGranted(permission.name);
                } else {
                    listener.onPermissionDenied(permission.name);
                }
            }
        });
    }

    /**
     * 请求权限
     *
     * @param activity
     * @param permissions
     * @param listener    回调
     * @param permission  可以多个权限，但只要有一个不成功就不成功
     */
    public static void requestPermission(BaseActivity activity, RxPermissions permissions,
                                         final IPermissionListener listener, String... permission) {
        Observable<Boolean> observable = permissions.request(permission).
                compose(activity.<Boolean>bindToLifecycle());
        requestPermissionParent(observable, listener);
    }


//    /**
//     * 请求权限
//     * @param activity
//     * @param permissions
//     * @param listener    回调
//     * @param permission  可以多个权限，但只要有一个不成功就不成功
//     */
//    public static void requestPermission(RxBaseActivity activity, RxPermissions permissions,
//                                         final IPermissionListener listener, String... permission) {
//        Observable<Boolean> observable= permissions.request(permission).
//                compose(activity.<Boolean>bindToLifecycle());
//        requestPermissionParent(observable,listener);
//    }


    /**
     * 请求权限
     *
     * @param fragment
     * @param permissions
     * @param listener    回调
     * @param permission  可以多个权限，但只要有一个不成功就不成功
     */
    public static void requestPermission(BaseFragment fragment, RxPermissions permissions,
                                         final IPermissionListener listener, String... permission) {
        Observable<Boolean> observable = permissions.request(permission).
                compose(fragment.<Boolean>bindToLifecycle());
        requestPermissionParent(observable, listener);
    }


    /**
     * 请求权限
     *
     * @param activity
     * @param permissions
     * @param listener    回调
     * @param permission  可以多个权限，多个结果单独处理
     */
    public static void requestEachPermission(BaseActivity activity, RxPermissions permissions,
                                             final IPermissionListener listener, String... permission) {
        Observable<Permission> observable = permissions.requestEach(permission).
                compose(activity.<Permission>bindToLifecycle());
        requestEachPermissionParent(observable, listener);
    }


//    /**
//     * 请求权限
//     * @param activity
//     * @param permissions
//     * @param listener    回调
//     * @param permission  可以多个权限，多个结果单独处理
//     */
//    public static void requestEachPermission(RxBaseActivity activity, RxPermissions permissions,
//                                             final IPermissionListener listener, String... permission) {
//        Observable<Permission> observable= permissions.requestEach(permission).
//                compose(activity.<Permission>bindToLifecycle());
//        requestEachPermissionParent(observable,listener);
//    }


    /**
     * 请求权限
     *
     * @param fragment
     * @param permissions
     * @param listener    回调
     * @param permission  可以多个权限，多个结果单独处理
     */
    public static void requestEachPermission(BaseFragment fragment, RxPermissions permissions,
                                             final IPermissionListener listener, String... permission) {
        Observable<Permission> observable = permissions.requestEach(permission).
                compose(fragment.<Permission>bindToLifecycle());
        requestEachPermissionParent(observable, listener);


    }


    /**
     * 请求权限 录音
     *
     * @param activity
     * @param permissions
     * @param listener    回调
     */
    public static void requestRecordAudioPermission(BaseActivity activity, RxPermissions permissions,
                                                    final IPermissionListener listener) {
        requestPermission(activity, permissions, listener, Manifest.permission.RECORD_AUDIO);
    }

    /**
     * 请求权限 录音
     *
     * @param fragment
     * @param permissions
     * @param listener    回调
     */
    public static void requestRecordAudioPermission(BaseFragment fragment, RxPermissions permissions,
                                                    final IPermissionListener listener) {
        requestPermission(fragment, permissions, listener, Manifest.permission.RECORD_AUDIO);
    }

    /**
     * 请求权限 照相
     *
     * @param activity
     * @param permissions
     * @param listener    回调
     */
    public static void requestCameraPermission(BaseActivity activity, RxPermissions permissions,
                                               final IPermissionListener listener) {
        requestPermission(activity, permissions, listener, Manifest.permission.CAMERA);
    }

    /**
     * 请求权限 照相
     *
     * @param fragment
     * @param permissions
     * @param listener    回调
     */
    public static void requestCameraPermission(BaseFragment fragment, RxPermissions permissions,
                                               final IPermissionListener listener) {
        requestPermission(fragment, permissions, listener, Manifest.permission.CAMERA);
    }

}
