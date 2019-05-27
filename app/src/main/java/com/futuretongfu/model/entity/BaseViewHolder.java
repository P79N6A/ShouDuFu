package com.futuretongfu.model.entity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public static BaseViewHolder get(Context context, ViewGroup parent, int layoutId) {

        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        BaseViewHolder holder = new BaseViewHolder(context, itemView, parent);

        return holder;
    }


}
