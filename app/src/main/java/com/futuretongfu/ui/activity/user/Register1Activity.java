package com.futuretongfu.ui.activity.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.NestedScrollView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.entity.RegistUserTypeBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IRegister1View;
import com.futuretongfu.iview.IRegister2View;
import com.futuretongfu.model.eventType.ResgisterFinishEventType;
import com.futuretongfu.model.eventType.ResgisterLoginEventType;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.Register1Presenter;
import com.futuretongfu.presenter.user.Register2Presenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.ui.activity.ShowWebViewActivity;
import com.futuretongfu.ui.component.ChineseLengthFilter;
import com.futuretongfu.ui.component.EditSpaceFilter;
import com.futuretongfu.ui.component.PicVerifyCodeView;
import com.futuretongfu.ui.component.checkBox.SmoothCheckBox;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.ui.component.dialog.RegistUserTypeDialog;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.KeyboardUtil;
import com.futuretongfu.utils.MatchUtil;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.utils.SoftHideKeyBoardUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.TimerUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 注册1
 *
 * @author ChenXiaoPeng
 */
public class Register1Activity extends BaseActivity implements IRegister1View, PicVerifyCodeView.PicVerifyCodeViewListener, IRegister2View {

    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.text_phone)
    public TextInputEditText textPhone;

//    @Bind(R.id.text_account)
//    public TextInputEditText textAccount;

    @Bind(R.id.text_yzm)
    public TextInputEditText textYzm;
    @Bind(R.id.edit_dxyzm)
    public TextInputEditText textDxYzm;
    @Bind(R.id.text_dxyzm)
    public TextView btnGetDxYzm;
    @Bind(R.id.view_pic_verify_code)
    public PicVerifyCodeView picVerifyCodeView;

    @Bind(R.id.checkbox_agreement)
    public SmoothCheckBox checkBoxAgreement;

    @Bind(R.id.btn_next)
    public Button btnNext;
    @Bind(R.id.text_password)
    public TextInputEditText editPassword;
    @Bind(R.id.text_password_confirm)
    public TextInputEditText editPasswordConfirm;
    @Bind(R.id.text_tjm)
    public TextInputEditText editTjm;
    @Bind(R.id.radio_group_state)
    RadioGroup radioGroupState;
    @Bind(R.id.cb1)
    RadioButton cb1;
    @Bind(R.id.cb2)
    RadioButton cb2;
    @Bind(R.id.cb3)
    RadioButton cb3;
    @Bind(R.id.cb4)
    RadioButton cb4;
    @Bind(R.id.cb5)
    RadioButton cb5;
    private Register1Presenter register1Presenter;
    private Register2Presenter register2Presenter;
    private boolean isGetVerifyCode = false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register1;
    }

    @Override
    protected Presenter getPresenter() {
        return register1Presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        LinearLayout register1lin = (LinearLayout) findViewById(R.id.register1lin);
        SoftHideKeyBoardUtil.assistActivity(this);
        SoftHideKeyBoardUtil s = new SoftHideKeyBoardUtil(this);
        textTitle.setText(R.string.user_register2);
        checkBoxAgreement.setChecked(true);
        btnNext.setEnabled(false);
        String yaoqingma = getIntent().getStringExtra("yaoqingma");
        textYzm.setFilters(new InputFilter[]{new EditSpaceFilter()});
        textDxYzm.setFilters(new InputFilter[]{new EditSpaceFilter()});
        CacheActivityUtil.addNewActivity(this);
        register1Presenter = new Register1Presenter(this, this);
        picVerifyCodeView.setPicVerifyCodeViewListener(this);
        register2Presenter = new Register2Presenter(this, this);

        editPassword.setFilters(new InputFilter[]{new ChineseLengthFilter(12, true)});
        editPasswordConfirm.setFilters(new InputFilter[]{new ChineseLengthFilter(12, true)});
        editTjm.setFilters(new InputFilter[]{new EditSpaceFilter()});
        editTjm.setText(yaoqingma);
        SharedPreferencesUtils.saveString(getContext(), "updateMoney", "10000");
        register1Presenter.getRegistUserList();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

    }

    @Override
    public void onResume() {
        super.onResume();
        picVerifyCodeView.init();

//        if (isverificationCodeTimer) {
//            verificationCodeTimer.start();
//        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        verificationCodeTimer.cancel();
        super.onDestroy();

    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.img_help)
    public void onClickHelp() {
        register1Presenter.getRegistUserList();
    }

    @OnClick(R.id.btn_agreement)
    public void onClickAgreement() {
        ShowWebViewActivity.startActivity(this, Constants.Url_Agreement_RegistProtocol, "首都富用户协议", true);
    }

    @OnTextChanged({R.id.text_phone, R.id.text_yzm, R.id.edit_dxyzm, R.id.text_password, R.id.text_password_confirm})
    public void onTextChange() {
        if (
                TextUtils.isEmpty(textPhone.getText().toString().trim())
                        || TextUtils.isEmpty(textYzm.getText().toString().trim())
                        || TextUtils.isEmpty(textDxYzm.getText().toString().trim())
                        || TextUtils.isEmpty(editPassword.getText().toString().toString().trim())
                        || TextUtils.isEmpty(editPasswordConfirm.getText().toString().toString().trim())) {
            btnNext.setEnabled(false);
            return;
        }

        btnNext.setEnabled(true);
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

        if (isEmpty(textYzm.getText().toString(), R.string.user_yzm)) {
            return;
        }
        register1Presenter.verifyPicCode(picVerifyCodeView.getDeviceId(), textYzm.getText().toString());
    }

    /**
     * 下一步
     */
    @OnClick(R.id.btn_next)
    public void onClickBtnNext() {
        hideKeyboard();
        if (isEmpty(textPhone.getText().toString().trim(), R.string.user_phone)) {
            return;
        }
        if (isEmpty(textYzm.getText().toString().trim(), R.string.user_yzm)) {
            return;
        }
        if (isEmpty(textDxYzm.getText().toString().trim(), R.string.user_dx_yzm)) {
            return;
        }
        if (isEmpty(editPassword.getText().toString(), R.string.user_password)) {
            return;
        }
        if (isEmpty(editPasswordConfirm.getText().toString(), R.string.user_password_comfit)) {
            return;
        }
        if (!editPassword.getText().toString().equals(editPasswordConfirm.getText().toString())) {
            showToast(R.string.user_password_un_same);
            return;
        }
        if (!MatchUtil.matchPassowrd(editPassword.getText().toString(), 6, 12)) {
            showToast(R.string.toast_password_err_form);
            return;
        }
        if (!checkBoxAgreement.isChecked()) {
            showToast("请先同意用户协议");
            return;
        }
        if (!isGetVerifyCode) {
            showToast("请先获取验证码");
            return;
        }
        showProgressDialog();
        register1Presenter.phoneCode(textPhone.getText().toString().trim(), textDxYzm.getText().toString().trim());
    }

    /***********************************************************************/

    /***********************************************************************/

    @Override
    public void onVerifyPicCodeSuccess() {
        showToast(R.string.toast_dx_yzm_send);
        sendPhoneVerCode();
    }

    @Override
    public void onVerifyPicCodeFaile(String msg) {
        showToast(msg);
        picVerifyCodeView.init();
        textYzm.setText("");
    }

    @Override
    public void onGetPhoneCodeSuccess() {
        isGetVerifyCode = true;
    }

    //注册接口
    private void toRegist() {
        showProgressDialog(R.string.dlg_progress_register);
        String phone = textPhone.getText().toString().trim();
        register2Presenter.register(phone, phone, editPassword.getText().toString().trim(), editTjm.getText().toString().trim());
    }

    @Override
    public void onGetPhoneCodeFaile(String msg) {
        isGetVerifyCode = false;
        showToast(msg);
        cleanVerificationCodeTimer();
    }

    @Override
    public void onPhoneCodeSuccess() {
        hideProgressDialog();
        toRegist();
//        Register2Activity.startActivity(this, textPhone.getText().toString().trim(), textPhone.getText().toString().trim());
        TimerUtil.startTimer(400, new TimerUtil.TimerCallBackListener2() {
            @Override
            public void onEnd() {
//                textAccount.setText("");
                textPhone.setText("");
                textYzm.setText("");
                textDxYzm.setText("");
                isverificationCodeTimer = false;
                btnGetDxYzm.setText(R.string.get_yzm);
                btnGetDxYzm.setEnabled(true);
            }
        });

    }

    @Override
    public void onPhoneCodeFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onUserTypeSuccess(List<RegistUserTypeBean> data) {
        RegistUserTypeDialog registUserTypeDialog = new RegistUserTypeDialog(this, data);
        registUserTypeDialog.show();
    }

    @Override
    public void onUserTypeFaile(String msg) {
        showToast(msg);
    }
    /***********************************************************************/

    /***********************************************************************/
    @Override
    public void onPicVerifyCodeViewRefresh() {
        textYzm.setText("");
    }
    /***********************************************************************/

    /**
     * 关闭键盘
     */
    private void hideKeyboard() {
        KeyboardUtil.showKeyboard(this, textPhone, false);
        KeyboardUtil.showKeyboard(this, textYzm, false);
        KeyboardUtil.showKeyboard(this, textDxYzm, false);
    }

    private void sendPhoneVerCode() {
        verificationCodeTimer.start();
        register1Presenter.getPhoneCode(textPhone.getText().toString(), Constants.PhoneCode_SignUp);
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

    /***********************************************************************/

    /***********************************************************************/
    /**
     * 注册并主动登录成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(ResgisterLoginEventType event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(ResgisterFinishEventType event) {
        finish();
    }

    /***********************************************************************/

    public static void startActivity(Context context,String yaoqingma) {
        Intent intent = new Intent(context, Register1Activity.class);
        intent.putExtra("yaoqingma",yaoqingma);
        context.startActivity(intent);
    }
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, Register1Activity.class);
        context.startActivity(intent);
    }
    /***********************************************************************/
    //注册成功
    @Override
    public void onIRegisterViewSuccess(String data) {
        hideProgressDialog();
        //TODO  zgf新注册修改
        String updateMoney = SharedPreferencesUtils.getString(this, "updateMoney", "10000");
        if (updateMoney.equals("0")) {
            showToast("注冊成功");
            register2Presenter.autoLogin(textPhone.getText().toString().trim(), editPassword.getText().toString());
        } else {
            Register3Activity.startActivity(this, data, textPhone.getText().toString().trim());
        }
    }

    @Override
    public void onIRegisterViewFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onIRegisterViewFaileUserExists() {
        hideProgressDialog();

        new PromptDialog
                .Builder(context)
                .setMessage("该手机号已注册")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("去登录", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        EventBus.getDefault().post(new ResgisterFinishEventType());
                        delayFinish(200);
                    }
                })
                .show();

    }

    @Override
    public void onAutoLoginViewFaile(String msg) {
        showToast("自动登录失败，请手动登录");
        EventBus.getDefault().post(new ResgisterLoginEventType(false));
        delayFinish(300);
    }

    @Override
    public void onAutoLoginViewSuccess() {
        hideProgressDialog();
        EventBus.getDefault().post(new ResgisterLoginEventType(true));
        startMainActivity();
    }

    /***********************************************************************/
    private void startMainActivity() {
        MainActivity.startActivity(this, true);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        TimerUtil.startTimer(400, new TimerUtil.TimerCallBackListener2() {
            @Override
            public void onEnd() {
                finish();
            }
        });
    }

    @OnClick({R.id.cb5,R.id.cb4, R.id.cb2, R.id.cb3, R.id.cb1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb5:
                cb5.setChecked(true);
                cb4.setChecked(false);
                cb1.setChecked(false);
                cb2.setChecked(false);
                cb3.setChecked(false);
                SharedPreferencesUtils.saveString(getContext(), "updateMoney", "3000");  //TODO  测试
                break;
            case R.id.cb4:
                cb5.setChecked(false);
                cb4.setChecked(true);
                cb1.setChecked(false);
                cb2.setChecked(false);
                cb3.setChecked(false);
                SharedPreferencesUtils.saveString(getContext(), "updateMoney", "10000");  //TODO  测试
                break;
            case R.id.cb3:
                cb3.setChecked(true);
                cb1.setChecked(false);
                cb2.setChecked(false);
                cb4.setChecked(false);
                cb5.setChecked(false);
                SharedPreferencesUtils.saveString(getContext(), "updateMoney", "1000");  //TODO  测试
                break;
            case R.id.cb2:
                cb2.setChecked(true);
                cb1.setChecked(false);
                cb3.setChecked(false);
                cb4.setChecked(false);
                cb5.setChecked(false);
                SharedPreferencesUtils.saveString(getContext(), "updateMoney", "100"); //TODO  测试
                break;
            case R.id.cb1:
                cb1.setChecked(true);
                cb3.setChecked(false);
                cb2.setChecked(false);
                cb4.setChecked(false);
                cb5.setChecked(false);
                SharedPreferencesUtils.saveString(getContext(), "updateMoney", "0");//TODO  测试
                break;
        }
    }


}
