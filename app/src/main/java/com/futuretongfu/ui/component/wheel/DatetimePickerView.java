package com.futuretongfu.ui.component.wheel;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.utils.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ChenXiaoPeng on 2017/6/17.
 */

public class DatetimePickerView extends LinearLayout implements TosGallery.OnEndFlingListener {
    private static final String DATE_FMT = "yyyy-MM-dd";
    private static final String DATETIME_FMT = "yyyy-MM-dd EE HH:mm";
    private static final String TIME_FMT = "HH:mm";
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat(DATE_FMT);
    private static final SimpleDateFormat SDF_DATETIME = new SimpleDateFormat(DATETIME_FMT);
    private static final SimpleDateFormat SDF_TIME = new SimpleDateFormat(TIME_FMT);
    private int minYear = 1950;
    private int maxYear = 2050;

    ArrayList<TextInfo> mMonths = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mYears = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mDates = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mHours = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mMins = new ArrayList<TextInfo>();

    private WheelView mYearView;
    private WheelView mMonthView;
    private WheelView mDayView;
    private WheelView mHourView;
    private WheelView mMinuteView;

    private TextView mYearLineTv;
    private TextView mMonthLineTv;
    private TextView mDayLineTv;
    private TextView mHourLineTv;
    private TextView mMinuteLineTv;

    private TextView mDatetimeTv;

    private Calendar mCal = Calendar.getInstance();

    private int mYear = mCal.get(Calendar.YEAR);
    private int mMonth = mCal.get(Calendar.MONTH);
    private int mDay = mCal.get(Calendar.DATE);
    private int mHour = mCal.get(Calendar.HOUR_OF_DAY);
    private int mMinute = mCal.get(Calendar.MINUTE);

    Type type = Type.DATETIME;

    /**
     * 显示类型
     *
     * @author hzw 2014-8-15 下午7:19:02
     */
    public enum Type {
        DATETIME, DATE, TIME, TIMETITLE;
    }

    public DatetimePickerView(Context context, int minYear, int maxYear) {
        super(context);
        this.minYear = minYear;
        this.maxYear = maxYear;
        init();
    }

    public DatetimePickerView(Context context) {
        super(context);
        init();
    }

    public DatetimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setType(Type type) {
        if (type == null) {
            type = Type.DATETIME;
        }
        this.type = type;
        init();
    }

    /**
     *
     */
    private void init() {

        mCal.set(Calendar.SECOND, 0);

        LayoutInflater.from(getContext()).inflate(R.layout.view_datetime_picker, this);

        mDatetimeTv = (TextView) findViewById(R.id.picker_datetime_tv);
        mYearView = (WheelView) findViewById(R.id.picker_year);
        mMonthView = (WheelView) findViewById(R.id.picker_month);
        mDayView = (WheelView) findViewById(R.id.picker_day);
        mHourView = (WheelView) findViewById(R.id.picker_hour);
        mMinuteView = (WheelView) findViewById(R.id.picker_mins);

        mYearLineTv = (TextView) findViewById(R.id.picker_year_line_tv);
        mMonthLineTv = (TextView) findViewById(R.id.picker_month_line_tv);
        mDayLineTv = (TextView) findViewById(R.id.picker_day_line_tv);
        mHourLineTv = (TextView) findViewById(R.id.picker_hour_line_tv);
        mMinuteLineTv = (TextView) findViewById(R.id.picker_mins_line_tv);

        mYearView.setOnEndFlingListener(this);
        mMonthView.setOnEndFlingListener(this);
        mDayView.setOnEndFlingListener(this);
        mHourView.setOnEndFlingListener(this);
        mMinuteView.setOnEndFlingListener(this);

        mYearView.setScrollCycle(true);
        mMonthView.setScrollCycle(true);
        mDayView.setScrollCycle(true);
        mHourView.setScrollCycle(true);
        mMinuteView.setScrollCycle(true);

        mYearView.setUnselectedAlpha(0.3f);
        mMonthView.setUnselectedAlpha(0.3f);
        mDayView.setUnselectedAlpha(0.3f);
        mHourView.setUnselectedAlpha(0.3f);
        mMinuteView.setUnselectedAlpha(0.3f);

        switch (type) {
            case DATE:
                mHourView.setVisibility(GONE);
                mHourLineTv.setVisibility(GONE);
                mMinuteView.setVisibility(GONE);
                mMinuteLineTv.setVisibility(GONE);
                break;
            case DATETIME:

                break;
            case TIME:
                mYearView.setVisibility(GONE);
                mYearLineTv.setVisibility(GONE);
                mMonthView.setVisibility(GONE);
                mMonthLineTv.setVisibility(GONE);
                mDayView.setVisibility(GONE);
                mDayLineTv.setVisibility(GONE);
                break;
            case TIMETITLE: {
                mYearView.setVisibility(GONE);
                mYearLineTv.setVisibility(GONE);
                mMonthView.setVisibility(GONE);
                mMonthLineTv.setVisibility(GONE);
                mDayView.setVisibility(GONE);
                mDayLineTv.setVisibility(GONE);
                break;
            }
            default:
                break;
        }

        initData();

    }

    /**
     *
     */
    private void initData() {

        // 年份
        mYearView.setAdapter(new WheelTextAdapter(getContext()));

        // 日
        mDayView.setAdapter(new WheelTextAdapter(getContext()));

        // 月份
        mMonthView.setAdapter(new WheelTextAdapter(getContext()));

        // 小时
        mHourView.setAdapter(new WheelTextAdapter(getContext()));

        // 分钟
        mMinuteView.setAdapter(new WheelTextAdapter(getContext()));

        prepareData();
    }

    /**
     * 设置年份范围
     *
     * @param minYear
     * @param maxYear
     */
    public void setYears(int minYear, int maxYear) {
        this.minYear = minYear;
        this.maxYear = maxYear;
        prepareData();
    }

    public void setYearRang(int minYear, int maxYear){
        this.minYear = minYear;
        this.maxYear = maxYear;

        mMonths.clear();
        mYears.clear();
        mDates.clear();
        mHours.clear();
        mMins.clear();

        initData();
    }

    /**
     * 设置时间
     *
     * @param year
     * @param month
     * @param day
     * @param hourOfDay
     * @param minute
     */
    public void set(int year, int month, int day, int hourOfDay, int minute) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mHour = hourOfDay;
        mMinute = minute;
        mCal.set(year, month, day, hourOfDay, minute);
        prepareData();
    }

    //	/**
//	 * 设置时间固定年
//	 *
//	 * @param year
//	 * @param month
//	 * @param day
//	 * @param hourOfDay
//	 * @param minute
//	 */
    public void set(int hourOfDay, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = hourOfDay;
        mMinute = minute;
        mCal.set(mYear, mMonth, mDay, hourOfDay, minute);
        prepareData();
    }

    /**
     * 设置日期
     *
     * @param year
     * @param month
     * @param day
     */
    public void set(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mCal.set(year, month, day);
        prepareData();
    }

    /**
     * 设置时间
     *
     * @param milliseconds
     */
    public void set(long milliseconds) {
        set(new Date(milliseconds));
    }

    /**
     * 设置时间
     *
     * @param date
     */
    public void set(Date date) {
        mCal.setTime(date);
        mYear = mCal.get(Calendar.YEAR);
        mMonth = mCal.get(Calendar.MONTH);
        mDay = mCal.get(Calendar.DATE);
        mHour = mCal.get(Calendar.HOUR_OF_DAY);
        mMinute = mCal.get(Calendar.MINUTE);
        prepareData();
    }

    /**
     * 获取时间
     *
     * @return
     */
    public Date getDate() {
        return mCal.getTime();
    }

    /**
     * 获取时间字符串，默认格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public String getDateStr() {
        String result = SDF_DATETIME.format(getDate());
        switch (type) {
            case DATE:
                result = SDF_DATE.format(getDate());
                break;
            case DATETIME:
                result = SDF_DATETIME.format(getDate());
                break;
            case TIME:
                result = SDF_TIME.format(getDate());
                break;
            case TIMETITLE: {
                result = SDF_TIME.format(getDate());
                break;
            }
            default:
                break;
        }
        return result;
    }

    /**
     * 获取时间字符串，自定义格式
     *
     * @param format
     * @return
     */
    public String getDateStr(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(getDate());
    }

    @Override
    public void onEndFling(TosGallery v) {
        int position = v.getSelectedItemPosition();
        switch (v.getId()) {
            case R.id.picker_year:
                setYear(mYears.get(position).mIndex);
                break;
            case R.id.picker_month:
                setMonth(mMonths.get(position).mIndex);
                break;
            case R.id.picker_day:
                setDate(mDates.get(position).mIndex);
                break;
            case R.id.picker_hour:
                setHour(mHours.get(position).mIndex);
                break;
            case R.id.picker_mins:
                setMins(mMins.get(position).mIndex);
                break;

            default:
                break;
        }
        if (type == Type.TIMETITLE) {
            mDatetimeTv.setText("运动提醒");
        } else {
            mDatetimeTv.setText(getDateStr());
        }
    }

    private void setMins(int mins) {
        if (mins != mMinute) {
            mMinute = mins;
            mCal.set(Calendar.MINUTE, mins);
        }
    }

    private void setHour(int hour) {
        if (hour != mHour) {
            mHour = hour;
            mCal.set(Calendar.HOUR_OF_DAY, hour);
        }
    }

    private void setDate(int date) {
        if (date != mDay) {
            mDay = date;
            mCal.set(Calendar.DATE, date);
        }
    }

    private void setYear(int year) {
        if (year != mYear) {
            mYear = year;
            mCal.set(Calendar.YEAR, year);
            if (mMonth == 1) {
                prepareDayData();
            }
        }
    }

    private void setMonth(int month) {
        if (month != mMonth) {
            mMonth = month;
            mCal.set(Calendar.MONTH, month);
            prepareDayData();
        }
    }

    private static final int[] DAYS_PER_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    private void prepareData() {
        if (type == Type.TIMETITLE) {
            mDatetimeTv.setText("运动提醒");
        } else {
            mDatetimeTv.setText(getDateStr());
        }

        for (int i = minYear; i <= maxYear; ++i) {
            mYears.add(new TextInfo(i, String.format("%d", i), (i == mYear)));
        }

        for (int i = 1; i <= 12; ++i) {
            mMonths.add(new TextInfo(i - 1, String.format("%02d", i), (i == mMonth)));
        }

        for (int i = 0; i <= 23; ++i) {
            mHours.add(new TextInfo(i, String.format("%02d", i), (i == mHour)));
        }

        for (int i = 0; i <= 59; ++i) {
            mMins.add(new TextInfo(i, String.format("%02d", i), (i == mMinute)));
        }

        ((WheelTextAdapter) mMonthView.getAdapter()).setData(mMonths);
        ((WheelTextAdapter) mYearView.getAdapter()).setData(mYears);
        ((WheelTextAdapter) mHourView.getAdapter()).setData(mHours);
        ((WheelTextAdapter) mMinuteView.getAdapter()).setData(mMins);

        prepareDayData();

        mMonthView.setSelection(mMonth);
        mYearView.setSelection(mYear - minYear);
        mHourView.setSelection(mHour);
        mMinuteView.setSelection(mMinute);
    }

    private void prepareDayData() {
        mDates.clear();

        int days = DAYS_PER_MONTH[mMonth];

        // The February.
        if (1 == mMonth) {
            days = DateUtil.isLeap(mYear) ? 29 : 28;
        }

        for (int i = 1; i <= days; ++i) {
            mDates.add(new TextInfo(i, String.format("%02d", i), (i == mDay)));
        }

        ((WheelTextAdapter) mDayView.getAdapter()).setData(mDates);
        if (mDay > mDates.size()) {
            setDate(mDates.size());
        }
        mDayView.setSelection(mDay - 1);
    }

    protected class TextInfo {
        public TextInfo(int index, String text, boolean isSelected) {
            mIndex = index;
            mText = text;
            mIsSelected = isSelected;

            if (isSelected) {
                mColor = Color.BLACK;
            }
        }

        public int mIndex;
        public String mText;
        public boolean mIsSelected = false;
        public int mColor = Color.BLACK;
    }

    protected class WheelTextAdapter extends BaseAdapter {
        ArrayList<TextInfo> mData = null;
        int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int mHeight = 50;
        Context mContext = null;

        public WheelTextAdapter(Context context) {
            mContext = context;
            mHeight = (int) px2dip(context, mHeight);
        }

        public void setData(ArrayList<TextInfo> data) {
            mData = data;
            this.notifyDataSetChanged();
        }

        public void setItemSize(int width, int height) {
            mWidth = width;
            mHeight = (int) px2dip(mContext, height);
        }

        @Override
        public int getCount() {
            return (null != mData) ? mData.size() : 0;
        }

        private float px2dip(Context context, float val) {
            float density = context.getResources().getDisplayMetrics().density;
            return val * density;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;

            if (null == convertView) {
                convertView = new TextView(mContext);
                convertView.setLayoutParams(new TosGallery.LayoutParams(mWidth, mHeight));
                textView = (TextView) convertView;
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                textView.setTextColor(Color.BLACK);
            }

            if (null == textView) {
                textView = (TextView) convertView;
            }
            TextInfo info = mData.get(position);

            textView.setText(info.mText);
            textView.setTextColor(info.mColor);

            return convertView;
        }
    }
}
