package com.futuretongfu.ui.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.HomeIncomeBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * APP 收入
 * Created by zhanggf on 2018/06/22.
 */

public class HomeIncomeDialog extends Dialog {

    @Bind(R.id.title)
    public TextView textTitle;
    @Bind(R.id.bt_confirm)
    TextView btConfirm;
    @Bind(R.id.tv_dialog_income_month)
    TextView tvDialogIncomeMonth;
    @Bind(R.id.tv_dialog_income_day)
    TextView tvDialogIncomeDay;
    private Context context;

    public HomeIncomeDialog(Context context, HomeIncomeBean data, HomeIncomeDialogListener homeIncomeDialogListener) {
        super(context, R.style.dialogWindowAnim);
        this.homeIncomeDialogListener = homeIncomeDialogListener;
        this.context = context;
        setContentView(R.layout.dialog_home_income);
        setCancelable(false);
        setCanceledOnTouchOutside(true);  //设置触摸对话框意外的地方取消对话框
        ButterKnife.bind(this);
        tvDialogIncomeMonth.setText("本月收益：+"+data.getMoneyByMonth());
        tvDialogIncomeDay.setText("当日收益：+"+data.getMoneyByDay());
    }


    @OnClick({R.id.bt_cancel, R.id.layout, R.id.bt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                dismiss();
                break;
            case R.id.layout:
                dismiss();
                break;
            case R.id.bt_confirm:
                if (homeIncomeDialogListener != null) {
                    homeIncomeDialogListener.onHomeIncomeDialog();
                }
                dismiss();
                break;
        }
    }


    private HomeIncomeDialogListener homeIncomeDialogListener;

    public interface HomeIncomeDialogListener {
        public void onHomeIncomeDialog();
    }

}
