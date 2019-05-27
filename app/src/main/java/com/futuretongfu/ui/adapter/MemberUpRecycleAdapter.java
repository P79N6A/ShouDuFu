package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.futuretongfu.model.entity.MemberListInfo;
import com.futuretongfu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 类: MemberUpRecycleAdapter
 * 描述:  会员升级表适配器
 * 作者： weiang on 2017/6/20
 */
public class MemberUpRecycleAdapter extends RecyclerView.Adapter<MemberUpRecycleAdapter.MemberViewHolder> {
    private List<MemberListInfoStruct> vipList;

    private MemberUpRecycleAdapterListener mOnItemClickListener;
    private Context context;

    public MemberUpRecycleAdapter(Context context, MemberUpRecycleAdapterListener mOnItemClickListener) {
        this.vipList = new ArrayList<>();
        this.context = context;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setData( List<MemberListInfo> datas){
        this.vipList = new ArrayList<>();
        for(MemberListInfo item : datas){
            this.vipList.add(new MemberListInfoStruct(item, false));
        }
    }

    @Override
    public int getItemCount() {
        return vipList == null ? 0 : vipList.size();
    }


    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member_upgrade_layout, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemberUpRecycleAdapter.MemberViewHolder holder, final int position) {
        if (vipList == null || vipList.size() <= position) {
            return;
        }
        MemberListInfo bean = vipList.get(position).data;
        holder.text_member.setText( bean.getName() + "   " + bean.getFee());

        holder.checkbox_member.setSelected(vipList.get(position).statue);

        holder.checkbox_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vipList.get(position).statue)
                    return ;

                if(mOnItemClickListener != null)
                    mOnItemClickListener.onMemberItemClick(vipList.get(position).data);

                updateCheckboxStatue(position);
            }
        });


    }

    private void updateCheckboxStatue(int position){
        if(vipList.get(position).statue){
            return ;
        }

        for(int i = 0; i < vipList.size(); i++){
            if(i == position){
                vipList.get(i).statue = true;
            }
            else{
                vipList.get(i).statue = false;
            }
        }

        notifyDataSetChanged();
    }

    public static class MemberListInfoStruct{
        public MemberListInfo data;
        public boolean statue = false;

        public MemberListInfoStruct(MemberListInfo data, boolean statue){
            this.data = data;
            this.statue = statue;
        }
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder{
        public TextView text_member;
        public Button checkbox_member;

        public MemberViewHolder(View itemView) {
            super(itemView);
            text_member = (TextView) itemView.findViewById(R.id.text_member);
            checkbox_member = (Button) itemView.findViewById(R.id.checkbox_member);
        }
    }

    public interface MemberUpRecycleAdapterListener {
        void onMemberItemClick(MemberListInfo data);
    }


}
