package com.futuretongfu.ui.activity.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appkefu.lib.interfaces.KFAPIs;
import com.futuretongfu.MyReceiver;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.model.eventType.FinishMainActivityEventType;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.SettingPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.HelpActivity;
import com.futuretongfu.ui.activity.ShowWebViewActivity;
import com.futuretongfu.ui.activity.goods.WareHorseAddressActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.PackageUtil;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.utils.TTSUtility;
import com.futuretongfu.utils.TimerUtil;
import com.futuretongfu.R;
import com.futuretongfu.iview.ISettingView;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.ui.component.dialog.UpdateAppDialog;
import com.suke.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 设置
 *
 * @author ChenXiaoPeng
 */
public class SettingActivity extends BaseActivity implements ISettingView {

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.text_vertion)
    public TextView textVertion;
    SwitchButton tb;
    private SettingPresenter settingPresenter;
    public static final String MY_NEW_LIFEFORM = "NEW_LIFEFORM";

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    private boolean ison = false;

    @Override
    protected void init(Bundle savedInstanceState) {
        tb = (SwitchButton) findViewById(R.id.switch1);
        final Intent intent = new Intent(MY_NEW_LIFEFORM);

        tb.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtils.saveBoolean(SettingActivity.this, "open", isChecked);

                } else {
                    SharedPreferencesUtils.saveBoolean(SettingActivity.this, "open", isChecked);
                }
            }
        });
        textTitle.setText("设置");
        textVertion.setText("V" + PackageUtil.getVersionName(this));

        settingPresenter = new SettingPresenter(this, this);
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }
    /***********************************************************************/

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    //实名认证
    @OnClick(R.id.btn_smrz)
    public void onClickBtnSmrz() {
        RealNameAuthActivity.startActivity(this, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean b = SharedPreferencesUtils.getBoolean(this, "open", true);
        if (b == true) {
            tb.setChecked(true);
        } else {
            tb.setChecked(false);
        }
    }

    //收货地址
    @OnClick(R.id.btn_address)
    public void onClickBtnAddress() {
        WareHorseAddressActivity.startActivity(this);
    }

    //帮助与反馈
    @OnClick(R.id.btn_help)
    public void onClickBtnHelp() {
        HelpActivity.startActivity(this);
    }

    //安全设置
    @OnClick(R.id.btn_aqsz)
    public void onClickBtnAqsz() {
        SettingSafeActivity.startActivity(this);
    }

    //当前版本号
    @OnClick(R.id.btn_dqbbh)
    public void onClickDqbbh() {
        showProgressDialog();
        settingPresenter.getApp();
    }

    //关于我们
    @OnClick(R.id.btn_about_us)
    public void onClickBtnAboutUs() {
        //ShowWebViewActivity.startActivity(this, "http://www.gdbfbz.com/weixin-cys/index_appointment_rule.html", "关于我们", true);
        ShowWebViewActivity.startActivity(this, Constants.Url_Agreement_AboutUs, "关于我们", true);
    }

    //安全退出
    @OnClick(R.id.btn_safe_quit)
    public void onClickQuit() {
        new PromptDialog.Builder(this)
                .setMessage("您确定退出当前账号？")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {

                        //清空登录数据
                        UserManager.getInstance().clean();
                        dialog.dismiss();

                        EventBus.getDefault().post(new FinishMainActivityEventType());

                        //用户没有登录
                        LoginActivity.startActivity(context, RequestCode.REQUEST_CODE_LOGIN_APP);
//                        MainActivity.startActivity(context, false);
                        KFAPIs.Logout(getContext());
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        delayFinish(200);

                    }
                }).setButton2TextColor(Color.parseColor("#0091EE")).show();
//
    }

    /***********************************************************************/

    /***********************************************************************/
    //需要更新APP
    @Override
    public void onSettingUpdateAppYes(final String url) {
        hideProgressDialog();

        new PromptDialog
                .Builder(this)
                .setTitle("版本更新")
                .setMessage("有新版本您需要更新吗？")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("更新", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                        UpdateAppDialog UpdateAppDialog = new UpdateAppDialog(
                                SettingActivity.this
                                , false, url
                                , new UpdateAppDialog.UpdateAppDialogListener() {
                            @Override
                            public void onUpdateAppDialogCancel(boolean isF) {

                            }

                            @Override
                            public void onUpdateAppDialogUpdate(boolean isF) {
                                TimerUtil.startTimer(100, new TimerUtil.TimerCallBackListener2() {
                                    @Override
                                    public void onEnd() {
                                        CacheActivityUtil.finishActivity();
                                    }
                                });

                            }
                        }
                        );

                        UpdateAppDialog.show();

                    }
                })
                .show();

    }

    //不需要更新APP
    @Override
    public void onSettingUpdateAppNo() {
        hideProgressDialog();

        new PromptDialog
                .Builder(this)
                .setMessage("已经是最新版本")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //需要更新APP接口异常
    @Override
    public void onSettingUpdateAppFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    /***********************************************************************/


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        ((Activity) context).startActivityForResult(intent, RequestCode.REQUEST_CODE_SET);
    }
}
