package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.ChoosePupulBean;
import com.futuretongfu.bean.onlinegoods.GoodsSearchTermBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zgf on 2017/8/25.
 */
public class SearchTremAdapter extends RecyclerView.Adapter<SearchTremAdapter.ViewHolder> {
    private Context context;
    private List<GoodsSearchTermBean> list;

    public SearchTremAdapter(Context context, List<GoodsSearchTermBean> list) {
        super();
        this.context = context;
        this.list = list;
    }
    public List<GoodsSearchTermBean> getData() {
        return this.list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.item_recycleview_searchchoose, null);
        ViewHolder holder = new ViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.cb_search_choose.setText(list.get(position).getDescription());
        if (list.get(position).getDictValue().equals("1")){
            holder.cb_search_choose.setChecked(true);
        }else {
            holder.cb_search_choose.setChecked(false);
        }
        holder.cb_search_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    list.get(position).setDictValue("1");
                }else {
                    list.get(position).setDictValue("0");
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cb_search_choose)
        CheckBox cb_search_choose;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
