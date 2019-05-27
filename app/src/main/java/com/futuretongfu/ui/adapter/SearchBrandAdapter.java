package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.ChoosePupulBean;
import com.futuretongfu.bean.onlinegoods.GoodsBrandBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zgf on 2017/8/25.
 */
public class SearchBrandAdapter extends RecyclerView.Adapter<SearchBrandAdapter.ViewHolder> {
    private Context context;
    private List<GoodsBrandBean> list;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    private int brandPosition;
    private OnItemClickListener mOnItemClickListener;
    public SearchBrandAdapter(Context context,List<GoodsBrandBean> list,int brandPosition) {
        super();
        this.context = context;
        this.list = list;
        this.brandPosition = brandPosition;
        isClicks = new ArrayList<>();
    }

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.item_recycleview_searchchoose, null);
        ViewHolder holder = new ViewHolder(convertView);
        for (int i = 0; i <list.size() ; i++) {
            if (brandPosition>=0&&i == brandPosition) {
                isClicks.add(true);
            } else {
                isClicks.add(false);
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (isClicks.get(position)) {
            holder.tvContent.setChecked(true);
        } else {
            holder.tvContent.setChecked(false);
        }
        holder.tvContent.setClickable(false);
        holder.tvContent.setText(list.get(position).getBrandName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < isClicks.size(); i++) {
                    isClicks.set(i, false);
                }
                isClicks.set(position, true);
                notifyDataSetChanged();
                mOnItemClickListener.onItemClick(holder.tvContent,list.get(position).getId(),position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cb_search_choose)
        CheckBox tvContent;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(TextView textView, String id,int position);
    }
}
