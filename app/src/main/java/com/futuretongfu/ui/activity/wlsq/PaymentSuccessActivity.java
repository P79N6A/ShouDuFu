package com.futuretongfu.ui.activity.wlsq;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.PaymentSetMoneyActivity;
import com.futuretongfu.ui.activity.order.UploadVoucheActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.R;
import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.MatchUtil;
import com.futuretongfu.utils.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 支付成功
 *
 * @author DoneYang 2017/6/22
 */

public class PaymentSuccessActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.tv_payment_success_account)
    TextView tv_account;

    @Bind(R.id.tv_payment_success_money)
    TextView tv_money;

    @Bind(R.id.tv_payment_success_total)
    TextView tv_total;

    @Bind(R.id.tv_payment_success_sq)
    TextView tv_sq;

    @Bind(R.id.tv_payment_success_wlf)
    TextView tv_wlf;

    @Bind(R.id.tv_payment_success_real)
    TextView tv_real;

    @Bind(R.id.tv_payment_success_pay_type)
    TextView tv_type;

    @Bind(R.id.tv_payment_success_reward_score)
    TextView tv_rewardScore;

    @Bind(R.id.tv_payment_success_date)
    TextView tv_date;

    @Bind(R.id.tv_payment_success_order_id)
    TextView tv_orderId;

    private PaySetMoneyBean payBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_payment_success;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tv_title.setText("支付成功");

        new PromptDialog
                .Builder(this)
                .setTitle("是否绑定推荐关系")
                .setMessage(R.string.dlg_confirm_shou_huo2)
                .setButton1(R.string.cancel, new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        //orderConsumerDetailPresenter.orderConsumerDetail(orderNo);

                        dialog.dismiss();
                        finish();
                    }
                })
                .setButton2("绑定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();


        String json = getIntent().getStringExtra(IntentKey.WLF_BEAN);
        if (!Util.isEmpty(json)) {
            payBean = JSONObject.parseObject(json, PaySetMoneyBean.class);
            if (!Util.isEmpty(payBean)) {
                setSecondeData(payBean);
            }
        }

    }

    /**
     * 更新UI
     */
    public void setSecondeData(PaySetMoneyBean data) {
        if (data.orderNo != 0) {
            tv_orderId.setText("" + data.orderNo);
        }

        if (!Util.isEmpty(data.payTypeName)) {
            tv_type.setText(data.payTypeName);
        }

        if (data.realMoney != 0) {
            String money = MatchUtil.formatFloatNumber(data.realMoney);
            tv_real.setText("￥" + money);
            tv_money.setText("￥" + money);
            tv_total.setText("￥" + money);
        }

        if (data.bussMoney != 0) {
            tv_sq.setText("￥" + MatchUtil.formatFloatNumber(data.bussMoney));
        }

        if (data.platMoney != 0) {
            tv_wlf.setText("￥" + MatchUtil.formatFloatNumber(data.platMoney));
        }

        if (!Util.isEmpty(data.storeName)) {
            tv_account.setText(data.storeName);
        }

        if (data.createTime != 0) {
            tv_date.setText(DateUtil.getDateStr3(data.createTime));
        }

    }

    @OnClick({R.id.bt_back, R.id.tv_payment_success_complete})
    public void onViewClicked(View view) {

            /*完成或者返回，都到主页面*/
        IntentUtil.startActivity(this, MainActivity.class
                , IntentKey.WLF_BEAN, Constants.Show_Wlsq);
    }
}
