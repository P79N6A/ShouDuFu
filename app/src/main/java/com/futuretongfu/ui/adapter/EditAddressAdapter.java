package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futuretongfu.model.entity.AddressEntity;
import com.futuretongfu.R;
import com.futuretongfu.ui.activity.user.EditAddressActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ChenXiaoPeng on 2017/6/16.
 */

public class EditAddressAdapter extends RecyclerView.Adapter<EditAddressAdapter.EditAddressViewHolder>{

    Context context;
    private int type;
    private List<AddressEntity> datas;

    public EditAddressAdapter(Context context, int type, EditAddressAdapterListener editAddressAdapterListener){
        this.context = context;
        this.datas = new ArrayList<>();
        this.type = type;
        this.editAddressAdapterListener = editAddressAdapterListener;
    }

    @Override
    public EditAddressViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        EditAddressViewHolder RecommendViewHolder = new EditAddressViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_edit_address, parent, false)
        );

        return RecommendViewHolder;
    }

    @Override
    public void onBindViewHolder(EditAddressViewHolder holder, final int position)
    {
        holder.textName.setText(datas.get(position).getAreaName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editAddressAdapterListener != null){
                    if(type == EditAddressActivity.Type_Province){
                        editAddressAdapterListener.onEditAddressAdapter2City(
                                datas.get(position).getAreaName()
                                , datas.get(position).getAreaId()
                        );
                    }
                    else if(type == EditAddressActivity.Type_Region){
                        editAddressAdapterListener.onEditAddressAdapterFinish(datas.get(position).getAreaName());
                    }
                    else{
                        editAddressAdapterListener.onEditAddressAdapter2Region(
                                datas.get(position).getAreaName()
                                , datas.get(position).getAreaId());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return datas.size();
    }

    public void setDatas(int type, List<AddressEntity> datas){
        this.type = type;
        this.datas = datas;
        notifyDataSetChanged();
    }

    public class EditAddressViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.text_name)
        public TextView textName;

        public EditAddressViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private EditAddressAdapterListener editAddressAdapterListener;
    public interface EditAddressAdapterListener{
        public void onEditAddressAdapter2City(String province, int provinceCode);
        public void onEditAddressAdapter2Region(String city, int cityCode);
        public void onEditAddressAdapterFinish(String district);
    }
}
