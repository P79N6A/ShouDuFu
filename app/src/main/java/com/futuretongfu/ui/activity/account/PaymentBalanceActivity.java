package com.futuretongfu.ui.activity.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.IPaymentBalanceView;
import com.futuretongfu.model.entity.Balance;
import com.futuretongfu.model.manager.SysDataManager;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.account.PaymentBalancePresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.MyBillActivity;
import com.futuretongfu.ui.activity.SetRechargeMoneyActivity;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.utils.StringUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 余额页面
 * @author ChenXiaoPeng
 */
public class PaymentBalanceActivity extends BaseActivity implements IPaymentBalanceView {


    @Bind(R.id.imgv_eye)
    public ImageView imgvEye;

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.text_amount)
    TextView textAmount;
    @Bind(R.id.text_cash_total)
    TextView textCashTotal;
    @Bind(R.id.text_real_cash_total)
    TextView textRealCashTotal;

    private boolean isEyeOpen = false;
    private Balance dataBalance;
    private PaymentBalancePresenter paymentBalancePresenter;

    private final static String Intent_Extra_Type = "type";

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_payment_balance;
    }

    @Override
    protected Presenter getPresenter() {
        return paymentBalancePresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvTitle.setText("余额");
        paymentBalancePresenter = new PaymentBalancePresenter(this, this);

        isEyeOpen = SysDataManager.getInstance().isEyePaymentBalance();
        updateView(isEyeOpen);
    }

    @Override
    protected void onResume() {
        super.onResume();

        paymentBalancePresenter.balance();
    }


    /***********************************************************************/
    @Override
    public void onPaymentBalanceSuccess(Balance data) {
        dataBalance = data;
        updateView();
    }

    @Override
    public void onPaymentBalanceFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onGetRealNameStatusFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onGetRealNameStatusSuccess(int operation) {
        hideProgressDialog();

        if (UserManager.getInstance().getRealNameStatus() == Constants.RealNameStatus_No) {
            PromptDialogUtils.showNotRuleNamePromptDialog(this);
            return;
        }

        if (UserManager.getInstance().getRealNameStatus() == Constants.RealNameStatus_Doing) {
            PromptDialogUtils.showRuleNameingPromptDialog(this);
            return;
        }

        //充值
        if (PaymentBalancePresenter.Operation_Cz == operation) {
            IntentUtil.startActivity(getContext(), SetRechargeMoneyActivity.class
                    , IntentKey.WLF_BEAN, RequestCode.RECHARGE_MONEY);
        }
        //提现
        else {
            MyBusinessCircleAccountActivity.startActivity(this, MyBusinessCircleAccountActivity.Type_Withdraw_User);



        }

    }

    /***********************************************************************/

    private void updateView(boolean isEyeOpen) {
        if (isEyeOpen) {
            imgvEye.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.icon_visible));
        } else {
            imgvEye.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.icon_invisible));
        }

        updateView();
    }

    private void updateView() {

        if (isEyeOpen) {
            if (null == dataBalance) {

                textAmount.setText("0.00");
                //待结算余额
                textCashTotal.setText("0.00");
                //累计转换
                textRealCashTotal.setText("0.00");

                return;
            }

            textAmount.setText(StringUtil.fmtMicrometer(dataBalance.getAvlBal()));
            //待结算余额
            textCashTotal.setText(StringUtil.fmtMicrometer(dataBalance.getCashTotal()));
            //累计转换
            textRealCashTotal.setText(StringUtil.fmtMicrometer(dataBalance.getRealCashTotal()));

        } else {
            textAmount.setText("******");
            //待结算余额
            textCashTotal.setText("***");
            //累计转换
            textRealCashTotal.setText("***");
        }

    }

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, PaymentBalanceActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        ((Activity) context).startActivityForResult(intent, RequestCode.REQUEST_CODE_ZYYE);
    }



    @OnClick({R.id.bt_back, R.id.imgv_eye, R.id.btn_sale_details,R.id.btn_recharge, R.id.btn_withdraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.imgv_eye:
                this.isEyeOpen = !isEyeOpen;
                SysDataManager.getInstance().setEyePaymentBalance(isEyeOpen);
                SysDataManager.getInstance().save();

                updateView(isEyeOpen);
                break;
            case R.id.btn_recharge:
                onClickRecharge();  //充值
                break;
            case R.id.btn_withdraw:
                onClickWithdraw();  //提现
                break;
            case R.id.btn_sale_details:  //账单明细
                MyBillActivity.startActivity(this, Constants.Bill_Type_Cash);

                break;
        }
    }



    //充值
    private void onClickRecharge() {
      /*  AppUtil.getRealNameStatus(this);
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
                    if (!UserManager.getInstance().isWithdrawCard()) {
                        PromptDialogUtils.showNotBindCardPromptDialog(this);
                    } else {
                        showProgressDialog();
                        paymentBalancePresenter.getRealNameStatus(PaymentBalancePresenter.Operation_Cz);
                    }
                }
            }
        } else if (realNameStatus == Constants.RealNameStatus_Doing) {
            PromptDialogUtils.showRuleNameingPromptDialog(this);
        } else if (realNameStatus == Constants.RealNameStatus_Faile) {
            PromptDialogUtils.showRuleNameFailPromptDialog(this);
        }*/
        if (!UserManager.getInstance().isAnswer()) {
            PromptDialogUtils.showNotSetQuestionPromptDialog(this);
        } else {
            if (!UserManager.getInstance().isHasPayPwd()) {
                PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
            } else {
                showProgressDialog();
                paymentBalancePresenter.getRealNameStatus(PaymentBalancePresenter.Operation_Cz);
            }
        }
    }

    //提现
    public void onClickWithdraw() {
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
                    if (!UserManager.getInstance().isWithdrawCard()) {
                        PromptDialogUtils.showNotBindCardPromptDialog(this);
                    } else {
                        showProgressDialog();
                        paymentBalancePresenter.getRealNameStatus(PaymentBalancePresenter.Operation_Tx);
                    }
                }
            }
        } else if (realNameStatus == Constants.RealNameStatus_Doing) {
            PromptDialogUtils.showRuleNameingPromptDialog(this);
        } else if (realNameStatus == Constants.RealNameStatus_Faile) {
            PromptDialogUtils.showRuleNameFailPromptDialog(this);
        } else if (realNameStatus == Constants.RealNameStatus_Imperfect) {
            PromptDialogUtils.showImPerfectRuleNamePromptDialog(this);
        }
    }

}
