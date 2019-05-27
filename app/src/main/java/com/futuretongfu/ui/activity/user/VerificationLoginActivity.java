package com.futuretongfu.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.futuretongfu.iview.IVerificationLoginView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.VerificationLoginPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.KeyboardUtil;
import com.futuretongfu.R;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class VerificationLoginActivity extends BaseActivity implements IVerificationLoginView {

    public final static int Type_GestureLock = 0;//手势密码
    public final static int Type_GestureLockLogin = 1;//忘记手势密码（手势登录）
    public final static int Type_MiBao = 2;//密保

    private final static String Intent_Extra_Type = "type";

    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.text_phone)
    public TextView textPhone;
    @Bind(R.id.text_password)
    public TextInputEditText editPassword;
    @Bind(R.id.btn_next)
    public Button btnNext;

    private int type;
    private VerificationLoginPresenter verificationLoginPresenter;

    /*******************************************************************************/
    @Override
    protected int getLayoutId(){
        return R.layout.activity_verification_login;
    }

    @Override
    protected Presenter getPresenter(){
        return verificationLoginPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState){
        textTitle.setText("身份验证");

        Intent intent = getIntent();
        type = intent.getIntExtra(Intent_Extra_Type, Type_GestureLock);

        String phone = UserManager.getInstance().getAccountNumber();
        textPhone.setText(phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length()));

        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClickNext();
                    return true;
                }
                return false;
            }

        });

        verificationLoginPresenter = new VerificationLoginPresenter(this, this);
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }
    /*******************************************************************************/

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack(){
        finish();
    }

    @OnTextChanged(R.id.text_password)
    public void onTextChangePassword(){
        int length = editPassword.getText().toString().length();
        if(length < 6){
            btnNext.setEnabled(false);
            return ;
        }
        btnNext.setEnabled(true);

    }

    @OnClick(R.id.btn_next)
    public void onClickNext(){
        hideKeyboard();

        if(isEmpty(editPassword.getText().toString(), "登录密码")){
            return ;
        }

        showProgressDialog();
        verificationLoginPresenter.verificationLogin(editPassword.getText().toString());
    }
    /***********************************************************************/

    /***********************************************************************/
    @Override
    public void onVerificationLoginSuccess(){
        hideProgressDialog();

        if(Type_GestureLock == type) {
            GestureLockActivity.startActivity(this, GestureLockActivity.Type_Shous_Edit);
        }
        else if(Type_GestureLockLogin == type){
            GestureLockActivity.startActivity(this, GestureLockActivity.Type_Shous_Forget);
        }
        else if(Type_MiBao == type){
            MiBaoQuestionActivity.startActivity(this, MiBaoQuestionActivity.Type_MiBao_New);
        }

        delayFinish(200);
    }

    @Override
    public void onVerificationLoginFaile(String msg){
        hideProgressDialog();
        showToast(msg);
    }
    /***********************************************************************/

    private void hideKeyboard(){
        KeyboardUtil.showKeyboard(this, editPassword, false);
    }

    public static void startActivity(Context context, int type){
        Intent intent = new Intent(context, VerificationLoginActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        context.startActivity(intent);
    }
}
