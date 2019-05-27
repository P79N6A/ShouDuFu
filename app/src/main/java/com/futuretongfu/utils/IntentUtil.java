package com.futuretongfu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.futuretongfu.base.BaseSerializable;


/**
 * 跳转工具类
 *
 * @author DoneYang
 */

public class IntentUtil {

    public static void startActivity(Context context, Class cla) {
        Intent intent = new Intent(context, cla);
        context.startActivity(intent);
    }
    public static void startActivity(Context context, Class cla, String key, String val) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class cla, String key, int val) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class cla, String key, String val, String key1, boolean val1) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class cla, String key, String val, String key1, int val1) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        intent.putExtra(key1, val1);
        context.startActivity(intent);
    }


    public static void startActivity(Context context, Class cla, String key, String val, String key1, String val1 ) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        intent.putExtra(key1, val1);
        context.startActivity(intent);
    }


    public static void startActivity(Context context, Class cla, String key, String val, String key1, String val1,String key2 ) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        intent.putExtra(key1, val1);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class cla, String key, String val, String key1, String val1, String key2, String val2) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        intent.putExtra(key1, val1);
        intent.putExtra(key2, val2);
        context.startActivity(intent);
    }
    public static void startActivity(Context context, Class cla, String key, String val, String key1, String val1, String key2, String val2,String key3,String val3,String key4,String val4) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        intent.putExtra(key1, val1);
        intent.putExtra(key2, val2);
        intent.putExtra(key3, val3);
        intent.putExtra(key4, val4);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class cla, String key, String val, String key1, String val1, String key2, int val2) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        intent.putExtra(key1, val1);
        intent.putExtra(key2, val2);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class cla, String key, BaseSerializable val) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class cla, Bundle bundle) {
        Intent intent = new Intent(context, cla);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }


    public static void startActivityForResult(Activity context, Class cla, int requestCode) {
        Intent intent = new Intent(context, cla);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Activity context, Class cla,
                                              String key, String val, int requestCode) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Activity context, Class cla,
                                              String key, int val, int requestCode) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Activity context, Class cla,
                                              String key, BaseSerializable val, int requestCode) {
        Intent intent = new Intent(context, cla);
        intent.putExtra(key, val);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Activity context, Class cla, Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, cla);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivityForResult(intent, requestCode);
    }


    /**
     * fragment  的跳转
     *
     * @param fragment
     * @param cla
     * @param requestCode
     */

    public static void startActivityForResult(Fragment fragment, Class cla, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), cla);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startActivity(Fragment fragment, Class cla, String key, String val) {
        Intent intent = new Intent(fragment.getContext(), cla);
        intent.putExtra(key, val);
        fragment.startActivity(intent);
    }

    public static void startActivity(Fragment fragment, Class cla, String key, String val, String key1, String val1) {
        Intent intent = new Intent(fragment.getContext(), cla);
        intent.putExtra(key, val);
        fragment.startActivity(intent);
    }

    public static void startActivity(Fragment fragment, Class cla, String key, String val, boolean key1, String val1) {
        Intent intent = new Intent(fragment.getContext(), cla);
        intent.putExtra(key, val);
        fragment.startActivity(intent);
    }

    public static void startActivity(Fragment fragment, Class cla, String key, boolean val) {
        Intent intent = new Intent(fragment.getContext(), cla);
        intent.putExtra(key, val);
        fragment.startActivity(intent);
    }

    public static void startActivityForResult(Fragment fragment, Class cla,
                                              String key, String val, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), cla);
        intent.putExtra(key, val);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Class cla,
                                              String key, BaseSerializable val, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), cla);
        intent.putExtra(key, val);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Class cla, Bundle bundle, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), cla);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        fragment.startActivityForResult(intent, requestCode);
    }


    /**
     * 返回上一层，值返回
     *
     * @param activity
     * @param key
     * @param val
     * @param resultCode
     */
    public static void setResult(Activity activity, String key, String val, int resultCode) {
        Intent intent = new Intent();
        intent.putExtra(key, val);
        activity.setResult(resultCode, intent);
    }


}
