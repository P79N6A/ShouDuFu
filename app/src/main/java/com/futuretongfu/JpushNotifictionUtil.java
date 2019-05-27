package com.futuretongfu;

import android.app.Notification;
import android.content.Context;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class JpushNotifictionUtil {

    public static void customPushNotification(Context context) {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
        builder.statusBarDrawable = R.mipmap.app_tuisongicons;//图标文件
        builder.notificationDefaults = Notification.DEFAULT_SOUND;
        JPushInterface.setPushNotificationBuilder(3, builder);

    }

}
