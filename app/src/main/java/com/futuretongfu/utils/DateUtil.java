package com.futuretongfu.utils;

import android.text.TextUtils;
import android.widget.TextView;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ChenXiaoPeng on 2017/6/17.
 */

public class DateUtil {
    /**
     * 判断是否闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeap(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    public static String getMoTime(long time){
        return getChineseTime(time);
    }

    public static String getChineseTime(long time) {
        Date date = new Date(time);

        int yearCount = DateUtils.toCalendar(new Date()).get(Calendar.YEAR) - DateUtils.toCalendar(date).get(Calendar
                .YEAR);
        if (yearCount == 0) {
            int mouthCount = DateUtils.toCalendar(new Date()).get(Calendar.MONTH) - DateUtils.toCalendar(date).get
                    (Calendar.MONTH);
            if (mouthCount == 0) {
                int dayCount = DateUtils.toCalendar(new Date()).get(Calendar.DATE) - DateUtils.toCalendar(date).get
                        (Calendar.DATE);
                if (dayCount == 0) {
                    int hourCount = DateUtils.toCalendar(new Date()).get(Calendar.HOUR) - DateUtils.toCalendar(date)
                            .get(Calendar.HOUR);
                    if (hourCount == 0)
                        return "刚刚";
                    else if (hourCount == 1)
                        return "一小时前";
                    else if (hourCount == 2)
                        return "两小时前";
                    else
                        return "今天";
                } else if (dayCount == 1) {
                    return "昨天";
                } else if (dayCount < 32) {
                    return dayCount + "天前";
                } else
                    return DateFormatUtils.format(time, "yyyy-MM-dd");
            } else
                return mouthCount + "个月前";
        } else
            return DateFormatUtils.format(time, "yyyy-MM-dd");
    }

    public static String getOrderTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long myTime = new Long(time);
        String d = format.format(myTime);
        return d;
    }

    public static String getY4M2D2Time(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Long myTime = new Long(time);
        String d = format.format(myTime);
        return d;
    }

    public static String getY4M2D2Time(){
        Date date = new Date();
        return DateFormatUtils.format(date.getTime(), "yyyy-MM-dd");
    }


    public static String getUpStorePicTime(long time){
        return DateFormatUtils.format(time, "yyyyMMdd");
    }
    public static String getTime(long time){
        return DateFormatUtils.format(time, "yyyyMM");
    }
    public static String getCurTime(){
        Date date = new Date();
        return date.getTime() + "";
    }

    //Wed -> 周三
    public static String getWeekStr(String week){
        if(week == null || TextUtils.isEmpty(week))
            return "";

        String str = "";

        switch (week){
            case "Mon":
                str = "周一";
                break;
            case "Tue":
                str = "周二";
                break;
            case "Wed":
                str = "周三";
                break;
            case "Thu":
                str = "周四";
                break;
            case "Fri":
                str = "周五";
                break;
            case "Sat":
                str = "周六";
                break;
            case "Sun":
                str = "周日";
                break;
            default:
                break;
        }

        return str;
    }

    public static String getDateStrWithHour1(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Long myTime = new Long(time);
        String d = format.format(myTime);
        return d;
    }

    public static String getDateStrWithHour2(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Long myTime = new Long(time);
        String d = format.format(myTime);
        return d;
    }

    public static String getDateStrWithHour3(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Long myTime = new Long(time);
        String d = format.format(myTime);
        return d;
    }

    //yyyy-MM-dd HH:mm
    public static String getDateStr1(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Long myTime = new Long(time);
        String d = format.format(myTime);
        return d;
    }

    //yyyy-MM-dd
    public static String getDateStr2(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Long myTime = new Long(time);
        String d = format.format(myTime);
        return d;
    }

    //yyyy-MM-dd HH:mm:ss
    public static String getDateStr3(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long myTime = new Long(time);
        String d = format.format(myTime);
        return d;
    }

    public static boolean isTodayOrBefore(long time){
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);
        long todayTime = today.getTimeInMillis();

        if(time <= todayTime)
            return true;
        return false;
    }

    //20170523 -> 星期几
    public static String getWeekFrmSteDate(String data){

        int year = Integer.valueOf(data.substring(0, 4));
        int month = Integer.valueOf(data.substring(4, 6));
        int day = Integer.valueOf(data.substring(6, 8));

        Calendar weekData = Calendar.getInstance();
        weekData.set(Calendar.YEAR, year);
        weekData.set(Calendar.MONTH, month - 1);
        weekData.set(Calendar.DAY_OF_MONTH, day);

        int week = weekData.get(Calendar.DAY_OF_WEEK);
        switch (week){
            case Calendar.MONDAY:
                return "星期一";

            case Calendar.TUESDAY:
                return "星期二";

            case Calendar.WEDNESDAY:
                return "星期三";

            case Calendar.THURSDAY:
                return "星期四";

            case Calendar.FRIDAY:
                return "星期五";

            case Calendar.SATURDAY:
                return "星期六";

            case Calendar.SUNDAY:
                return "星期日";
        }

        return "";
    }

    /**
     *   获取年份
     * */
    public static int getYear(long time){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));

        return cal.get(Calendar.YEAR);
    }

    /**
     *   获取月份 从0开始
     * */
    public static int getMonth(long time){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));

        return cal.get(Calendar.MONTH);
    }

    /**
     *   获取当前时间戳
     * */
    public static long getCurTimeInMillis(){
        Calendar today = Calendar.getInstance();
        return today.getTimeInMillis();
    }

    /**
     *   获取指定时间戳
     * */
    public static long getTimeInMillis(int year, int month){
        Calendar time = Calendar.getInstance();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month);
        return time.getTimeInMillis();
    }

    public static String getMonthMM(int month){
        if(month < 10)
            return "0" + month;

        return "" + month;
    }


    public static void setDateInfo(long addTime,TextView textView) {
        try {
            Date currDate = new Date(System.currentTimeMillis());
            Date endDate = new Date(addTime);
            long diff = currDate.getTime() - endDate.getTime(); // 得到的差值是微秒级别，可以忽略
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
            // 0 代表前面补充0 2 代表长度为2  d 代表参数为正数型
            textView.setText("剩余 "+String.format("%02d:%02d:%02d",(24 - (hours + 1)),(60 - (minutes + 1)),(60 - (seconds + 1)))+" 将自动关闭");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String format(long ms) {//将毫秒数换算成x天x时x分x秒x毫秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;
        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
//        return strDay + " " + strHour + ":" + strMinute + ":" + strSecond + " " + strMilliSecond;
        return strHour + ":" + strMinute + ":" + strSecond;
    }

    public static boolean isDifferTime(long date1,long date2){
        long cha = date1 - date2;
        double result = cha * 1.0 / (1000 * 60 * 60);
        if(result<=24){
            return false; //说明小于24小时
        }else{
            return true;
        }
    }
}
