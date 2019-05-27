package com.futuretongfu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 设置收付款金额（该页面已弃用）
 *
 * @author DoneYang 2017/6/19
 */

public class SetPaymentMoney extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.edt_set_payment_money_input)
    EditText edt_input;

    @Bind(R.id.tv_set_payment_money_platform_available_balance)
    TextView tv_have_money;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_payment_money;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tv_title.setText("设置金额");
    }

    @OnClick({R.id.bt_back, R.id.tv_set_payment_money_platform_available_balance, R.id.tv_set_payment_money_generate_qr_code_pay, R.id.tv_set_payment_money_generate_qr_code_collect, R.id.tv_set_payment_money_apply_receivables_qr_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

            /*生成付款二维码*/
            case R.id.tv_set_payment_money_generate_qr_code_pay:

                break;

            /*生成收款二维码*/
            case R.id.tv_set_payment_money_generate_qr_code_collect:

                break;

            /*申请商家收款码*/
            case R.id.tv_set_payment_money_apply_receivables_qr_code:

                break;
        }
    }
}
