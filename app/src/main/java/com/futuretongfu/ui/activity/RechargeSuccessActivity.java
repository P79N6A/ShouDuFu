package com.futuretongfu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.constants.ParameterConstants;
import com.futuretongfu.iview.IRechargeSuccessView;
import com.futuretongfu.model.entity.Balance;
import com.futuretongfu.model.entity.RechargeRequestDetail;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.wlsq.RechargeSuccessPresenter;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 支付成功
 *
 * @author DoneYang 2017/6/16
 */

public class RechargeSuccessActivity extends BaseActivity implements IRechargeSuccessView {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.bt_right)
    TextView bt_right;

    @Bind(R.id.text)
    TextView text;

    @Bind(R.id.tv_recharge_done)
    TextView tv_done;

    @Bind(R.id.tv_rechargesuccess_remaining)
    TextView tv_remaining;

    private String remaining = null;

    private String userId = null;

    private String orderId = "";

    private RechargeSuccessPresenter mPresenter;
    private int requestCountNumber = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rechargesuccess;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (mPresenter == null) {
            mPresenter = new RechargeSuccessPresenter(this, this);
        }

        tv_title.setText("充值处理中");
        text.setText("充值处理中");
        bt_right.setText("详情");
        orderId = getIntent().getStringExtra(IntentKey.ORDER_ID);
        userId = "" + UserManager.getInstance().getUserId();
        if (Util.isEmpty(userId)) {
            showToast(R.string.get_userinfo_error);
        }
        mPresenter.isRechargeSuccess(userId, orderId);
        showProgressDialog();
        Log.d(TAG, "-------orderId---->:" + orderId);
        if (bt_right.getVisibility() == View.GONE) {
            bt_right.setVisibility(View.VISIBLE);
        }

        if (!Util.isEmpty(remaining)) {
            try {
                tv_remaining.setText(StringUtil.getDouble2(Double.parseDouble(remaining)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    //  mPresenter.businessIntoShow(userId);
    }

    @OnClick({R.id.bt_back, R.id.bt_right, R.id.tv_recharge_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            /*返回*/
            case R.id.bt_back:
                IntentUtil.startActivity(this, MainActivity.class, IntentKey.WLF_BEAN, Constants.Show_Home);
                break;

            /*详情*/
            case R.id.bt_right:
                BillDetailActivity.startActivity(this, orderId, ParameterConstants.rechargeBusinessType);
                finish();
                //IntentUtil.startActivity(this, BillDetailActivity.class);
                break;

            /*充值完成*/
            case R.id.tv_recharge_done:
                IntentUtil.startActivity(this, MainActivity.class, IntentKey.WLF_BEAN, Constants.Show_Home);
                break;
        }
    }

    @Override
    public void onRechargeSuccessFail(int code, String msg) {

    }

    @Override
    public void onRechargeSuccessSuccess(Balance data) {
        if (!Util.isEmpty(data)) {
            tv_remaining.setText(StringUtil.getDouble2(data.getAvlBal()));
        }
    }

    @Override
    public void isRechargeSuccessSuccess(RechargeRequestDetail data) {
        requestCountNumber++;
        if ("Underway".equals(data.getRechargeStatus())) {
            tv_title.setText("充值中");
            text.setText("充值中");
            if (requestCountNumber < 10) {
                requestCountNumber++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.isRechargeSuccess(userId, orderId);
                    }
                }, 2000);
            }
        } else if ("Success".equals(data.getRechargeStatus())) {
            hideProgressDialog();
            tv_title.setText("充值成功");
            text.setText("充值成功");
        } else if ("Fail".equals(data.getRechargeStatus())) {
            hideProgressDialog();
            tv_title.setText("充值失败");
            text.setText("充值失败");
        } else if ("Deleted".equals(data.getRechargeStatus())) {
            hideProgressDialog();
            text.setText("充值订单已删除");
            tv_title.setText("充值订单已删除");
        }
    }

    @Override
    public void isRechargeSuccessFail(int code, String msg) {
        if (requestCountNumber < 10) {
            requestCountNumber++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPresenter.isRechargeSuccess(userId, orderId);
                }
            }, 2000);
        } else {
            hideProgressDialog();
        }

    }
}
