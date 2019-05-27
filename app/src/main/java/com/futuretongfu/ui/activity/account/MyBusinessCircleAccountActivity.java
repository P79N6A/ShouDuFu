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
import com.futuretongfu.model.entity.WithdrawalShowInfo;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.MyBankActivity;
import com.futuretongfu.ui.activity.WithdrawalSucessActivity;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.ui.component.dialog.PaymentDialog;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;
import com.futuretongfu.presenter.account.MyBusinessCircleAccountPresenter;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的商圈：转入、转出、提现
 *
 * @author ChenXiaoPeng
 */
public class MyBusinessCircleAccountActivity extends BaseActivity implements IMyBusinessCircleAccountView {

    public static final int Type_Into = 0;//转入
    public static final int Type_Out = 1;//转出
    public static final int Type_Withdraw = 2;//提现

    public static final int Type_Withdraw_User = 3;//提现

    public static final int Type_Withdraw_Recommend = 4;//推荐奖提现


    private static final String Intent_Extra_Type = "type";

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
    @Bind(R.id.text_into_tip)
    public TextView text_into_tip;
    @Bind(R.id.text_into_tip4)
    public TextView text_into_tip4;

    @Bind(R.id.text_account)
    public TextInputEditText editAccount;

    private int type = Type_Into;
    private WithdrawalShowInfo withdrawalShowInfo;
    private RecommendBean.DataBean dataBean;
    private PaymentDialog paymentDialog;
    private MyBusinessCircleAccountPresenter myBusinessCircleAccountPresenter;
    private int cashMoneyMin;
    private int cashMoneyAdd;
    private int cashMoneyMax;
    private double avc = 0;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_tx_into;
    }

    @Override
    protected Presenter getPresenter() {
        return myBusinessCircleAccountPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        type = intent.getIntExtra(Intent_Extra_Type, Type_Into);

        myBusinessCircleAccountPresenter = new MyBusinessCircleAccountPresenter(this, this);

        switch (type) {
            //转入
            case Type_Into:
                textTitle.setText("转入");
                textAccountTitle.setText("金额（请输入100的整数倍）");
                textTip1.setVisibility(View.VISIBLE);
                textTip1.setText("可用平台余额：0元");
                textTip2.setVisibility(View.GONE);
                btnConfirm.setText("确认转入");
                textTip3.setVisibility(View.GONE);

                break;

            //转出
            case Type_Out:
                textTitle.setText("转出");
                textAccountTitle.setText("金额（本次最多可转出0.00元）");
                textTip1.setVisibility(View.GONE);
                textTip2.setVisibility(View.GONE);
                btnConfirm.setText("确认转出");
                textTip3.setVisibility(View.GONE);
                break;

            //提现
            case Type_Withdraw:
                textTitle.setText("提现");
                textTip2.setVisibility(View.VISIBLE);
                textTip2.setText("可用余额");
                btnConfirm.setText("申请提现");
                myBusinessCircleAccountPresenter.getWithDrawConfig();
                break;
            case Type_Withdraw_User:
                textTitle.setText("提现");
                textTip2.setVisibility(View.VISIBLE);
                textTip2.setText("可用余额");
                btnConfirm.setText("申请提现");
                text_into_tip4.setVisibility(View.VISIBLE);
                myBusinessCircleAccountPresenter.getWithDrawConfig();
                break;
            case Type_Withdraw_Recommend:
                textTitle.setText("提现");
                textTip2.setVisibility(View.VISIBLE);
                textTip2.setText("可用余额");
                btnConfirm.setText("申请提现");
                myBusinessCircleAccountPresenter.getWithDrawConfig();
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
            myBusinessCircleAccountPresenter.getBusinessBalance();
        }
        //提现
        else if (Type_Withdraw == type) {
            myBusinessCircleAccountPresenter.getBusinessBalance();
            text_into_tip.setText("次日到账,手续费千分之三");


        }
        //提现
        else if (Type_Withdraw_User == type) {
            myBusinessCircleAccountPresenter.withdrawalShow();
            text_into_tip.setText("次日到账,手续费10%");

            text_into_tip4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PromptDialog.Builder(context)
                            .setMessage(R.string.tishi)
                            .setButton1("关闭", new PromptDialog.OnClickListener() {
                                @Override
                                public void onClick(Dialog dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });

            //推荐奖提现
        } else if (Type_Withdraw_Recommend == type) {
            myBusinessCircleAccountPresenter.recommend();
            text_into_tip.setText("次日到账,手续费2%");

        }
    }


    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.btn_confirm)
    public void onClickConfirm() {
        String money = editAccount.getText().toString().trim();
        if (isEmpty(money, "金额")) {
            return;
        }
        //提现
        if (Type_Withdraw == type) {
            double d = 0;
            try {
                d = Double.parseDouble(money);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (d > avc) {
                showToast("金额已超过可提现余额");
                return;


            }

            //是否有安全卡
            if (!UserManager.getInstance().isWithdrawCard()) {
                new PromptDialog.Builder(context)
                        .setMessage("您还没有银行卡,是否去添加")
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
            //提现
            if (!UserManager.getInstance().isHasPayPwd()) {
                PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
                return;
            }

            showPaymentDialog();

        } else if (Type_Withdraw_User == type) {

            double d = 0;
            try {
                d = Double.parseDouble(money);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (d > withdrawalShowInfo.getAvlBal()) {
                showToast("金额已超过可提现余额");
                return;
            }
            if (d > cashMoneyMax) {
                showToast("提现金额不得大于" + cashMoneyMax);
                return;
            }
            if (d < cashMoneyMin) {
                showToast("提现金额不得小于" + cashMoneyMin);
                return;
            }
            if (d % cashMoneyAdd != 0) {
                showToast("提现金额为" + cashMoneyAdd + "的整数倍");
                return;
            }


            //是否有安全卡
            if (!UserManager.getInstance().isWithdrawCard()) {
                new PromptDialog.Builder(context)
                        .setMessage("您还没有银行卡,是否去添加")
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
            if (!UserManager.getInstance().isHasPayPwd()) {
                PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
                return;
            }
            showPaymentDialogs();
        } else if (Type_Withdraw_Recommend == type) {

            double d = 0;
            try {
                d = Double.parseDouble(money);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (d > dataBean.getAvlBal()) {
                showToast("金额已超过可提现余额");
                return;
            }
            if (d > cashMoneyMax) {
                showToast("提现金额不得大于" + cashMoneyMax);
                return;
            }
            if (d < cashMoneyMin) {
                showToast("提现金额不得小于" + cashMoneyMin);
                return;
            }
            if (d % cashMoneyAdd != 0) {
                showToast("提现金额为" + cashMoneyAdd + "的整数倍");
                return;
            }


            //是否有安全卡
            if (!UserManager.getInstance().isWithdrawCard()) {
                new PromptDialog.Builder(context)
                        .setMessage("您还没有银行卡,是否去添加")
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
            if (!UserManager.getInstance().isHasPayPwd()) {
                PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
                return;
            }
            showPaymentDialogtx();
        }

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
    public void onMyBusinessCircleSuccess(final double avlBal) {
        //转出
        if (Type_Out == type) {
            textAccountTitle.setText(String.format("金额（本次最多可转出%s元）", StringUtil.getDouble2(avlBal)));

        }
        if (Type_Withdraw == type) {
            textAccountTitle.setVisibility(View.GONE);
            //withdrawalShowInfo = data;
            textTip2.setText("可用金额：" + StringUtil.getDouble2(avlBal));
            avc = avlBal;
            editAccount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        textTip1.setText("提现金额：0.00");
                        textTip1.setTextColor(getResources().getColor(R.color.text_color_gray));
                        return;
                    }
                    int len = s.toString().length();
                    if (len == 1 && s.toString().equals("0")) {
                        textTip1.setText("提现金额：0.00");
                        textTip1.setTextColor(getResources().getColor(R.color.text_color_gray));
                        s.clear();
                    }

                    if (!TextUtils.isEmpty(s.toString())) {
                        double d = 0;
                        try {
                            d = Double.parseDouble(s.toString());
                        } catch (NumberFormatException e) {
                            textTip1.setText("提现金额：0.00");
                            textTip1.setTextColor(getResources().getColor(R.color.text_color_gray));
                            e.printStackTrace();
                        }

                        if (d > avlBal) {
                            textTip1.setText("金额已超过可提现余额");
                            textTip1.setTextColor(getResources().getColor(R.color.text_color_red));
                            text_into_tip.setText("次日到账 手续费千分之三");

                            return;
                        }
                        text_into_tip.setText("手续费" + d * 0.003);
                        textTip1.setTextColor(getResources().getColor(R.color.text_color_gray));
                        textTip1.setText("提现金额：" + StringUtil.getDouble2(d));
                    }
                }
            });
        }

    }

    //最多可转出金额 失败
    public void onMyBusinessCircleFaile(String msg) {
        showToast(msg);
        //转出
        if (Type_Out == type) {
            textAccountTitle.setText(String.format("金额（本次最多可转出%s元）", StringUtil.getDouble2(0)));
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

        if (Type_Withdraw_User == type) {
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

        showProgressDialog();
        editAccount.setText("");
        WithdrawalSucessActivity.startActivity(this);
        finish();
    }

    //提现失败
    @Override
    public void onWithdrawalFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onRecommendSuccess(RecommendBean.DataBean data) {
        if (Type_Withdraw_Recommend == type) {
            dataBean = data;
            textTip2.setText("可用推荐奖金额：" + StringUtil.getDouble2(data.getAvlBal()));

        }
    }

    @Override
    public void onRecommendFaile(String msg) {

    }

    @Override
    public void ongetWithDrawConfigSuccess(AccountsystemConfig data) {
        cashMoneyMin = Integer.parseInt(data.getCashMoneyMin());
        cashMoneyAdd = Integer.parseInt(data.getCashMoneyAdd());
        cashMoneyMax = Integer.parseInt(data.getCashMoneyMax());
        textAccountTitle.setText("提现金额（" + data.getCashMoneyMin() + "起，且只能是" + data.getCashMoneyAdd() + "的整数倍)");
        textTip1.setVisibility(View.VISIBLE);
        textTip1.setText("提现金额：0.00");
        textTip3.setVisibility(View.VISIBLE);
        textTip3.setText("单笔最多可提现" + data.getCashMoneyMax() + "，单日最多可提现" + data.getCashMoneyMaxDay());
        if (Type_Withdraw_User == type) {
            editAccount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        textTip1.setText("提现金额：0.00");
                        textTip1.setTextColor(getResources().getColor(R.color.text_color_gray));
                        return;
                    }
                    int len = s.toString().length();
                    if (len == 1 && s.toString().equals("0")) {
                        textTip1.setText("提现金额：0.00");
                        textTip1.setTextColor(getResources().getColor(R.color.text_color_gray));
                        s.clear();
                    }
                    if (!TextUtils.isEmpty(s.toString())) {
                        double d = 0;
                        try {
                            d = Double.parseDouble(s.toString());
                        } catch (NumberFormatException e) {
                            textTip1.setText("提现金额：0.00");
                            textTip1.setTextColor(getResources().getColor(R.color.text_color_gray));
                            e.printStackTrace();
                        }
                        if (d > withdrawalShowInfo.getAvlBal()) {
                            textTip1.setText("金额已超过可提现余额");
                            textTip1.setTextColor(getResources().getColor(R.color.text_color_red));
                            text_into_tip.setText("次日到账 手续费10%");

                            return;
                        }
                        text_into_tip.setText("手续费" + d * 0.1);

                        if (d < cashMoneyMin) {
                            textTip1.setText("提现金额不得小于" + cashMoneyMin);
                            textTip1.setTextColor(getResources().getColor(R.color.text_color_red));
                            return;
                        }
                        if (d > cashMoneyMax) {
                            textTip1.setText("提现金额不得大于" + cashMoneyMax);
                            textTip1.setTextColor(getResources().getColor(R.color.text_color_red));
                            return;
                        }
                        if (d % cashMoneyAdd != 0) {
                            textTip1.setText("提现金额必须为" + cashMoneyAdd + "的整数倍");
                            textTip1.setTextColor(getResources().getColor(R.color.text_color_red));
                            return;
                        }
                        textTip1.setTextColor(getResources().getColor(R.color.text_color_gray));
                        textTip1.setText("提现金额：" + StringUtil.getDouble2(d));
                    }
                }
            });
        } else if (Type_Withdraw_Recommend == type) {
            editAccount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        textTip1.setText("提现金额：0.00");
                        textTip1.setTextColor(getResources().getColor(R.color.text_color_gray));
                        return;
                    }
                    int len = s.toString().length();
                    if (len == 1 && s.toString().equals("0")) {
                        textTip1.setText("提现金额：0.00");
                        textTip1.setTextColor(getResources().getColor(R.color.text_color_gray));
                        s.clear();
                    }
                    if (!TextUtils.isEmpty(s.toString())) {
                        double d = 0;
                        try {
                            d = Double.parseDouble(s.toString());
                        } catch (NumberFormatException e) {
                            textTip1.setText("提现金额：0.00");
                            textTip1.setTextColor(getResources().getColor(R.color.text_color_gray));
                            e.printStackTrace();
                        }
                        if (d > dataBean.getAvlBal()) {
                            textTip1.setText("金额已超过可提现余额");
                            textTip1.setTextColor(getResources().getColor(R.color.text_color_red));
                            text_into_tip.setText("次日到账 手续费2%");

                            return;
                        }
                        text_into_tip.setText("手续费" + d * 0.02);

                        if (d < cashMoneyMin) {
                            textTip1.setText("提现金额不得小于" + cashMoneyMin);
                            textTip1.setTextColor(getResources().getColor(R.color.text_color_red));
                            return;
                        }
                        if (d > cashMoneyMax) {
                            textTip1.setText("提现金额不得大于" + cashMoneyMax);
                            textTip1.setTextColor(getResources().getColor(R.color.text_color_red));
                            return;
                        }
                        if (d % cashMoneyAdd != 0) {
                            textTip1.setText("提现金额必须为" + cashMoneyAdd + "的整数倍");
                            textTip1.setTextColor(getResources().getColor(R.color.text_color_red));
                            return;
                        }
                        textTip1.setTextColor(getResources().getColor(R.color.text_color_gray));
                        textTip1.setText("提现金额：" + StringUtil.getDouble2(d));
                    }
                }
            });
        }

    }

    @Override
    public void ongetWithDrawConfigFaile(String msg) {
        showToast(msg);
    }

    /***********************************************************************/

    private void showPaymentDialog() {
        if (null == paymentDialog) {
            paymentDialog = new PaymentDialog(this, new PaymentDialog.PaymentDialogWithPwdListener() {
                @Override
                public void onPaymentPwdSuccess(String password) {
                    //转入
                    if (Type_Into == type) {
                        myBusinessCircleAccountPresenter.businessAccountInto(editAccount.getText().toString());
                    }
                    //转出
                    else if (Type_Out == type) {
                        myBusinessCircleAccountPresenter.businessAccountOut(editAccount.getText().toString());
                    }
                    //提现
                    else {
                        try {
                            showProgressDialog();
                            String money = editAccount.getText().toString();
                            Double dMoney = Double.parseDouble(money);
                            myBusinessCircleAccountPresenter.withdrawal(dMoney + "", "Buss", password);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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


    private void showPaymentDialogs() {
        if (null == paymentDialog) {
            paymentDialog = new PaymentDialog(this, new PaymentDialog.PaymentDialogWithPwdListener() {
                @Override
                public void onPaymentPwdSuccess(String password) {
                    //转入
                    if (Type_Into == type) {
                        myBusinessCircleAccountPresenter.businessAccountInto(editAccount.getText().toString());
                    }
                    //转出
                    else if (Type_Out == type) {
                        myBusinessCircleAccountPresenter.businessAccountOut(editAccount.getText().toString());
                    }
                    //提现
                    else {
                        try {
                            String money = editAccount.getText().toString();
                            Double dMoney = Double.parseDouble(money);
                            myBusinessCircleAccountPresenter.withdrawal(dMoney + "", "User", password);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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


    private void showPaymentDialogtx() {
        if (null == paymentDialog) {
            paymentDialog = new PaymentDialog(this, new PaymentDialog.PaymentDialogWithPwdListener() {
                @Override
                public void onPaymentPwdSuccess(String password) {
                    //转入
                    if (Type_Into == type) {
                        myBusinessCircleAccountPresenter.recommendOut(editAccount.getText().toString());
                    }
                    //转出
                    else if (Type_Out == type) {
                        myBusinessCircleAccountPresenter.recommendOut(editAccount.getText().toString());
                    }
                    //提现
                    else if (Type_Withdraw_Recommend == type) {
                        try {
                            String money = editAccount.getText().toString();
                            Double dMoney = Double.parseDouble(money);
                            myBusinessCircleAccountPresenter.withdrawal(dMoney + "", "Invite", password);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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


    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, MyBusinessCircleAccountActivity.class);
        intent.putExtra(Intent_Extra_Type, type);

        context.startActivity(intent);
    }
}
