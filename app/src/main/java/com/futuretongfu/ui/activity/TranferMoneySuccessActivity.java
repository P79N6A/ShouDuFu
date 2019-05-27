package com.futuretongfu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 转账成功
 *
 * @author DoneYang 2017/6/17
 */

public class TranferMoneySuccessActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.bt_right)
    TextView bt_right;

    @Bind(R.id.tv_tranfer_success_name)
    TextView tv_tranfer_name;

    @Bind(R.id.tv_tranfer_money_success_type)
    TextView tv_success_type;

    @Bind(R.id.tv_tranfer_success_desc)
    TextView tv_success_desc;

    @Bind(R.id.tv_tranfer_success_money)
    TextView tv_success_money;

    @Bind(R.id.ll_transfer_money_success)
    LinearLayout ll_success;

    @Bind(R.id.tv_tranfer_money_next)
    TextView tv_next;

    private String orderNo = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tranfermoneysuccess;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tv_title.setText("转账成功");
        bt_right.setText("详情");
        if (bt_right.getVisibility() == View.GONE) {
            bt_right.setVisibility(View.VISIBLE);
        }
        String nickName = getIntent().getStringExtra(IntentKey.WLF_BEAN);
        String mMoney = getIntent().getStringExtra(IntentKey.REMAINING);
        orderNo = getIntent().getStringExtra(IntentKey.WLF_Order);
        int index = getIntent().getIntExtra(IntentKey.WLF_TYPE, -1);
        if (index == RequestCode.FRIEND_TRANSFER) {
            ll_success.setVisibility(View.GONE);
            tv_success_type.setText("转账成功");
            if (!Util.isEmpty(nickName)) {
//                tv_tranfer_name.setText("转账人：" + nickName);
                tv_success_desc.setText(nickName + "已收到你的转账");
            }
        } else if (index == RequestCode.RECHARGE_MONEY) {
            tv_success_type.setText("付款成功");
            ll_success.setVisibility(View.VISIBLE);
            if (!Util.isEmpty(nickName)) {
                tv_tranfer_name.setText("付款人：" + nickName);
                tv_success_desc.setText(nickName + "已收到你的付账");
            }
        }
        if (!Util.isEmpty(mMoney)) {
            tv_success_money.setText("￥" + mMoney);
        }

    }

    @OnClick({R.id.bt_back, R.id.tv_tranfer_money_next, R.id.bt_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

            /*完成*/
            case R.id.tv_tranfer_money_next:
                IntentUtil.startActivity(this, MainActivity.class
                        , IntentKey.WLF_BEAN, 1);
                break;

            /*详情*/
            case R.id.bt_right:
                if (!Util.isEmpty(orderNo)) {
                    BillDetailActivity.startActivity(this, orderNo, "transfer_out");
                }
                break;
        }
    }
}
