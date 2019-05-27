package com.futuretongfu.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.ExchangeListBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2017/3/9.
 */
public class ExpandableSecondsAdapter extends RecyclerView.Adapter<ExpandableSecondsAdapter.ViewHolder> {
    private Activity ctx;
    private List<ExchangeListBean> mList;
    public ExpandableSecondsAdapter(Activity ctx, List<ExchangeListBean> childrenInfoList) {
        this.ctx = ctx;
        this.mList = childrenInfoList;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(ctx, R.layout.item_expandable_childen, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        holder.channel_childen_name.setText(mList.get(position).getClassname());
        holder.channel_childen_name.setText(mList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.channel_childen_name)
        TextView channel_childen_name;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
