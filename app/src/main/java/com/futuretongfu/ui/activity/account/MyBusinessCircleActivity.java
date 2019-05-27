package com.futuretongfu.ui.activity.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.presenter.account.PaymentBalancePresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.SetRechargeMoneyActivity;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IMyBusinessCircleView;
import com.futuretongfu.model.eventType.FinishMyBusinessCircle;
import com.futuretongfu.model.manager.SysDataManager;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.account.MyBusinessCirclePresenter;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.view.SegmentView;
import com.futuretongfu.view.SegmentView2;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的商圈---通宝
 *
 * @author ChenXiaoPeng
 */
public class MyBusinessCircleActivity extends BaseActivity implements IMyBusinessCircleView {
    private String BusinessCircle="";
    @Bind(R.id.segmentview)
    SegmentView2 segmentview;
    @Bind(R.id.imgv_eye)
    public ImageView imgvEye;
    @Bind(R.id.text_amount)
    public TextView textAmount;
    @Bind(R.id.tv_shangquan_tixian)
    public TextView mtv_sqtx;
    @Bind(R.id.btn_roll_into)
    FrameLayout btn_roll_into;
    private boolean isEyeOpen = true;
    private double avlBal = 0;
    private int region=0;
    private MyBusinessCirclePresenter myBusinessCirclePresenter;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_business_circle;
    }

    @Override
    protected Presenter getPresenter() {
        return myBusinessCirclePresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        segmentview.setSegmentText("商城通宝",0);
        segmentview.setSegmentText("商圈通宝",1);
        segmentview.setSegmentTextSize(14);
        segmentview.setOnSegmentViewClickListener(new SegmentView2.onSegmentViewClickListener() {
            @Override
            public void onSegmentViewClick(View v, int position) {
                if (position==0){
                    region =0;
                    myBusinessCirclePresenter.getOnlineBusinessBalance();
                    mtv_sqtx.setText("转入");
                    btn_roll_into.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setZhuanru();
                        }
                    });
                }else {
                    region=1;
                    myBusinessCirclePresenter.getBusinessBalance();
                    mtv_sqtx.setText("提现");
                    btn_roll_into.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MyBusinessCircleAccountActivity.startActivity(MyBusinessCircleActivity.this, MyBusinessCircleAccountActivity.Type_Withdraw);

                        }
                    });


                }
            }
        });

        myBusinessCirclePresenter = new MyBusinessCirclePresenter(this, this);

        isEyeOpen = SysDataManager.getInstance().isEyeMyBusinessCircle();
        updateView(isEyeOpen);

    }

    @Override
    public void onResume() {
        if (region==0){
            myBusinessCirclePresenter.getOnlineBusinessBalance();
        }else {
            myBusinessCirclePresenter.getBusinessBalance();
        }
        super.onResume();
    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.imgv_eye)
    public void onClickEye() {
        isEyeOpen = !isEyeOpen;

        SysDataManager.getInstance().setEyeMyBusinessCircle(isEyeOpen);
        SysDataManager.getInstance().save();

        updateView(isEyeOpen);
    }

    /**
     * 转入
     */
    @OnClick(R.id.btn_roll_into)
    public void onClickRollInto() {
        setZhuanru();

    }
    public void  setZhuanru(){
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
                    MyAccountInOutActivity.startActivity(this, MyAccountInOutActivity.Type_Into,region,avlBal);
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

    /**
     * 转出
     */
    @OnClick(R.id.btn_roll_out)
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

//    //检查用户状态
//    private boolean checkStatus() {
//        if (UserManager.getInstance().isWithdrawCard()) {
//            if (UserManager.getInstance().getPayPwd()) {
//                return true;
//            } else {
//                new PromptDialog
//                        .Builder(MyBusinessCircleActivity.this)
//                        .setMessage("请先设置支付密码")
//                        .setButton1("取消", new PromptDialog.OnClickListener() {
//                            @Override
//                            public void onClick(Dialog dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .setButton2("去设置", new PromptDialog.OnClickListener() {
//                            @Override
//                            public void onClick(Dialog dialog, int which) {
//                                dialog.dismiss();
//                                EditPayPasswordActivity.startActivity(context, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
//                            }
//                        })
//                        .show();
//                return false;
//            }
//        }
//        return false;
//    }

    /***********************************************************************/
    @Override
    public void onMyBusinessCircleSuccess(double avlBal) {
        this.avlBal = avlBal;
        updateView();
    }

    @Override
    public void onMyBusinessCircleFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onMyOnlineBusinessCircleSuccess(double mallMoney) {
        this.avlBal = mallMoney;
        updateView();
    }

    @Override
    public void onMyOnlineBusinessCircleFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onGetRealNameStatusFaile(String msg) {

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
            MyBusinessCircleAccountActivity.startActivity(this, MyBusinessCircleAccountActivity.Type_Withdraw);
        }

    }

    /***********************************************************************/
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(FinishMyBusinessCircle event) {
        delayFinish(200);
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
            textAmount.setText(StringUtil.fmtMicrometer(avlBal));
        } else {
            textAmount.setText("******");
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyBusinessCircleActivity.class);
        ((Activity) context).startActivityForResult(intent, RequestCode.REQUEST_CODE_SQ);
    }
}
