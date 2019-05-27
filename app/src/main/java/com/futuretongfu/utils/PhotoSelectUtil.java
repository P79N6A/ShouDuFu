package com.futuretongfu.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.futuretongfu.constants.Constants;

import java.io.File;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by ChenXiaoPeng on 2017/7/7.
 */

public class PhotoSelectUtil {

    /**
     * 请求相机
     */
    public static final int Request_Code_Camera = 1;
    /**
     * 请求相册
     */
    public static final int Request_Code_Photo = 2;
    /**
     * 请求裁剪
     */
    public static final int Request_Code_CropImage = 3;

    private static final String cameraImagePath = Constants.Cache_Dir + "cameraCrash/";
    private static final String cameraImageName = "camera_img_crash.jpg";
    private static final String cropImageName = "crop_img_crash.jpg";
    private static final String wlfProvider = "com.futuretongfu.WlfProvider";

    public static String getCameraImagePath() {
        return cameraImagePath;
    }

    public static String getCameraImageName() {
        return cameraImageName;
    }

    public static String getCropImageName() {
        return cropImageName;
    }

    public static String getCameraImageAllPath() {
        return cameraImagePath + cameraImageName;
    }

    /**
     * 打开相机
     */
    public static void openCamre(Context context) {
        ((Activity) context).startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                , Request_Code_Camera);
    }

    /**
     * 打开相机
     */
    public static void openCamre(Context context, File file) {
//        photo_image = createImagePath(APP_NAME + DATE);
//        File file = new File(photo_image);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Android7.0以上URI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider创建一个content类型的Uri
            Uri uri = FileProvider.getUriForFile(context, wlfProvider, file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }

        try {
            ((Activity) context).startActivityForResult(intent, Request_Code_Camera);
        } catch (ActivityNotFoundException anf) {
            To.s("异常"+anf);
        }

    }

    /**
     * 打开相册
     */
    public static void openPhoto(Context context) {
        ((Activity) context).startActivityForResult(
                new Intent(
                        Intent.ACTION_PICK
                        , MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                , Request_Code_Photo);
    }

    /**
     * 调用系统剪裁功能
     */
    public static void cropPicture(Activity activity, File srcFile, File dstFile) {

        Uri imageUri;
        Uri outputUri;

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(activity, wlfProvider, srcFile);
            outputUri = Uri.fromFile(dstFile);
        } else {
            imageUri = Uri.fromFile(srcFile);
            outputUri = Uri.fromFile(dstFile);
        }

        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");

        //设置宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        //设置裁剪图片宽高
        intent.putExtra("outputX", 240);
        intent.putExtra("outputY", 240);

        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);

        activity.startActivityForResult(intent, Request_Code_CropImage);
    }

    /**
     * 调用系统剪裁功能
     */
    public static void cropPicture2(Activity activity, File srcFile, File dstFile) {

        Uri imageUri;
        Uri outputUri;

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(activity, wlfProvider, srcFile);
            outputUri = Uri.fromFile(dstFile);
        } else {
            imageUri = Uri.fromFile(srcFile);
            outputUri = Uri.fromFile(dstFile);
        }

        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");

        //设置宽高比例
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 9);

        //设置裁剪图片宽高
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 450);

        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);

        activity.startActivityForResult(intent, Request_Code_CropImage);
    }

    /**
     * 打开单选图片选择器  PhotoPicker
     */
    public static void openImageSingleSelector(Activity activity) {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(false)
                .start(activity, PhotoPicker.REQUEST_CODE);
    }

}
