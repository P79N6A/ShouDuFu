package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.StoreBean;
import com.futuretongfu.utils.GlideLoad;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2017/2/17.
 */
public class ShangQuanGoodsAdapter extends RecyclerView.Adapter<ShangQuanGoodsAdapter.ViewHolder> {
    private Context mContext;
    private List<StoreBean> mList;

    public ShangQuanGoodsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<StoreBean> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void addList(List<StoreBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public List<StoreBean> getData() {
        return this.mList;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(mContext, R.layout.item_recycleview_shangquanstore, null);
        ViewHolder holder = new ViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvShangquanName.setText(mList.get(position).storeName);
        if (!TextUtils.isEmpty(mList.get(position).logoUrl)) {
            GlideLoad.loadImage(mList.get(position).logoUrl, holder.imgShangquanImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_shangquan_image)
        ImageView imgShangquanImage;
        @Bind(R.id.tv_shangquan_name)
        TextView tvShangquanName;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
