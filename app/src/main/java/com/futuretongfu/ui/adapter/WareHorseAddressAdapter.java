package com.futuretongfu.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.ui.component.SwipeMenuLayout;
import com.futuretongfu.ui.component.dialog.PromptDialog;

import java.util.List;

/**
 * Created by zhanggf on 2018/5/10.
 */

public class WareHorseAddressAdapter extends  BaseQuickAdapter<WareHorseAddressEntity, BaseViewHolder >{

    List<WareHorseAddressEntity> list;
    private OnItemClickListener mOnItemClickListener;
    private Context context;

    public WareHorseAddressAdapter(Context context, OnItemClickListener mOnItemClickListener, @Nullable List<WareHorseAddressEntity> datas) {
        super(R.layout.item_warehorse_address, datas);
        this.context = context;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WareHorseAddressEntity item){
        helper.setIsRecyclable(false);//不推荐使用
        helper.setText(R.id.tv_name,item.getReceiverName());
        helper.setText(R.id.tv_phoneNum,item.getReceiverMobile());
        helper.setText(R.id.tv_address,item.getReceiverAddress());
        CheckBox check_cb = helper.getView(R.id.check_cb);
        showCheck(check_cb, item.getIsDefault());
        helper.setOnClickListener(R.id.view_click, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null)
                    mOnItemClickListener.onMyAddressAdapterClick(item);
            }
        });
        helper.setOnClickListener(R.id.iv_change, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null)
                    mOnItemClickListener.onMyAddressAdapterEditClick(item);
            }
        });
        helper.setOnCheckedChangeListener(R.id.check_cb,new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mOnItemClickListener.onMyAddressAdapterSet(item);
                }
            }
        });
        helper.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PromptDialog
                        .Builder(context)
                        .setMessage("您确定删除此条地址？")
                        .setButton1("取消", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setButton2("确定", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                                if(mOnItemClickListener != null)
                                    mOnItemClickListener.onMyAddressAdapterDelete(item);

                            }
                        })
                        .show();
            }
        });
    }

    public void deleteItem(WareHorseAddressEntity item){
        List<WareHorseAddressEntity> beans = getData();
        for(int i = 0; i < beans.size(); i++){
            if(beans.get(i).getId() == item.getId()){
                remove(i);
                break;
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    private void showCheck(CheckBox view, String markStatus) {
        if (!TextUtils.isEmpty(markStatus) && markStatus.equals("0")) {
            view.setChecked(true);
            view.setText("默认地址");
            view.setClickable(false);
        } else {
            view.setClickable(true);
            view.setChecked(false);
            view.setText("设为默认");
        }
    }

    public void setIsClick(boolean isClick) {
    }

    public interface OnItemClickListener{
        public void onMyAddressAdapterClick(WareHorseAddressEntity item);
        public void onMyAddressAdapterEditClick(WareHorseAddressEntity item);
        public void onMyAddressAdapterDelete(WareHorseAddressEntity item);
        public void onMyAddressAdapterSet(WareHorseAddressEntity item);
    }

}
