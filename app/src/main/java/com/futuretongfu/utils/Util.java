package com.futuretongfu.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.futuretongfu.utils.decoration.SpacesItemDecoration;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 一些工具方法
 *
 * @author DoneYang 2017/6/14
 */

public class Util {

    private static long lastClickTime;

    /**
     * 生成某个范围内的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    /**
     * 判断对象是否为空
     *
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        if (o == null)
            return true;
        if (o instanceof String)
            return (String.valueOf(o).trim().replace(" ", "").length() == 0);
        else if (o instanceof List)
            return ((List<?>) o).isEmpty();
        else if (o instanceof Map)
            return ((Map<?, ?>) o).isEmpty();
        else if (o instanceof String[])
            return ((String[]) o).length == 0;
        else
            return false;
    }

    /**
     * Get status bar height
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    /**
     * 格式转换
     *
     * @param pattern
     * @param milli
     * @return
     */
    public static String formatTime(String pattern, long milli) {
        int m = (int) (milli / (60 * 1000));
        int s = (int) ((milli / 1000) % 60);
        String mm = String.format("%02d", m);
        String ss = String.format("%02d", s);
        return pattern.replace("m", mm).replace("ss", ss);
    }

    /**
     * 防暴力点击 true则return false则走方法
     *
     * @author DoneYang
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * recycleView设置分割线(默认高度)
     */
    public static void setRecyclerViewLayoutManager(Context context, RecyclerView recyclerView, int color) {
        //这里使用线性布局像listview那样展示列表,第二个参数可以改为 HORIZONTAL实现水平展示
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //设置分割线使用的divider
        int dpiHeight = (int) DensityUtil.deviceAllDPI(context, 18);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0, dpiHeight, ContextCompat.getColor(context, color)));
    }

    /**
     * recycleView设置分割线(默认高度)
     */
    public static LinearLayoutManager setRecyclerViewLayoutManager2(Context context, RecyclerView recyclerView, int color) {
        //这里使用线性布局像listview那样展示列表,第二个参数可以改为 HORIZONTAL实现水平展示
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //设置分割线使用的divider
        int dpiHeight = (int) DensityUtil.deviceAllDPI(context, 18);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0, dpiHeight, ContextCompat.getColor(context, color)));
        return layoutManager;
    }
    /**
     * recycleView设置分割线(默认高度)
     */
    public static void setRecyclerViewGridLayoutManager(Context context, RecyclerView recyclerView, int color,int spanCount) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        GridLayoutManager manager4 = new GridLayoutManager(context, spanCount);
        recyclerView.setLayoutManager(manager4);
        recyclerView.setNestedScrollingEnabled(false);
        manager4.setSmoothScrollbarEnabled(true);
        //设置分割线使用的divider
        int dpiHeight = (int) DensityUtil.deviceAllDPI(context, 18);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0, dpiHeight, ContextCompat.getColor(context, color)));
    }


    /**
     * recycleView设置分割线(自定义高度)
     * VERTICAL
     */
    public static void setRecyclerViewLayoutManager(Context context, RecyclerView recyclerView, int color, int height) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        int dpiHeight = (int) DensityUtil.deviceAllDPI(context, height);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0, dpiHeight, ContextCompat.getColor(context, color)));
    }

    public static LinearLayoutManager setRecyclerViewLayoutManager2(Context context, RecyclerView recyclerView, int color, int height) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        int dpiHeight = (int) DensityUtil.deviceAllDPI(context, height);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0, dpiHeight, ContextCompat.getColor(context, color)));

        return layoutManager;
    }

    /**
     * recycleView设置分割线(自定义高度)
     * HORIZONTAL
     */
    public static void setRecyclerViewHorizontal(Context context, RecyclerView recyclerView, int color, int height) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        int dpiHeight = (int) DensityUtil.deviceAllDPI(context, height);
        recyclerView.addItemDecoration(new SpacesItemDecoration(dpiHeight, 0, ContextCompat.getColor(context, color)));
    }


    public static void setRecyclerViewLayoutManager(Context context, RecyclerView recyclerView, int type, int index, int color) {
        StaggeredGridLayoutManager staggeredGridLayoutManager;
        if (type == 1) {
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(index,
                    StaggeredGridLayoutManager.VERTICAL);
        } else {
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(index,
                    StaggeredGridLayoutManager.HORIZONTAL);
        }
//        staggeredGridLayoutManager.setSmoothScrollbarEnabled(true);
        staggeredGridLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        int dpiHeight = (int) DensityUtil.deviceAllDPI(context, 18);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0, dpiHeight, ContextCompat.getColor(context, color)));

    }

    /**
     * 透明度
     *
     * @param context
     * @param alpha
     */
    public static void setAlpha(Context context, float alpha) {
        WindowManager.LayoutParams layoutParams = ((Activity) context).getWindow().getAttributes();
        layoutParams.alpha = alpha;
        ((Activity) context).getWindow().setAttributes(layoutParams);
    }

    public static void setEnable(EditText editText, final View view) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    view.setEnabled(false);
                } else {
                    view.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int lenth = editable.length();
                if (lenth == 1 && editable.toString().equals("0")) {
                    editable.clear();
                }
            }
        });
    }

    public static void setEnabled(EditText editText, final View view) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    view.setEnabled(false);
                } else {
                    view.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    /**
     * 显示/隐藏
     *
     * @param editText
     * @param view
     */
    public static void setVisibility(final EditText editText, final View view) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    view.setVisibility(View.GONE);
                } else {
                    if (charSequence.toString().equals(" ")) {
                        editText.setText("");
                    }
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 进度百分比
     */
    public static float getPercentage(float base, float coefficient, long currentSize, long totalSize) {
        return base + (float) currentSize / (float) totalSize * coefficient;
    }

    /**
     * 获取设备ID
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(context);
        String id = deviceUuidFactory.getDeviceUuidStr();
        if (!Util.isEmpty(id)) {
            return id;
        }
        return "";
    }

    /**
     * 判断是否是debug模式
     *
     * @param context
     * @return
     */
    public static boolean isDebug(Context context) {
        ApplicationInfo info = context.getApplicationInfo();
        return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }


    /**
     * 是否为纯数字
     * @param str
     * @return
     */
    public static boolean isAllNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher((CharSequence) str);
        return matcher.matches();
    }

    /***************QQ登陆*******************/
    private static Dialog mProgressDialog;
    public static final void showResultDialog(Context context, String msg,
                                              String title) {
        if(msg == null) return;
        String rmsg = msg.replace(",", "\n");
        Log.d("Util", rmsg);
        new AlertDialog.Builder(context).setTitle(title).setMessage(rmsg)
                .setNegativeButton("知道了", null).create().show();
    }

    public static final void showProgressDialog(Context context, String title,
                                                String message) {
        dismissDialog();
        if (TextUtils.isEmpty(title)) {
            title = "请稍候";
        }
        if (TextUtils.isEmpty(message)) {
            message = "正在加载...";
        }
        mProgressDialog = ProgressDialog.show(context, title, message);
    }

    public static final void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }


    /**
     * 初始化popupWindow
     * @param view
     */
    public static PopupWindow menuWindow;

    public static void showPopwindow(final Activity context, View view) {
        menuWindow = new PopupWindow(view, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        menuWindow.setFocusable(true);
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.6f;
        context.getWindow().setAttributes(lp);
        menuWindow.setBackgroundDrawable(new BitmapDrawable());
        menuWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                menuWindow = null;
                WindowManager.LayoutParams lp = context.getWindow().getAttributes();
                lp.alpha = 1f;
                context.getWindow().setAttributes(lp);
            }
        });
    }

}


