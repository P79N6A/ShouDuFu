package com.futuretongfu.utils;

import android.content.ClipboardManager;
import android.content.Context;

import com.futuretongfu.WeiLaiFuApplication;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class ClipboardUtil {

    public static void clip(String text){
        ClipboardManager cmb = (ClipboardManager) WeiLaiFuApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(text.trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
    }

}
