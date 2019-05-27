package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.ContactsFriend;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 类:   NewFriendRecycleAdapter
 * 描述: 新的朋友列表数据适配器
 * 作者： weiang on 2017/6/23
 */
public class NewFriendRecycleAdapter extends RecyclerView.Adapter<NewFriendRecycleAdapter.NewFriendViewHolder> {
    private List<ContactsFriend> list;
    private OnFriendClickListener mOnItemClickListener;
    private Context context;

    public NewFriendRecycleAdapter(Context context, List<ContactsFriend> list) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnFriendClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public NewFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tranfer_friend, parent, false);
        return new NewFriendRecycleAdapter.NewFriendViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(NewFriendViewHolder holder, final int position) {

        ContactsFriend data = list.get(position);

        holder.textName.setText(StringUtil.getNoEmptyStr(data.getNickName(), "未命名"));
        holder.textPhone.setText(StringUtil.getSafeString(data.getMsg()));//验证信息

        GlideLoad.loadCrossFadeImageView(
                  data.getFaceURL()
                , R.mipmap.icon_transfer_2, R.mipmap.icon_transfer_2
                , holder.imgHeader);

        switch (data.getAddStatus()) {
            case Constants.AddStatus_NewFriend_Request://等待确认
                holder.choose_layout.setVisibility(View.VISIBLE);
                holder.textRefuse.setVisibility(View.GONE);
                holder.agreeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onAccept(v, list.get(position).getFriendUserId());
                        }
                    }
                });
                holder.refuseView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onRefuse(v, list.get(position).getFriendUserId());
                        }
                    }
                });

                break;

            case Constants.AddStatus_NewFriend_Adopt://通过
                holder.choose_layout.setVisibility(View.GONE);
                holder.textRefuse.setVisibility(View.VISIBLE);
//                holder.textRefuse.setText("已通过");
                holder.textRefuse.setText("已添加");
                break;

            case Constants.AddStatus_NewFriend_Adopt_No://不通过
                holder.choose_layout.setVisibility(View.GONE);
                holder.textRefuse.setVisibility(View.VISIBLE);
                holder.textRefuse.setText("已拒绝");
                break;

        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setDatas(List<ContactsFriend> list){
        if(null == list)
            return ;

        this.list = list;
        notifyDataSetChanged();
    }

    static class NewFriendViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image_item_tranfer_friend_head)
        public RoundedImageView imgHeader;
        @Bind(R.id.tv_item_tranfer_friend_name)
        public TextView textName;
        @Bind(R.id.tv_item_tranfer_friend_mobile)
        public TextView textPhone;

        public OnFriendClickListener onItemClickListener;

        @Bind(R.id.ll_item_tranfer_friend_apply)
        public LinearLayout choose_layout;
        @Bind(R.id.tv_item_tranfer_friend_agree)
        public TextView agreeView;
        @Bind(R.id.tv_item_tranfer_friend_refuse)
        public TextView refuseView;
        @Bind(R.id.tv_item_tranfer_friend_result)
        public TextView textRefuse;

        public NewFriendViewHolder(View itemView, OnFriendClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onItemClickListener = onItemClickListener;
        }
    }

    /**
     * 事件监听器
     */
    public interface OnFriendClickListener {
        //接受
        void onAccept(View view, String id);

        //拒绝
        void onRefuse(View view, String id);
    }


}
