package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.futuretongfu.bean.BankListItemBean;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.utils.GlideLoad;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 类: MyBankRecycleAdapter
 * 描述: 我的银行卡列表适配器
 * 作者： weiang on 2017/6/20
 */
public class MyBankRecycleAdapter extends RecyclerView.Adapter<MyBankRecycleAdapter.BankViewHolder> {
    private List<BankListItemBean> bankList;

    private OnItemClickListener mOnItemClickListener;
    private Context context;

    public MyBankRecycleAdapter(Context context, List<BankListItemBean> bankLists) {
        this.bankList = bankLists;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return bankList == null ? 0 : bankList.size();
        //return 10;
    }


    @Override
    public BankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bank_content_layout, parent, false);
        return new BankViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(final MyBankRecycleAdapter.BankViewHolder holder, final int position) {
        if (bankList == null || bankList.size() <= position) {
            return;
        }
        BankListItemBean itemBean = bankList.get(position);
        if(!TextUtils.isEmpty(itemBean.getBankUrl())){
//            GlideLoad.load(itemBean.getBankUrl(),holder.imageIcon);
            Glide.with(context).load(itemBean.getBankUrl()).into(holder.imageIcon);
        }
        if(!TextUtils.isEmpty(itemBean.getBankColor())){
            holder.itemLayout.setBackgroundColor(Color.parseColor(itemBean.getBankColor()));
        }else{
            holder.itemLayout.setBackgroundColor(Color.parseColor("#ffb121"));
        }
        holder.cradType.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(itemBean.getBankName())) {
            holder.cardNameView.setText(itemBean.getBankName());
        }

        if (!TextUtils.isEmpty(itemBean.getAccNo())) {
            String accNo = itemBean.getAccNo();
            accNo = accNo.substring(accNo.length() - 4, accNo.length());
            holder.cradNumber.setText(context.getResources().getString(R.string.bank_card_number_hint) + accNo);
        }
        if (!TextUtils.isEmpty(itemBean.getType())) {
            if (IntentKey.SAFETY_CARD.equals(itemBean.getType())) {
                holder.cradType.setText("安全卡");
            } else {
                holder.cradType.setText("");
            }
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.itemLayout, position);
            }
        });
    }

    static class BankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.view_swipe_menu)
        public RelativeLayout viewSwipeMenu;
        @Bind(R.id.layout_bank_card)
        public RelativeLayout itemLayout;
        @Bind(R.id.image_bank_bg)
        public ImageView imageBg;
        @Bind(R.id.iamge_bank_icon)
        public ImageView imageIcon;
        @Bind(R.id.bank_card_name)
        public TextView cardNameView;
         @Bind(R.id.bank_card_function_type)
        public TextView cradType;
        @Bind(R.id.bank_card_number)
        public TextView cradNumber;
        OnItemClickListener onItemClickListener;

        public BankViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onItemClickListener = onItemClickListener;
            itemLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }


}
