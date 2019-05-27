package com.futuretongfu.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.bean.Vip_Bean;
import com.futuretongfu.bean.WxPayEntity;
import com.futuretongfu.bean.entity.RegistUserTypeBean;
import com.futuretongfu.iview.IMemberUpgradeView;
import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.pay.AliPay;
import com.futuretongfu.pay.WXPay;
import com.futuretongfu.ui.activity.goods.FirmOrderActivity;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.BusinessWlfBalanceIView;
import com.futuretongfu.model.entity.MemberListInfo;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.home.TransferFriendPresenter;
import com.futuretongfu.presenter.user.MemberUpgradePresenter;
import com.futuretongfu.ui.component.dialog.PaymentDialog;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.PayPopupWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 类:   PayDetailsActivity
 * 描述:  支付详情
 * 作者： weiang on 2017/7/17
 */
public class PayDetailsActivity extends BaseActivity implements PaymentDialog.PaymentDialogWithPwdListener, IMemberUpgradeView, BusinessWlfBalanceIView, PayPopupWindow.OnItemClickLister,AliPay.PayResultListener {
    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.bt_back)
    ImageView backView;
    @Bind(R.id.upgrade_fee_number)
    TextView upgrade_fee_number;
    @Bind(R.id.upgrade_user_type)
    TextView upgrade_user_type;
    @Bind(R.id.tv_account_balance)
    TextView tv_account_balance;

    private double vipFree = 0;
    private int selectUserType = 0;
    private boolean isPayBallance = true;
    private boolean isFirst = false;
    private PayPopupWindow payPopup;
    private MemberUpgradePresenter memberUpgradePresenter;
    private TransferFriendPresenter transferFriendPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_details;
    }

    @Override
    protected Presenter getPresenter() {
        return memberUpgradePresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        titleView.setText("支付详情");
        registerFinishReceiver();
//        CacheActivityUtil.addNewActivity(this);
        memberUpgradePresenter = new MemberUpgradePresenter(this, this);
        if (transferFriendPresenter == null) {
            transferFriendPresenter = new TransferFriendPresenter(this, this);
        }
        transferFriendPresenter.businessIntoShow(UserManager.getInstance().getUserId() + "");
        Intent intent = getIntent();
        if (intent != null) {
            String vipName = intent.getStringExtra(IntentKey.UP_NAME_VIP);
            vipFree = intent.getDoubleExtra(IntentKey.UP_FREE_VIP, 0);
            selectUserType = intent.getIntExtra(IntentKey.UP_SELECT_TYPE, 0);
            double platform_balance = intent.getDoubleExtra(IntentKey.PLATFORM_BALANCE, 0);
            upgrade_fee_number.setText("￥" + StringUtil.fmtMicrometer(vipFree));
            upgrade_user_type.setText("升级" + vipName + "VIP");
        }
    }

    RefreshReceive refreshReceive;

    public void registerFinishReceiver() {
        refreshReceive = new RefreshReceive();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(Constants.REQUEST_CODE_MONEY);
        registerReceiver(refreshReceive, filter);
    }
    private String rechargeChannel = "";//MPAY 余额  ALIPAY  支付宝  WECHATPAY 微信
    @Override
    public void setOnItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wallet:
                if (!UserManager.getInstance().isAnswer()) {
                    PromptDialogUtils.showNotSetQuestionPromptDialog(this);  //密保
                } else {
                    if (!UserManager.getInstance().isHasPayPwd()) {   //支付密码
                        PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
                    } else {
                        new PaymentDialog(this, this).show();
                    }
                }
                break;
            case R.id.ll_alipy:  //支付宝
                memberUpgradePresenter.VipSjZ(UserManager.getInstance().getUserId() + "",vipFree+"",1,"ALIPAY");
                rechargeChannel = Constants.ALIPAY;
                break;
            case R.id.ll_wechat:   //微信
                memberUpgradePresenter.VipSjW(UserManager.getInstance().getUserId() + "",vipFree+"",2,"WECHATPAY");
                rechargeChannel = Constants.WECHATPAY;
                break;
        }
    }

    @Override
    public void onSuccess(String resultInfo, String resultStatus) {
        showToast("恭喜您升级成功");
        finish();

    }

    @Override
    public void onFailed() {

    }


    public class RefreshReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Constants.REQUEST_CODE_MONEY)) {
                transferFriendPresenter.businessIntoShow(UserManager.getInstance().getUserId() + "");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(refreshReceive);
    }


    @OnClick({R.id.bt_back})
    public void onBackClick(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
        }
    }

    @OnClick({R.id.tv_recharge_done})
    public void onCommitClick(View view) {
        switch (view.getId()) {
            case R.id.tv_recharge_done:
              /*  AppUtil.getRealNameStatus(this);
                int realNameStatus = UserManager.getInstance().getRealNameStatus();
                if (realNameStatus == Constants.RealNameStatus_No) {
                    PromptDialogUtils.showNotRuleNamePromptDialog(this);
                    return;
                } else if (realNameStatus == Constants.RealNameStatus_Yes) {
                    if (!UserManager.getInstance().isAnswer()) {
                        PromptDialogUtils.showNotSetQuestionPromptDialog(this);
                        return;
                    } else {
                        if (!UserManager.getInstance().isHasPayPwd()) {
                            PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
                            return;
                        } else {
                            new PaymentDialog(PayDetailsActivity.this, PayDetailsActivity.this).show();
                            return;
                        }
                    }
                } else if (realNameStatus == Constants.RealNameStatus_Doing) {
                    PromptDialogUtils.showRuleNameingPromptDialog(this);
                    return;
                } else if (realNameStatus == Constants.RealNameStatus_Faile) {
                    PromptDialogUtils.showRuleNameFailPromptDialog(this);
                    return;
                }*/
//                if (!UserManager.getInstance().isAnswer()) {
//                    // PromptDialogUtils.showNotSetQuestionPromptDialog(this);
//                    return;
//                } else {
                    if (!UserManager.getInstance().isHasPayPwd()) {
                        PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
                        return;
                    } else {
                        payPopup = new PayPopupWindow(this, 1);
                        payPopup.setOnItemClickListener(this);
                        //payPopup.setAnimationStyle(R.style.pop_animation);
                        payPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                Util.setAlpha(PayDetailsActivity.this, 1.0f);
                            }
                        });
                        payPopup.showAtLocation(findViewById(R.id.activity_eva), Gravity.BOTTOM, 0, 0);
                        Util.setAlpha(this, 0.7f);
                        return;
                    }
                }
        }

    private void onResponseUpgrade() {
        PaymentDialog dlg = new PaymentDialog(this, new PaymentDialog.PaymentDialogWithPwdListener() {

            @Override
            public void onPaymentPwdSuccess(String password) {
                showProgressDialog();
                memberUpgradePresenter.memberUpgrade(UserManager.getInstance().getUserId() + "", selectUserType + "", password);
            }

            @Override
            public void onPaymentPwdFaile(String msg) {
                showToast(msg);
            }
        });
        dlg.show();

    }


    @Override
    public void onMemberListViewFaile(String msg) {

    }

    @Override
    public void onMemberVpi(String data) {

    }



    @Override
    public void onMemberListViewSuccess(List<MemberListInfo> memberListInfo) {

    }

    @Override
    public void onMemberUpFaile(String msg) {
        hideProgressDialog();
       if (msg.equals("账户余额不足")) {
           PromptDialogUtils.showNoMoneyRechargePromptDialog(getContext(), vipFree, vipFree);
        }
    }

    @Override
    public void onMemberUpSuccess(String msg) {
        hideProgressDialog();
        showToast("升级成功");
        //TODO  刷新会员级别
        Intent intent1 = new Intent();
        intent1.setAction(Constants.REQUEST_CODE_UPGRADE_SUCCESS);
        sendBroadcast(intent1);
        UserManager.getInstance().setUserType(selectUserType);
        UserManager.getInstance().save();
        CacheActivityUtil.newFinishActivity();
        delayFinish(200);
    }

    @Override
    public void onGetRealNameStatusFaile(String msg) {

    }

    @Override
    public void onGetRealNameStatusSuccess() {

    }

    @Override
    public void onCommitAddressFaile(String msg) {

    }

    @Override
    public void onCommitAddressSuccess() {

    }

    @Override
    public void onWareAddressSuccess(WareHorseAddressEntity datas) {

    }

    @Override
    public void onWareAddressFaile(String msg) {

    }

    @Override
    public void onUserTypeSuccess(List<RegistUserTypeBean> data) {

    }

    @Override
    public void onUserTypeFaile(String msg) {

    }

    @Override
    public void onFirmOrderFail(String msg) {

    }

    @Override
    public void onFirmOrderSuccess(String result) {
        Log.e("结果result",result);
        if (rechargeChannel.equals(Constants.ALIPAY)) {
            AliPay.pay(result, this, new AliPay.PayResultListener() {
                @Override
                public void onSuccess(String resultInfo, String resultStatus) {
                    finish();
                    showToast("支付成功");
                }

                @Override
                public void onFailed() {
                    finish();
                    //  LogUtil.e("支付失败".);
                }
            });
        } else if (rechargeChannel.equals(Constants.WECHATPAY)) {
            WxPayEntity data = new Gson().fromJson(result
                    , new TypeToken<WxPayEntity>() {
                    }.getType()
            );
            WXPay.weChatPay(this, data.getPrepayid(), data.getPartnerid(), data.getNoncestr(), data.getTimestamp(), data.getSign());
        }
    }

    @Override
    public void onPaymentFail(int code, String msg) {

    }

    @Override
    public void onPaymentSuccess(String data) {
        hideProgressDialog();
        if (rechargeChannel.equals(Constants.ALIPAY)) {
            AliPay.pay(data, this, new AliPay.PayResultListener() {
                @Override
                public void onSuccess(String resultInfo, String resultStatus) {
                    finish();
                    showToast("支付成功");
                }

                @Override
                public void onFailed() {
                    finish();
                    //  LogUtil.e("支付失败".);
                }
            });
        } else if (rechargeChannel.equals(Constants.WECHATPAY)) {
            WxPayEntity datas = new Gson().fromJson(data
                    , new TypeToken<WxPayEntity>() {
                    }.getType()
            );
            WXPay.weChatPay(this, datas.getPrepayid(), datas.getPartnerid(), datas.getNoncestr(), datas.getTimestamp(), datas.getSign());
        }

    }

    @Override
    public void onPaymentWlfBalanceFail(int code, String msg) {
        showToast(msg);
    }
//余额不足请充值
    @Override
    public void onPaymentWlfBalanceSuccess(double money) {
              tv_account_balance.setText("￥" + StringUtil.fmtMicrometer(money));

//        if (isFirst) {
//            tv_account_balance.setText("￥" + StringUtil.fmtMicrometer(money));
//        } else {
//            if (0 != money) {
//                if (vipFree > money) {
//                    PromptDialogUtils.showNoMoneyRechargePromptDialog(this, money, vipFree);
//                } else {
//                    onResponseUpgrade();
//                }
//            } else {
//                PromptDialogUtils.showNoMoneyRechargePromptDialog(this, 0.00, vipFree);
//            }
//        }
    }

    @Override
    public void onPaymentPwdSuccess(String password) {
        showProgressDialog();
        memberUpgradePresenter.memberUpgrade(UserManager.getInstance().getUserId() + "", selectUserType + "", password);
    }

    @Override
    public void onPaymentPwdFaile(String msg) {
        showToast(msg);
    }

    @Override
    protected void onResume() {
        transferFriendPresenter.businessIntoShow(UserManager.getInstance().getUserId() + "");
        super.onResume();
    }
}
