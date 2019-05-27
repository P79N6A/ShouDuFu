package com.futuretongfu.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.widget.Button;
import android.widget.TextView;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IEditPhoneView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.EditPersonalDataPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.KeyboardUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;
import com.futuretongfu.presenter.user.Register1Presenter;

import butterknife.Bind;
import butterknife.OnClick;

public class EditPhoneActivity extends BaseActivity implements IEditPhoneView {

    private static final String Intent_Extra_Phone = "phone";

    @Bind(R.id.text_phone)
    public TextInputEditText textPhone;
    @Bind(R.id.text_yzm)
    public TextInputEditText textYzm;
    @Bind(R.id.btn_next)
    public Button btnNext;
    @Bind(R.id.text_dxyzm)
    public TextView btnGetDxYzm;
    private boolean isGetVerifyCode = false;
    private EditPersonalDataPresenter presenter;
    private Register1Presenter register1Presenter;

    private String phone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_phone;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter = new EditPersonalDataPresenter(this, this);
        register1Presenter = new Register1Presenter(this, this);
        Intent intent = getIntent();
        phone = intent.getStringExtra(Intent_Extra_Phone);
        textPhone.setText(phone);
    }


    /**
     * 获取验证码
     */
    @OnClick(R.id.text_dxyzm)
    public void onClickGetYzm() {
        hideKeyboard();
        if (isEmpty(textPhone.getText().toString(), R.string.user_phone)) {
            return;
        }
        if (!StringUtil.isPhoneNumber(textPhone.getText().toString().trim())) {
            showToast("手机号码格式不正确");
            return;
        }
        sendPhoneVerCode();
    }


    /**
     * 下一步
     */
    @OnClick(R.id.btn_next)
    public void onClickBtnNext() {
        //TODO  模拟
        hideKeyboard();
        String account = textPhone.getText().toString().trim();
        if (isEmpty(textPhone.getText().toString(), R.string.user_phone)) {
            return;
        }
        if (!StringUtil.isPhoneNumber(textPhone.getText().toString().trim())) {
            showToast("手机号码格式不正确");
            return;
        }
        if (isEmpty(textYzm.getText().toString().trim(), R.string.user_yzm)) {
            return;
        }

        if (!isGetVerifyCode) {
            showToast("请先获取验证码");
            return;
        }
        showProgressDialog();
        presenter.changePhoneNumber(textPhone.getText().toString().trim(), textYzm.getText().toString().trim());
    }

    /**
     * 发送验证码
     */
    private void sendPhoneVerCode() {
        verificationCodeTimer.start();
        register1Presenter.getPhoneCode(textPhone.getText().toString().trim(), Constants.PhoneCode_SignUp);
    }

    @Override
    public void onEditPhoneSuccess() {

    }

    @Override
    public void onEditPhoneFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onGetPhoneCodeSuccess() {
        isGetVerifyCode = true;
    }

    @Override
    public void onGetPhoneCodeFaile(String msg) {
        isGetVerifyCode = false;
        showToast(msg);
        cleanVerificationCodeTimer();
    }

    /**
     * 关闭键盘
     */
    private void hideKeyboard() {
        KeyboardUtil.showKeyboard(this, textPhone, false);
        KeyboardUtil.showKeyboard(this, textYzm, false);
    }


    /**
     * 倒计时
     */
    private boolean isverificationCodeTimer = false;
    private CountDownTimer verificationCodeTimer = new CountDownTimer(1000 * 60, 1000) {
        @Override
        public void onTick(long t) {
            isverificationCodeTimer = true;
            String time = "（" + (t / 1000) + "s）";
            btnGetDxYzm.setEnabled(false);
            btnGetDxYzm.setText(time);
        }

        @Override
        public void onFinish() {
            isverificationCodeTimer = false;
            btnGetDxYzm.setText(R.string.get_yzm);
            btnGetDxYzm.setEnabled(true);
        }
    };

    private void cleanVerificationCodeTimer() {
        verificationCodeTimer.cancel();
        btnGetDxYzm.setText(R.string.get_yzm);
        btnGetDxYzm.setEnabled(true);
    }

    public static void startActivity(Context context, String phone) {
        Intent intent = new Intent(context, EditPhoneActivity.class);
        intent.putExtra(Intent_Extra_Phone, phone);
        context.startActivity(intent);
    }


}
