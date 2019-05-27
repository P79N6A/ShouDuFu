package com.futuretongfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.IPayView;
import com.futuretongfu.pay.AliPay;
import com.futuretongfu.pay.WXPay;
import com.futuretongfu.bean.WxPayEntity;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.R;
import com.futuretongfu.bean.BankListBean;
import com.futuretongfu.bean.BankListItemBean;
import com.futuretongfu.bean.FirstRechargeBean;
import com.futuretongfu.bean.TransferBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.BankListIView;
import com.futuretongfu.iview.TransferAccountIView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.home.RechargePresenter;
import com.futuretongfu.ui.component.dialog.PaymentDialog;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.futuretongfu.constants.ParameterConstants.password;

/**
 * 充值(选择充值方式)
 *
 * @author DoneYang 2017/6/16
 */

public class RechargeActivity extends BaseActivity implements PaymentDialog.PaymentDialogWithPwdListener
        , TransferAccountIView, BankListIView ,IPayView{

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.tv_recharge_money)
    TextView tv_money;

    @Bind(R.id.tv_recharge_desc)
    TextView tv_desc;

    private RechargePresenter mPresenter;

    //    private String money = null;
    private String totalMoney = null;
    private int index = -1;

    private String userId = null;
    //    private String rechargeChannel = "";//充值渠道：KLTONG-开联通，PAYECO-易联支付，ETONEPAY-易通支付
    private String rechargeChannel = "";//MPAY 汇付天下  ALIPAY  支付宝  WECHATPAY 微信
    private String bankNo = "";
    private String bankId = "";
    private String friendId = "";
    private String friendName = "";
    private long orderId = 0;

    private boolean isFirst = false;

    private PopupWindow pop_bankCard;

    private List<BankListItemBean> cardList = new ArrayList<>();

    private int chooseItem = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (mPresenter == null) {
            mPresenter = new RechargePresenter(this, this, this, this);
        }
        tv_title.setText("支付方式");
        index = getIntent().getIntExtra(IntentKey.WLF_BEAN, 0);
        CacheActivityUtil.addNewActivity(this);
//        money = getIntent().getStringExtra(IntentKey.RECHARGE_MONEY);
        totalMoney = getIntent().getStringExtra(IntentKey.Total_Money);
        friendId = getIntent().getStringExtra(IntentKey.WLF_ID);
        friendName = getIntent().getStringExtra(IntentKey.TRANSFER_COLLECT_ACCOUNT);
        userId = "" + UserManager.getInstance().getUserId();
//        userId = "1149975540164519";
        if (index == RequestCode.RECHARGE_MONEY) {
            tv_title.setText("充值方式");
            tv_desc.setText("充值金额(元)");
        } else if (index == RequestCode.WLF_ACCOUNT) {
            tv_title.setText("转账方式");
            tv_desc.setText("转账金额(元)");
        }

//        if (!Util.isEmpty(money)) {
//            try {
//                tv_money.setText(StringUtil.getDouble2(Double.parseDouble(money)));
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//        }

        if (!Util.isEmpty(totalMoney)) {
            try {
                tv_money.setText(StringUtil.fmtMicrometer(Double.parseDouble(totalMoney)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        //TODO  刷新会员级别
        Intent intent1 = new Intent();
        intent1.setAction(Constants.REQUEST_CODE_MONEY);
        sendBroadcast(intent1);

    }

    @OnClick({R.id.bt_back, R.id.ll_recharge_yilian, R.id.ll_recharge_kailiantong, R.id.ll_recharge_yitong,
            R.id.ll_recharge_aliply,R.id.ll_recharge_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*返回*/
            case R.id.bt_back:
                finish();
                break;
            /*银联支付*/
            case R.id.ll_recharge_kailiantong:
              /*  if (!UserManager.getInstance().isWithdrawCard()) {
                    PromptDialogUtils.showNotBindCardPromptDialog(this);
                    return;
                }*/
                rechargeChannel = Constants.MPAY;
                recharge();
                break;
            /*支付宝*/
            case R.id.ll_recharge_aliply:
                rechargeChannel = Constants.ALIPAY;
                recharge();
                break;
            /*微信支付*/
            case R.id.ll_recharge_wechat:
                rechargeChannel = Constants.WECHATPAY;
                recharge();
                break;
        }
    }
    /**
     * 充值
     */
    private void recharge() {
        if (Util.isEmpty(totalMoney)) {
            return;
        }
        //TODO 判断是否首次充值
//        if (!Util.isEmpty(userId) && !Util.isEmpty(rechargeChannel)) {
//            mPresenter.onIsFirstRecharge(userId, bankId, rechargeChannel);
//        } else {
//            showToast(R.string.get_data_error);
//        }
        //充值
        if (index == RequestCode.RECHARGE_MONEY) {
            if (Util.isEmpty(userId)) {
                showToast("获取用户信息失败");
                return;
            }
            if (Util.isEmpty(totalMoney)) {
                showToast("获取金额失败");
                return;
            }
            if (Util.isEmpty(rechargeChannel)) {
                showToast("获取支付渠道失败");
                return;
            }
//            if (Util.isEmpty(bankId)) {
//                showToast("获取充值银行卡信息失败");
//                return;
//            }
            showProgressDialog();
            mPresenter.onRecharge(userId, totalMoney, rechargeChannel, Constants.Terminal, bankNo, bankId);
            //转账
        } else if (index == RequestCode.WLF_ACCOUNT) {
            if (Util.isEmpty(userId) || Util.isEmpty(friendId)) {
                showToast("获取用户信息失败");
                return;
            }
            if (Util.isEmpty(totalMoney)) {
                showToast("获取金额失败");
                return;
            }
            mPresenter.onTransferFriend(userId, friendId, "", totalMoney, Constants.Terminal, password);
        }
    }

    //支付充值成功
    @Override
    public void onPayRechargeSuccess(String url) {
        hideProgressDialog();
        if (rechargeChannel.equals(Constants.MPAY)){
            ShowWebViewActivity.startActivity(this, Constants.Url_Pay+url, "银行卡充值", true);
            CacheActivityUtil.newFinishActivity();
        }else if (rechargeChannel.equals(Constants.ALIPAY)){
            gotoAlipay(url);
        }else if (rechargeChannel.equals(Constants.WECHATPAY)){
            WxPayEntity data = new Gson().fromJson(url
                    , new TypeToken<WxPayEntity>() {
                    }.getType()
            );
            WXPay.weChatPay(this, data.getPrepayid(), data.getPartnerid(), data.getNoncestr(), data.getTimestamp(), data.getSign());
        }
    }

    //支付宝支付回掉
    private void gotoAlipay(String url) {
        AliPay.pay(url, this, new AliPay.PayResultListener() {
            @Override
            public void onSuccess(String resultInfo, String resultStatus) {
                showToast("支付成功");
                CacheActivityUtil.newFinishActivity();
            }
            @Override
            public void onFailed() {
                showToast("支付失败");
            }
        });

    }

    @Override
    public void onPayRechargeFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onBankListFail(int code, String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onBankListSuccess(BankListBean data) {
        if (!Util.isEmpty(data) && !Util.isEmpty(data.getData())) {
            cardList.clear();
            cardList.addAll(data.getData());
//            showBankCard();
        } else {
            PromptDialogUtils.showNotBindCardPromptDialog(this);
        }
    }

    @Override
    public void onFirstRechargeFail(int code, String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onFirstRechargeSuccess(FirstRechargeBean data) {
        hideProgressDialog();
        if (!Util.isEmpty(data) && !Util.isEmpty(data.isfirst)) {
            isFirst = data.isfirst.equals("1");
        }
    }
    @Override
    public void onPaymentPwdSuccess(String password) {
//        showBankCard();
        //充值
        if (index == RequestCode.RECHARGE_MONEY) {
            if (Util.isEmpty(userId)) {
                showToast("获取用户信息失败");
                return;
            }
            if (Util.isEmpty(totalMoney)) {
                showToast("获取金额失败");
                return;
            }
            if (Util.isEmpty(rechargeChannel)) {
                showToast("获取支付渠道失败");
                return;
            }
            if (Util.isEmpty(bankId)) {
                showToast("获取充值银行卡信息失败");
                return;
            }
            showProgressDialog();
            mPresenter.onRecharge(userId, totalMoney, rechargeChannel, Constants.Terminal, bankNo, bankId);

            //转账
        } else if (index == RequestCode.WLF_ACCOUNT) {
            if (Util.isEmpty(userId) || Util.isEmpty(friendId)) {
                showToast("获取用户信息失败");
                return;
            }
            if (Util.isEmpty(totalMoney)) {
                showToast("获取金额失败");
                return;
            }
            mPresenter.onTransferFriend(userId, friendId, "", totalMoney, Constants.Terminal, password);
        }
    }

    @Override
    public void onPaymentPwdFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onTransferAccountSuccess(TransferBean data) {
        hideProgressDialog();
        Intent intent = new Intent(this, TranferMoneySuccessActivity.class);
        if (!Util.isEmpty(friendName)) {
            intent.putExtra(IntentKey.WLF_BEAN, friendName);
        }
//        intent.putExtra(IntentKey.REMAINING, money);
        intent.putExtra(IntentKey.REMAINING, totalMoney);
        intent.putExtra(IntentKey.WLF_TYPE, RequestCode.FRIEND_TRANSFER);
        startActivity(intent);
    }

    @Override
    public void onTransferAccountFail(int code, String msg) {
        hideProgressDialog();
        showToast(msg);
    }
}
