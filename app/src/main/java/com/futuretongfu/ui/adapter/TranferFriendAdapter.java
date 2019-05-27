package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.listener.OnRecyclerViewItemClickListener;
import com.futuretongfu.ui.component.contacts.indexRecyclerView.BaseAdapter;
import com.futuretongfu.ui.component.contacts.indexRecyclerView.expand.StickyRecyclerHeadersAdapter;
import com.futuretongfu.ui.component.contacts.model.ContactModel;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.To;
import com.futuretongfu.utils.Util;
import com.futuretongfu.R;
import com.futuretongfu.ui.component.contacts.widget.IndexAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 转账朋友
 *
 * @author DoneYang 2017/6/22
 */

public class TranferFriendAdapter extends BaseAdapter<ContactModel.MembersEntity, TranferFriendAdapter.TranferFriendViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>, IndexAdapter {

    private Context context;
    private List<ContactModel.MembersEntity> datas;
    private OnRecyclerViewItemClickListener listener = null;

    public TranferFriendAdapter(Context context, List<ContactModel.MembersEntity> datas) {
        if (datas != null) {
            this.datas = datas;
        } else {
            this.datas = new ArrayList<>();
        }
        this.context = context;
    }

    public void setDatas(List<ContactModel.MembersEntity> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public ContactModel.MembersEntity getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public TranferFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tranfer_friend, parent, false);
        return new TranferFriendAdapter.TranferFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TranferFriendViewHolder holder, final int position) {

        ContactModel.MembersEntity data = datas.get(position);
        if (Util.isEmpty(data)) {
            To.s(R.string.get_data_error);
            return;
        }
        TextView tv_name = holder.name;
        TextView tv_mobile = holder.mobile;
        ImageView image_head = holder.head;

        if (!Util.isEmpty(data.getUsername())) {
            tv_name.setText(data.getUsername());
        } else {
            if (!Util.isEmpty(data.getMobile())) {
                tv_name.setText("用户" + data.getMobile());
            }
        }
        if (!Util.isEmpty(data.getMobile())) {
            tv_mobile.setText(data.getMobile());
        }

        if (!Util.isEmpty(data.getFaceURL())) {
            GlideLoad.loadHead(data.getFaceURL(), image_head);
        }

        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(holder.itemView, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }
    }

    @Override
    public long getHeaderId(int position) {
        return datas.get(position).getSortLetters().charAt(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_txl_contact_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        String showValue = String.valueOf(getItem(position).getSortLetters().charAt(0));
        textView.setText(showValue);
    }

    public int getPositionForSection(char section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = datas.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;

    }

    class TranferFriendViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        TextView mobile;

        ImageView head;

        public TranferFriendViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_item_tranfer_friend_name);
            mobile = (TextView) itemView.findViewById(R.id.tv_item_tranfer_friend_mobile);
            head = (ImageView) itemView.findViewById(R.id.image_item_tranfer_friend_head);
        }
    }
}
