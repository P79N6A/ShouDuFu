package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.ChoosePupulBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zgf on 2017/8/25.
 */
public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {
    private Context context;
    private List<ChoosePupulBean> list;
    private Handler handler;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    private int choosePosition;

    public SelectAdapter(Context context, List<ChoosePupulBean> list, Handler handler, int choosePosition) {
        super();
        this.context = context;
        this.list = list;
        this.handler = handler;
        isClicks = new ArrayList<>();
        this.choosePosition = choosePosition;
    }

    public void setList(List<ChoosePupulBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<ChoosePupulBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<ChoosePupulBean> getData() {
        return this.list;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.item_recycleview_pop, null);
        ViewHolder holder = new ViewHolder(convertView);
        for (int i = 0; i <list.size() ; i++) {
            if (i == choosePosition) {
                isClicks.add(true);
            } else {
                isClicks.add(false);
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (isClicks.get(position)) {
            holder.tvContent.setTextColor(Color.parseColor("#F26146"));
        } else {
            holder.tvContent.setTextColor(Color.parseColor("#000000"));
        }
        holder.tvContent.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1001;
                msg.obj = list.get(position).getId();
                handler.sendMessage(msg);
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
        @Bind(R.id.tv_content)
        TextView tvContent;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
