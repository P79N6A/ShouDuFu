package com.futuretongfu.ui.component.contacts.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.futuretongfu.ui.component.contacts.indexRecyclerView.expand.StickyRecyclerHeadersAdapter;
import com.futuretongfu.ui.component.contacts.model.ContactModel;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.ui.activity.ScanAddFriendActivity;
import com.futuretongfu.ui.component.contacts.widget.IndexAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *   通讯录
 * Created by ChenXiaoPeng on 2017/6/15.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>, IndexAdapter {

    private Context context;
    private List<ContactModel.MembersEntity> datas;

    public ContactAdapter(Context context, List<ContactModel.MembersEntity> datas) {
        if(datas != null) {
            this.datas = datas;
        }
        else{
            this.datas = new ArrayList<>();
        }
        this.context = context;

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_txl_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {

        ContactModel.MembersEntity data = datas.get(position);
        holder.mName.setText(data.getUsername());

        GlideLoad.loadCrossFadeImageView(
                data.getFaceURL()
                , R.mipmap.icon_head_2, R.mipmap.icon_head_2
                , holder.imgHeader);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("onBindViewHolder", datas.get(position).getUsername());
//                IntentUtil.startActivity(context, ScanAddFriendActivity.class
//                        , IntentKey.WLF_BEAN, JSON.toJSONString(datas.get(position)));

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                (Activity)context
                                , holder.imgHeader
                                , context.getResources().getString(R.string.scene_animation_personal_data_photo)
                        );

                Intent intent = new Intent(context, ScanAddFriendActivity.class);
                intent.putExtra(IntentKey.WLF_BEAN, JSON.toJSONString(datas.get(position)));
                context.startActivity(intent, options.toBundle());

            }
        });

    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public ContactModel.MembersEntity getItem(int position) {
        return datas.get(position);
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
        String showValue = String.valueOf(datas.get(position).getSortLetters().charAt(0));
        textView.setText(showValue);
    }

    public void setDatas(List<ContactModel.MembersEntity> datas){
        this.datas = datas;
        notifyDataSetChanged();
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

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgHeader;
        public TextView mName;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mName = (TextView)itemView.findViewById(R.id.text_name);
            imgHeader = (ImageView)itemView.findViewById(R.id.imgv_photo);

        }
    }
}
