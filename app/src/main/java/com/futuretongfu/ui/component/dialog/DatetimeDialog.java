package com.futuretongfu.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.futuretongfu.ui.component.wheel.DatetimePickerView;
import com.futuretongfu.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ChenXiaoPeng on 2017/6/17.
 */

public class DatetimeDialog extends AbsCustomDialog implements
        View.OnClickListener{
    private DatetimePickerView picker;

    private Button okBtn;

    private Button todayBtn;

    private Button cancelBtn;

    private OnConfirmListener onConfirmListener;

    private Calendar cal = Calendar.getInstance();

    private DatetimePickerView.Type type = DatetimePickerView.Type.DATETIME;

    private Context mContext;

    public DatetimeDialog(Context context, Date date, DatetimePickerView.Type type) {
        super(context);
        mContext = context;
        cal.setTime(date);
        this.type = type;
    }

    public DatetimeDialog(Context context, Date date) {
        this(context, date, DatetimePickerView.Type.DATETIME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        picker = (DatetimePickerView) findViewById(R.id.picker);
        picker.setType(type);
        Calendar calendar = Calendar.getInstance();
        picker.setYearRang(1950, calendar.get(Calendar.YEAR));

        okBtn = (Button) findViewById(R.id.ok_btn);
        todayBtn = (Button) findViewById(R.id.today_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        if (type == DatetimePickerView.Type.TIME) {
            todayBtn.setText("今天");
        }
    }

    @Override
    public void initData() {
        picker.set(cal.getTime());
    }

    @Override
    public void initListener() {
        okBtn.setOnClickListener(this);
        todayBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }


    @Override
    public int getWindowAnimationsResId() {
        // return android.R.style.Animation_Dialog;
        return R.style.BottomDialogAnim;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.view_picker_dialog;
    }

    @Override
    public int getWidth() {
        return android.view.ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getHeight() {
        return android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int getBackgroundDrawableResourceId() {
        // return R.drawable.list_item_bg_gray_first_normal;
        return super.getBackgroundDrawableResourceId();
    }

    @Override
    public boolean getCancelable() {
        return true;
    }

    @Override
    public boolean getCanceledOnTouchOutside() {
        return true;
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
                if (onConfirmListener != null) {
                    onConfirmListener.OnConfirm(picker.getDate(),
                            picker.getDateStr());
                }
                dismiss();
                break;
            case R.id.today_btn:
                picker.set(System.currentTimeMillis());
                break;
            case R.id.cancel_btn:
                dismiss();
                break;

            default:
                break;
        }
    }

    public interface OnConfirmListener {

        void OnConfirm(Date date, String dateStr);
    }

    public void setYearRang(int minYear, int MaxYear){
        picker.setYearRang(minYear, MaxYear);
    }
}
