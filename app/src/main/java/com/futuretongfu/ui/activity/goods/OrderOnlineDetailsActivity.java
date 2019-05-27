package com.futuretongfu.ui.activity.goods;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.OrderOnlineDetailsBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineGoodsBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IOrderDetailsOnlineDetailsView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.OrderOnlineDetailsPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.order.OrderDetailsActivity;
import com.futuretongfu.ui.activity.order.UploadVoucheActivity;
import com.futuretongfu.ui.adapter.OrderOnlineGoodsAdapter;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/5/30.
 * 订单详情
 * 张
 */

public class OrderOnlineDetailsActivity extends BaseActivity implements IOrderDetailsOnlineDetailsView {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_aorderdetails_name)
    TextView tvAorderdetailsName;
    @Bind(R.id.tv_aorderdetails_address)
    TextView tvAorderdetailsAddress;
    @Bind(R.id.tv_aorderdetails_phone)
    TextView tv_aorderdetails_phone;
    @Bind(R.id.img_aorderdetails_storeimage)
    ImageView imgAorderdetailsStoreimage;
    @Bind(R.id.rv_aorderdetails_storename)
    TextView rvAorderdetailsStorename;
    @Bind(R.id.rv_aorderdetails_goods)
    RecyclerView rvAorderdetailsGoods;
    @Bind(R.id.tv_aorderdetails_fee)
    TextView tvAorderdetailsFee;
    @Bind(R.id.tv_aorderdetails_realmoney)
    TextView tvAorderdetailsRealmoney;
    @Bind(R.id.tv_aorderdetails_orderno)
    TextView tvAorderdetailsOrderno;
    @Bind(R.id.tv_aorderdetails_createtime)
    TextView tvAorderdetailsCreatetime;
    @Bind(R.id.tv_aorderdetails_paytime)
    TextView tvAorderdetailsPaytime;
    @Bind(R.id.tv_aorderdetails_status)
    TextView tvAorderdetailsStatus;
    @Bind(R.id.tv_aorderdetails_difftime)
    TextView tvAorderdetailsDiffTime;
    @Bind(R.id.activity_evaluate)
    LinearLayout activityEvaluate;
    @Bind(R.id.tv_aorderdetails_zhifufangshi)
    TextView textView_zhifufangshi;
    @Bind(R.id.btn_confirms)
    TextView mbtn_confirms;
    private OrderOnlineDetailsPresenter mPresenter;
    private String orderId,onlineOrderNo;
    private String orderStatus;
    private OrderOnlineGoodsAdapter adapter;
    private OrderOnlineDetailsBean result =null;
    String pz;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_orderonline_details;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        tvTitle.setText("订单详情");
        mPresenter = new OrderOnlineDetailsPresenter(this,this);
        orderId = getIntent().getStringExtra("id");
        onlineOrderNo = getIntent().getStringExtra("onlineOrderNo");
        orderStatus = getIntent().getStringExtra("orderStatus");
        pz=getIntent().getStringExtra("pingzheng");
        Util.setRecyclerViewLayoutManager(context, rvAorderdetailsGoods, R.color.transparent, 1);
        List<OrderOnlineGoodsBean> orderDetailList =new ArrayList<>();
        adapter = new OrderOnlineGoodsAdapter(this,orderDetailList,onlineOrderNo,orderStatus);
        rvAorderdetailsGoods.setAdapter(adapter);
        mPresenter.onorderOnlineDetails(UserManager.getInstance().getUserId()+"",orderId);
    }


    @OnClick({R.id.bt_back, R.id.tv_aorderdetails_copy,R.id.tv_aorderdetails_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.tv_aorderdetails_copy:
                if (result==null)
                    return;
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(result.getOnlineOrderNo());
                showToast("复制成功");
                break;
            case R.id.tv_aorderdetails_call:
                if (result==null)
                    return;
                AppUtil.openPhoneService(this, result.getTellPhone());
                break;
        }
    }

    @Override
    public void onOrderDetailsOnlineDetailsFail(int code, String msg) {
      //  showToast(msg);
    }

    @Override
    public void onOrderDetailsOnlineDetailsSuccess(final OrderOnlineDetailsBean data) {
        result = data;
        tvAorderdetailsStatus.setText(StringUtil.getOrderOnlineStatues(data.getOrderStatus()));

        if (data.getOrderStatus()==0){
            addTime = result.getCreateDate();
            currentTime = System.currentTimeMillis();
            runnable.run();
        }
        textView_zhifufangshi.setText(StringUtil.getSafeString(StringUtil.getOrderPayCashPayType(data.getPayType())));
        tvAorderdetailsAddress.setText("收货地址："+data.getReceiverAddress());
        tv_aorderdetails_phone.setText(data.getReceiverMobile());
        tvAorderdetailsName.setText("收货人："+data.getReceiverName());
        rvAorderdetailsStorename.setText(data.getStoreName());
        tvAorderdetailsFee.setText("¥"+data.getLogisticsFee());
        String paytype=data.getPayType();
        if (paytype.equals("WECHATPAY_BALANCE")){
            tvAorderdetailsRealmoney.setText("¥ 微信:"+data.getWechatMoney()+"平台:"+data.getPlatMoney());

        }else if (paytype.equals("ALIPAY_BALANCE")){
            tvAorderdetailsRealmoney.setText("¥ 支付宝:"+data.getAlipayMoney()+"平台:"+data.getPlatMoney());

        }else if (paytype.equals("WECHATPAY")){
            tvAorderdetailsRealmoney.setText(data.getTotalMoney()+"");

        }else if (paytype.equals("ALIPAY")){
            tvAorderdetailsRealmoney.setText(data.getTotalMoney()+"");

        }
        tvAorderdetailsOrderno.setText("订单编号："+data.getOnlineOrderNo());
        tvAorderdetailsCreatetime.setText("创建时间：" +DateUtil.getDateStr3(data.getCreateDate()));
        if (data.getOrderStatus()==0){
            tvAorderdetailsPaytime.setVisibility(View.GONE);
        }else {
            tvAorderdetailsPaytime.setVisibility(View.VISIBLE);
            tvAorderdetailsPaytime.setText("付款时间："+ DateUtil.getDateStr3(data.getPayDate()));
        }
        adapter.setNewData(data.getOrderDetailList());
        GlideLoad.loadCropCircle(data.getStoreLogo(),imgAorderdetailsStoreimage);

        String sc = tvAorderdetailsStatus.getText().toString();
        if (pz.equals("pingzheng")){
            mbtn_confirms.setVisibility(View.VISIBLE);
            mbtn_confirms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PromptDialog
                            .Builder(context)
                            .setTitle("上传消费凭证")
                            .setMessage(R.string.dlg_confirm_shou_huo2)
                            .setButton1(R.string.cancel, new PromptDialog.OnClickListener() {
                                @Override
                                public void onClick(Dialog dialog, int which) {
                                    dialog.dismiss();
                                    Toast.makeText(OrderOnlineDetailsActivity.this, "进入确认收货页面", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setButton2("上传", new PromptDialog.OnClickListener() {
                                @Override
                                public void onClick(Dialog dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(getContext(), UploadVoucheActivity.class);
                                    intent.putExtra("isOpereteType", false);
                                    intent.putExtra("xiaofeiType", 2);
                                    intent.putExtra("money", data.getTotalMoney());
                                    intent.putExtra("id", data.getId());
                                    intent.putExtra("orderNo", onlineOrderNo);
                                    Log.e("订单哈", data.getOnlineOrderNo());
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            });
        }else {
            mbtn_confirms.setVisibility(View.GONE);

        }


    }

    private long addTime,currentTime;
    final Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (DateUtil.isDifferTime(currentTime,addTime)) {
                tvAorderdetailsDiffTime.setText("订单已过期");
                return;
            }
            DateUtil.setDateInfo(addTime,tvAorderdetailsDiffTime);
            handler.postDelayed(this, 1000);
        }
    };

}
