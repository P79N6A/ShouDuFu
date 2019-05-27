package com.futuretongfu.ui.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.GoodsAttrValuesBean;
import com.futuretongfu.ui.activity.goods.SearchRankActivity;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2017/4/26.
 */
public class GoodsDesColorRvAdapter extends BaseAdapter {
    private Activity ctx;
    private List<GoodsAttrValuesBean> mList;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    private String attributeId;
    private String format="";

    public GoodsDesColorRvAdapter(Activity ctx,String attributeId,List<GoodsAttrValuesBean> catgoryList, MyItemListener myItemListener,String formats) {
        this.ctx = ctx;
        this.attributeId = attributeId;
        this.mList = catgoryList;
        this.myItemListener = myItemListener;
        this.format = formats;
        isClicks = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            String str = mList.get(i).getAttrValue();
            if (format.contains(str)) {
                isClicks.add(true);
            } else {
                isClicks.add(false);
            }
        }
    }

    private MyItemListener myItemListener;

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_recycleview_goods_attr, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(mList.get(position).getAttrValue());
        if (isClicks.get(position)) {
            holder.tvName.setTextColor(ctx.getResources().getColor(R.color.text_color_red));
            holder.tvName.setBackground(ctx.getResources().getDrawable(R.drawable.shape_rect_biankuang_red));
        } else {
            holder.tvName.setTextColor(ctx.getResources().getColor(R.color.text_color_black));
            holder.tvName.setBackground(ctx.getResources().getDrawable(R.drawable.shape_rect_biankuang_gray));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < isClicks.size(); i++) {
                    isClicks.set(i, false);
                }
                isClicks.set(position, true);
                if(myItemListener != null)
                     myItemListener.onitemAdapterClick(attributeId,mList.get(position),holder.tvName.getText().toString());
                notifyDataSetChanged();
            }
        });
//        if (isClicks.get(position)) {
//            if(myItemListener != null)
//                myItemListener.onitemAdapterClick(attributeId,mList.get(position),holder.tvName.getText().toString());
//        }
        return convertView;
    }

    public interface MyItemListener{
        public void onitemAdapterClick(String attributeId,GoodsAttrValuesBean item,String formatInfo);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
