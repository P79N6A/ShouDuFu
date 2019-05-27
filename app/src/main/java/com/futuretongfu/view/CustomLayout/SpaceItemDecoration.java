package com.futuretongfu.view.CustomLayout;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 类:   SpaceItemDecoration
 * 描述:  设置我的银行条目间距
 * 作者： weiang on 2017/6/20
 */
public class SpaceItemDecoration  extends RecyclerView.ItemDecoration{
    private int space;
    public SpaceItemDecoration(int space) {
        this.space = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildPosition(view) != 0)
            outRect.top = space;
    }

}
