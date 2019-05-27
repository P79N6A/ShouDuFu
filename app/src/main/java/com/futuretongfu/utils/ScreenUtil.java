package com.futuretongfu.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.futuretongfu.R;
import com.futuretongfu.WeiLaiFuApplication;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class ScreenUtil {
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return toolbarHeight;
    }

    /**获取屏幕分辨率宽*/
    public static int getScreenWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) WeiLaiFuApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getDisplayMectricsWith(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**获取屏幕分辨率高*/
    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

}
