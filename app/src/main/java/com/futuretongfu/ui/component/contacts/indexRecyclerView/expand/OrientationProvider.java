package com.futuretongfu.ui.component.contacts.indexRecyclerView.expand;

import android.support.v7.widget.RecyclerView;

/**
 * Created by ChenXiaoPeng on 2017/6/15.
 */

public interface OrientationProvider {
    public int getOrientation(RecyclerView recyclerView);

    public boolean isReverseLayout(RecyclerView recyclerView);
}
