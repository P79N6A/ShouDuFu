package com.futuretongfu.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.ui.activity.PaymentSetMoneyActivity;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.model.entity.OrderList;
import com.futuretongfu.ui.activity.order.OrderConsumerCommentActivity;
import com.futuretongfu.ui.activity.order.OrderConsumerDetailActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.StringUtil;

import java.util.List;

/**
 * 消费订单列表
 * <p>
 * Created by ChenXiaoPeng on 2017/6/19.
 */

public class OrderConsumerAdapter extends BaseQuickAdapter<OrderList, BaseViewHolder> {

    private Context context;
    private String Yjjljf;
    private OrderConsumerAdapterListener orderConsumerAdapterListener;

    public OrderConsumerAdapter(Context context, List<OrderList> datas, OrderConsumerAdapterListener orderConsumerAdapterListener) {
        super(R.layout.item_order_consumer_list, datas);
        this.context = context;
        this.Yjjljf = context.getResources().getString(R.string.jifen_yjjl);
        this.orderConsumerAdapterListener = orderConsumerAdapterListener;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final OrderList item) {

        holder.setText(R.id.text_name, StringUtil.getSafeString(item.getStoreName()));

        holder.setText(R.id.text_account, "￥" + StringUtil.fmtMicrometer(Double.parseDouble(StringUtil.getDouble2(item.getRealMoney()))));
        holder.setText(R.id.text_jifen, Yjjljf + StringUtil.fmtMicrometer(Double.parseDouble(StringUtil.getDouble2(item.getRealCash()))));
        holder.setText(R.id.text_create_time, DateUtil.getOrderTime(item.getCreateTime()));
        holder.setText(R.id.text_state, StringUtil.getOrderConsumerStatues(item.getOrderStatus()));

        GlideLoad.loadCrossFadeImageView2(StringUtil.getSafeString(item.getLogoUrl()), (ImageView) holder.getView(R.id.imgv_pic));

        Button btn = holder.getView(R.id.btn_comment);
        switch (item.getOrderStatus()) {
            case Constants.OrderConsumer_Status_Recipient://待收货
                btn.setVisibility(View.VISIBLE);
                btn.setText("确认收货");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderConsumerDetailActivity.startActivity(context, item.getOrderNo());
                      /*  new PromptDialog
                                .Builder(context)
                                .setMessage(R.string.dlg_confirm_shou_huo)
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
                                        if (orderConsumerAdapterListener != null)
                                            orderConsumerAdapterListener.onOrderConsumerAdapterOrderReceive(item.getOrderNo());
                                    }
                                })
                                .show();*/
                    }
                });
                break;

            case Constants.OrderConsumer_Status_Pay://待付款
                btn.setVisibility(View.VISIBLE);
                btn.setText("付款");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtil.startActivity(context
                                , PaymentSetMoneyActivity.class
                                , IntentKey.WLF_ID, item.getStoreId() + ""
                                , IntentKey.WLF_Order, item.getOrderNo() + ""
                                , IntentKey.WLF_Money, item.getRealMoney() + ""
                        );
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (orderConsumerAdapterListener != null)
                            orderConsumerAdapterListener.onOrderConsumerAdapterOrderDel(item.getOrderNo());
                        return false;
                    }
                });
                break;

            case Constants.OrderConsumer_Status_Comment://待评价
                btn.setVisibility(View.VISIBLE);
                btn.setText("评价");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderConsumerCommentActivity.startActivity(context, item.getStoreId(), item.getOrderNo(), item.getLogoUrl());
                    }
                });
                holder.setVisible(R.id.btn_comment, !((item.getOrderStatus() == 3 || item.getOrderStatus() == 5) && item.getCommentStatus() == 1));
                break;

            case Constants.OrderConsumer_Status_Finish://已完成 退款成功
                if (Constants.CommentStatus_Yes == item.getCommentStatus()) {
                    btn.setVisibility(View.GONE);
                } else {
                    btn.setVisibility(View.VISIBLE);
                    btn.setText("评价");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OrderConsumerCommentActivity.startActivity(context, item.getStoreId(), item.getOrderNo(), item.getLogoUrl());
                        }
                    });
                }
                holder.setVisible(R.id.btn_comment, !((item.getOrderStatus() == 3 || item.getOrderStatus() == 5) && item.getCommentStatus() == 1));
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (orderConsumerAdapterListener != null)
                        orderConsumerAdapterListener.onOrderConsumerAdapterOrderDel(item.getOrderNo());
                    return false;
                }
            });
                break;

            default:
                btn.setVisibility(View.GONE);
                break;
        }
        //进入详情
        holder.setOnClickListener(R.id.view_click, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderConsumerDetailActivity.startActivity(context, item.getOrderNo());
            }
        });

    }

    public interface OrderConsumerAdapterListener {
        //确认收货
        public void onOrderConsumerAdapterOrderReceive(long orderNo);
        public void onOrderConsumerAdapterOrderDel(long orderNo);
    }

}
