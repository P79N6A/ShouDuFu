package com.futuretongfu.ui.component.contacts.indexRecyclerView.expand;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ChenXiaoPeng on 2017/6/15.
 */

public interface HeaderProvider {
    /**
     * Will provide a header view for a given position in the RecyclerView
     *
     * @param recyclerView that will display the header
     * @param position     that will be headed by the header
     * @return a header view for the given position and list
     */
    public View getHeader(RecyclerView recyclerView, int position);

    /**
     * TODO: describe this functionality and its necessity
     */
    void invalidate();

}
