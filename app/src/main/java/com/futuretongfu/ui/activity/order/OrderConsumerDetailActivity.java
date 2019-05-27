package com.futuretongfu.ui.activity.order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IOrderConsumerDetailView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.order.OrderConsumerDetailPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.PaymentSetMoneyActivity;
import com.futuretongfu.ui.activity.StoreDetailsActivity2;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.PhotoSelectUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.model.entity.OrderDetail;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

/**
 * 消费订单详情
 *
 * @author ChenXiaoPeng
 */

public class OrderConsumerDetailActivity extends BaseActivity implements IOrderConsumerDetailView {

    private final static String Intent_Extra_Order = "orderList";

    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.text_name)
    public TextView textName;
    @Bind(R.id.text_order_statues)
    public TextView textOrderStatues;

    @Bind(R.id.imgv_photo)
    public ImageView imgvPhoto;
    @Bind(R.id.text_examine_doing)
    public TextView textExamineDoing;
    @Bind(R.id.text_examine_faile)
    public TextView textExamineFaile;

    @Bind(R.id.text_order_no)
    public TextView textOrderNo;
    @Bind(R.id.text_create_time)
    public TextView textCreateTime;
    @Bind(R.id.text_update_time)
    public TextView textUpdateTime;

    @Bind(R.id.text_apply_for_returns)
    public TextView textApplyForReturns;

    @Bind(R.id.text_account)
    public TextView textAccount;
    @Bind(R.id.text_account_sore)
    public TextView textAccountSore;

    @Bind(R.id.btn_confirm)
    public TextView btnConfirm;

    private long orderNo;
    private OrderDetail orderDetail;
    private OrderConsumerDetailPresenter orderConsumerDetailPresenter;
    private String tickPath="";
    private double money;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_consumer_detail;
    }

    @Override
    protected Presenter getPresenter() {
        return orderConsumerDetailPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("订单详情");

        Intent intent = getIntent();
        orderNo = intent.getLongExtra(Intent_Extra_Order, 0);

        orderConsumerDetailPresenter = new OrderConsumerDetailPresenter(this, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        orderConsumerDetailPresenter.orderConsumerDetail(orderNo);
    }

    /***********************************************************************/

    /***********************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //图片选择器 结果
        if (requestCode == PhotoPicker.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

                if (photos.size() > 0) {

                    if (null == orderDetail)
                        return;

                    tickPath = photos.get(0);
                    imgvPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    GlideLoad.load(tickPath, imgvPhoto);

                    showProgressDialog();
//                    orderConsumerDetailPresenter.UploadVoucheImage(orderDetail.getStoreId(), orderDetail.getOrderNo(), tickPath,
//                            "","","",1);
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (PermissionUtil.isReadStoragePermission(permissions)) {
            if (PermissionUtil.getPermissionReadStorageResult(permissions, grantResults)) {
                PhotoSelectUtil.openImageSingleSelector(this);
            } else {
                showToast(R.string.err_permission_read_photo);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    /***********************************************************************/

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
        //CacheActivityUtil.finishTail(MainActivity.class);
    }

    //店铺详情页
    @OnClick(R.id.view_click_name)
    public void onClickStoreName() {
        if (null == orderDetail)
            return;

        IntentUtil.startActivity(this, StoreDetailsActivity2.class
                , IntentKey.WLF_ID, "" + orderDetail.getStoreId());
    }

    //上传消费凭证
    @OnClick(R.id.imgv_photo)
    public void onClickImgvPhoto() {
        if (Constants.OrderCheckStatus_Examine_Doing == orderDetail.getOrderCheckStatus()
                || Constants.OrderCheckStatus_Examine_Success == orderDetail.getOrderCheckStatus()
                ) {
            return;
        }

        if (PermissionUtil.permissionReadStorage(this))
            PhotoSelectUtil.openImageSingleSelector(this);
        //TODO  待需求完全确定后删除
        //UploadVoucheActivity.startActivity(this, orderDetail.getStoreId(), orderDetail.getOrderNo());
    }

    //申请退货
    @OnClick(R.id.text_apply_for_returns)
    public void onClickApplyForReturns() {
        if (null == orderDetail)
            return;

        new PromptDialog.Builder(this)
                .setMessage(R.string.dlg_confirm_tui_huo)
                .setButton1(R.string.cancel, new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2(R.string.confirm, new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                        showProgressDialog();
                        orderConsumerDetailPresenter.orderBack(orderDetail.getOrderNo());
                        btnConfirm.setVisibility(View.GONE);
                    }
                })
                .show();

    }

    //底部 按钮
    @OnClick(R.id.btn_confirm)
    public void onClickConfirm() {
        if (null == orderDetail)
            return;
        //待收货 确定收货
        if (Constants.OrderConsumer_Status_Recipient == orderDetail.getOrderStatus()) {
            showProgressDialog();
            orderConsumerDetailPresenter.orderReceive(orderDetail.getOrderNo());
        }
        //已完成 评价
        else if (Constants.OrderConsumer_Status_Finish == orderDetail.getOrderStatus()) {
            OrderConsumerCommentActivity.startActivity(context, orderDetail.getStoreId(), orderDetail.getOrderNo(), orderDetail.getLogoUrl());
        }
        //待付款
        else if (Constants.OrderConsumer_Status_Pay == orderDetail.getOrderStatus()) {

            IntentUtil.startActivity(context
                    , PaymentSetMoneyActivity.class
                    , IntentKey.WLF_ID, orderDetail.getStoreId() + ""
                    , IntentKey.WLF_Order, orderDetail.getOrderNo() + ""
                    , IntentKey.WLF_Money, orderDetail.getRealMoney()+""
            );
        }
        //待评价，已完成
        else if (Constants.OrderConsumer_Status_Comment == orderDetail.getOrderStatus()) {
            OrderConsumerCommentActivity.startActivity(context, orderDetail.getStoreId(), orderDetail.getOrderNo(), orderDetail.getLogoUrl());
        }
    }

    /***********************************************************************/
    //获取消费订单详情 成功
    @Override
    public void onOrderConsumerDetailSuccess(OrderDetail data) {
        orderDetail = data;
        updateView();
    }

    //获取消费订单详情 失败
    @Override
    public void onOrderConsumerDetailFaile(String msg) {
        showToast(msg);
    }

    //申请退货 成功
    @Override
    public void onOrderConsumeOrderBackSuccess() {
        hideProgressDialog();
        orderConsumerDetailPresenter.orderConsumerDetail(orderNo);
    }

    //申请退货 失败
    @Override
    public void onOrderConsumeOrderBackFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    //确认收货 成功
    @Override
    public void onOrderConsumeOrderReceiveSuccess() {

        return;
    }

    //确认收货 失败
    @Override
    public void onOrderConsumeOrderReceiveFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    //上传消费凭证 成功
    @Override
    public void onUploadVoucheSuccess() {
        hideProgressDialog();
        orderConsumerDetailPresenter.orderConsumerDetail(orderNo);
    }

    //上传消费凭证 失败
    @Override
    public void onUploadVoucheFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    /***********************************************************************/

    private void updateView() {
        if (null == orderDetail)
            return;

        textName.setText(StringUtil.getSafeString(orderDetail.getStoreName()));
        textOrderStatues.setText(StringUtil.getOrderConsumerStatues(orderDetail.getOrderStatus()));

        //消费凭证审核状态
        switch (orderDetail.getOrderCheckStatus()) {
            case Constants.OrderCheckStatus_Examine_No://未上传审核
                textExamineDoing.setVisibility(View.GONE);
                textExamineFaile.setVisibility(View.GONE);
                break;

            case Constants.OrderCheckStatus_Examine_Doing://待审核
                if (orderDetail.getImgStr() != null && (!TextUtils.isEmpty(orderDetail.getImgStr()))) {
                    imgvPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    GlideLoad.load(orderDetail.getImgStr(), imgvPhoto);
                }
                textExamineDoing.setVisibility(View.VISIBLE);
                textExamineFaile.setVisibility(View.GONE);
                break;

            case Constants.OrderCheckStatus_Examine_Success://审核通过
                if (orderDetail.getImgStr() != null && (!TextUtils.isEmpty(orderDetail.getImgStr()))) {
                    imgvPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    GlideLoad.load(orderDetail.getImgStr(), imgvPhoto);
                }
                textExamineDoing.setVisibility(View.GONE);
                textExamineFaile.setVisibility(View.GONE);
                break;

            case Constants.OrderCheckStatus_Examine_Faile://审核未通过
                textExamineDoing.setVisibility(View.GONE);
                textExamineFaile.setVisibility(View.VISIBLE);
                break;
        }

        textOrderNo.setText(orderDetail.getOrderNo() + "");

        long stime = orderDetail.getCreateTime();
        long utime = orderDetail.getUpdateTime();

        textCreateTime.setText(DateUtil.getOrderTime(stime));
        textUpdateTime.setText(DateUtil.getOrderTime(utime));

        money = orderDetail.getRealMoney();
        textAccount.setText("￥" + StringUtil.fmtMicrometer(orderDetail.getRealMoney()));
        textAccountSore.setText("" + StringUtil.fmtMicrometer(orderDetail.getRealCash()));

        if (Constants.OrderConsumer_Status_Recipient == orderDetail.getOrderStatus()) { //待收货
            textApplyForReturns.setEnabled(true);
            btnConfirm.setVisibility(View.VISIBLE);
            btnConfirm.setText("确认收货");
        } else if (Constants.OrderConsumer_Status_Finish == orderDetail.getOrderStatus()) {  //已完成
            textApplyForReturns.setEnabled(false);

            if (Constants.CommentStatus_Yes == orderDetail.getCommentStatus()) {
                btnConfirm.setVisibility(View.GONE);
            } else {
                btnConfirm.setVisibility(View.VISIBLE);
                btnConfirm.setText("评价");
            }
            btnConfirm.setVisibility(!((orderDetail.getOrderStatus() == 3 || orderDetail.getOrderStatus() == 5) && orderDetail.getCommentStatus() == 1) ? View.VISIBLE : View.GONE);
        } else if (Constants.OrderConsumer_Status_Pay == orderDetail.getOrderStatus()) { //待付款
            textApplyForReturns.setEnabled(false);
            btnConfirm.setVisibility(View.VISIBLE);
            btnConfirm.setText("付款");
        } else if (Constants.OrderConsumer_Status_Comment == orderDetail.getOrderStatus()) {  //待评价
            textApplyForReturns.setEnabled(false);
            btnConfirm.setVisibility(View.VISIBLE);
            btnConfirm.setText("评价");
            btnConfirm.setVisibility(!((orderDetail.getOrderStatus() == 3 || orderDetail.getOrderStatus() == 5) && orderDetail.getCommentStatus() == 1) ? View.VISIBLE : View.GONE);
        } else {
            textApplyForReturns.setEnabled(false);
            btnConfirm.setVisibility(View.GONE);
        }
    }

    public static void startActivity(Context context, long orderNo) {
        Intent intent = new Intent(context, OrderConsumerDetailActivity.class);
        intent.putExtra(Intent_Extra_Order, orderNo);
        context.startActivity(intent);
    }
}
