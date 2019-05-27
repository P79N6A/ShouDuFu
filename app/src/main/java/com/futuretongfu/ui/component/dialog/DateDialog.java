package com.futuretongfu.ui.component.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bruce.pickerview.LoopScrollListener;
import com.bruce.pickerview.LoopView;
import com.futuretongfu.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/4.
 */

public class DateDialog extends AbsCustomDialog implements View.OnClickListener{

    public TextView btnCancel;
    public TextView btnConfirm;

    private LoopView loopViewYear;
    private LoopView loopViewMonth;

    private Context context;
    private Calendar cal = Calendar.getInstance();

    private int minYear = 1950;
    private int maxYear = 2050;
    private int selectYear;
    private int selectMonth;

    private List<String> listYear = new ArrayList<>();
    private List<String> listMonth = new ArrayList<>();

    public DateDialog(Context context, long time, DateDialogListener dateDialogListener){
        super(context);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        init(context, year, month, dateDialogListener);
    }

    public DateDialog(Context context, int selectYear, int selectMonth, DateDialogListener dateDialogListener) {
        super(context);
        init(context, selectYear, selectMonth, dateDialogListener);
    }

    private void init(Context context, int selectYear, int selectMonth, DateDialogListener dateDialogListener){
        this.context = context;
        this.selectYear = selectYear;
        this.selectMonth = selectMonth;
        this.dateDialogListener = dateDialogListener;

        Calendar cur = Calendar.getInstance();
        cur.setTime(new Date());
        maxYear = cur.get(Calendar.YEAR);
    }

    @Override
    public void initView() {
        btnCancel = (TextView)findViewById(R.id.text_cancel);
        btnConfirm = (TextView)findViewById(R.id.text_confirm);

        loopViewYear = (LoopView)findViewById(R.id.loop_view_year);
        loopViewMonth = (LoopView)findViewById(R.id.loop_view_month);
    }

    @Override
    public void initData() {
        int count = maxYear - minYear + 1;
        for(int i = 0; i < count; i++){
            listYear.add(minYear + i + "");
        }

        for(int i = 1; i < 13; i++){
            listMonth.add(i + "");
        }

        loopViewYear.setDataList(listYear);
        loopViewMonth.setDataList(listMonth);

        loopViewYear.setInitPosition(selectYear - minYear);
        loopViewMonth.setInitPosition(selectMonth - 1);

        loopViewYear.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                selectYear = minYear + item;
            }
        });
        loopViewMonth.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                selectMonth = item + 1;
            }
        });
    }

    @Override
    public void initListener() {
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public int getWindowAnimationsResId() {
        return R.style.BottomDialogAnim;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.view_dialog_date;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_cancel:
                dismiss();
                break;

            case R.id.text_confirm:
                if(dateDialogListener != null)
                    dateDialogListener.onDateDialogConfirm(selectYear, selectMonth);
                dismiss();
                break;
        }
    }

    private DateDialogListener dateDialogListener;
    public interface DateDialogListener{
        public void onDateDialogConfirm(int year, int month);
    }

}
