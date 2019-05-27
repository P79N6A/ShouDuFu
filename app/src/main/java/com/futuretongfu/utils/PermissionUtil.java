package com.futuretongfu.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.futuretongfu.utils.Logger.Logger;

/*
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class PermissionUtil {
    private static final int RequestCode_Permission_Location = 2001;
    private static final int RequestCode_Permission_Camera = 2002;
    private static final int RequestCode_Permission_SMS = 2003;
    private static final int RequestCode_Permission_ReadStorage = 2004;
    private static final int RequestCode_Permission_CameraReadStorage = 2005;
    private static final int RequestCode_Permission_CameraStorage = 2006;
    private static final int RequestCode_Permission_PhoneLinkMan = 2007;
    private static final int RequestCode_Permission_Phone = 2008;

    public static final int Permission_Result_Allow = 1;//权限被允许
    public static final int Permission_Result_Refuse = 2;//权限被拒绝
    private static final int Permission_Result_No = 3;//非请求权限

    //定位权限
    public static boolean permissionLocation(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermissionLocation = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
            int checkPermissionCorarse = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

            if (checkPermissionLocation != PackageManager.PERMISSION_GRANTED || checkPermissionCorarse != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity
                        , new String[]
                                {
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                        , Manifest.permission.ACCESS_COARSE_LOCATION
                                }
                        , RequestCode_Permission_Location);
                Logger.d("test", "开始询问定位权限");
                return false;
            } else {
                Logger.d("test", "已经获取定位权限");
                return true;
            }
        } else {
            Logger.d("test", "非6.0版本");
            return true;
        }
    }

    public static boolean isLocationPermission(String permissions[]) {
        return !(permissions == null || permissions.length < 1) && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION);

    }

    //获取定位权限结果
    public static boolean getPermissionLocationResult(String permissions[], int[] grantResults) {
        return permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && permissions[1].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                && grantResults[1] == PackageManager.PERMISSION_GRANTED;

    }

    //相机权限
    public static boolean permissionCamera(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermissionCamera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            if (checkPermissionCamera != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, RequestCode_Permission_Camera);
                return false;
            } else {
                return true;
            }
        }

        return true;
    }

    public static boolean isCarmeraPermission(String permissions[]) {
        return !(permissions == null || permissions.length < 1) && permissions[0].equals(Manifest.permission.CAMERA);

    }

    //获取相机权限限结果
    public static boolean getPermissionCameraResult(String permissions[], int[] grantResults) {
        return permissions[0].equals(Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED;

    }

    //短信接收、读取
    public static boolean permissionSMS(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermissionReadSMS = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS);
            int checkPermissionReceiveSMS = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS);

            if (checkPermissionReadSMS != PackageManager.PERMISSION_GRANTED || checkPermissionReceiveSMS != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity
                        , new String[]
                                {
                                        Manifest.permission.RECEIVE_SMS
                                        , Manifest.permission.READ_SMS
                                        , Manifest.permission.RECEIVE_WAP_PUSH
                                }
                        , RequestCode_Permission_SMS);

                return false;
            }

            return true;
        } else {
            return true;
        }
    }

    //读取手机存储
    public static boolean permissionReadStorage(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermissionCamera = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

            if (checkPermissionCamera != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity
                        , new String[]
                                {
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                }
                        , RequestCode_Permission_ReadStorage);

                return false;
            } else {
                return true;
            }
        }

        return true;
    }

    public static boolean isReadStoragePermission(String permissions[]) {
        return !(permissions == null || permissions.length < 1) && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    //获取手机存储权限限结果
    public static boolean getPermissionReadStorageResult(String permissions[], int[] grantResults) {
        return permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    //相机权限 读取文件
    public static boolean permissionCameraAndReadStorage(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermissionCamera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            int checkPermissionReadStorage = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

            if (checkPermissionCamera != PackageManager.PERMISSION_GRANTED || checkPermissionReadStorage != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity
                        , new String[]
                                {
                                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                                }
                        , RequestCode_Permission_CameraReadStorage);

                return false;
            } else {
                return true;
            }
        }

        return true;
    }

    public static boolean isCarmeraAndReadStoragePermission(String permissions[]) {
        return !(permissions == null || permissions.length < 1) && permissions[0].equals(Manifest.permission.CAMERA) && permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    //获取相机权限限结果
    public static boolean getPermissionCarmeraAndReadStorageResult(String permissions[], int[] grantResults) {
        return permissions[0].equals(Manifest.permission.CAMERA)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                && grantResults[1] == PackageManager.PERMISSION_GRANTED;

    }

    //相机权限 读写文件
    public static boolean permissionCameraAndStorage(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermissionCamera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            int checkPermissionReadStorage = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            int checkPermissionWriteStorage = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (checkPermissionCamera != PackageManager.PERMISSION_GRANTED
                    || checkPermissionReadStorage != PackageManager.PERMISSION_GRANTED
                    || checkPermissionWriteStorage != PackageManager.PERMISSION_GRANTED
                    ) {
                ActivityCompat.requestPermissions(activity
                        , new String[]
                                {
                                        Manifest.permission.CAMERA
                                        , Manifest.permission.READ_EXTERNAL_STORAGE
                                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                }
                        , RequestCode_Permission_CameraStorage);

                return false;
            } else {
                return true;
            }
        }

        return true;
    }

    public static boolean isCarmeraAndStoragePermission(String permissions[]) {
        return !(permissions == null || permissions.length < 3) && permissions[0].equals(Manifest.permission.CAMERA) && permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && permissions[2].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    //获取相机权限限结果
    public static boolean getPermissionCarmeraAndStorageResult(String permissions[], int[] grantResults) {
        return permissions[0].equals(Manifest.permission.CAMERA)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED

                && permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                && grantResults[1] == PackageManager.PERMISSION_GRANTED

                && permissions[2].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && grantResults[2] == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * 是否有手机通讯录权限
     *
     * @param permissions 权限
     * @return 布尔值
     */
    public static boolean isPhoneLinkmanPermission(String permissions[]) {
        return !(permissions == null || permissions.length < 1) && permissions[0].equals(Manifest.permission.READ_CONTACTS) && permissions[1].equals(Manifest.permission.WRITE_CONTACTS);

    }

    /**
     * 手机通讯录权限
     *
     * @param activity 上下文环境
     * @return 布尔值
     */
    public static boolean permissionPhoneLinkman(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int READ_CONTACTS = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
            int WRITE_CONTACTS = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS);

            if (READ_CONTACTS != PackageManager.PERMISSION_GRANTED
                    || WRITE_CONTACTS != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity
                        , new String[]{
                                Manifest.permission.READ_CONTACTS
                                , Manifest.permission.WRITE_CONTACTS
                        }
                        , RequestCode_Permission_PhoneLinkMan);

                return false;
            } else {
                return true;
            }
        }

        return true;
    }

    /**
     * 获取手机通讯录权限结果
     *
     * @param permissions 权限集合
     * @param grantResults *
     * @return 布尔值
     */
    public static boolean getPermissionPhoneLinkmanResult(String permissions[], int[] grantResults) {
        return permissions[0].equals(Manifest.permission.READ_CONTACTS)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED

                && permissions[1].equals(Manifest.permission.WRITE_CONTACTS)
                && grantResults[1] == PackageManager.PERMISSION_GRANTED;

    }

    //电话权限
    public static boolean permissionPhone(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermissionPhone = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
            if (checkPermissionPhone != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        activity
                        , new String[]{Manifest.permission.CALL_PHONE}
                        , RequestCode_Permission_Phone);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    //获取电话权限结果
    public static int getPermissionPhoneResult(int requestCode, String permissions[], int[] grantResults) {
        if (RequestCode_Permission_Phone != requestCode)
            return Permission_Result_No;

        if (permissions == null || permissions.length < 1)
            return Permission_Result_No;

        if (permissions[0].equals(Manifest.permission.CALL_PHONE)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                return Permission_Result_Allow;
            else
                return Permission_Result_Refuse;
        }

        return Permission_Result_No;
    }

}
