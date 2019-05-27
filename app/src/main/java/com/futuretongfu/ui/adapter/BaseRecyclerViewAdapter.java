package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.futuretongfu.R;
import com.futuretongfu.model.entity.BaseViewHolder;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;


    public BaseRecyclerViewAdapter(Context context, int layoutId, List<T> datas)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        BaseViewHolder viewHolder = BaseViewHolder.get(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position)
    {
        convert(holder, mDatas.get(position));
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder holder) {
        holder.itemView.clearAnimation();
    }

    public void setItemViewAnimation(boolean isItemViewAnimation){
        this.isItemViewAnimation = isItemViewAnimation;
    }

    private int lastAnimationPosition = 0;
    private boolean isItemViewAnimation = false;
    protected void setAnimation(View viewToAnimate, int position){
        if (isItemViewAnimation && position > lastAnimationPosition) {
            Animation animation = AnimationUtils
                    .loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);

            lastAnimationPosition = position;
        }
    }

    public abstract void convert(BaseViewHolder holder, T t);

}
