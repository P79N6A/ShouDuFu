package com.futuretongfu.ui.activity.account;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.futuretongfu.bean.AccountsystemConfig;
import com.futuretongfu.bean.RecommendBean;
import com.futuretongfu.iview.IMyBusinessCircleAccountView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.WithdrawalSucessActivity;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.ui.component.dialog.PaymentDialog;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;
import com.futuretongfu.model.entity.WithdrawalShowInfo;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.account.MyBusinessCircleAccountPresenter;
import com.futuretongfu.ui.activity.MyBankActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的商圈：转入、转出、提现
 *
 * @author ChenXiaoPeng
 */
public class MyAccountInOutActivity extends BaseActivity implements IMyBusinessCircleAccountView {

    public static final int Type_Into = 0;//转入
    public static final int Type_Out = 1;//转出
    public static final int Type_Withdraw = 2;//提现

    private static final String Intent_Extra_Type = "type";
    private static final String Intent_Extra_Region = "region";
    private static final String Intent_Extra_AvBal = "avlBal";

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.text_account_title1)
    public TextView textAccountTitle;
    @Bind(R.id.text_into_tip1)
    public TextView textTip1;
    @Bind(R.id.text_into_tip2)
    public TextView textTip2;
    @Bind(R.id.btn_confirm)
    public Button btnConfirm;
    @Bind(R.id.text_into_tip3)
    public TextView textTip3;

    @Bind(R.id.text_account)
    public TextInputEditText editAccount;

    private int type = Type_Into;
    private int region = 0;
    private double onlineavlBal;
    private WithdrawalShowInfo withdrawalShowInfo;
    private PaymentDialog paymentDialog;
    private MyBusinessCircleAccountPresenter myBusinessCircleAccountPresenter;
    private double momey = 0;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_business_circle_into;
    }

    @Override
    protected Presenter getPresenter() {
        return myBusinessCircleAccountPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        type = intent.getIntExtra(Intent_Extra_Type, Type_Into);
        region = intent.getIntExtra(Intent_Extra_Region, 0);
        onlineavlBal = intent.getDoubleExtra(Intent_Extra_AvBal, 0.0);

        myBusinessCircleAccountPresenter = new MyBusinessCircleAccountPresenter(this, this);

        switch (type) {
            //转入
            case Type_Into:
                textTitle.setText("转入");
                textAccountTitle.setText("转入金额");
                textTip1.setVisibility(View.VISIBLE);
                textTip1.setText("可用平台余额：0元");
                textTip2.setVisibility(View.GONE);
                btnConfirm.setText("确认转入");
                textTip3.setVisibility(View.GONE);

                break;

            //转出
            case Type_Out:
                textTitle.setText("转出");
                textAccountTitle.setText("转出金额（本次最多可转出0.00元）");
                textTip1.setVisibility(View.GONE);
                textTip2.setVisibility(View.GONE);
                btnConfirm.setText("确认转出");
                textTip3.setVisibility(View.GONE);
                break;
            //提现
            case Type_Withdraw:
                textTitle.setText("提现");
                textAccountTitle.setText("提现金额（请输入100的整数倍）");
                textTip1.setVisibility(View.VISIBLE);
                textTip1.setText("提现金额：0.00");
                textTip2.setVisibility(View.VISIBLE);
                textTip2.setText("可用余额");
                btnConfirm.setText("申请提现");
                textTip3.setVisibility(View.VISIBLE);
                textTip3.setText(Html.fromHtml(
                        "<font color = '#333333'>单日最多可提现</font>"
                                + "<font color = '#FF5500'>5</font>"
                                + "<font color = '#333333'>万</font>"));
                editAccount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        try {
                            momey = Double.parseDouble(s.toString());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!TextUtils.isEmpty(s.toString())) {
                            double d = Double.parseDouble(s.toString());
                            if (d > 500) {
                                showToast("提现金额不得大于5万");
                                editAccount.setText("提现金额：" + momey);
                                return;
                            }
                            if (d < 1) {
                                showToast("提现金额不得小于100");
                                editAccount.setText("提现金额：" + 0.00);
                                return;
                            }
                            textTip1.setText("提现金额：" + d * 100);
                        } else {
                            editAccount.setText("提现金额：" + 0.00);
                        }
                    }
                });
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        //转入
        if (Type_Into == type) {
            myBusinessCircleAccountPresenter.businessIntoShow();
        }
        //转出
        else if (Type_Out == type) {
            if (region==0){
                if (Type_Out == type) {
                    textAccountTitle.setText(String.format("转出金额（本次最多可转出%s元）", StringUtil.getDouble2(onlineavlBal)));
                }else {
                    myBusinessCircleAccountPresenter.getBusinessBalance();
                }
            }else if (region==1){
                if (Type_Out == type) {
                    textAccountTitle.setText(String.format("转出金额（本次最多可转出%s元）", StringUtil.getDouble2(onlineavlBal)));
                }
            }else {
                myBusinessCircleAccountPresenter.recommend();
            }
        }
        //提现
        else if (Type_Withdraw == type) {
            myBusinessCircleAccountPresenter.withdrawalShow();
        }
    }


    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.btn_confirm)
    public void onClickConfirm() {
        if (isEmpty(editAccount.getText().toString(), "金额")) {
            return;
        }

        //提现
        if (Type_Withdraw == type) {

            //是否有安全卡
            if (!UserManager.getInstance().isWithdrawCard()) {
                new PromptDialog
                        .Builder(context)
                        .setMessage("您还没有银行卡,是否去添加")
                        .setMessageSize(12)
                        .setButton1("取消", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setButton2("确定", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                                IntentUtil.startActivity(context, MyBankActivity.class);
                            }
                        })
                        .show();

                return;
            }
        }

        if (!UserManager.getInstance().isHasPayPwd()) {
            PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
            return;
        }

        showPaymentDialog();
    }

    /***********************************************************************/
    @Override
    public void onAccountIntoShowSuccess(double avlBal) {
        //转入
        if (Type_Into == type) {
            textTip1.setText(String.format("可用平台余额：%s元", StringUtil.getDouble2(avlBal)));
        }
    }

    @Override
    public void onAccountIntoShowFaile(String msg) {
        showToast(msg);
        //转入
        if (Type_Into == type) {
            textTip1.setText(String.format("可用平台余额：%s元", StringUtil.getDouble2(0)));
        }
    }

    //最多可转出金额 成功
    public void onMyBusinessCircleSuccess(double avlBal) {
        //转出
        if (Type_Out == type) {
            textAccountTitle.setText(String.format("转出金额（本次最多可转出%s元）", StringUtil.getDouble2(avlBal)));
        }
    }

    //最多可转出金额 失败
    public void onMyBusinessCircleFaile(String msg) {
        showToast(msg);
        //转出
        if (Type_Out == type) {
            textAccountTitle.setText(String.format("转出金额（本次最多可转出%s元）", StringUtil.getDouble2(0)));
        }
    }

    @Override
    public void onBusinessAccountIntoSuccess() {
        hideProgressDialog();
        showToast("转入成功");
        delayFinish(200);
    }

    @Override
    public void onBusinessAccountIntoFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onBusinessAccountOutSuccess() {
        hideProgressDialog();
        showToast("转出成功");
        delayFinish(200);
    }

    @Override
    public void onBusinessAccountOutFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }


    @Override
    public void onWithdrawalShowSuccess(WithdrawalShowInfo data) {
        if (Type_Withdraw == type) {
            withdrawalShowInfo = data;
            textTip2.setText("可用金额：" + StringUtil.getDouble2(data.getAvlBal()));
        }
    }

    @Override
    public void onWithdrawalShowFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    //提现成功
    @Override
    public void onWithdrawalSuccess() {
        hideProgressDialog();
        editAccount.setText("");
        WithdrawalSucessActivity.startActivity(this);
    }

    //提现失败
    @Override
    public void onWithdrawalFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onRecommendSuccess(RecommendBean.DataBean data) {
        hideProgressDialog();
        editAccount.setText("");
        WithdrawalSucessActivity.startActivity(this);
    }

    @Override
    public void onRecommendFaile(String msg) {

    }

    @Override
    public void ongetWithDrawConfigSuccess(AccountsystemConfig data) {
        
    }

    @Override
    public void ongetWithDrawConfigFaile(String msg) {

    }

    /***********************************************************************/

    private void showPaymentDialog() {
        if (null == paymentDialog) {
            paymentDialog = new PaymentDialog(this, new PaymentDialog.PaymentDialogWithPwdListener() {
                @Override
                public void onPaymentPwdSuccess(String password) {
                    //转入
                    if (Type_Into == type) {
                        if (region==0){
                            myBusinessCircleAccountPresenter.OnlinebusinessAccountInto(editAccount.getText().toString());
                        }else {
                            myBusinessCircleAccountPresenter.businessAccountInto(editAccount.getText().toString());
                        }
                    }
                    //转出
                    else if (Type_Out == type) {
                        if (region==0){
                            myBusinessCircleAccountPresenter.OnlinebusinessAccountOut(editAccount.getText().toString());
                        }else if (region==1){
                            myBusinessCircleAccountPresenter.recommendOut(editAccount.getText().toString());
                        }
                        else {
                         myBusinessCircleAccountPresenter.businessAccountOut(editAccount.getText().toString());
                        }

                    }
                    //提现
                    else {
                        myBusinessCircleAccountPresenter.withdrawal(editAccount.getText().toString(), withdrawalShowInfo.getList().get(1).getFieldCode(), password);
                    }
                }

                @Override
                public void onPaymentPwdFaile(String msg) {
                    showToast(msg);
                }
            });
        }

        paymentDialog.show();
    }

    public static void startActivity(Context context, int type,int region,double avlBal) {
        Intent intent = new Intent(context, MyAccountInOutActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        intent.putExtra(Intent_Extra_Region, region);
        intent.putExtra(Intent_Extra_AvBal, avlBal);
        context.startActivity(intent);
    }



    public static void startActivitys(Context context, int type,double avlBal) {
        Intent intent = new Intent(context, MyAccountInOutActivity.class);
        intent.putExtra(Intent_Extra_AvBal, avlBal);
        context.startActivity(intent);
    }
}
