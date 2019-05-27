package com.futuretongfu.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class KeyboardUtil {

    public static void showKeyboard(Activity activity, boolean isShow){
        if(isShow){
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        else{
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    public static void showKeyboard(Activity activity, View view, boolean isShow){
        if(isShow)
            ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInputFromInputMethod(view.getWindowToken(), 0);
        else
            ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void toggleSoftInput(View view){
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public static void showKeyboard(View view){
        view.requestFocus();
        ((InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view ,InputMethodManager.SHOW_FORCED);
    }
}
