package com.futuretongfu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.model.entity.MyBillEntity;
import com.futuretongfu.ui.activity.BillDetailActivity;
import com.futuretongfu.ui.component.dialog.BillScreenDialog;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.oss.OSSUtil;
import com.futuretongfu.R;

import java.util.ArrayList;

/**
 * 我的账单
 * Created by ChenXiaoPeng on 2017/6/19.
 */

public class MyBillAdapter extends BaseQuickAdapter<MyBillEntity, BaseViewHolder> {

    private Context context;
    private int type = -1;

    public MyBillAdapter(Context context, int type) {
        super(R.layout.item_my_bill_list, new ArrayList<MyBillEntity>());
        this.context = context;
        this.type = type;
    }

    public void putType(int type) {
        this.type = type;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(final BaseViewHolder holder, final MyBillEntity item) {
        TextView addView = holder.getView(R.id.addView);
        ImageView iconView = holder.getView(R.id.imgv_icon);
        TextView text_monery = holder.getView(R.id.text_monery);
        if (!TextUtils.isEmpty(item.getBusinessTypeName())) {
            holder.setText(R.id.text_name, StringUtil.getSafeString(item.getBusinessTypeName()));
        } else {
            holder.setText(R.id.text_name, StringUtil.getSafeString(item.getRemark()));
        }
        if (BillScreenDialog.Type_FSort == type) {
            addView.setVisibility(View.VISIBLE);
            iconView.setVisibility(View.VISIBLE);
            if ("0".equals(item.getTradeType())) {
                addView.setText("-");
                text_monery.setTextColor(R.color.black);
            } else if ("1".equals(item.getTradeType())) {
                addView.setText("+");
                text_monery.setTextColor(context.getResources().getColor(R.color.wored));
            }
            holder.setText(R.id.text_monery, StringUtil.fmtMicrometer(item.getMoney()));
        } else {
            addView.setVisibility(View.GONE);
            iconView.setVisibility(View.GONE);
            if ("0".equals(item.getTradeType())) {
                holder.setText(R.id.text_monery, "- ￥" + StringUtil.fmtMicrometer(item.getMoney()));
                text_monery.setTextColor(context.getResources().getColor(R.color.black));

            } else if ("1".equals(item.getTradeType())) {
                holder.setText(R.id.text_monery, "+ ￥" + StringUtil.fmtMicrometer(item.getMoney()));
                text_monery.setTextColor(context.getResources().getColor(R.color.wored));


            } else {
                holder.setText(R.id.text_monery, "￥" + StringUtil.fmtMicrometer(item.getMoney()));
                text_monery.setTextColor(context.getResources().getColor(R.color.wored));

            }
        }

        holder.setText(R.id.text_time, DateUtil.getDateStrWithHour1(item.getCreateTime()));

        GlideLoad.loadCrossFadeImageView2(OSSUtil.imageBaseUrl + "/billcion/" + item.getBusinessType().toLowerCase() + ".png", (ImageView) holder.getView(R.id.imgv_photo));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillDetailActivity.startActivity(context, "" + item.getBusinessNo(), item.getBusinessType(), type);
            }
        });

    }

}
