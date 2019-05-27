package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;
import com.futuretongfu.ui.activity.goods.ClassDetailsActivity;
import com.futuretongfu.ui.activity.goods.ClassGoodsActivity;
import com.futuretongfu.ui.component.CircleImageView;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2017/2/17.
 */
public class HomeSortsAdapter extends RecyclerView.Adapter<HomeSortsAdapter.ViewHolder> {
    private Context context;
    private List<HomeSortBean> mList;

    public HomeSortsAdapter(Context mContext) {
        this.context = mContext;
    }

    public void setData(List<HomeSortBean> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void addList(List<HomeSortBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public List<HomeSortBean> getData() {
        return this.mList;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.item_recycleview_homesort, null);
        ViewHolder holder = new ViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == 7) {
            holder.tvName.setText("更多");
            holder.imgSort.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_home_sort));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.startActivity(context, ClassGoodsActivity.class);
                }
            });
        }  else {//0,1,2,----10
            if (!TextUtils.isEmpty(mList.get(position).getCategoryImg())) {
                GlideLoad.loadCropCircle(mList.get(position).getCategoryImg(), holder.imgSort);
            }
            holder.tvName.setText(mList.get(position).getCategoryName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.startActivity(context, ClassDetailsActivity.class,
                            "classId", "" +mList.get(position).getCategoryCode(),
                            "className", "" +mList.get(position).getCategoryName());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if ( mList == null){
            return 0;
        }else if (mList.size()<=8){
            return mList.size();
        }else {
            return 8;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.img_sort)
        ImageView imgSort;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
