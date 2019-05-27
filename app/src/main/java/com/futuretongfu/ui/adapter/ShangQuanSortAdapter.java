package com.futuretongfu.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.WlsqTypeBean;
import com.futuretongfu.ui.activity.ShangQuanActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2017/3/9.
 */
public class ShangQuanSortAdapter extends RecyclerView.Adapter<ShangQuanSortAdapter.ViewHolder> {
    private Activity ctx;
    private List<WlsqTypeBean> list;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    private OnItemClickListener mOnItemClickListener;
    private int choosePosition;
    public ShangQuanSortAdapter(Activity ctx, int choosePosition) {
        this.ctx = ctx;
        isClicks = new ArrayList<>();
        this.choosePosition = choosePosition;
    }
    public void setList(List<WlsqTypeBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<WlsqTypeBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<WlsqTypeBean> getData() {
        return this.list;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(ctx, R.layout.item_recycle_shangquan_sort, null);
        ViewHolder holder = new ViewHolder(view);
        for (int i = 0; i <list.size() ; i++) {
            if (i == choosePosition) {
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
            holder.tvName.setTextColor(Color.parseColor("#F26146"));
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.tvName.setTextColor(Color.parseColor("#000000"));
            holder.view.setVisibility(View.INVISIBLE);
        }
        if (position<3){
            holder.tvName.setText(list.get(position).hyNamePc);
        }else {
            holder.tvName.setText("更多");
        }
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < isClicks.size(); i++) {
                        isClicks.set(i, false);
                    }
                    isClicks.set(position, true);
                    notifyDataSetChanged();
                    if (position>=3){
                        Intent intent = new Intent(ctx,ShangQuanActivity.class);
                        ctx.startActivity(intent);
                    }else {
                        mOnItemClickListener.onItemClick(holder.tvName, holder.tvName, list.get(position).id);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if ( list == null){
            return 0;
        }else if (list.size()<=4){
            return list.size();
        }else {
            return 4;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.selectview)
        View view;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, TextView textView, int id);
    }
}
