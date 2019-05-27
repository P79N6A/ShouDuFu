package com.futuretongfu.utils;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/10.
 */

public class CacheActivityUtil {

    public static List<Activity> activityList = new LinkedList<Activity>();
    public static List<Activity> activityNewList = new LinkedList<Activity>();
    public CacheActivityUtil() {

    }

    public static boolean isSizeMax2(){
        return activityList.size() > 1;
    }

    /**
     * 添加到Activity容器中
     */
    public static void addActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    /**
     * 添加到Activity容器中
     */
    public static void addNewActivity(Activity activity) {
        if (!activityNewList.contains(activity)) {
            activityNewList.add(activity);
        }
    }

    /**
     * 添加activity并清除之前数据
     * @param activity
     */
    public static void addActivityWithClear(Activity activity){
        activityNewList.clear();
        if (!activityNewList.contains(activity)) {
            activityNewList.add(activity);
        }
    }


    public static void removeActivity(Activity activity) {
        if (activity != null) {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
        }
    }


    /**
     * 遍历所有Activigty并finish
     */
    public static void finishActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }

    public static void finishActivityButLast(){
        for(int i = 0, s = activityList.size(); i < s; i++){
            activityList.get(i).finish();
        }
    }


    /**
     * 遍历所有Activigty并finish
     */
    public static void newFinishActivity() {
        for (Activity activity : activityNewList) {
            activity.finish();
        }
        activityNewList.clear();
    }


    /**
     * 结束指定的Activity
     */
    public static void finishSingleActivity(Activity activity) {
        if (activity != null) {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity 在遍历一个列表的时候不能执行删除操作，所有我们先记住要删除的对象，遍历之后才去删除。
     */
    public static void finishSingleActivityByClass(Class<?> cls) {
        Activity tempActivity = null;
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                tempActivity = activity;
                break;
            }
        }

        finishSingleActivity(tempActivity);
    }

    /**
     *   关闭activityList中cls之后的所有activity
     * */
    public static void finishTail(Class<?> cls){
        List<Activity> activitys = new ArrayList<>();
        for (int i = activityList.size() - 1; i >= 0; i--) {

            if (activityList.get(i).getClass().equals(cls)) {
                break;
            }

            activitys.add(activityList.get(i));

        }

        for(Activity item : activitys){
            finishSingleActivity(item);
        }

    }

}
