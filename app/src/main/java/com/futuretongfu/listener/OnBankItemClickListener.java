package com.futuretongfu.listener;

import android.view.View;

/**
 * 类: OnItemClickListener
 * 描述: 列表点击监听器
 * 作者： weiang on 2017/6/21
 */
public interface OnBankItemClickListener {

    void onItemClick(View view);

    void onItemClick(View view, View rootView);

    void onItemClick(View view, int position);
}
