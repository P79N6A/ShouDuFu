package com.futuretongfu.ui.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.futuretongfu.R;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.To;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册--选择收货地址
 */

public class RegistAddressDialog extends Dialog {

    @Bind(R.id.et_name)
    EditText textName;
    @Bind(R.id.et_phone)
    EditText textPhone;
    @Bind(R.id.et_address)
    EditText textAddredd;
    private OnPhoneConfirmListener onConfirmListener;
    private Context context;
    String receiverName="",receiverMobile="",receiverAddress="";

    public RegistAddressDialog(Context context,String receiverName,String receiverMobile,String receiverAddress,OnPhoneConfirmListener onConfirmListener) {
        super(context, R.style.dialogWindowAnim);
        this.context = context;
        this.receiverName = receiverName;
        this.receiverMobile = receiverMobile;
        this.receiverAddress = receiverAddress;
        this.onConfirmListener = onConfirmListener;
        setContentView(R.layout.dialog_regist_address_layout);
        setCancelable(true);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        textName.setText(receiverName);
        textPhone.setText(receiverMobile);
        textAddredd.setText(receiverAddress);
    }


    @OnClick({R.id.btn_cancel, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                String name = textName.getText().toString().trim();
                String phone = textPhone.getText().toString().trim();
                String address = textAddredd.getText().toString().trim();
                if (TextUtils.isEmpty(name)){
                    To.s("收货人姓名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    To.s("收货人电话不能为空");
                    return;
                }
                if (!StringUtil.isPhoneNumber(phone)) {
                    To.s("收货联系人手机号码格式不正确");
                    return;
                }
                if (TextUtils.isEmpty(address)){
                    To.s("收货人地址不能为空");
                    return;
                }
                if (onConfirmListener != null) {
                    onConfirmListener.onSelectorAdddress(name,phone,address);
                }
                dismiss();
                break;
        }
    }

    public interface OnPhoneConfirmListener {
        public void onSelectorAdddress(String name, String phone, String address);
    }

}