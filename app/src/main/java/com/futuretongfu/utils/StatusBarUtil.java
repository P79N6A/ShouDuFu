package com.futuretongfu.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.R;

/**
 * Created by ChenXiaoPeng on 2017/6/28.
 */

public class StatusBarUtil {

    public static void setImmersiveStatusBarStoreDetail(@NonNull Activity activity) {
        if (sdkVersionGe(21)) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        if (sdkVersionGe(19)) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        activity.getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            View view = activity.getWindow().getDecorView().findViewById(R.id.view_stroe_title);

            if(null == view)
                return ;

            ViewGroup.MarginLayoutParams viewParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            viewParams.height = WeiLaiFuApplication.statusBarHeight + (int)activity.getResources().getDimension(R.dimen.store_detail_title_height);
            view.setLayoutParams(viewParams);
            view.setPadding(0, WeiLaiFuApplication.statusBarHeight, 0, 0);
            view.requestLayout();

        }
    }

    private static boolean sdkVersionGe(int version) {
        return Build.VERSION.SDK_INT >= version;
    }
}
