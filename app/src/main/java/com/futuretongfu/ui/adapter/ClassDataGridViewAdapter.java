package com.futuretongfu.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.futuretongfu.R;
import com.futuretongfu.bean.classification.ClassThreeListViewBean;
import com.futuretongfu.ui.activity.goods.SearchRankActivity;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2017/3/14.
 */
public class ClassDataGridViewAdapter extends BaseAdapter {
    private final Activity ctx;
    private final List<ClassThreeListViewBean> mList;

    public ClassDataGridViewAdapter(Activity ctx, List<ClassThreeListViewBean> childrenInfoList) {
        this.ctx = ctx;
        this.mList = childrenInfoList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_classification_gridview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvDes.setText(mList.get(position).getCategoryName());
        if (!TextUtils.isEmpty(mList.get(position).getCategoryImg())) {
            GlideLoad.loadImage(mList.get(position).getCategoryImg(), holder.ivPic);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(ctx, SearchRankActivity.class,
                        "classId",mList.get(position).getCategoryCode());
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_pic)
        ImageView ivPic;
        @Bind(R.id.tv_des)
        TextView tvDes;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
