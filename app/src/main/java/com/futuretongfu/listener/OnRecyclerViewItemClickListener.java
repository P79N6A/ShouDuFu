package com.futuretongfu.listener;

import android.view.View;

/**
 * 监听事件
 *
 * @author DoneYang 2017/6/20
 */

public interface OnRecyclerViewItemClickListener {

    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
}
