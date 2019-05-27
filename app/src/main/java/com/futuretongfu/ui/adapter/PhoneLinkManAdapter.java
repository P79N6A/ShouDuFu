package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.listener.OnRecyclerViewItemClickListener;
import com.futuretongfu.ui.component.contacts.indexRecyclerView.expand.StickyRecyclerHeadersAdapter;
import com.futuretongfu.ui.component.contacts.model.ContactModel;
import com.futuretongfu.R;
import com.futuretongfu.ui.component.contacts.indexRecyclerView.BaseAdapter;
import com.futuretongfu.ui.component.contacts.widget.IndexAdapter;

import java.util.List;

/**
 * 手机通讯录
 *
 * @author DoneYang 2017/6/28
 */

public class PhoneLinkManAdapter extends BaseAdapter<ContactModel.MembersEntity, PhoneLinkManAdapter.PhoneLinkManViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>, IndexAdapter {

    private Context context;
    private List<ContactModel.MembersEntity> datas;
    private OnRecyclerViewItemClickListener listener = null;

    public PhoneLinkManAdapter(Context context, List<ContactModel.MembersEntity> datas) {
        this.context = context;
        this.datas = datas;
        this.addAll(datas);
    }

    @Override
    public PhoneLinkManViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tranfer_friend, parent, false);
        return new PhoneLinkManAdapter.PhoneLinkManViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhoneLinkManViewHolder holder, final int position) {
        LinearLayout ll_invite = holder.ll_invite;
        ll_invite.setVisibility(View.VISIBLE);
        TextView tv_name = holder.name;
        TextView tv_mobile = holder.mobile;
        ImageView image_head = holder.head;
        if (datas.get(position).isAgree()) {
            holder.text_agree.setText("已邀请");
            holder.ll_invite.setEnabled(false);
        } else {
            holder.text_agree.setText("+ 邀请");
            holder.ll_invite.setEnabled(true);
        }
        tv_name.setText(getItem(position).getUsername());
        tv_mobile.setText(getItem(position).getMobile());

        ll_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(view, position);
                }
            }
        });
    }


    public void setOnClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getSortLetters().charAt(0);
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

    class PhoneLinkManViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_invite;
        TextView name;
        TextView mobile;
        ImageView head;
        TextView text_agree;


        public PhoneLinkManViewHolder(View itemView) {
            super(itemView);
            ll_invite = (LinearLayout) itemView.findViewById(R.id.ll_item_tranfer_friend_invite);
            name = (TextView) itemView.findViewById(R.id.tv_item_tranfer_friend_name);
            text_agree = (TextView) itemView.findViewById(R.id.text_agree);
            mobile = (TextView) itemView.findViewById(R.id.tv_item_tranfer_friend_mobile);
            head = (ImageView) itemView.findViewById(R.id.image_item_tranfer_friend_head);
        }
    }
}
