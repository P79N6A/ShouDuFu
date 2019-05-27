package com.futuretongfu.ui.activity.goods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.OrderOnlineGoodsBean;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.StringUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 选择服务类型
 * Created by zhanggf on 2018/6/2.
 */

public class GoodsServiceActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_good_image)
    ImageView imgGoodImage;
    @Bind(R.id.tv_good_name)
    TextView tvGoodName;
    @Bind(R.id.tv_good_format)
    TextView tvGoodFormat;
    @Bind(R.id.tv_good_price)
    TextView tvGoodPrice;
    @Bind(R.id.tv_good_num)
    TextView tvGoodNum;
    @Bind(R.id.rl_goodservice_onlymoney)
    RelativeLayout rlGoodserviceOnlymoney;
    @Bind(R.id.rl_goodservice_moneygoods)
    RelativeLayout rlGoodserviceMoneygoods;
    OrderOnlineGoodsBean entity =null;
    private String onlineOrderNo,orderStatus;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_service;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvTitle.setText("选择服务类型");
        CacheActivityUtil.addNewActivity(this);
        entity = (OrderOnlineGoodsBean) getIntent().getSerializableExtra("entity");
        onlineOrderNo = getIntent().getStringExtra("onlineOrderNo");
        orderStatus = getIntent().getStringExtra("orderStatus");
        if (entity!=null){
            tvGoodName.setText( StringUtil.getSafeString(entity.getSkuName()));
            tvGoodNum.setText("*"+ StringUtil.getSafeString(entity.getAmount()+""));
            tvGoodFormat.setText( StringUtil.getSafeString(entity.getFormat()));
            tvGoodPrice.setText( "￥" + StringUtil.fmtMicrometer(Double.parseDouble(StringUtil.getDouble2(entity.getPrice()))));
            GlideLoad.loadCrossFadeImageView2(entity.getSkuImages(),imgGoodImage);
        }
        if (orderStatus.equals("2")){
            rlGoodserviceMoneygoods.setVisibility(View.VISIBLE);
        }else {
            rlGoodserviceMoneygoods.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.bt_back, R.id.rl_goodservice_onlymoney, R.id.rl_goodservice_moneygoods})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.rl_goodservice_onlymoney:
                Intent intent = new Intent(context,GoodsRefundActivity.class);
                intent.putExtra("type",0);
                intent.putExtra("onlineOrderNo",onlineOrderNo);
                intent.putExtra("entity",entity);
                startActivity(intent);
                break;
            case R.id.rl_goodservice_moneygoods:
                Intent intent1 = new Intent(context,GoodsRefundActivity.class);
                intent1.putExtra("type",1);
                intent1.putExtra("onlineOrderNo",onlineOrderNo);
                intent1.putExtra("entity",entity);
                startActivity(intent1);
                break;
        }
    }
}
