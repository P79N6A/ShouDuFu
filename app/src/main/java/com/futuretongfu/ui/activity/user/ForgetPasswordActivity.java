package com.futuretongfu.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.futuretongfu.iview.IForgetPasswordView;
import com.futuretongfu.model.eventType.ResetPasswordBackSafeSetFinishEventType;
import com.futuretongfu.model.eventType.ResetPasswordFinishEventType;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.KeyboardUtil;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.ForgetPasswordPresenter;
import com.futuretongfu.utils.MatchUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 *     忘记密码
 *
 * @author ChenXiaoPeng
 */
public class ForgetPasswordActivity extends BaseActivity implements IForgetPasswordView {

    private static final String Intent_Extra_Type = "type";

    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.text_account)
    public TextInputEditText editAccount;
    @Bind(R.id.edit_yzm)
    public TextInputEditText editYzm;
    @Bind(R.id.text_yzm)
    public TextView textYzm;

    @Bind(R.id.btn_next)
    public Button btnNext;

    private boolean isGetVerifyCode = false;

    private int type = ResetPasswordActivity.Type_Back_Main;
    private ForgetPasswordPresenter forgetPasswordPresenter;

    /*********************************************************/
    @Override
    protected int getLayoutId(){
        return R.layout.activity_forget_password;
    }

    @Override
    protected Presenter getPresenter(){
        return forgetPasswordPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState){
        Intent intent = getIntent();
        type = intent.getIntExtra(Intent_Extra_Type, ResetPasswordActivity.Type_Back_Main);

        textTitle.setText(R.string.user_forget_password2);
        btnNext.setEnabled(false);
        forgetPasswordPresenter = new ForgetPasswordPresenter(this, this);
    }

    @OnClick(R.id.bt_back)
    public void onClickBack(){
        finish();
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    @Override
    public void onPause(){
        super.onPause();
        verificationCodeTimer.cancel();
    }

    /** 获取验证码 */
    @OnClick(R.id.text_yzm)
    public void onClickYzm(){
        hideKeyboard();

        String phone = editAccount.getText().toString().trim();
        if(isEmpty(phone, R.string.user_phone)){
            return ;
        }

        if(!MatchUtil.isMobile(phone)){
            showToast("手机号码格式错误");
            return ;
        }

        sendYzm();
    }

    /** 下一步 */
    @OnClick(R.id.btn_next)
    public void onClickBtnNext(){
        if(isEmpty(editAccount.getText().toString(), R.string.user_phone)){
            return ;
        }

        if(!isGetVerifyCode){
            showToast("请先获取验证码");
            return ;
        }

        if(isEmpty(editYzm.getText().toString(), R.string.user_yzm)){
            return ;
        }

        showProgressDialog();
        forgetPasswordPresenter.phoneCode(editAccount.getText().toString(), editYzm.getText().toString());

    }

    @OnTextChanged({R.id.text_account, R.id.edit_yzm})
    public void onTextChange(){
        if(TextUtils.isEmpty(editAccount.getText().toString().trim()) || TextUtils.isEmpty(editYzm.getText().toString().trim())){
            btnNext.setEnabled(false);
            return ;
        }

        btnNext.setEnabled(true);
    }

    /*********************************************************/
    @Override
    public void onGetPhoneCodeSuccess(){
    }

    @Override
    public void onGetPhoneCodeFaile(String msg){
        showToast(msg);
        cleanVerificationCodeTimer();
    }

    @Override
    public void onPhoneCodeFaile(String msg){
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onPhoneCodeSuccess(){
        hideProgressDialog();

        ResetPasswordActivity.startActivity(this, editAccount.getText().toString(), type,editYzm.getText().toString().trim());
        editAccount.setText("");
        editYzm.setText("");
        isGetVerifyCode = false;
        verificationCodeTimer.cancel();
        textYzm.setText(R.string.get_yzm);
        textYzm.setEnabled(true);

    }
    /*********************************************************/

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(ResetPasswordFinishEventType event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(ResetPasswordBackSafeSetFinishEventType event) {
        finish();
    }

    private void sendYzm(){
        isGetVerifyCode = true;
        forgetPasswordPresenter.getPhoneCode(editAccount.getText().toString(), Constants.PhoneCode_Login);
        verificationCodeTimer.start();
        showToast(R.string.toast_dx_yzm_send);
    }

    /** 关闭键盘 */
    private void hideKeyboard(){
        KeyboardUtil.showKeyboard(this, editAccount, false);
        KeyboardUtil.showKeyboard(this, editYzm, false);
    }

    /** 倒计时 */
    private CountDownTimer verificationCodeTimer = new CountDownTimer(1000 * 60, 1000) {
        @Override
        public void onTick(long t) {
            String time = "(" + (t / 1000) + "s)";
            textYzm.setEnabled(false);
            textYzm.setText(time);
        }

        @Override
        public void onFinish() {
            textYzm.setText(R.string.get_yzm);
            textYzm.setEnabled(true);
        }
    };

    private void cleanVerificationCodeTimer(){
        verificationCodeTimer.cancel();
        textYzm.setText(R.string.get_yzm);
        textYzm.setEnabled(true);
    }

    public static void startActivity(Context context, int type){
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        context.startActivity(intent);
    }
}
