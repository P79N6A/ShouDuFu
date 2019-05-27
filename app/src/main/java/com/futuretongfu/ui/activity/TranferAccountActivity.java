package com.futuretongfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.FriendBean;
import com.futuretongfu.bean.TransferBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.BusinessWlfBalanceIView;
import com.futuretongfu.iview.TransferAccountIView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.home.TransferFriendPresenter;
import com.futuretongfu.ui.component.dialog.PaymentDialog;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 转到对方账户
 *
 * @author DoneYang 2017/6/19
 */

public class TranferAccountActivity extends BaseActivity implements PaymentDialog.PaymentDialogWithPwdListener
        , TransferAccountIView, BusinessWlfBalanceIView {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.bt_right)
    TextView bt_right;

    @Bind(R.id.image_tranfer_account_head)
    ImageView image_head;

    @Bind(R.id.tv_tranfer_account_name)
    TextView tv_name;

    @Bind(R.id.tv_tranfer_account_mobile)
    TextView tv_mobile;

    @Bind(R.id.edt_tranfer_account_input_money)
    TextInputEditText edt_inputMoney;

    @Bind(R.id.edt_tranfer_account_desc)
    TextInputEditText edt_desc;

    @Bind(R.id.tv_tranfer_account_confirm)
    TextView tv_confirm;

    @Bind(R.id.tv_tranfer_account_have_money)
    TextView tv_haveMoney;

    private TransferFriendPresenter mPresenter;
    private String userId = null;
    private String friendId = null;
    private String friendName = null;
    private String orderNo = null;

    private String phone = "";

    private double WlfBalance = -1;
    private int userType = -1;

    private boolean isStore = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tranfer_account;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        userType = getIntent().getIntExtra(IntentKey.WLF_BEAN, 0);
        if (userType == 0) {
            tv_title.setText("转首都富账户");
        } else if (userType == RequestCode.STORE_ACCOUNT) {
            tv_title.setText("转商户");
        }
        bt_right.setText("转账记录");
        if (mPresenter == null) {
            mPresenter = new TransferFriendPresenter(this, this, this);
        }
        if (bt_right.getVisibility() == View.GONE) {
            bt_right.setVisibility(View.VISIBLE);
        }
        Util.setEnabled(edt_inputMoney, tv_confirm);
        userId = "" + UserManager.getInstance().getUserId();
        friendId = getIntent().getStringExtra(IntentKey.WLF_ID);
        if (!Util.isEmpty(userId)) {
            mPresenter.businessIntoShow(userId);
        }
        if (!Util.isEmpty(friendId)) {
            mPresenter.onSearchFriend(false, userId, friendId);
        }
    }

    @OnClick({R.id.bt_back, R.id.tv_tranfer_account_confirm, R.id.bt_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

            /*转账记录*/
            case R.id.bt_right:
                IntentUtil.startActivity(this, MyBillActivity.class
                        , IntentKey.WLF_BEAN, "type");//类型 -转账
                break;

            /*确认*/
            case R.id.tv_tranfer_account_confirm:
                if (Util.isEmpty(userId) || Util.isEmpty(friendId)) {
                    showToast("账号信息有误");
                    return;
                }

                 /*是否有支付密码*/
                if (!UserManager.getInstance().isHasPayPwd()) {
                    AppUtil.showPayPasswordDialog(context);
                    return;
                }
                new PaymentDialog(this, this).show();
                break;
        }
    }

    @Override
    public void onSuccess(BaseSerializable data) {
        super.onSuccess(data);
        hideProgressDialog();
        if (!Util.isEmpty(data)) {
            FriendBean bean = (FriendBean) data;
            if (!Util.isEmpty(bean)) {
                if (!Util.isEmpty(bean.nickName)) {
                    friendName = bean.nickName;
                    tv_name.setText(bean.nickName);
                } else {
                    if (!Util.isEmpty(bean.phone)) {
                        tv_name.setText("用户" + bean.phone);
                    }
                }
                if (!Util.isEmpty(bean.phone)) {
                    tv_mobile.setText(bean.phone);
                }
                if (!Util.isEmpty(bean.faceURL)) {
                    GlideLoad.loadHead(bean.faceURL, image_head);
                }

                if (!Util.isEmpty(bean.phone)) {
                    phone = bean.phone;
                }
                isStore = bean.store;
            }
        }
    }

    @Override
    public void onFail(int code, String msg) {
        super.onFail(code, msg);
    }

    @Override
    public void onTransferAccountSuccess(TransferBean data) {
        hideProgressDialog();
        if (!Util.isEmpty(data)) {
            if (!Util.isEmpty(data.businessNo))
                orderNo = data.businessNo;
        }
        Intent intent = new Intent(this, TranferMoneySuccessActivity.class);
        if (!Util.isEmpty(friendName)) {
            intent.putExtra(IntentKey.WLF_BEAN, friendName);
        }
        intent.putExtra(IntentKey.REMAINING, edt_inputMoney.getText().toString());
        intent.putExtra(IntentKey.WLF_TYPE, RequestCode.FRIEND_TRANSFER);
        intent.putExtra(IntentKey.WLF_Order, orderNo);
        startActivity(intent);

    }

    @Override
    public void onTransferAccountFail(int code, String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onPaymentWlfBalanceFail(final int code, String msg) {
        //showToast("获取平台余额失败");
        hideProgressDialog();
    }

    @Override
    public void onPaymentWlfBalanceSuccess(double money) {
        hideProgressDialog();
        WlfBalance = 0;
        if (money != 0) {
            WlfBalance = money;
            tv_haveMoney.setText("￥" + StringUtil.fmtMicrometer(money));
        }
    }

    @Override
    public void onPaymentPwdSuccess(String password) {
        hideProgressDialog();
        String money = edt_inputMoney.getText().toString();
//        showProgressDialog();
        double money_d = Double.parseDouble(money);
        if (WlfBalance < money_d) {
            PromptDialogUtils.showNoMoneyRechargePromptDialog(this, WlfBalance, money_d);
        } else {
            showProgressDialog();
            mPresenter.onTransferFriend(userId, friendId, phone, money, Constants.Terminal, password);
        }
    }

    @Override
    public void onPaymentPwdFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }
}
