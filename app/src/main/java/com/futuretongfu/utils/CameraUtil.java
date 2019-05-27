package com.futuretongfu.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.futuretongfu.constants.Constants;

import java.io.File;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class CameraUtil {

    /** 请求相册 */
    public static final int Request_Code_Photo = 4;
    /** 请求相机 */
    public static final int Request_Code_Camera = 1;
    /** 请求裁剪 */
    public static final int Request_Code_CutImage = 2;

    private static final String cameraImagePath = Constants.Cache_Dir + "cameraCrash/";
    private static final String cameraImageName = "camera_img_crash.jpg";

    public static String getCameraCrashPath(){
        return cameraImagePath + cameraImageName;
    }

    public static String getCameraImagePath(){
        return cameraImagePath;
    }

    public static String getCameraImageName(){
        return cameraImageName;
    }

    //打开相机
    public static void openCamera(Activity acticity){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断文件夹是否存在,如果不存在则创建文件夹
        FileUtil.isFolderExist(cameraImagePath);
        //调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT
                , Uri.fromFile(new File(
                        cameraImagePath, cameraImageName
                ))
        );

        acticity.startActivityForResult(intent, Request_Code_Camera);
    }

    //打开相册
    public static void openPhoto(Activity acticity){
        Intent data = new Intent(Intent.ACTION_PICK);
        data.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        acticity.startActivityForResult(data, Request_Code_Photo);
    }

    //打开裁剪界面
    public static void openCroper(Activity activity, Uri srcImgUri, int outputX, int outputY){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(srcImgUri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, Request_Code_CutImage);
    }

}
