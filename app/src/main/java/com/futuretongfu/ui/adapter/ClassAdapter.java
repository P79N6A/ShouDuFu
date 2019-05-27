package com.futuretongfu.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.classification.ClassOneListViewBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2017/3/14.
 */
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private Activity ctx;
    private List<ClassOneListViewBean> mList;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    private OnItemClickListener mOnItemClickListener;

    public ClassAdapter(Activity activity) {
        this.ctx = activity;
        isClicks = new ArrayList<>();
    }

    public void setData(List<ClassOneListViewBean> data) {
        this.mList = data;
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                isClicks.add(true);
            } else {
                isClicks.add(false);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(ctx, R.layout.item_class_recycle, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvName.setText(mList.get(position).getCategoryName());
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(holder.tvName);
        //TODO 条目复用
        holder.setIsRecyclable(false);
        DisplayMetrics metric = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        LinearLayout.LayoutParams paramTest = (LinearLayout.LayoutParams) holder.tvName.getLayoutParams();
        paramTest.width = width/4;
        holder.tvName.setLayoutParams(paramTest);
        if (isClicks.get(position)) {
            holder.tvName.setTextColor(Color.parseColor("#ff7e00"));//ff7e00
            holder.tvName.setBackgroundColor(Color.parseColor("#f0f0f0"));
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.view.setVisibility(View.INVISIBLE);
            holder.tvName.setTextColor(Color.parseColor("#666666"));
            holder.tvName.setBackgroundColor(Color.parseColor("#ffffff"));
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
                    mOnItemClickListener.onItemClick(holder.itemView, holder.tvName, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.view)
        View view;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, TextView textView, int position);
    }
}
