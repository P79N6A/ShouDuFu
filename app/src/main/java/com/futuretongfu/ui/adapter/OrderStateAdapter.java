package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.OrderOnlineStateBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2017/3/28.
 */
public class OrderStateAdapter extends  RecyclerView.Adapter<OrderStateAdapter.ViewHolder>  {
    private Context context;
    private List<OrderOnlineStateBean> list;
    public OrderStateAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<OrderOnlineStateBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public List<OrderOnlineStateBean> getData() {
        return this.list;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.item_recycleview_orderstate, null);
        OrderStateAdapter.ViewHolder holder = new OrderStateAdapter.ViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position==0){
            holder.view1.setVisibility(View.INVISIBLE);
        }else {
            holder.view1.setVisibility(View.VISIBLE);
        }
        if (position==list.size()-1){
            holder.view2.setVisibility(View.INVISIBLE);
        }else {
            holder.view2.setVisibility(View.VISIBLE);
        }
        holder.tvDetails.setText(list.get(position).getAcceptStation());
        holder.tvDetailsdate.setText(list.get(position).getAcceptTime());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.view_1)
        View view1;
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.view_2)
        View view2;
        @Bind(R.id.tv_details)
        TextView tvDetails;
        @Bind(R.id.tv_detailsdate)
        TextView tvDetailsdate;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
