package com.futuretongfu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.utils.Logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by aa on 2016/4/8.
 * 一些小算法和小功能集合
 */
public class SmallFeatureUtils {

    /**
     * 中文姓名验证
     * 2~5个中文姓名
     * 如果需要考虑支持少数民族的人名，或者外国人的中译名，例如：阿沛·阿旺晋美、卡尔·马克思等，
     *
     * @param text
     * @return
     */
    public static boolean isChineseName(String text) {
        if (!TextUtils.isEmpty(text)) {
            Pattern p = Pattern.compile("[\\u4E00-\\u9FA5]{2,7}(?:·[\\u4E00-\\u9FA5]{2,7})*");
            Matcher m = p.matcher(text);
            return m.matches();
        } else {
            return false;
        }
    }

    public static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return false;
    }

    /**
     * 传入时间戳
     *
     * @param longTim
     * @return
     */
    public static String getYMD(String longTim) {
        long tempTimLong = System.currentTimeMillis() / 1000;
        long timestam = tempTimLong;
        try {
            timestam = Long.parseLong(longTim);
        } catch (NumberFormatException e) {
            timestam = tempTimLong;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date(timestam * 1000));

        return date;
    }


    /**
     * 传时间戳 秒
     * 转换后的格式
     *
     * @param timeStr
     * @return
     */
    public static String getTimeYMDHMStr(String timeStr, String form) {

        long timestam = 0;
        try {
            timestam = Long.parseLong(timeStr);
        } catch (NumberFormatException e) {
            timestam = System.currentTimeMillis()/1000;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(form);
        String date = simpleDateFormat.format(new Date(timestam * 1000));
        return date;
    }


    /* 将字符串转为时间戳  返回的是秒*/
    public static long getStringToDate(String time, String form) {
        SimpleDateFormat sf = new SimpleDateFormat(form);
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (Exception e) {
            return System.currentTimeMillis() / 1000;
        }
        return date.getTime() / 1000;
    }

    /**
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 获取圆角位图的方法
     *
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    public static int getWindowWith() {
        WindowManager wm = (WindowManager) WeiLaiFuApplication.getInstance()
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }
    public static int getWindowHeight() {
        WindowManager wm = (WindowManager) WeiLaiFuApplication.getInstance()
                .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    /**
     * 获取版本名字
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String version = null;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            version = null;
        }
        return version;
    }


    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {   //判断网络连接是否打开
            return mNetworkInfo.isConnected();
        }
        return false;
    }


    /**
     * 关闭输入法
     */
    public static void closeEditer(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void setScreenDetailsImgSize(ImageView imageView) {
        int size = getWindowWith();
        /**
         * 设置图片宽高
         */
        ViewGroup.LayoutParams imgparms = imageView.getLayoutParams();
        imgparms.height = size * 1 / 2;
        imgparms.width = size;
        imageView.setLayoutParams(imgparms);
    }

    public static void setScreenImgSize(View imageView, int height) {
        int size = getWindowWith();
        /**
         * 设置图片宽高
         */
        ViewGroup.LayoutParams imgparms = imageView.getLayoutParams();
        imgparms.height = height;
        imgparms.width = size;
        imageView.setLayoutParams(imgparms);
    }

    public static float getDensityDpi(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        float m = width / density;
        return density;
    }

    public static String getIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        int[] ipAddr = new int[4];
        ipAddr[0] = ipAddress & 0xFF;
        ipAddr[1] = (ipAddress >> 8) & 0xFF;
        ipAddr[2] = (ipAddress >> 16) & 0xFF;
        ipAddr[3] = (ipAddress >> 24) & 0xFF;
        return new StringBuilder().append(ipAddr[0]).append(".").append(ipAddr[1]).append(".").append(ipAddr[2])
                .append(".").append(ipAddr[3]).toString();

    }


    //生成圆角图片
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            final float roundPx = 8;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
//        Meizu


//        if (Build.VERSION.SDK_INT >= 20) {
            String phoneType = Build.BRAND;

            String systemNum = Build.MODEL;
            if (phoneType.equals("OPPO")) {//5.1
                result = 20;
            }
            else {
                int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = context.getResources().getDimensionPixelSize(resourceId);
                }
            }


        return result;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */

    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 大图片处理机制
     * 利用Bitmap 转存 R图片
     */
    public static Bitmap btp;

    public static void getBitmapForImgResourse(Context mContext, int imgId, ImageView mImageView) {
        InputStream is = mContext.getResources().openRawResource(imgId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = 1;
        btp = BitmapFactory.decodeStream(is, null, options);
        mImageView.setImageBitmap(btp);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime(100);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.FULL_SCREEN_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
    /**
     * Flag只有在使用了FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
     * <p>
     * 并且没有使用 FLAG_TRANSLUCENT_STATUS的时候才有效，也就是只有在状态栏全透明的时候才有效。
     *
     * @param activity
     * @param bDark
     */

    public static void setStatusBarMode(Activity activity, boolean bDark) {

        //6.0以上

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            View decorView = activity.getWindow().getDecorView();

            if (decorView != null) {

                int vis = decorView.getSystemUiVisibility();

                if (bDark) {

                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

                } else {

                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

                }

                decorView.setSystemUiVisibility(vis);

            }
        }
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)){
            return false;
        } else{
            return mobiles.matches(telRegex);
        }
    }
    /**
     * 保留两位小数
     * @param value
     * @return
     */
    public static String formatFloatNumber1(Double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        if(value != null){
            if(value.doubleValue() != 0){

                return String.valueOf(df.format(value));
            }else{
                return "0.00";
            }
        }
        return "0.00";
    }

    public static boolean verifyIDCardNumber(String cardNumStr){

        if (cardNumStr.length()!=18){
            return false;
        }else {
            // 正则表达式判断基本 身份证号是否满足格式
            String regexRule ="^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
            boolean isMatch = Pattern.matches(regexRule, cardNumStr);
            if (!isMatch){
                return false;
            }
            //** 开始进行校验 *//
            //将前17位加权因子保存在数组里
            String[] idCardWiArray={"7","9","10","5","8","4","2","1","6","3","7","9","10","5","8", "4","2"};

            //这是除以11后，可能产生的11位余数、验证码，也保存成数组
            String[] idCardYArray={"1","0","10","9","8","7","6","5","4","3","2"};

            //用来保存前17位各自乖以加权因子后的总和
            int idCardWiSum = 0;
            for(int i = 0;i < 17;i++) {
                int subStrIndex = Integer.parseInt(cardNumStr.substring(i,17));
                int idCardWiIndex = Integer.parseInt(idCardWiArray[i]);
                idCardWiSum+= subStrIndex * idCardWiIndex;

            }
            //计算出校验码所在数组的位置
            int idCardMod=idCardWiSum%11;
            //得到最后一位身份证号码
            String idCardLast= cardNumStr.substring(17);

            //如果等于2，则说明校验码是10，身份证号码最后一位应该是X
            if(idCardMod==2) {
                if(!idCardLast.equals("X")||idCardLast.equals("x")) {
                    return false;
                }
            }
            else{
                //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
                if(!idCardLast.equals(idCardYArray[idCardMod])) {
                    return false;
                }
            }

        }


        return true;
    }
    //打电话
    public static void callPhone(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + number);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param image （根据Bitmap图片压缩）
     * @return
     */
    public static Bitmap compressScale(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        // float hh = 800f;// 这里设置高度为800f
        // float ww = 480f;// 这里设置宽度为480f
        float hh = 512f;
        float ww = 512f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be; // 设置缩放比例
        // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565

        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
        //return bitmap;
    }
    public static BitmapFactory.Options getBitmapOption(int inSampleSize){
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


}