package com.futuretongfu.ui.activity.order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.OrderSellDetail;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.order.OrderDetailsPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;
import com.futuretongfu.iview.IOrderDetailsView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *     销售订单详情
 *
 * @author ChenXiaoPeng
 */
public class OrderDetailsActivity extends BaseActivity implements IOrderDetailsView {

    private final static String Intent_Extra_StoreId = "storeId";
    private final static String Intent_Extra_OrderNo = "orderNo";

    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.imgv_photo)
    public ImageView imgvPhoto;
    @Bind(R.id.text_name)
    public TextView textName;
    @Bind(R.id.text_time)
    public TextView textTime;

    @Bind(R.id.imgv_xfpz)
    public ImageView imgvXfpz;
    @Bind(R.id.text_examine_doing)
    public TextView textExamineDoing;
    @Bind(R.id.text_examine_faile)
    public TextView textExamineFaile;

    @Bind(R.id.text_order_no)
    public TextView textOrderNo;
    @Bind(R.id.text_all_money)
    public TextView textAllMoney;
    @Bind(R.id.text_real_money)
    public TextView textRealMoney;

    @Bind(R.id.view_shenhe)
    public LinearLayout viewShenhe;
    @Bind(R.id.text_time_sh)
    public TextView textTimeSh;

    @Bind(R.id.text_is_tongbei)
    public TextView textIsTongBei;

    @Bind(R.id.view_tuihuo)
    public LinearLayout viewTuiHuo;
    @Bind(R.id.text_tuihuo)
    public TextView textTuiHuo;

    @Bind(R.id.btn_confirm)
    public Button btnConfirm;

    private long storeId;
    private long orderNo;
    private OrderDetailsPresenter orderDetailsPresenter;

    /************************************************************************/
    @Override
    protected int getLayoutId(){
        return R.layout.activity_order_details;
    }

    @Override
    protected Presenter getPresenter(){
        return orderDetailsPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState){
        textTitle.setText("订单详情");

        Intent intent = getIntent();
        storeId = intent.getLongExtra(Intent_Extra_StoreId, -1);
        orderNo = intent.getLongExtra(Intent_Extra_OrderNo, -1);

        orderDetailsPresenter = new OrderDetailsPresenter(this, this);
    }

    @Override
    public void onResume(){
        super.onResume();

        if(-1 == orderNo)
            return ;

        showProgressDialog();
        orderDetailsPresenter.getOrderDetail(storeId, orderNo);

    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }
    /************************************************************************/

    /************************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack(){
        finish();
    }

    @OnClick(R.id.btn_confirm)
    public void onClickConfirm(){
        if(-1 == orderNo)
            return ;

        new PromptDialog.Builder(getContext())
                .setMessage("确认退货")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                        showProgressDialog();
                        orderDetailsPresenter.confirmOrderBack(UserManager.getInstance().getUserId(), orderNo);

                    }
                })
                .show();

    }
    /************************************************************************/

    /************************************************************************/
    //获取销售订单详情 成功
    @Override
    public void onOrderDetailGetSuccess(OrderSellDetail data){
        hideProgressDialog();
        updateView(data);
    }

    //获取销售订单详情 失败
    @Override
    public void onOrderDetailGetFaile(String msg){
        hideProgressDialog();
        showToast(msg);
    }

    //确认退货 成功
    @Override
    public void onOrderDetailsrConfirmOrderBackSuccess(){
        orderDetailsPresenter.getOrderDetail(storeId, orderNo);
        finish();
    }

    //确认退货 失败
    @Override
    public void onOrderDetailsConfirmOrderBackFaile(String msg){
        hideProgressDialog();
        showToast(msg);
    }
    /************************************************************************/

    private void updateView(OrderSellDetail data){
        GlideLoad.loadCrossFadeImageView(
                data.getUserFaceUrl()
                , R.mipmap.icon_head_2
                , R.mipmap.icon_head_2
                , imgvPhoto
        );

        textName.setText(StringUtil.getNoEmptyStr(data.getUserNickName(), "未命名"));
        textTime.setText(DateUtil.getDateStrWithHour1(data.getCreateTime()));

        //消费凭证审核状态
        switch (data.getOrderCheckStatus()){
            case Constants.OrderCheckStatus_Examine_No://未上传审核
                textExamineDoing.setVisibility(View.VISIBLE);
                textExamineDoing.setText("用户未上传");
                textExamineFaile.setVisibility(View.GONE);
                viewShenhe.setVisibility(View.GONE);
                break;

            case Constants.OrderCheckStatus_Examine_Doing://待审核
                if(data.getImgStr() != null && (!TextUtils.isEmpty(data.getImgStr()))){
                    imgvPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    GlideLoad.load(data.getImgStr(), imgvXfpz);
                }
                textExamineDoing.setVisibility(View.VISIBLE);
                textExamineDoing.setText("审核中");
                textExamineFaile.setVisibility(View.GONE);
                viewShenhe.setVisibility(View.GONE);
                break;

            case Constants.OrderCheckStatus_Examine_Success://审核通过
                if(data.getImgStr() != null && (!TextUtils.isEmpty(data.getImgStr()))){
                    imgvPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    GlideLoad.load(data.getImgStr(), imgvXfpz);
                }
                textExamineDoing.setVisibility(View.GONE);
                textExamineFaile.setVisibility(View.GONE);

                viewShenhe.setVisibility(View.VISIBLE);
                textTimeSh.setText(DateUtil.getDateStrWithHour1(data.getTicketCheckTime()));

                break;

            case Constants.OrderCheckStatus_Examine_Faile://审核未通过
                if(data.getImgStr() != null && (!TextUtils.isEmpty(data.getImgStr()))){
                    imgvPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    GlideLoad.load(data.getImgStr(), imgvXfpz);
                }
                textExamineDoing.setVisibility(View.GONE);
                textExamineFaile.setVisibility(View.VISIBLE);
                viewShenhe.setVisibility(View.GONE);

                break;
        }

        textOrderNo.setText(data.getOrderNo() + "");
        textAllMoney.setText("￥" + StringUtil.fmtMicrometer(data.getTotalMoney()));
        textRealMoney.setText("￥" + StringUtil.fmtMicrometer(data.getRealMoney()));

        textIsTongBei.setText(StringUtil.fmtMicrometer(data.getFeeJifen()));

        //退款中
        if(Constants.OrderConsumer_Status_Refunding == data.getOrderStatus()){
            viewTuiHuo.setVisibility(View.VISIBLE);
            textTuiHuo.setText("已申请");
            btnConfirm.setVisibility(View.VISIBLE);
        }
        else{
            viewTuiHuo.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
        }
    }

    public static void startActivity(Context context, long storeId, long orderNo){
        Intent intent = new Intent(context, OrderDetailsActivity.class);
        intent.putExtra(Intent_Extra_StoreId, storeId);
        intent.putExtra(Intent_Extra_OrderNo, orderNo);
        context.startActivity(intent);
    }
}
