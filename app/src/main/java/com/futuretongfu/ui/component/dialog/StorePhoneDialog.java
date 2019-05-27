package com.futuretongfu.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.futuretongfu.R;

/**
 * 商家详情电话
 */

public class StorePhoneDialog extends AbsCustomDialog implements View.OnClickListener {

    private TextView textCall;
    private TextView textPhone;
    private TextView btnCancel;
    private String callNumber;
    private String telNumber;

    private OnPhoneConfirmListener onConfirmListener;

    public StorePhoneDialog(Context context, String tel, String call) {
        super(context);
        callNumber = call;
        telNumber = tel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        textCall = (TextView) findViewById(R.id.text_call);
        textPhone = (TextView) findViewById(R.id.text_tel);
        btnCancel = (TextView) findViewById(R.id.text_cancel);
        textCall.setText(callNumber);
        textPhone.setText(telNumber);
    }

    @Override
    public void initData() {

    }

    public void setData(String tel, String call){
        textCall.setText(call);
        textPhone.setText(tel);
    }

    @Override
    public void initListener() {
        textCall.setOnClickListener(this);
        textPhone.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public void showDialog (){
        show();
    }


    @Override
    public int getWindowAnimationsResId() {
        return R.style.BottomDialogAnim;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.dialog_store_phone;
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

    public void setOnPhoneConfirmListener(OnPhoneConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_tel:
                if (onConfirmListener != null) {
                    onConfirmListener.onSelectorTel();
                }
                dismiss();
                break;

            case R.id.text_call:
                if (onConfirmListener != null) {
                    onConfirmListener.onSelectorCall();
                }
                dismiss();
                break;

            case R.id.text_cancel:
                dismiss();
                break;

            default:
                break;
        }
    }

    public interface OnPhoneConfirmListener {
        public void onSelectorCall();

        public void onSelectorTel();
    }

}
