package com.futuretongfu.ui.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.UploadVoucheBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineGoodsBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.ui.activity.goods.GoodsEvaluateActivity;
import com.futuretongfu.ui.activity.goods.OrderOnlineDetailsActivity;
import com.futuretongfu.ui.activity.goods.OrderOnlineStateActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.Util;

import java.io.Serializable;
import java.util.List;

/**
 * 线上订单
 * Created by zhanggf on 2018/05/29.
 */

public class UploadVoucheAdapter extends BaseQuickAdapter<UploadVoucheBean, BaseViewHolder> {

    private Activity context;
    private OrderConsumerAdapterListener orderConsumerAdapterListener;

    public UploadVoucheAdapter(Activity context, List<UploadVoucheBean> datas, OrderConsumerAdapterListener orderConsumerAdapterListener) {
        super(R.layout.item_uploadvouche_list, datas);
        this.context = context;
        this.orderConsumerAdapterListener = orderConsumerAdapterListener;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final UploadVoucheBean item) {
        holder.setText(R.id.tv_upload_orderno, StringUtil.getSafeString(item.getOrderNo()));
        holder.setText(R.id.tv_upload_money,"¥ "+item.getMoney());
        holder.setText(R.id.tv_upload_paymoney,"¥ "+item.getRatioFee());
        holder.setText(R.id.tv_upload_tongbei, item.getJifen()+"");
        holder.setText(R.id.tv_upload_time,  DateUtil.getOrderTime(item.getCreateTime()));
        TextView orderIcon = holder.getView(R.id.tv_upload_ordericon);
        ImageView checkstate = holder.getView(R.id.image_checkstate);
        TextView btn_comment = holder.getView(R.id.btn_comment);
        switch (item.getCheckStatus()) {
            case Constants.OrderCheckStatus_Examine_Doing:
                btn_comment.setVisibility(View.GONE);
                checkstate.setVisibility(View.VISIBLE);
                checkstate.setImageResource(R.mipmap.icon_upload_check);
                orderIcon.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.icon_upload_checking),null,null,null);
                break;
            case Constants.OrderCheckStatus_Examine_Success:
                btn_comment.setVisibility(View.VISIBLE);
                btn_comment.setText("付款");
                checkstate.setVisibility(View.GONE);
                orderIcon.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.icon_upload_checkpass),null,null,null);
                btn_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderConsumerAdapterListener != null)
                            orderConsumerAdapterListener.onUploadVoucheOrderPay(item.getOrderNo());
                    }
                });
                break;
            case Constants.OrderCheckStatus_Examine_Faile:
                btn_comment.setVisibility(View.VISIBLE);
                btn_comment.setText("重新提交");
                checkstate.setVisibility(View.GONE);
                orderIcon.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.icon_upload_checkfaile),null,null,null);
                btn_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderConsumerAdapterListener != null)
                            orderConsumerAdapterListener.onUploadVoucheOrderUpdate(item.getId(),item.getMoney(),item.getJifen(),item.getRatioFee());
                    }
                });
                break;
            case Constants.OrderCheckStatus_Examine_Finish:
                btn_comment.setVisibility(View.GONE);
                checkstate.setVisibility(View.VISIBLE);
                checkstate.setImageResource(R.mipmap.icon_upload_finish);
                orderIcon.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.icon_upload_checkpass),null,null,null);
                break;
            default:
                break;
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (orderConsumerAdapterListener != null)
                    orderConsumerAdapterListener.onUploadVoucheOrderDel(item.getId());
                return false;
            }
        });
    }


    public interface OrderConsumerAdapterListener {
        public void onUploadVoucheOrderPay(String orderNo);
        public void onUploadVoucheOrderUpdate(String id, double money, double jifen,double ratioFee);
        public void onUploadVoucheOrderDel(String id);
    }

}
