package com.futuretongfu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.home.PaymentPresenter;
import com.futuretongfu.ui.activity.BillDetailActivity;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.ui.activity.MessageActivity;
import com.futuretongfu.ui.activity.MyBillActivity;
import com.futuretongfu.ui.activity.order.OrderConsumer2Activity;
import com.futuretongfu.ui.activity.order.UploadVocucheListActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.fragment.OrderFragment;
import com.futuretongfu.ui.fragment.PersonFragment;
import com.futuretongfu.utils.AudioUtil;
import com.futuretongfu.utils.ShareUtil;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.utils.TTSUtility;
import com.futuretongfu.utils.To;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    private Bitmap bitmap = null;
    private NotificationManager notifyManager = null;
    private NotificationCompat.Builder notifyBuilder = null;
    private Notification notification = null;
    private String url = "";
    String regId;
    @Override
    public void onReceive(Context context, Intent intent) {

        String content=intent.getStringExtra("msgContent");
        Bundle bundle=intent.getExtras();
        regId = JPushInterface.getRegistrationID(context);
       // SharedPreferencesUtils.
        Log.e(TAG, "广播的id " + regId);
        SharedPreferencesUtils.saveString(context, "regId", regId);
        Log.e(TAG, "onReceive - " + intent.getAction());
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了自定义消息：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            String s = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            // AudioUtils.getInstance().speakText(s);
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了通知");
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
            builder.statusBarDrawable = R.mipmap.app_tuisongicons;
            builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                    | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
            builder.notificationDefaults = Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE
                    | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
            JPushInterface.setPushNotificationBuilder(1, builder);
            //拿到语音开关保存的状态值
            boolean open = SharedPreferencesUtils.getBoolean(context, "open", true);
            //等于true的时候为开，关闭时不执行语音合成状态
            if (open==true){
                printBundles(context, bundle.getString(JPushInterface.EXTRA_EXTRA));

            }else {
                return;

            }
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
             printBundle(context, bundle.getString(JPushInterface.EXTRA_EXTRA));
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }


    }



    // 打印所有的 intent extra 数据
    //打开通知时拿到json字段。判断打开通知时进相对的该页面
    private void printBundle(Context context, String msg) {
        Log.e("我是接收的数据", msg);
        try {
            JSONObject extras = new JSONObject(msg);
            String msgType = extras.getString("msgType");
            if ("AUDIO_ALERT".equals(msgType)) {
                String jumpTo = extras.getString("jumpTo");
                if (jumpTo.equals("TICKET_LIST")) {
                    Intent intent = new Intent(context, UploadVocucheListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (jumpTo.equals("BILL_LIST")) {
                    Intent intent = new Intent(context, MyBillActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else if (jumpTo.equals("ORDER_LIST")) {
                    Intent intent = new Intent(context, OrderConsumer2Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (jumpTo.equals("MY_VIEW")) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("bigMake", 1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            } else if ("NORMAL_ALERT".equals(msgType)) {
                String jumpTo = extras.getString("jumpTo");
                if (jumpTo.equals("TICKET_LIST")) {
                    Intent intent = new Intent(context, UploadVocucheListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (jumpTo.equals("BILL_LIST")) {
                    Intent intent = new Intent(context, MyBillActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (jumpTo.equals("ORDER_LIST")) {
                    Intent intent = new Intent(context, OrderConsumer2Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (jumpTo.equals("MY_VIEW")) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }


            } else if ("JUMP_ALERT".equals(msgType)) {
                String jumpTo = extras.getString("jumpTo");
                if (jumpTo.equals("TICKET_LIST")) {
                    Intent intent = new Intent(context, UploadVocucheListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (jumpTo.equals("BILL_LIST")) {
                    Intent intent = new Intent(context, MyBillActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (jumpTo.equals("ORDER_LIST")) {
                    Intent intent = new Intent(context, OrderConsumer2Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (jumpTo.equals("MY_VIEW")) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // 打印所有的 intent extra 数据
    private void printBundles(Context context, String msg) {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = R.mipmap.app_tuisongicons;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(1, builder);
        Log.e("我是接收的数据", msg);
        try {
            JSONObject extras = new JSONObject(msg);
            String msgType = extras.getString("msgType");
            if ("AUDIO_ALERT".equals(msgType)) {
                // 极光
                JPushInterface.init(context);
                String jumpTo = extras.getString("jumpTo");
                String messages = extras.getString("message");
                if (jumpTo.equals("TICKET_LIST")) {

                    JPushInterface.setPushNotificationBuilder(1, builder);
                    AudioUtil.getInstance(context).setMediaVolume(100);
                    TTSUtility.getInstance(context).speaking(messages);
                } else if (jumpTo.equals("BILL_LIST")) {
                    TTSUtility.getInstance(context).speaking(messages);
                    AudioUtil.getInstance(context).setMediaVolume(100);
                } else if (jumpTo.equals("ORDER_LIST")) {
                    TTSUtility.getInstance(context).speaking(messages);
                    AudioUtil.getInstance(context).setMediaVolume(100);

                } else if (jumpTo.equals("MY_VIEW")) {
                    TTSUtility.getInstance(context).speaking(messages);
                    AudioUtil.getInstance(context).setMediaVolume(100);


                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        notifyManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notifyBuilder = new NotificationCompat.Builder(context);
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        //自定义信息： 获取
        if (extras != null) {
            try {
                JSONObject object = new JSONObject(extras);
                url = object.optString("aaa");
                Log.e(TAG, "xxxxxxx: " + url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Intent i = new Intent(context, LoginActivity.class);
        bundle.putBoolean("push", true);
        bundle.putString("message", regId);
        i.putExtras(bundle);
        PendingIntent pi = PendingIntent.getActivity(context, 1000, i, PendingIntent.FLAG_UPDATE_CURRENT);

        notifyBuilder.setContentTitle(title);
        notifyBuilder.setContentText(message);
        notifyBuilder.setContentIntent(pi);
        notifyBuilder.setAutoCancel(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = returnBitMap(url);
                handler.sendEmptyMessage(1);
            }
        }).start();

        handler.sendEmptyMessage(1);
    }

    //以Bitmap的方式获取一张图片
    public Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (bitmap != null) {
                notifyBuilder.setLargeIcon(bitmap);
            } else {
                notifyBuilder.setSmallIcon(R.mipmap.app_tuisongicons);
            }
            notification = notifyBuilder.build();
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notifyManager.notify(1000, notification);
        }
    };
    public static void customPushNotification(Context context) {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = R.mipmap.app_tuisongicons;//图标文件
        builder.notificationDefaults = Notification.DEFAULT_SOUND;
        JPushInterface.setPushNotificationBuilder(3, builder);

    }


}