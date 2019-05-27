package com.futuretongfu.ui.activity.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.futuretongfu.MyReceiver;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.ILoginView;
import com.futuretongfu.model.eventType.ResetPasswordFinishEventType;
import com.futuretongfu.model.eventType.ResgisterLoginEventType;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.LoginPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.ui.activity.PaymentSetMoneyActivity;
import com.futuretongfu.ui.activity.ScanAddFriendActivity;
import com.futuretongfu.ui.component.ChineseLengthFilter;
import com.futuretongfu.ui.component.EditSpaceFilter;
import com.futuretongfu.ui.component.PicVerifyCodeView;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.KeyboardUtil;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.utils.TimerUtil;
import com.futuretongfu.utils.Util;
import com.skjr.zxinglib.CaptureActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;


/**
 * 登录
 *
 * @author ChenXiaoPeng
 */
public class LoginActivity extends BaseActivity implements ILoginView, PicVerifyCodeView.PicVerifyCodeViewListener,
        UMAuthListener {

    private final static String Intent_Extra_RequsetCode = "requsetCode";

    @Bind(R.id.bt_back)
    public ImageView imgvBack;
    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.et_login_username)
    EditText textAccount;
    @Bind(R.id.et_login_password)
    TextInputEditText textPassword;
    @Bind(R.id.textlayout_login_password)
    TextInputLayout textlayoutLoginPassword;
    @Bind(R.id.view_pic_verify_code)
    PicVerifyCodeView picVerifyCodeView;
    @Bind(R.id.text_yzm)
    TextInputEditText textYzm;
    @Bind(R.id.view_yzm)
    RelativeLayout viewYzm;
    @Bind(R.id.btn_login_commit)
    Button btnLogin;

    private int requsetCode;
    private LoginPresenter loginPresenter;

    private int loginErrCount = 0;
    private UMShareAPI mShareAPI;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected Presenter getPresenter() {
        return loginPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mShareAPI = UMShareAPI.get(getContext());
        Intent intent = getIntent();
        requsetCode = intent.getIntExtra(Intent_Extra_RequsetCode, 0);


        textTitle.setText(R.string.user_login);
        loginPresenter = new LoginPresenter(this, this);

        textAccount.setFilters(new InputFilter[]{new ChineseLengthFilter(32, true)});
        textPassword.setFilters(new InputFilter[]{new ChineseLengthFilter(12, true)});
        textYzm.setFilters(new InputFilter[]{new EditSpaceFilter()});

        if (UserManager.getInstance().getAccountNumber() != null && (!TextUtils.isEmpty(UserManager.getInstance().getAccountNumber()))) {
            textAccount.setText(UserManager.getInstance().getAccountNumber());
        }

        textPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClickLogin();
                    return true;
                }
                return false;
            }

        });

        //进入app
        if (RequestCode.REQUEST_CODE_LOGIN_APP == requsetCode) {
            imgvBack.setVisibility(View.GONE);
        } else {
            imgvBack.setVisibility(View.VISIBLE);
        }

        picVerifyCodeView.setPicVerifyCodeViewListener(this);
    }

    boolean doubleBackToExitPressedOnce = false;

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();

        //未进入APP模式下要按返回2次才退出
        if (!success && RequestCode.REQUEST_CODE_LOGIN_APP == requsetCode) {
            if (!doubleBackToExitPressedOnce) {
                showToast("再按一次返回键退出");
                doubleBackToExitPressedOnce = true;
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        picVerifyCodeView.init();
    }


//    @OnClick(R.id.view_click)
//    public void onClickView() {
//        hideAllKeyboard();
//    }

    @OnTextChanged({R.id.et_login_username, R.id.et_login_password})
    public void onTextChange() {
        if (TextUtils.isEmpty(textAccount.getText().toString().trim()) || TextUtils.isEmpty(textPassword.getText().toString().trim())) {
            btnLogin.setEnabled(false);
            return;
        }

        btnLogin.setEnabled(true);
    }


    //登录
    @OnClick(R.id.btn_login_commit)
    public void onClickLogin() {
        hideAllKeyboard();
        if (isEmpty(textAccount.getText().toString(), "手机号/账号")) {
            return;
        }
        if (isEmpty(textPassword.getText().toString(), R.string.user_password)) {
            return;
        }
        if (viewYzm.isShown() && isEmpty(textYzm.getText().toString(), R.string.user_yzm)) {
            return;
        }
        showProgressDialog(R.string.dlg_progress_login);

        if (viewYzm.isShown()) {
            loginPresenter.verifyPicCode(
                    picVerifyCodeView.getDeviceId()
                    , textYzm.getText().toString()
            );
            return;
        }
        loginPresenter.login(
                textAccount.getText().toString()
                , textPassword.getText().toString());
    }

    /**
     * 注册
     */
    @OnClick(R.id.tv_login_regist)
    public void onClickRegister() {
        hideAllKeyboard();
        showMultiBtnDialog();

    }

    //扫一扫
    public void openScan() {
        IntentUtil.startActivityForResult(this, CaptureActivity.class, RequestCode.REQUEST_CODE_SCAN);
    }

    /**
     * 客服
     */
    @OnClick(R.id.tv_tellphone)
    public void onClickTellphone() {
        AppUtil.openPhoneCustomerService(this);
    }

    /**
     * 忘记密码
     */
    @OnClick(R.id.tv_login_forget)
    public void onClickForgetPassword() {
        ForgetPasswordActivity.startActivity(this, ResetPasswordActivity.Type_Back_Main);
    }

    /***********************************************************************/
    @Override
    public void onLoginViewSuccess() {
//        loginPresenter.getRealNameStatus();

//        hideProgressDialog();
        //showToast("登录成功");
        TimerUtil.startTimer(200, new TimerUtil.TimerCallBackListener2() {
            @Override
            public void onEnd() {
                startMainActivity(false);
            }
        });
    }

    @Override
    public void onLoginViewFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
        textPassword.setText("");
        updateYzm();
    }

    @Override
    public void onGetRealNameStatusSuccess(boolean isRealName) {
        hideProgressDialog();
        startMainActivity(isRealName);
    }

    @Override
    public void onGetRealNameStatusFaile(String msg) {
        hideProgressDialog();
        startMainActivity(false);
    }


    @Override
    public void onVerifyPicCodeSuccess() {
        loginPresenter.login(
                textAccount.getText().toString()
                , textPassword.getText().toString());
    }

    @Override
    public void onVerifyPicCodeFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }


    /**
     * 注册并主动登录成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(ResgisterLoginEventType event) {
        if (event.isFinishActivity())
            finish();

    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(ResetPasswordFinishEventType event) {
        finish();
    }


    /***********************************************************************/
    @Override
    public void onPicVerifyCodeViewRefresh() {
        textYzm.setText("");
    }

    /***********************************************************************/

    private void hideAllKeyboard() {
        KeyboardUtil.showKeyboard(this, textAccount, false);
        KeyboardUtil.showKeyboard(this, textPassword, false);
        KeyboardUtil.showKeyboard(this, textYzm, false);
    }
    /* @setNeutralButton 设置中间的按钮
     * 若只需一个按钮，仅设置 setPositiveButton 即可
     */
    private void showMultiBtnDialog(){
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(LoginActivity.this);
        normalDialog.setIcon(R.mipmap.app_icon);
        normalDialog.setPositiveButton("新用户注册",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideAllKeyboard();
                        Register1Activity.startActivity(LoginActivity.this);
                    }
                });
        normalDialog.setNeutralButton("扫码注册",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideAllKeyboard();
                        openScan();
                    }
                });
        // 创建实例并显示
        normalDialog.show();
    }

    private void updateYzm() {
        loginErrCount++;

        if (loginErrCount >= 2) {
            viewYzm.setVisibility(View.VISIBLE);
        }

        if (viewYzm.isShown()) {
            picVerifyCodeView.init();
            textYzm.setText("");
        }

    }

    private void startMainActivity(boolean isRealName) {
        MainActivity.startActivity(this, isRealName);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void startActivity(Context context, int requestCode) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(Intent_Extra_RequsetCode, requestCode);

        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @OnClick({R.id.bt_back, R.id.tv_login_wechat, R.id.tv_login_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.tv_login_wechat:
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, this);
                break;
            case R.id.tv_login_qq:
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, this);
                break;
        }
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        showToast(share_media + "登陆中……");
    }

    @Override
    public void onComplete(SHARE_MEDIA platform, int i, Map<String, String> map) {
        switch (platform) {
            case QQ:
                showToast("QQ登陆成功");
//                NetQQ(map, platform);
                break;
            case WEIXIN:
                showToast("微信登陆成功");
//                NetSINA(map, platform);
                break;
            default:
                break;
        }

    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        showToast(share_media + "授权登陆失败" + throwable);
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        showToast(share_media + "授权登陆取消");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == RequestCode.REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(IntentKey.SCAN_CONTENT_KEY);
                if (content.indexOf("h5")!=-1){
                    String imagec = content.substring(content.lastIndexOf("&")-10);
                    String i=imagec.substring(0,10);
                    Register1Activity.startActivity(this, i + "");
                    return;
                }else {
                    if (!content.contains("?")) {
                        showToast("二维码错误，请重新确认后重新扫码。");
                        return;
                    }

                    String image = content.substring(content.lastIndexOf("=") + 1);
                    Log.e("1123image", image);

                    boolean numeric = isNumeric(image);
                    if (numeric == true) {
                        String imagec = content.substring(content.lastIndexOf("&")-10);
                        String i=imagec.substring(0,10);
                        Register1Activity.startActivity(this, i + "");
                    } else {
                        Register1Activity.startActivity(this, image + "");

                    }

                }

            } else {
                UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
            }
        }
    }



    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


}
