package com.futuretongfu.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.RecommendBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.model.manager.SysDataManager;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.RecommendJinPresenter;
import com.futuretongfu.presenter.account.PaymentBalancePresenter;
import com.futuretongfu.ui.activity.account.MyAccountInOutActivity;
import com.futuretongfu.ui.activity.account.MyBusinessCircleAccountActivity;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.view.IRecommendView;

import butterknife.Bind;
import butterknife.OnClick;

public class RecommendedActivity extends  BaseActivity implements IRecommendView {
    private final static String Intent_Extra_Type = "type";
    @Bind(R.id.recommend_text_amount)
    TextView mtext_amount;
    @Bind(R.id.recommend_imgv_eye)
    ImageView mimgv_eye;
    @Bind(R.id.recommend_btn_recharge)
    FrameLayout mbtb_recharge;
    @Bind(R.id.recommend_btn_withdraw)
    FrameLayout mbtb_withdraw;
    @Bind(R.id.recommend_btn_sale_details)
    FrameLayout mrecommend_btn_sale_details;
    RecommendJinPresenter recommendPresenter;
    private boolean isEyeOpen = false;

    RecommendBean.DataBean dataBalance;
    private int region=1;

    private double avlBal = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recommended;
    }

    @Override
    protected Presenter getPresenter() {
        return recommendPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        recommendPresenter=new RecommendJinPresenter(this,this);


        isEyeOpen = SysDataManager.getInstance().isEyePaymentBalance();
        updateView(isEyeOpen);

    }
    /***********************************************************************/

    private void updateView(boolean isEyeOpen) {
        if (isEyeOpen) {
            mimgv_eye.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.icon_visible));
        } else {
            mimgv_eye.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.icon_invisible));
        }

        updateView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recommendPresenter.recommend();
    }

    private void updateView() {

        if (isEyeOpen) {
            if (null == dataBalance) {
                mtext_amount.setText("0.00");
                return;
            }

            mtext_amount.setText(StringUtil.fmtMicrometer(dataBalance.getAvlBal()));
            avlBal=dataBalance.getAvlBal();
        } else {
            mtext_amount.setText("******");
        }

    }

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, RecommendedActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        ((Activity) context).startActivityForResult(intent, RequestCode.REQUEST_CODE_ZYYE);
    }

    @Override
    public void onRecommendSuccess(RecommendBean.DataBean data) {
        dataBalance = data;
        updateView();
    }

    @Override
    public void onRecommendFaile(String msg) {
        //  showToast(msg);
    }

    @Override
    public void onRecommendJinSuccess(boolean data) {
        this.avlBal = avlBal;
        updateView();
    }

    @Override
    public void onRecommendJinFaile(String msg) {

    }

    @Override
    public void onGetRealNameStatusFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    /**
     * 转出
     */
    public void onClickRollOut() {
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
                    MyAccountInOutActivity.startActivity(this, MyAccountInOutActivity.Type_Out,region,avlBal);
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
        if (RecommendJinPresenter.Recommend_ZC == operation) {
//            IntentUtil.startActivity(getContext(), SetRechargeMoneyActivity.class
//                    , IntentKey.WLF_BEAN, RequestCode.RECHARGE_MONEY);
        }
        //提现
        else {
            MyBusinessCircleAccountActivity.startActivity(this, MyBusinessCircleAccountActivity.Type_Withdraw_Recommend);



        }
    }
    @OnClick({R.id.recommend_bt_back, R.id.recommend_imgv_eye, R.id.recommend_btn_recharge, R.id.recommend_btn_withdraw,R.id.recommend_btn_sale_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recommend_bt_back:
                finish();
                break;
            case R.id.recommend_imgv_eye:
                this.isEyeOpen = !isEyeOpen;
                SysDataManager.getInstance().setEyePaymentBalance(isEyeOpen);
                SysDataManager.getInstance().save();

                updateView(isEyeOpen);
                break;
            case R.id.recommend_btn_recharge:
                onClickRollOut();
                break;
            case R.id.recommend_btn_withdraw:
                onClickWithdraw();
                break;
//            case R.id.btn_sale_details:  //账单明细
//                MyBillActivity.startActivity(this, Constants.Bill_Type_Cash);
//
//                break;
            case R.id.recommend_btn_sale_details:
                RecommendDetails_Activity.startActivity(getContext(), Constants.Bill_Type_Recommend);

                break;
            default:
                break;
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
                        recommendPresenter.getRealNameStatus(RecommendJinPresenter.Recommend_TX);
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
