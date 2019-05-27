package com.futuretongfu.ui.activity.wlsq;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 支付订单-弃用
 *
 * @author DoneYang 2017/6/22
 */

public class PaymentOrderActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.tv_payment_order_account)
    TextView tv_account;

    @Bind(R.id.tv_payment_order_money)
    TextView tv_money;

    @Bind(R.id.image_payment_order_weixin)
    ImageView image_wechat;

    @Bind(R.id.image_payment_order_alipay)
    ImageView image_alipay;

    @Bind(R.id.image_payment_order_up)
    ImageView image_up;

    private String realPay = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_payment_order;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tv_title.setText("支付订单");
        realPay = getIntent().getStringExtra(IntentKey.WLF_BEAN);
        if (!Util.isEmpty(realPay)) {
            try {
                tv_money.setText("￥" + StringUtil.getDouble2(Double.parseDouble(realPay)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.bt_back, R.id.ll_payment_order_weixin, R.id.ll_payment_order_alipay, R.id.ll_payment_order_more_pay_type, R.id.tv_payment_order_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

            /*微信支付*/
            case R.id.ll_payment_order_weixin:

                break;

            /*支付宝支付*/
            case R.id.ll_payment_order_alipay:

                break;

            /*更多支付*/
            case R.id.ll_payment_order_more_pay_type:

                break;

            /*确认*/
            case R.id.tv_payment_order_confirm:
                IntentUtil.startActivity(this, PaymentSuccessActivity.class);
                break;
        }
    }
}
