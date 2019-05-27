package com.futuretongfu.ui.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.OrderOnlineBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineGoodsBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.ui.activity.goods.GoodsEvaluateActivity;
import com.futuretongfu.ui.activity.goods.OrderOnlineDetailsActivity;
import com.futuretongfu.ui.activity.goods.OrderOnlineStateActivity;
import com.futuretongfu.ui.activity.order.UploadVoucheActivity;
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

public class OrderOnlineAdapter extends BaseQuickAdapter<OrderOnlineBean, BaseViewHolder> {

    private Activity context;
    private OrderConsumerAdapterListener orderConsumerAdapterListener;
    private TextView tv_difftime;
    private String orderId;
    private boolean isPayBallance;

    public OrderOnlineAdapter(Activity context, List<OrderOnlineBean> datas, OrderConsumerAdapterListener orderConsumerAdapterListener) {
        super(R.layout.item_order_online_list, datas);
        this.context = context;
        this.orderConsumerAdapterListener = orderConsumerAdapterListener;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final OrderOnlineBean item) {
        holder.setText(R.id.tv_store_name, StringUtil.getSafeString(item.getStoreName()));
        holder.setText(R.id.tv_order_state, StringUtil.getOrderOnlineStatues(item.getOrderStatus()));
        RecyclerView recyclerList = holder.getView(R.id.recycler_goods);
        Util.setRecyclerViewLayoutManager(context, recyclerList, R.color.transparent, 1);
        OrderOnlineGoodsAdapter adapter = new OrderOnlineGoodsAdapter(context,item.getOrderDetailList(),"","");
        recyclerList.setAdapter(adapter);
        List<OrderOnlineGoodsBean> orderDetailList = item.getOrderDetailList();
        int amount =0;
        if (orderDetailList!=null&&orderDetailList.size()>0){
            for (int j = 0; j < orderDetailList.size(); j++) {
                if (orderDetailList.get(0).getPrice()/orderDetailList.get(0).getSendTongbei()>=2){
                    isPayBallance = true;
                }else {
                    isPayBallance = false;
                }
                OrderOnlineGoodsBean productInfo = orderDetailList.get(j);
                amount += productInfo.getAmount();
            }
        }
        if (item.getLogisticsFee() <= 0) {
            holder.setText(R.id.tv_all_message, "共" + amount+ "件商品，合计：￥" + item.getTotalMoney());
        } else {
            holder.setText(R.id.tv_all_message, "共" +amount+ "件商品，合计：￥" + item.getTotalMoney() + "(含运费" + item.getLogisticsFee() + ")");
        }
        GlideLoad.loadCropCircle(item.getStoreLogo(), (ImageView) holder.getView(R.id.tv_store_logo));
        orderId = item.getId();
        RelativeLayout rl_comment = holder.getView(R.id.rl_comment);
        TextView btn_comment1 = holder.getView(R.id.btn_comment1);
        TextView btn_comment2 = holder.getView(R.id.btn_comment2);
        TextView btn_comment3 = holder.getView(R.id.btn_comment3);
        tv_difftime = holder.getView(R.id.tv_difftime);
        switch (item.getOrderStatus()) {
            case Constants.OrderOnline_Status_Pay://待付款
                rl_comment.setVisibility(View.VISIBLE);
                btn_comment1.setVisibility(View.VISIBLE);
                btn_comment2.setVisibility(View.VISIBLE);
                btn_comment3.setVisibility(View.VISIBLE);

                btn_comment1.setText("付款");
                btn_comment2.setText("取消订单");
                btn_comment3.setText("删除订单");
                differTime = item.getExpireTime();
                runnable.run();
                btn_comment1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderConsumerAdapterListener != null)
                            orderConsumerAdapterListener.onOrderConsumerAdapterOrderPay(item.getOnlineOrderNo(),item.getSellerId(),isPayBallance);
                    }
                });
                btn_comment2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new PromptDialog
                                .Builder(context)
                                .setMessage("您确定取消订单？")
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
                                        if (orderConsumerAdapterListener != null)
                                            orderConsumerAdapterListener.onOrderConsumerAdapterOrderCancel(item.getId());
                                    }
                                })
                                .show();
                    }
                });
                btn_comment3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new PromptDialog
                                .Builder(context)
                                .setMessage("您确定删除订单？")
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
                                        if (orderConsumerAdapterListener != null)
                                            orderConsumerAdapterListener.onOrderConsumerAdapterOrderDelete(item.getId());

                                    }
                                })
                                .show();
                    }
                });
                break;
            case Constants.OrderOnline_Status_Ship://待发货
                rl_comment.setVisibility(View.VISIBLE);
                btn_comment1.setVisibility(View.VISIBLE);
                btn_comment2.setVisibility(View.GONE);
                btn_comment3.setVisibility(View.GONE);
                btn_comment1.setText("提醒发货");
                btn_comment1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderConsumerAdapterListener.onOrderConsumerAdapterOrdeRemain(item.getOnlineOrderNo());
                    }
                });
                break;
            case Constants.OrderConsumer_Status_Recipient://待收货
                rl_comment.setVisibility(View.VISIBLE);
                btn_comment1.setVisibility(View.VISIBLE);
                btn_comment2.setVisibility(View.VISIBLE);
                btn_comment3.setVisibility(View.GONE);
                btn_comment1.setText("确认收货");
                btn_comment2.setText("查看物流");
                btn_comment3.setText("延长收货");
                btn_comment1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtil.startActivity(context,OrderOnlineDetailsActivity.class,"id",item.getId(),"onlineOrderNo",item.getOnlineOrderNo(),
                                "orderStatus",item.getOrderStatus()+"","pingzheng","pingzheng","","");

                        orderConsumerAdapterListener.onOrderConsumerAdapterOrderReceive(item.getOnlineOrderNo(),item.getSellerId(),item.getTotalMoney());
                    }
                });
                btn_comment2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtil.startActivity(context,OrderOnlineStateActivity.class,"id",item.getId(),"onlineOrderNo",item.getOnlineOrderNo());
                    }
                });
                btn_comment3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new PromptDialog
                                .Builder(context)
                                .setTitle("您确定延长收货?")
                                .setMessage("每笔订单只能延长一次？")
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
                                        if (orderConsumerAdapterListener != null)
                                            orderConsumerAdapterListener.onOrderConsumerAdapterOrderExtend(item.getId());

                                    }
                                })
                                .show();
                    }
                });
                break;
            case Constants.OrderOnline_Status_Comment://待评价
                rl_comment.setVisibility(View.VISIBLE);
                btn_comment1.setVisibility(View.VISIBLE);
                btn_comment2.setVisibility(View.VISIBLE);
                btn_comment3.setVisibility(View.VISIBLE);
                btn_comment1.setText("评价");
                btn_comment2.setText("查看物流");
                btn_comment3.setText("删除订单");

                btn_comment1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context,GoodsEvaluateActivity.class);
                        intent.putExtra("orderNo",item.getOnlineOrderNo());
                        intent.putExtra("goodsList", (Serializable) item.getOrderDetailList());
                        context.startActivity(intent);
                    }
                });

                btn_comment2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentUtil.startActivity(context,OrderOnlineStateActivity.class,"id",item.getId(),"onlineOrderNo",item.getOnlineOrderNo());
                    }
                });
                btn_comment3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new PromptDialog
                                .Builder(context)
                                .setMessage("您确定删除订单？")
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
                                        if (orderConsumerAdapterListener != null)
                                            orderConsumerAdapterListener.onOrderConsumerAdapterOrderDelete(item.getId());

                                    }
                                })
                                .show();
                    }
                });
                break;
            default:
                rl_comment.setVisibility(View.GONE);
                break;
        }
        //进入详情
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context,OrderOnlineDetailsActivity.class,"id",item.getId(),"onlineOrderNo",item.getOnlineOrderNo(),
                        "orderStatus",item.getOrderStatus()+"");
            }
        });
    }
    public interface OrderConsumerAdapterListener {
        public void onOrderConsumerAdapterOrderPay(String orderNo,String sellerId,boolean isPayBallance);

        public void onOrderConsumerAdapterOrderReceive(String orderNo,String sellerId,double realMoney);//确认收货

        public void onOrderConsumerAdapterOrderDelete(String orderNo);

        public void onOrderConsumerAdapterOrderCancel(String orderId);

        public void onOrderConsumerAdapterOrderExpired(String orderId);  //过期

        public void onOrderConsumerAdapterOrderExtend(String orderNo); //延长

        public void onOrderConsumerAdapterOrdeRemain(String orderNo); //提醒发货
    }


    private long differTime;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (differTime <=0) {
                tv_difftime.setText("订单已过期");
                if (orderConsumerAdapterListener != null)
                    orderConsumerAdapterListener.onOrderConsumerAdapterOrderExpired(orderId);
                return;
            }
            differTime = differTime-1000;
            tv_difftime.setText("订单剩余时间："+DateUtil.format(differTime));
        }
    };


}
