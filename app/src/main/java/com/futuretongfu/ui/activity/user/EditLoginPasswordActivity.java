package com.futuretongfu.ui.activity.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.iview.IEditLoginPasswordView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.EditLoginPasswordPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.component.ChineseLengthFilter;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.MatchUtil;
import com.futuretongfu.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 修改登录密码
 *
 * @author ChenXiaoPeng
 */
public class EditLoginPasswordActivity extends BaseActivity implements IEditLoginPasswordView {

    public static final int Type_First = 0;//修改登录密码第一步：输入原密码
    public static final int Type_Second = 1;//修改登录密码第二步：设置新密码

    private static final String Intent_Extra_Type = "type";
    private static final String Intent_Extra_SrcPassword = "srcPasswordc";

    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.text_password_src)
    public TextInputEditText textPasswordSrc;

    @Bind(R.id.view_password_new)
    public LinearLayout viewPasswordNew;
    @Bind(R.id.text_password_new)
    public TextInputEditText textPasswordNew;
    @Bind(R.id.text_password_new_again)
    public TextInputEditText textPasswordNewAgain;

    @Bind(R.id.btn_next)
    public Button btnNext;

    private int type = Type_First;
    private String srcPassword;
    private EditLoginPasswordPresenter editLoginPasswordPresenter;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_login_password;
    }

    @Override
    protected Presenter getPresenter() {
        return editLoginPasswordPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("修改密码");
        WeiLaiFuApplication.getInstance().addActivity(this);
        Intent intent = getIntent();
        type = intent.getIntExtra(Intent_Extra_Type, Type_First);
        editLoginPasswordPresenter = new EditLoginPasswordPresenter(this, this);

        textPasswordSrc.setFilters(new InputFilter[]{new ChineseLengthFilter(12, true)});
        textPasswordNew.setFilters(new InputFilter[]{new ChineseLengthFilter(12, true)});
        textPasswordNewAgain.setFilters(new InputFilter[]{new ChineseLengthFilter(12, true)});

        switch (type){
            case Type_First:
                textPasswordSrc.setVisibility(View.VISIBLE);
                viewPasswordNew.setVisibility(View.GONE);
                btnNext.setText("下一步");
                //btnNext.setEnabled(false);
                break;

            case Type_Second: {
                srcPassword = intent.getStringExtra(Intent_Extra_SrcPassword);
                textPasswordSrc.setVisibility(View.GONE);
                viewPasswordNew.setVisibility(View.VISIBLE);
                btnNext.setText("修改密码");
                //btnNext.setEnabled(false);

                textPasswordNewAgain.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            onClickNext();
                            return true;
                        }
                        return false;
                    }

                });

            }
            break;
        }
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

    @OnClick(R.id.btn_next)
    public void onClickNext() {
        if (Type_First == type) {

            if (isEmpty(textPasswordSrc.getText().toString(), "原密码")) {
                return;
            }

            showProgressDialog();
            editLoginPasswordPresenter.verificationSrcPassword(textPasswordSrc.getText().toString());

            return;
        }

        if (isEmpty(textPasswordNew.getText().toString(), R.string.user_password_new)) {
            return;
        }
        if (!MatchUtil.matchPassowrd(textPasswordNew.getText().toString().trim(), 6, 12)) {
            showToast("请输入6-12位由数字字母结合的密码，字母区分大小写");
            return;
        }

        if (isEmpty(textPasswordNewAgain.getText().toString(), R.string.user_password_comfit)) {
            return;
        }

        if (!textPasswordNew.getText().toString().equals(textPasswordNewAgain.getText().toString())) {
            showToast(R.string.user_password_un_same);
            return;
        }

        if (!MatchUtil.matchPassowrd(textPasswordNew.getText().toString(), 6, 12)) {
            showToast(R.string.toast_password_err_form);
            return;
        }

        showProgressDialog("正在修改密码");
        editLoginPasswordPresenter.editLoginPassword(srcPassword, textPasswordNew.getText().toString());
    }

    /***********************************************************************/
    @Override
    public void onEditLoginPasswordVertifSuccess() {
        hideProgressDialog();
        EditLoginPasswordActivity.startActivity(this, textPasswordSrc.getText().toString());
        textPasswordSrc.setText("");
    }

    @Override
    public void onEditLoginPasswordVertifFaile(String msg) {
        hideProgressDialog();

        textPasswordSrc.setText("");
        new PromptDialog.Builder(this)
                .setMessage(msg)
                .setButton1("找回密码", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        ForgetPasswordActivity.startActivity(context, ResetPasswordActivity.Type_Back_safeSetting);
                        //delayFinish(200);
                    }
                })
                .setButton2("再试一次", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onEditLoginPasswordSuccess() {
        hideProgressDialog();
        showToast("密码修改成功");
        CacheActivityUtil.finishActivity();
        UserManager.getInstance().clean();

        IntentUtil.startActivity(this, LoginActivity.class);
        //SettingSafeActivity.startActivity(this);
    }

    @Override
    public void onEditLoginPasswordFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    /***********************************************************************/

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, EditLoginPasswordActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String srcPassword) {
        Intent intent = new Intent(context, EditLoginPasswordActivity.class);
        intent.putExtra(Intent_Extra_Type, Type_Second);
        intent.putExtra(Intent_Extra_SrcPassword, srcPassword);
        context.startActivity(intent);
    }
}
