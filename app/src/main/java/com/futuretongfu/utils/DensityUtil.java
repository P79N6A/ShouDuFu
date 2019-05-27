package com.futuretongfu.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DimenRes;

import com.futuretongfu.WeiLaiFuApplication;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class DensityUtil {

    private static Resources sRes = Resources.getSystem();
    private static int sDensityDpi = sRes.getDisplayMetrics().densityDpi;
    private static float sScaledDensity = sRes.getDisplayMetrics().scaledDensity;

    public static int getsDensityDpi(){
        return sDensityDpi;
    }

    public static int dp2px(float value) {
        final float scale = sDensityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

    public static int px2dp(float value) {
        final float scale = sDensityDpi;
        return (int) ((value * 160) / scale + 0.5f);
    }

    public static int sp2px(float value) {
        float spValue = value * sScaledDensity;
        return (int) (spValue + 0.5f);
    }

    public static int px2sp(float value) {
        final float scale = sScaledDensity;
        return (int) (value / scale + 0.5f);
    }

    public static int dimenPixelSize(@DimenRes int id) {
        return WeiLaiFuApplication.getContext().getResources().getDimensionPixelSize(id);
    }

    /**
     * 根据 分辨率为1080*1920  的密度进行转换
     *
     * @param context
     * @param val
     * @return
     */
    public static float deviceAllDPI(Context context, float val) {
        final float density = context.getResources().getDisplayMetrics().density;
        return val * (density / 3);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dpToPx(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
