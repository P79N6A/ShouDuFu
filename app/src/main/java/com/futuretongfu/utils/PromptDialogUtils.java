package com.futuretongfu.utils;
/*
 * Created by hhm on 2017/7/31.
 */

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.ui.activity.AddSafeCardActivity;
import com.futuretongfu.ui.activity.RechargeActivity;
import com.futuretongfu.ui.activity.UpgradeMember2Activity;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.activity.user.MiBaoQuestionActivity;
import com.futuretongfu.ui.activity.user.RealNameAuthActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;

public class PromptDialogUtils {

    //实名中的对话框
    public static void showRuleNameingPromptDialog(final Context context) {
        new PromptDialog
                .Builder(context)
                .setTitle("实名认证审核中")
                .setTitleSize(14)
                .setMessage("请耐心等待")
                .setMessageSize(12)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton1TextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .show();
    }

    //未实名的对话框
    public static void showNotRuleNamePromptDialog(final Context context) {
        new PromptDialog.Builder(context)
                .setTitle("未实名认证！")
                .setTitleSize(14)
                .setMessage("是否前往实名认证")
                .setMessageSize(12)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确认", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        IntentUtil.startActivity(context, RealNameAuthActivity.class);
                        dialog.dismiss();
                    }
                })
                .setButton2TextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .show();
    }

    //身份证信息不完善
    public static void showImPerfectRuleNamePromptDialog(final Context context) {
        new PromptDialog.Builder(context)
                .setTitle("身份信息不完善！")
                .setTitleSize(14)
                .setMessage("是否前往补充身份认证")
                .setMessageSize(12)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确认", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        IntentUtil.startActivity(context, RealNameAuthActivity.class);
                        dialog.dismiss();
                    }
                })
                .setButton2TextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .show();
    }


    //实名失败的对话框
    public static void showRuleNameFailPromptDialog(final Context context) {
        new PromptDialog.Builder(context)
                .setTitle("审核不通过")
                .setTitleSize(14)
                .setMessage("是否前往实名认证")
                .setMessageSize(12)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确认", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        IntentUtil.startActivity(context, RealNameAuthActivity.class);
                        dialog.dismiss();
                    }
                })
                .setButton2TextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .show();
    }

    //未绑卡的对话框
    public static void showNotBindCardPromptDialog(final Context context) {
        new PromptDialog
                .Builder(context)
                .setMessage("请先绑定银行卡")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("去绑卡", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        AddSafeCardActivity.startActivity(context);
                    }
                })
                .show();
    }

    //未设置支付密码的对话框
    public static void showNotSetPwdPromptDialog(final Context context, final int type) {
        new PromptDialog
                .Builder(context)
                .setMessage("您还没有设置支付密码")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("去设置", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        EditPayPasswordActivity.startActivity(context, type);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    //未设置密保问题的对话框
    public static void showNotSetQuestionPromptDialog(final Context context) {
        new PromptDialog
                .Builder(context)
                .setMessage("您还没有设置密保问题")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("去设置", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        MiBaoQuestionActivity.startActivity(context, MiBaoQuestionActivity.Type_MiBao_New);
                    }
                }).show();
    }

    //余额不足的对话框
    public static void showNoMoneyRechargePromptDialog(final Context context, final double realPay, final double payMentMoney) {
        new PromptDialog
                .Builder(context)
                .setMessage("您的余额不足，是否充值")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("去充值", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        IntentUtil.startActivity(context, RechargeActivity.class
                                , IntentKey.RECHARGE_MONEY, "" + realPay
                                , IntentKey.Total_Money, "" + payMentMoney
                                , IntentKey.WLF_BEAN, RequestCode.RECHARGE_MONEY);
                    }
                }).show();
    }

    //未设置密保问题的对话框
    public static void showStoreUpdatePromptDialog(final Context context) {
        new PromptDialog
                .Builder(context)
                .setTitle("商家升级审核中")
                .setTitleSize(14)
                .setMessage("请耐心等待")
                .setMessageSize(12)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton1TextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .show();
    }

    //请完成升级
    public static void showUpgradePromptDialog(final Context context) {
        new PromptDialog
                .Builder(context)
                .setTitle("提示")
                .setTitleSize(14)
                .setMessage("请完成会员资质升级")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setButton1("切换账号", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        //清空登录数据
                        UserManager.getInstance().clean();

                        LoginActivity.startActivity(context, RequestCode.REQUEST_CODE_LOGIN_APP);
                        CacheActivityUtil.finishActivityButLast();
                        dialog.dismiss();
                    }
                })
                .setButton2("去升级", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        UpgradeMember2Activity.startActivity(context);
                    }
                }).show();
    }

    //级别不够
    public static void showStoreUpgarePromptDialog(final Context context) {
        new PromptDialog
                .Builder(context)
                .setTitle("提示")
                .setTitleSize(14)
                .setMessage("抱歉，您的级别不够，请先升级至创客及以上级别")
                .setMessageSize(12)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton1TextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .show();
    }

}
