package com.futuretongfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 设置金额
 *
 * @author DoneYang 2017/6/16
 */

public class SetRechargeMoneyActivity extends BaseActivity {


    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.tv_recharge_confirm)
    TextView tv_confirm;

    @Bind(R.id.tv_set_recharge_money_desc)
    TextView tv_desc;

    @Bind(R.id.edt_set_recharge_money)
    EditText edt_setmoney;


    private int index = -1;

    private String friendId = null;
    private String friendName = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setrechargemoney;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tv_title.setText("设置金额");
        index = getIntent().getIntExtra(IntentKey.WLF_BEAN, 0);
        friendId = getIntent().getStringExtra(IntentKey.WLF_ID);
        friendName = getIntent().getStringExtra(IntentKey.TRANSFER_COLLECT_ACCOUNT);
        CacheActivityUtil.addActivityWithClear(this);
        if (index == RequestCode.RECHARGE_MONEY) {
            tv_desc.setText("充值金额");
            tv_confirm.setText("确认充值");
        }
        Util.setEnabled(edt_setmoney, tv_confirm);

    }

    @OnClick({R.id.bt_back, R.id.tv_recharge_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

            /*确认*/
            case R.id.tv_recharge_confirm:
                if (checkStatus()) {
                    String money = edt_setmoney.getText().toString();
                    if (index == RequestCode.RECHARGE_MONEY) {
                        IntentUtil.startActivity(this, RechargeActivity.class, IntentKey.Total_Money, money
                                , IntentKey.WLF_BEAN, RequestCode.RECHARGE_MONEY);
                    } else if (index == RequestCode.WLF_ACCOUNT) {
                        Intent intent = new Intent(this, RechargeActivity.class);
                        intent.putExtra(IntentKey.RECHARGE_MONEY, money);
                        if (!Util.isEmpty(friendName)) {
                            intent.putExtra(IntentKey.TRANSFER_COLLECT_ACCOUNT, friendName);
                        }
                        intent.putExtra(IntentKey.WLF_BEAN, RequestCode.WLF_ACCOUNT);
                        if (!Util.isEmpty(friendId)) {
                            intent.putExtra(IntentKey.WLF_ID, friendId);
                        }
                        startActivity(intent);
                        finish();
                    }
                }
                break;
        }
    }

    private boolean checkStatus() {
        String money = edt_setmoney.getText().toString();
        if (Double.parseDouble(money) < 10) {
            showToast("充值金额不能小于10元");
            return false;    //TODO liusha测试
        }
        if (Double.parseDouble(money) > 20000) {
            showToast("充值金额不能大于20,000元");
            return false;
        }
        return true;
    }
}
