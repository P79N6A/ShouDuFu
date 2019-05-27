package com.futuretongfu.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.utils.TimerUtil;

/**
 * Created by ChenXiaoPeng on 2017/6/19.
 */

public class BillScreenDialog extends AbsCustomDialog implements View.OnClickListener{

    public static final int Type_Mall = 0;
    public static final int Type_PaymentBalance = 1;
    public static final int Type_FSort = 2;

    private ImageView imgvClose;

    private TextView textMall;
    private TextView textPaymentBalance;
    private TextView textFSort;

    private int type = Type_PaymentBalance;

    public BillScreenDialog(Context context, BillScreenDialogListener billScreenDialogListener) {
        super(context);
        this.billScreenDialogListener = billScreenDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(){
        imgvClose = (ImageView)findViewById(R.id.imgv_close);
        textMall = (TextView)findViewById(R.id.text_f_mall);
        textPaymentBalance = (TextView)findViewById(R.id.text_payment_balance);
        textFSort = (TextView)findViewById(R.id.text_f_sort);
    }

    @Override
    public void initData() {
        select();
    }

    @Override
    public void initListener(){
        imgvClose.setOnClickListener(this);
        textMall.setOnClickListener(this);
        textPaymentBalance.setOnClickListener(this);
        textFSort.setOnClickListener(this);
    }

    @Override
    public int getWindowAnimationsResId() {
        return R.style.BottomDialogAnim;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.layout_dialog_bill_screen;
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
        return Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    }

    @Override
    public void onClick(View view){
        int id = view.getId();

        switch (id){
            case R.id.imgv_close:
                dismiss();
                break;

            case R.id.text_f_mall:
                type = Type_Mall;
                select();
                TimerUtil.startTimer(200, new TimerUtil.TimerCallBackListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onEnd() {
                        if(billScreenDialogListener != null)
                            billScreenDialogListener.onBillScreenDialogSelecMall();
                        dismiss();
                    }
                });

                break;

            case R.id.text_payment_balance:
                type = Type_PaymentBalance;
                select();

                TimerUtil.startTimer(200, new TimerUtil.TimerCallBackListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onEnd() {
                        if(billScreenDialogListener != null)
                            billScreenDialogListener.onBillScreenDialogPaymentBalance();

                        dismiss();
                    }
                });

                break;

            case R.id.text_f_sort:
                type = Type_FSort;
                select();

                TimerUtil.startTimer(200, new TimerUtil.TimerCallBackListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onEnd() {
                        if(billScreenDialogListener != null)
                            billScreenDialogListener.onBillScreenDialogFSort();

                        dismiss();
                    }
                });

                break;
        }
    }

    private void select(){
        switch (type){
            case Type_Mall:
                textMall.setSelected(true);
                textPaymentBalance.setSelected(false);
                textFSort.setSelected(false);
                break;

            case Type_PaymentBalance:
                textMall.setSelected(false);
                textPaymentBalance.setSelected(true);
                textFSort.setSelected(false);
                break;

            case Type_FSort:
                textMall.setSelected(false);
                textPaymentBalance.setSelected(false);
                textFSort.setSelected(true);
                break;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private BillScreenDialogListener billScreenDialogListener;
    public interface BillScreenDialogListener{
        public void onBillScreenDialogSelecMall();
        public void onBillScreenDialogPaymentBalance();
        public void onBillScreenDialogFSort();
    }
}
