package com.futuretongfu.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.ISettingSafeView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.PromptDialogUtils;
import com.suke.widget.SwitchButton;
import com.futuretongfu.R;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.SettingSafePresenter;
import com.futuretongfu.utils.AppUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 安全设置
 *
 * @author ChenXiaoPeng
 */
public class SettingSafeActivity extends BaseActivity implements ISettingSafeView {

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.switch_button)
    public SwitchButton btnUpgrad;

    private SettingSafePresenter settingSafePresenter;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_safe;
    }

    @Override
    protected Presenter getPresenter() {
        return settingSafePresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("安全设置");
        btnUpgrad.setChecked(UserManager.getInstance().isUpgrad());
        WeiLaiFuApplication.getInstance().addActivity(this);
        settingSafePresenter = new SettingSafePresenter(this, this);
        //获取实名认证信息
        AppUtil.getRealNameStatus(context);
        btnUpgrad.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                settingSafePresenter.setAutoUpgrade(isChecked);
            }
        });
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();

        AppUtil.getRealNameStatus(this);

    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.text_upgrad)
    public void onClickUpgradInfo() {

    }

    //修改登录密码
    @OnClick(R.id.btn_edit_login_password)
    public void onClcikEditLoginPassword() {
        EditLoginPasswordActivity.startActivity(this, EditLoginPasswordActivity.Type_First);
    }

    //修改支付密码
    @OnClick(R.id.btn_edit_pay_password)
    public void onClcikEditPayPassword() {
        AppUtil.getRealNameStatus(this);
        int realNameStatus = UserManager.getInstance().getRealNameStatus();
        if (realNameStatus == Constants.RealNameStatus_No) {
            PromptDialogUtils.showNotRuleNamePromptDialog(this);
        } else if (realNameStatus == Constants.RealNameStatus_Yes) {
            if (!UserManager.getInstance().isAnswer()) {
                PromptDialogUtils.showNotSetQuestionPromptDialog(this);
            } else {
                if (!UserManager.getInstance().isHasPayPwd()) {
                    PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
                } else {
                    EditPayPasswordActivity.startActivity(this, EditPayPasswordActivity.Type_Edit_Password_First);
                }
            }
        } else if (realNameStatus == Constants.RealNameStatus_Doing) {
            PromptDialogUtils.showRuleNameingPromptDialog(this);
        } else if (realNameStatus == Constants.RealNameStatus_Faile) {
            PromptDialogUtils.showRuleNameFailPromptDialog(this);
        }
    }

    //修改密保问题
    @OnClick(R.id.btn_edit_mibao_wenti)
    public void onClcikEditMibaoWenti() {
        AppUtil.getRealNameStatus(this);
        int realNameStatus = UserManager.getInstance().getRealNameStatus();
        if (realNameStatus == Constants.RealNameStatus_No) {
            PromptDialogUtils.showNotRuleNamePromptDialog(this);
        } else if (realNameStatus == Constants.RealNameStatus_Yes) {
            Intent intent = new Intent(context, MiBaoQuestionActivity.class);
            if (UserManager.getInstance().isAnswer()) {
                intent.putExtra("type", MiBaoQuestionActivity.Type_MiBao_Edit);
            } else {
                intent.putExtra("type", MiBaoQuestionActivity.Type_MiBao_New);
            }
            startActivity(intent);
        } else if (realNameStatus == Constants.RealNameStatus_Doing) {
            PromptDialogUtils.showRuleNameingPromptDialog(this);
        } else if (realNameStatus == Constants.RealNameStatus_Faile) {
            PromptDialogUtils.showRuleNameFailPromptDialog(this);
        } else if (realNameStatus == Constants.RealNameStatus_Imperfect) {
            PromptDialogUtils.showImPerfectRuleNamePromptDialog(this);
        }
    }

    //修改手势密码
    @OnClick(R.id.btn_edit_shous_password)
    public void onClcikEditShousPassword() {
        if (UserManager.getInstance().isHasGesturePwd()) {
            VerificationLoginActivity.startActivity(this, VerificationLoginActivity.Type_GestureLock);
            return;
        }
        GestureLockActivity.startActivity(this, GestureLockActivity.Type_Shous_New);
    }

    /***********************************************************************/
    @Override
    public void onSettingSafeUpgradeSuccess() {
        //hideProgressDialog();
    }

    @Override
    public void onSettingSafeUpgradeFaile(String msg, boolean upgrade) {
        //hideProgressDialog();
        //btnUpgrad.setChecked(!upgrade);
        showToast(msg);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettingSafeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 102://选择银行卡code
                /*是否有支付密码*/
                if (!UserManager.getInstance().isHasPayPwd()) {
                    PromptDialogUtils.showNotSetPwdPromptDialog(this, IntentKey.TYPE_SET_NEW_PAY_PWD);
                    return;
                }
                if (!UserManager.getInstance().isHasPayPwd()) {
                    EditPayPasswordActivity.startActivity(this, IntentKey.TYPE_SET_NEW_PAY_PWD);
                    return;
                }
                EditPayPasswordActivity.startActivity(this, EditPayPasswordActivity.Type_Edit_Password_First);
                break;
        }
    }
}
