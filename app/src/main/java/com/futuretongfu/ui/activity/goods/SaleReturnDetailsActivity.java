package com.futuretongfu.ui.activity.goods;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.SaleReturnDetailsBean;
import com.futuretongfu.iview.ISaleReturnDetailsView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.SaleReturnDetailsPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.StringUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/6/4.
 */

public class SaleReturnDetailsActivity extends BaseActivity implements ISaleReturnDetailsView {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_asaledetails_state)
    TextView tvAsaledetailsState;
    @Bind(R.id.tv_asaledetails_date)
    TextView tvAsaledetailsDate;
    @Bind(R.id.tv_asaledetails_payprice)
    TextView tvAsaledetailsPayprice;
    @Bind(R.id.tv_asaledetails_method)
    TextView tvAsaledetailsMethod;
    @Bind(R.id.tv_asaledetails_methodprice)
    TextView tvAsaledetailsMethodprice;
    @Bind(R.id.img_good_image)
    ImageView imgGoodImage;
    @Bind(R.id.tv_good_name)
    TextView tvGoodName;
    @Bind(R.id.tv_good_format)
    TextView tvGoodFormat;
    @Bind(R.id.tv_asaledetails_reason)
    TextView tvAsaledetailsReason;
    @Bind(R.id.tv_asaledetails_price)
    TextView tvAsaledetailsPrice;
    @Bind(R.id.tv_asaledetails_time)
    TextView tvAsaledetailsTime;
    @Bind(R.id.tv_asaledetails_orderno)
    TextView tvAsaledetailsOrderno;
    private SaleReturnDetailsPresenter mPresenter;
    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sale_details;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        id = getIntent().getStringExtra("id");
        mPresenter = new SaleReturnDetailsPresenter(this, this);
        mPresenter.getsaleReturnStateList(UserManager.getInstance().getUserId()+"",id);
    }

    @Override
    public void onSaleReturnDetailsSuccess(SaleReturnDetailsBean datas) {
        tvTitle.setText("退款详情");
        tvGoodFormat.setText(datas.getFormat());
        tvGoodName.setText(StringUtil.getSafeString(datas.getSkuName()));
        GlideLoad.loadCrossFadeImageView2(datas.getSkuImages(), imgGoodImage);
        tvAsaledetailsPayprice.setText("￥" + StringUtil.fmtMicrometer(Double.parseDouble(StringUtil.getDouble2(datas.getTotalPrice()))));
        tvAsaledetailsMethodprice.setText("￥" + StringUtil.fmtMicrometer(Double.parseDouble(StringUtil.getDouble2(datas.getTotalPrice()))));
        tvAsaledetailsReason.setText("退款原因："+ (TextUtils.isEmpty(datas.getReason())?"无":datas.getReason()));
        tvAsaledetailsPrice.setText("退款金额￥："+StringUtil.fmtMicrometer(Double.parseDouble(StringUtil.getDouble2(datas.getTotalPrice()))));
        tvAsaledetailsOrderno.setText("退款编号："+datas.getReturnNo());
        tvAsaledetailsDate.setText(DateUtil.getDateStrWithHour1(datas.getAcceptDate()));
        tvAsaledetailsTime.setText("申请时间："+DateUtil.getDateStr1(datas.getCreateDate()));
        tvAsaledetailsState.setText(StringUtil.getOrderOnlineStatues(datas.getOrderStatus()));
    }

    @Override
    public void onSaleReturnDetailsFaile(String msg) {
        showToast(msg);
    }

    @OnClick(R.id.bt_back)
    public void onViewClicked() {
        finish();
    }
}
